package project.dashboard.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.dashboard.models.Dashboard;
import project.dashboard.models.User;
import project.dashboard.services.DashboardService;

@RestController
@RequestMapping("/api/dashboards")
public class DashboardController {
    private final DashboardService dashboardService;

    @Autowired
    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @PostMapping("/{workspaceId}/create")
    public ResponseEntity<?> createDashboard(@PathVariable Long workspaceId, @RequestParam("name") String title) {
        var dashboard = dashboardService.createDashboard(workspaceId, title);
        if (dashboard == null)
            return ResponseEntity.ok().build();
        return ResponseEntity.badRequest().build();
    }

    @DeleteMapping("/remove/{dashboardId}")
    public ResponseEntity<?> removeDashboard(@PathVariable Long dashboardId) {
        if (dashboardService.removeDashboard(dashboardId))
            return ResponseEntity.ok().build();
        return ResponseEntity.badRequest().build();
    }

    @PutMapping("/{dashboardId}/rename")
    public ResponseEntity<?> renameDashboard(@PathVariable Long dashboardId, @RequestParam("name") String name) {
        if (dashboardService.renameDashboard(dashboardId, name))
            return ResponseEntity.ok().build();
        return ResponseEntity.badRequest().build();
    }

    @PutMapping("/{dashboardId}/addUser")
    public ResponseEntity<?> addDashboardMember(@PathVariable Long dashboardId, @RequestBody User user) {
        if (dashboardService.addDashboardMember(dashboardId, user)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }
}
