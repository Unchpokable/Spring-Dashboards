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
        //TODO: Authentication check here
        if (dashboardService.createDashboard(workspaceId, title))
            return ResponseEntity.ok().build();
        return ResponseEntity.badRequest().build();
    }

    @DeleteMapping("/remove/{dashboardId}")
    public ResponseEntity<?> removeDashboard(@PathVariable Long dashboardId) {
        //TODO: Auth!!!!!!!!!!
        if (dashboardService.removeDashboard(dashboardId))
            return ResponseEntity.ok().build();
        return ResponseEntity.badRequest().build();
    }

    @PutMapping("/{dashboardId}/rename")
    public ResponseEntity<?> renameDashboard(@PathVariable Long dashboardId, @RequestParam("name") String name) {
        // Should I write EVERYFCKINGWHERE "We need auth checking"??????????????????????????????????????????????????
        if (dashboardService.renameDashboard(dashboardId, name))
            return ResponseEntity.ok().build();
        return ResponseEntity.badRequest().build();
    }

    @PutMapping("/{dashboardId}/addUser")
    public ResponseEntity<?> addDashboardMember(@PathVariable Long dashboardId, @RequestBody User user) {
        // you know what here should be written, isnt?

        if (dashboardService.addDashboardMember(dashboardId, user)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }
}
