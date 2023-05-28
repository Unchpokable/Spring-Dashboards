package project.dashboard.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import project.dashboard.models.Dashboard;
import project.dashboard.models.User;
import project.dashboard.models.Workspace;
import project.dashboard.services.DashboardService;
import project.dashboard.services.UserService;
import project.dashboard.services.WorkspaceService;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/workspaces")
public class WorkspaceController {
    private final WorkspaceService workspaceService;



    @Autowired
    public WorkspaceController(WorkspaceService workspaceService) {
        this.workspaceService = workspaceService;
    }

//    @PostMapping("/{userId}/create")
//    public ResponseEntity<?> createWorkspace(@PathVariable Long userId, @RequestBody String workspace) {
//        if (workspaceService.createWorkspace(userId, workspace))
//            return ResponseEntity.ok().build();
//        return ResponseEntity.badRequest().build();
//    }

    @GetMapping("/{userId}/create")
    public String createWorkspace(@PathVariable Long userId, String workspaceTitle) { return "create_workspace"; }

    @PostMapping("/create")
    public String createWorkspaces(
            @RequestParam(name = "workspaceName") String workspaceTitle,
            Model model,
            @RequestParam(name = "userId") Long userId
    ) {
        System.out.println(userId);
        try {
            workspaceService.createWorkspace(userId, workspaceTitle);
            return "redirect:/{workspaceTitle}";
        }
        catch (Exception ex)
        {
            model.addAttribute("message", "Something went wrong");
            ex.printStackTrace();
            return "/{userId}/create";
        }
    }

    @GetMapping("/{userId}/list")
    public List<Workspace> getUserWorkspaces(@PathVariable Long userId) {
        return workspaceService.getUserWorkspaces(userId);
    }

    @DeleteMapping("/delete/{workspaceId}")
    public String deleteWorkspace(@PathVariable Long workspaceId, Model model) {
        //TODO: Authentication check here

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

    @GetMapping("/workspaces")
    public String getUserWorkspaces(Principal principal, Model model) {
        String nickname = principal.getName();
        User user = workspaceService.getUserByName(nickname);
        Long userId = user.getId();

        List<Workspace> workspaces = workspaceService.getUserWorkspaces(nickname);
        model.addAttribute("workspaceBoardCount", workspaces.size());
        model.addAttribute("workspaces", workspaces);
        model.addAttribute("username", nickname);
        model.addAttribute("userId", userId);
        return "workspaces";
    }
}
