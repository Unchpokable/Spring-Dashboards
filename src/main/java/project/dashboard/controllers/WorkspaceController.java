package project.dashboard.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import project.dashboard.models.Dashboard;
import project.dashboard.models.User;
import project.dashboard.models.Workspace;
import project.dashboard.services.DashboardService;
import project.dashboard.services.UserService;
import project.dashboard.services.WorkspaceService;

import java.util.List;

@RestController
@RequestMapping("/api/workspaces")
public class WorkspaceController {
    private final WorkspaceService workspaceService;


    @Autowired
    public WorkspaceController(WorkspaceService workspaceService) {
        this.workspaceService = workspaceService;
    }

    @PostMapping("/{userId}/create")
    public ResponseEntity<?> createWorkspace(@PathVariable Long userId, @RequestBody String workspace) {
        if (workspaceService.createWorkspace(userId, workspace))
            return ResponseEntity.ok().build();
        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/{userId}/list")
    public List<Workspace> getUserWorkspaces(@PathVariable Long userId) {
        return workspaceService.getUserWorkspaces(userId);
    }

    @DeleteMapping("/delete/{workspaceId}")
    public ResponseEntity<?> deleteWorkspace(@PathVariable Long workspaceId) {
        //TODO: Authentication check here

        if (workspaceService.removeWorkspace(workspaceId))
            return ResponseEntity.ok().build();

        return ResponseEntity.notFound().build();
    }

    @PutMapping("/rename/{workspaceId}")
    public ResponseEntity<?> editWorkspace(@PathVariable Long workspaceId, @RequestParam("name") String newName) {

        //TODO: Authentication check here
        if (!workspaceService.renameWorkspace(workspaceId, newName)) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{workspaceId}/addDashboard")
    public ResponseEntity<?> addWorkspaceDashboard(@PathVariable Long workspaceId, @RequestBody String dashboard) {
        //TODO: Authentication check here
        if (!workspaceService.addWorkspaceDashboard(workspaceId, dashboard))
            return ResponseEntity.badRequest().build();
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/deleteDashboard/{workspaceId}/{dashboardId}")
    public ResponseEntity<?> removeWorkspaceDashboard(@PathVariable Long workspaceId, @PathVariable Long dashboardId) {
        //TODO: Check Authentication here

        if (!workspaceService.removeWorkspaceDashboard(workspaceId, dashboardId)) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok().build();
    }

    private boolean isUserOwner(Authentication auth, Long workspaceId) {
        var user = (User)auth.getPrincipal();

        return workspaceService.isUserOwner(user.getId(), workspaceId);
    }
}
