package project.dashboard.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import project.dashboard.services.UserService;
import project.dashboard.services.WorkspaceService;

import java.security.Principal;

@RestController
@RequestMapping("/api/workspaces")
public class WorkspaceRESTController {
    private final WorkspaceService workspaceService;
    private final UserService userService;

    @Autowired
    public WorkspaceRESTController(WorkspaceService workspaceService, UserService userService) {
        this.workspaceService = workspaceService;
        this.userService = userService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createWorkspaces(Principal principal, @RequestBody String workspaceTitle) {
        var user = userService.getUserByName(principal.getName());
        if (user == null)
            return ResponseEntity.badRequest().build();
        var userId = user.getId();
        if (!workspaceService.createWorkspace(userId, workspaceTitle))
            return ResponseEntity.badRequest().build();
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete/{workspaceId}")
    public ResponseEntity<?> deleteWorkspace(@PathVariable Long workspaceId, Principal principal) {
        var ownerId = workspaceService.getWorkspaceByItsId(workspaceId).getUser();
        if(workspaceService.removeWorkspace(workspaceId))
            return ResponseEntity.ok().build();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @PutMapping("/rename")
    public ResponseEntity<?> renameWorkspace(@RequestParam("name") String newName, @RequestParam("workspace") Long wsId) {
        if (workspaceService.renameWorkspace(wsId, newName))
            return ResponseEntity.ok().build();
        return ResponseEntity.badRequest().build();
    }
}
