package project.dashboard.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import project.dashboard.services.DashboardService;
import project.dashboard.services.UserService;
import project.dashboard.services.WorkspaceService;

import java.security.Principal;

@RestController
@RequestMapping("/api/dashboards")
public class DashboardRESTController {
    private final DashboardService dashboardService;
    private final WorkspaceService workspaceService;
    private final UserService userService;

    @Autowired
    public DashboardRESTController(DashboardService dashboardService, WorkspaceService workspaceService, UserService userService) {
        this.dashboardService = dashboardService;
        this.workspaceService = workspaceService;
        this.userService = userService;
    }

    @PostMapping("/create/{workspaceId}")
    public ResponseEntity<?> createDashboard(Principal principal, @PathVariable Long workspaceId, @RequestBody String title) {
        var dashboard = dashboardService.createDashboard(workspaceId, title);
        dashboard.setOwner(userService.getUserByName(principal.getName()));

        if (dashboard == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(dashboard);
    }

    @DeleteMapping("/delete/{dashboardId}")
    public ResponseEntity<?> removeDashboard(@PathVariable Long dashboardId) {
        if (!dashboardService.removeDashboard(dashboardId))
            return ResponseEntity.badRequest().build();
        return ResponseEntity.ok().build();
    }

    @PutMapping("/addmember")
    public ResponseEntity<?> addDashboardMember(@RequestParam("user") String username, @RequestParam("board") Long boardId) {
        var user = userService.getUserByName(username);
        if (user == null)
            return ResponseEntity.badRequest().build();

        var board = dashboardService.getDashboardByItsId(boardId);
        if (board == null)
            return ResponseEntity.badRequest().build();
        try {
            dashboardService.addDashboardMember(board, user);
        } catch ( Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        return ResponseEntity.ok().build();
    }

    @PutMapping("/rename")
    public ResponseEntity<?> renameDashboard(@RequestParam("name") String newName, @RequestParam("board") Long boardId) {
        if (dashboardService.renameDashboard(boardId, newName))
            return ResponseEntity.ok().build();
        return ResponseEntity.badRequest().build();
    }
}
