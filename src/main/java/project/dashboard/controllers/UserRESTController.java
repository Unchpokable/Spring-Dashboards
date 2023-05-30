package project.dashboard.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.dashboard.internal.ArgumentGuard;
import project.dashboard.models.internals.UpdateUserInfoRequestData;
import project.dashboard.services.DashboardService;
import project.dashboard.services.UserService;

@RestController
@RequestMapping("/api/users")
public class UserRESTController {
    private final UserService userService;
    private final DashboardService dashboardService;

    public UserRESTController(UserService userService, DashboardService dashboardService) {
        this.userService = userService;
        this.dashboardService = dashboardService;
    }

    @GetMapping("/validate")
    public ResponseEntity<?> validateUser(@RequestParam("username") String username, @RequestParam("boardId") Long callerBoard) {
        var user = userService.getUserByName(username);
        var board = dashboardService.getDashboardByItsId(callerBoard);
        if (user == null || board.getOwner().getNickname() != username
                         || board.getMembers().stream().anyMatch(n -> n.getNickname() != username)) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok().build();
    }

    @PostMapping("/update")
    public ResponseEntity<?> updateUserInfo(@RequestParam Long userId, @RequestBody UpdateUserInfoRequestData userData) {
        try {
            ArgumentGuard.assertEmailIsValid(userData.getEmail());
            userService.updateUserInfo(userData, userId);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        return ResponseEntity.ok().build();
    }
}
