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

    @PostMapping("/create")
    public String createWorkspaces(Principal principal, @RequestBody String workspaceTitle, Model model) {
            var user = userService.getUserByName(principal.getName());
            if (user == null)
                return "errorpage404";
            var userId = user.getId();
            if (!workspaceService.createWorkspace(userId, workspaceTitle))
                return "workspaces";
            return "workspaces";
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

        model.addAttribute("username", principal.getName());
        model.addAttribute("boardsCount", dashboards.size());
        model.addAttribute("parentWorkspaceName", workspaceService.getWorkspaceByItsId(workspaceId).getName());
        model.addAttribute("workspaceId", workspaceId);
        model.addAttribute("dashboards", dashboards);
        return "boards";
    }

    @DeleteMapping("/delete/{workspaceId}")
    public String deleteWorkspace(@PathVariable Long workspaceId, Model model) {
        try {
            workspaceService.removeWorkspace(workspaceId);
            return "workspaces";
        }
        catch (Exception ex){
            model.addAttribute("message", "Something went wrong");
            ex.printStackTrace();
            return "/delete/{workspaceId}";
        }
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
    public String addWorkspaceDashboard(@PathVariable Long workspaceId, @RequestBody String dashboard, Model model) {
        //TODO: Authentication check here
        try {
            workspaceService.addWorkspaceDashboard(workspaceId, dashboard);
            return "redirect:/dashboards";
        }
        catch (Exception ex)
        {
            model.addAttribute("message", "Something went wrong");
            ex.printStackTrace();
            return "/{workspaceId}/addDashboard";
        }
    }

    @DeleteMapping("/deleteDashboard/{workspaceId}/{dashboardId}")
    public String removeWorkspaceDashboard(@PathVariable Long workspaceId, @PathVariable Long dashboardId, Model model) {
        //TODO: Check Authentication here

        try{
            workspaceService.removeWorkspaceDashboard(workspaceId, dashboardId);
        }
        catch (Exception ex)
        {
            model.addAttribute("message", "Something went wrong");
            ex.printStackTrace();
            return "redirect:/deleteDashboard/{workspaceId}/{dashboardId}";
        }

        return "dashboards";
    }

    private boolean isUserOwner(Authentication auth, Long workspaceId) {
        var user = (User)auth.getPrincipal();
        return workspaceService.isUserOwner(user.getId(), workspaceId);
    }
}
