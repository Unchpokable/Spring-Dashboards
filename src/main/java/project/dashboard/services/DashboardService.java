package project.dashboard.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.dashboard.internal.ArgumentGuard;
import project.dashboard.models.Dashboard;
import project.dashboard.models.TaskCard;
import project.dashboard.models.User;
import project.dashboard.models.Workspace;
import project.dashboard.repos.IDashboardRepository;
import project.dashboard.repos.IWorkspaceRepository;


@Service
public class DashboardService {
    @Autowired
    private IDashboardRepository dashboardRepository;

    @Autowired
    private IWorkspaceRepository workspaceRepository;

    @Transactional
    public void createDashboard(Workspace root, String title) {
        ArgumentGuard.assertNotNull(root);
        ArgumentGuard.assertStringNotNullOrEmpty(title);

        var dashboard = new Dashboard();
        dashboard.setName(title);
        dashboard.setWorkspace(root);
        dashboard.setOwner(root.getUser());
        dashboardRepository.save(dashboard);
    }

    @Transactional
    public boolean createDashboard(Long workspaceId, String title) {
        var workspaceRootOpt = workspaceRepository.findById(workspaceId);

        if (workspaceRootOpt.isEmpty())
            return false;

        var workspaceRoot = workspaceRootOpt.get();
        createDashboard(workspaceRoot, title);
        return true;
    }

    @Transactional
    public void removeDashboard(Dashboard dashboard) {
        ArgumentGuard.assertNotNull(dashboard);
        if (dashboardRepository.existsById(dashboard.getId()))
            dashboardRepository.delete(dashboard);
    }

    @Transactional
    public boolean removeDashboard(Long dashboardId) {
        var boardCandidate = dashboardRepository.findById(dashboardId);
        if (boardCandidate.isEmpty())
            return false;

        var board = boardCandidate.get();

        removeDashboard(board);
        return true;
    }

    @Transactional
    public void renameDashboard(Dashboard dashboard, String newTitle) throws IllegalArgumentException {
        ArgumentGuard.assertNotNull(dashboard);
        ArgumentGuard.assertStringNotNullOrEmpty(newTitle);

        if (!dashboardRepository.existsById(dashboard.getId()))
            return;

        dashboard.setName(newTitle);
        dashboardRepository.save(dashboard);
    }

    @Transactional
    public boolean renameDashboard(Long dashboardId, String title) {
        var boardCandidate = dashboardRepository.findById(dashboardId);

        if (boardCandidate.isEmpty())
            return false;
        try {
            renameDashboard(boardCandidate.get(), title);
        } catch (IllegalArgumentException ex) {
            return false;
        }
        return true;
    }

    @Transactional
    public void addDashboardCard(Dashboard dashboard, TaskCard card) {
        ArgumentGuard.assertNotNull(dashboard, card);

        if (!dashboardRepository.existsById(dashboard.getId()))
            return;

        dashboard.getTaskCards().add(card);
        dashboardRepository.save(dashboard);
    }

    @Transactional
    public void removeDashboardCard(Dashboard dashboard, TaskCard card) {
        ArgumentGuard.assertNotNull(dashboard, card);

        dashboard.getTaskCards().remove(card);
        dashboardRepository.save(dashboard);
    }

    @Transactional
    public void addDashboardMember(Dashboard dashboard, User member) {
        ArgumentGuard.assertNotNull(dashboard, member);

        dashboard.getMembers().add(member);
        dashboardRepository.save(dashboard);
    }

    @Transactional
    public boolean addDashboardMember(Long dashboardId, User user) {
        var boardCandidate = dashboardRepository.findById(dashboardId);
        if (boardCandidate.isEmpty())
            return false;

        try {
            addDashboardMember(boardCandidate.get(), user);
        } catch (IllegalArgumentException ex) {
            return false;
        }
        return true;
    }

    @Transactional
    public void setDashboardOwner(Dashboard dashboard, User newOwner){
        ArgumentGuard.assertNotNull(dashboard, newOwner);

        dashboard.setOwner(newOwner);
    }

    @Transactional
    public void setDashboardWorkspace(Dashboard dashboard, Workspace newWorkspace) {
        ArgumentGuard.assertNotNull(dashboard, newWorkspace);

        dashboard.setWorkspace(newWorkspace);
        dashboardRepository.save(dashboard);
        workspaceRepository.save(newWorkspace);
    }
}
