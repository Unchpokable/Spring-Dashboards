package project.dashboard.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import project.dashboard.models.Dashboard;
import project.dashboard.models.User;
import project.dashboard.models.Workspace;
import project.dashboard.services.DashboardService;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/dashboards")
public class DashboardController {
    private final DashboardService dashboardService;

    @Autowired
    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @PostMapping("/{workspaceId}/create")
    public String createDashboard(@PathVariable Long workspaceId, @RequestParam("name") String title, Model model) {
        var dashboard = dashboardService.createDashboard(workspaceId, title);
        if (dashboard == null) {
            model.addAttribute("message", "Something went wrong");
            return "/{workspaceId}/create";
        }
        return " ";
    }

    @DeleteMapping("/remove/{dashboardId}")
    public String removeDashboard(@PathVariable Long dashboardId, Model model) {
        if (dashboardService.removeDashboard(dashboardId))
            return " ";
        model.addAttribute("message", "Something went wrong");
        return "/remove/{dashboardId}";
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

//    @GetMapping("/dashboards")
//    public String getUserDashboards(Principal principal, Model model) {
//        String nickname = principal.getName();
//
//        List<Dashboard> dashboards = dashboardService.getWorkspaceDashboards(nickname);
//        model.addAttribute("dashboardsCount", dashboards.size());
//        model.addAttribute("dashboards", dashboards);
//        return "dashboards";
//
//    }
}
