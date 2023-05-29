package project.dashboard.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import project.dashboard.models.Dashboard;
import project.dashboard.models.User;
import project.dashboard.services.DashboardService;
import project.dashboard.services.WorkspaceService;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/dashboards")
public class DashboardController {
    private final DashboardService dashboardService;
    private final WorkspaceService workspaceService;

    @Autowired
    public DashboardController(DashboardService dashboardService, WorkspaceService workspaceService) {
        this.dashboardService = dashboardService;
        this.workspaceService = workspaceService;
    }

    @PostMapping("/create/{workspaceId}")
    public String createDashboard(Principal principal, @PathVariable Long workspaceId, @RequestBody String title,
                                  Model model) {
        var dashboard = dashboardService.createDashboard(workspaceId, title);
        if (dashboard == null) {
            return "boards";
        }

        var dashboards = workspaceService.getWorkspaceDashboards(workspaceId);
        model.addAttribute("ownedBoards", dashboards);
        return "boards";
    }

    @PostMapping("/remove/{dashboardId}")
    public String removeDashboard(@PathVariable Long dashboardId, @RequestParam("rootWorkspace") Long wsId, Model model) {
        if (!dashboardService.removeDashboard(dashboardId))
            return "boards";

        var boards = workspaceService.getWorkspaceDashboards(wsId);
        model.addAttribute("ownedBoards", boards);
        return "boards";
    }

//    @PutMapping("/{dashboardId}/rename")
//    public ResponseEntity<?> renameDashboard(@PathVariable Long dashboardId, @RequestParam("name") String name) {
//        if (dashboardService.renameDashboard(dashboardId, name))
//            return ResponseEntity.ok().build();
//        return ResponseEntity.badRequest().build();
//    }

//    @PutMapping("/{dashboardId}/addUser")
//    public ResponseEntity<?> addDashboardMember(@PathVariable Long dashboardId, @RequestBody User user) {
//        if (dashboardService.addDashboardMember(dashboardId, user)) {
//            return ResponseEntity.ok().build();
//        }
//        return ResponseEntity.badRequest().build();
//    }

    @GetMapping("/dashboards")
    public String getUserDashboards(Principal principal, Model model, @RequestParam("workspaceId") Long workspaceId) {
        String nickname = principal.getName();

        List<Dashboard> dashboards = dashboardService.getWorkspaceDashboards(nickname);
        model.addAttribute("dashboardsCount", dashboards.size());
        model.addAttribute("dashboards", dashboards);
        return "dashboards";
    }
}
