package project.dashboard.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.dashboard.internal.ArgumentGuard;
import project.dashboard.models.Dashboard;
import project.dashboard.models.User;
import project.dashboard.models.Workspace;
import project.dashboard.repos.IDashboardRepository;
import project.dashboard.repos.IUserRepository;
import project.dashboard.repos.IWorkspaceRepository;

import java.util.List;
import java.util.Objects;

@Service
public class WorkspaceService {
    @Autowired
    private IWorkspaceRepository workspaceRepository;
    @Autowired
    private IDashboardRepository dashboardRepository;
    @Autowired
    private IUserRepository userRepository;

    public User getUserByName(String nickname) {
        return userRepository.findByNickname(nickname);
    }

    public List<Workspace> getUserWorkspaces(User user) {
        return workspaceRepository.findByUser(user).orElse(null);
    }

    public List<Workspace> getUserWorkspaces(Long userId) {
        var user = userRepository.findById(userId);
        if (user.isEmpty())
            return null;

        return getUserWorkspaces(user.get());
    }

    @Transactional
    public void createWorkspace(String title, User owner) throws IllegalArgumentException {
        ArgumentGuard.assertStringNotNullOrEmpty(title);
        ArgumentGuard.assertNotNull(owner);

        var newWorkspace = new Workspace();
        newWorkspace.setName(title);
        newWorkspace.setUser(owner);
        workspaceRepository.save(newWorkspace);
    }

    @Transactional
    public boolean createWorkspace(Long ownerId, String title) {
        var userCandidate = userRepository.findById(ownerId);
        if (userCandidate.isEmpty())
            return false;

        var user = userCandidate.get();

        try {
            createWorkspace(title, user);
        } catch (IllegalArgumentException ex) {
            return false;
        }
        return true;
    }

    @Transactional
    public void removeWorkspace(Workspace workspace) throws IllegalArgumentException {
        ArgumentGuard.assertNotNull(workspace);
        if (workspaceRepository.existsById(workspace.getId()))
            workspaceRepository.delete(workspace);
    }

    @Transactional
    public boolean removeWorkspace(Long wsId) throws IllegalArgumentException {
        var wsCandidate = workspaceRepository.findById(wsId);
        if (wsCandidate.isEmpty()) {
            return false;
        }

        var ws = wsCandidate.get();
        removeWorkspace(ws);
        return true;
    }

    @Transactional
    public void addWorkspaceDashboard(Workspace workspace, Dashboard dashboard) throws IllegalArgumentException {
        ArgumentGuard.assertNotNull(workspace, dashboard);

        workspace.getBoards().add(dashboard);
        workspaceRepository.save(workspace);
    }

    @Transactional
    public boolean addWorkspaceDashboard(Long wsId, String title) {
        var wsCandidate = workspaceRepository.findById(wsId);
        if (wsCandidate.isEmpty())
            return false;

        var ws = wsCandidate.get();
        var board = new Dashboard();
        board.setWorkspace(ws);
        board.setOwner(ws.getUser());
        board.setName(title);
        try {
            addWorkspaceDashboard(ws, board);
        } catch (IllegalArgumentException ex) {
            return false;
        }
        return true;
    }

    @Transactional
    public void removeWorkspaceDashboard(Workspace workspace, Dashboard dashboard) throws IllegalArgumentException{
        ArgumentGuard.assertNotNull(workspace, dashboard);

        workspace.getBoards().remove(dashboard);
        workspaceRepository.save(workspace);
    }

    @Transactional
    public boolean removeWorkspaceDashboard(Long wsId, Long dbId) throws IllegalArgumentException {
        var wsCandidate = workspaceRepository.findById(wsId);
        if (wsCandidate.isEmpty())
            return false;

        var workspace = wsCandidate.get();

        return  workspace.getBoards().removeIf(dashboard -> Objects.equals(dashboard.getId(), dbId));
    }

    @Transactional
    public void renameWorkspace(Workspace workspace, String newTitle) throws IllegalArgumentException {
        ArgumentGuard.assertNotNull(workspace);
        ArgumentGuard.assertStringNotNullOrEmpty(newTitle);

        workspace.setName(newTitle);
        workspaceRepository.save(workspace);
    }

    @Transactional
    public boolean renameWorkspace(Long wsId, String newTitle) throws IllegalArgumentException {
        var wsCandidate = workspaceRepository.findById(wsId);
        if (wsCandidate.isEmpty())
            return false;

        var workspace = wsCandidate.get();
        renameWorkspace(workspace, newTitle);
        return true;
    }

    public boolean isUserOwner(Long userId, Long workspaceId) {
        var wsCandidate = workspaceRepository.findById(workspaceId);
        if (wsCandidate.isEmpty())
            return false;

        var ws = wsCandidate.get();

        return Objects.equals(ws.getUser().getId(), userId);
    }

    public List<Dashboard> getWorkspaceDashboards(Long workspaceId) {
        var workspace =  workspaceRepository.findById(workspaceId);
        if (workspace.isEmpty()) {
            return null;
        }
        return workspace.get().getBoards();
    }

    public List<Workspace> getUserWorkspaces(String nickname) {
        User user = userRepository.findByNickname(nickname);
        if (user != null)
         return user.getWorkspaces();
        return null;
    }

    public Workspace getWorkspaceByItsId(Long wsId) {
        var maybeWs = workspaceRepository.findById(wsId);
        return maybeWs.orElse(null);
    }
}
