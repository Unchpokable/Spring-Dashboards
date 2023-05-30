package project.dashboard.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import project.dashboard.models.User;
import project.dashboard.services.DashboardService;
import project.dashboard.services.UserService;
import project.dashboard.services.WorkspaceService;

import java.security.Principal;

@Controller
@RequestMapping("/workspaces")
public class WorkspaceController {
    private final WorkspaceService workspaceService;
    private final UserService userService;
    private final DashboardService dashboardService;

    @Autowired
    public WorkspaceController(WorkspaceService workspaceService, UserService userService, DashboardService dashboardService) {
        this.workspaceService = workspaceService;
        this.userService = userService;
        this.dashboardService = dashboardService;
    }

    @GetMapping("/{userId}")
    public String getUserWorkspaces(@PathVariable Long userId, Model model) {
        var workspaces = workspaceService.getUserWorkspaces(userId);
        var user = userService.getUser(userId);
        model.addAttribute("username", user.getNickname());
        model.addAttribute("workspaceBoardsCount", workspaces.size());
        model.addAttribute("workspaces", workspaces);
        return "workspaces";
    }

    @GetMapping("/workspace/{workspaceId}")
    public String getUserDashboardsOnWorkspace(@PathVariable Long workspaceId, Model model, Principal principal) {
        var dashboards = workspaceService.getWorkspaceDashboards(workspaceId);

        String nickname = principal.getName();
        var sharedDashboards = dashboardService.getSharedDashboardsForUser(nickname);
        model.addAttribute("sharedBoardsCount", sharedDashboards.size());
        model.addAttribute("sharedBoards", sharedDashboards);
        model.addAttribute("userId", userService.getUserByName(principal.getName()).getId());
        model.addAttribute("username", principal.getName());
        model.addAttribute("boardsCount", dashboards.size());
        model.addAttribute("parentWorkspaceName", workspaceService.getWorkspaceByItsId(workspaceId).getName());
        model.addAttribute("workspaceId", workspaceId);
        model.addAttribute("dashboards", dashboards);
        return "boards";
    }

    @PutMapping("/rename/{workspaceId}")
    public ResponseEntity<?> editWorkspace(@PathVariable Long workspaceId, @RequestParam("name") String newName) {
        if (!workspaceService.renameWorkspace(workspaceId, newName)) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.ok().build();
    }
}
