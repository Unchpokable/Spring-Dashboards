package project.dashboard.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.dashboard.internal.ArgumentGuard;
import project.dashboard.models.Dashboard;
import project.dashboard.models.TaskCard;
import project.dashboard.models.User;
import project.dashboard.models.Workspace;
import project.dashboard.repos.ICardRepository;
import project.dashboard.repos.IDashboardRepository;
import project.dashboard.repos.IUserRepository;
import project.dashboard.repos.IWorkspaceRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Service
public class DashboardService {
    @Autowired
    private IDashboardRepository dashboardRepository;

    @Autowired
    private IWorkspaceRepository workspaceRepository;

    @Autowired
    private ICardRepository cardRepository;

    @Autowired
    private IUserRepository userRepository;

    @Transactional
    public Dashboard createDashboard(Workspace root, String title) {
        ArgumentGuard.assertNotNull(root);
        ArgumentGuard.assertStringNotNullOrEmpty(title);

        var dashboard = new Dashboard();
        dashboard.setName(title);
        dashboard.setWorkspace(root);
        dashboard.setOwner(root.getUser());
        dashboardRepository.save(dashboard);
        return dashboard;
    }

    @Transactional
    public Dashboard createDashboard(Long workspaceId, String title) {
        var workspaceRootOpt = workspaceRepository.findById(workspaceId);

        if (workspaceRootOpt.isEmpty())
            return null;

        var workspaceRoot = workspaceRootOpt.get();
        return createDashboard(workspaceRoot, title);
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
    public boolean addDashboardCard(Dashboard dashboard, TaskCard card) {
        ArgumentGuard.assertNotNull(dashboard, card);

        if (!dashboardRepository.existsById(dashboard.getId()))
            return false;

        try {
            dashboard.getTaskCards().add(card);
            dashboardRepository.save(dashboard);
        } catch (Exception ex) {
            return false;
        }
        return true;
    }

    @Transactional
    public void removeDashboardCard(Dashboard dashboard, TaskCard card) {
        ArgumentGuard.assertNotNull(dashboard, card);

        dashboard.getTaskCards().remove(card);
        dashboardRepository.save(dashboard);
    }

    @Transactional
    public void removeDashboardCard(Dashboard dashboard, Long cardId) {
        ArgumentGuard.assertNotNull(dashboard, cardId);
        var card = cardRepository.findById(cardId).orElse(null);
        if (card != null) {
            cardRepository.delete(card);
        }
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

    public List<Dashboard> getWorkspaceDashboards(String nickname) {
        var userCandidate = userRepository.findByNickname(nickname);
        if (userCandidate == null)
            return null;

        return null;
    }

    public Dashboard getDashboardByItsId(Long dbId) {
        return dashboardRepository.findById(dbId).orElse(null);
    }

    public List<Dashboard> getSharedDashboardsForUser(String nickname) {
        var user = userRepository.findByNickname(nickname);
        if (user == null)
            return new ArrayList<>();

        //TODO: VERY VERY VERY UNEFFECTIVE. It was worth to do something with this thing if there was a free time
        return dashboardRepository.findAll().stream().filter(board -> board.getMembers().contains(user))
                .toList();
    }
}
