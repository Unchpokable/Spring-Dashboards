package project.dashboard.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.dashboard.models.TaskCard;
import project.dashboard.models.TaskCategory;
import project.dashboard.models.internals.TaskCardCreationRequestData;
import project.dashboard.services.DashboardService;
import project.dashboard.services.TaskCardService;
import project.dashboard.services.UserService;
import project.dashboard.services.WorkspaceService;

import java.security.Principal;
import java.util.Objects;

@RestController
@RequestMapping("/api/cards")
public class CardRESTController {
    private final UserService userService;
    private final TaskCardService cardService;
    private final DashboardService dashboardService;
    private final WorkspaceService workspaceService;

    public CardRESTController(UserService userService, TaskCardService cardService, DashboardService dashboardService, WorkspaceService workspaceService) {
        this.userService = userService;
        this.cardService = cardService;
        this.dashboardService = dashboardService;
        this.workspaceService = workspaceService;
    }

    @PostMapping("/add")
    public ResponseEntity<?> addBoardCard(Principal principal, @RequestBody TaskCardCreationRequestData cardCreationData) {
        var board = dashboardService.getDashboardByItsId(cardCreationData.getDashboardId());
        var user = userService.getUserByName(cardCreationData.getAssignedUser());
        if (user == null) {
            return ResponseEntity.status(HttpStatus.I_AM_A_TEAPOT).build();
        }

        var card = new TaskCard();
        card.fillFieldsFromCreationData(cardCreationData);
        card.setBoard(board);
        card.setAssignedUser(user);

        if (!Objects.equals(board.getOwner().getNickname(), principal.getName()))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

        if (dashboardService.addDashboardCard(board, card))
            return ResponseEntity.ok().build();

        return ResponseEntity.badRequest().build();
    }

    @DeleteMapping("/remove")
    public ResponseEntity<?> removeBoardCard(Principal principal, @RequestParam Long cardId, @RequestParam Long boardId) {
        var board = dashboardService.getDashboardByItsId(boardId);

        dashboardService.removeDashboardCard(board, cardId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/shift")
    public ResponseEntity<?> changeCardCategory(@RequestParam("card") Long cardId,
                                                @RequestParam("category") TaskCategory category) {
        cardService.editTaskCardCategory(cardId, category);
        return ResponseEntity.ok().build();
    }
}
