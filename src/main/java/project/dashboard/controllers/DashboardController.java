package project.dashboard.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import project.dashboard.models.Dashboard;
import project.dashboard.models.TaskCard;
import project.dashboard.models.TaskCategory;
import project.dashboard.services.DashboardService;
import project.dashboard.services.TaskCardService;
import project.dashboard.services.UserService;
import project.dashboard.services.WorkspaceService;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/dashboards")
public class DashboardController {
    private final DashboardService dashboardService;
    private final WorkspaceService workspaceService;
    private final TaskCardService cardService;
    private final UserService userService;

    @Autowired
    public DashboardController(DashboardService dashboardService, WorkspaceService workspaceService, TaskCardService cardService, UserService userService) {
        this.dashboardService = dashboardService;
        this.workspaceService = workspaceService;
        this.cardService = cardService;
        this.userService = userService;
    }

    @GetMapping("/board/{boardId}")
    public String getDashboard(Principal principal, Model model, @PathVariable Long boardId) {
        var dashboard = dashboardService.getDashboardByItsId(boardId);
        var cardsByCategories = getCardsByCategories(dashboard);

        model.addAttribute("members", dashboard.getMembers());
        model.addAttribute("owner", dashboard.getOwner().getNickname());
        model.addAttribute("workspaceId", dashboard.getWorkspace().getId());
        model.addAttribute("username", principal.getName());
        model.addAttribute("boardId", boardId);
        model.addAttribute("boardTitle", dashboard.getName());
        model.addAttribute("plannedTasks", cardsByCategories.get(TaskCategory.Planned));
        model.addAttribute("inProgressTasks", cardsByCategories.get(TaskCategory.InProgress));
        model.addAttribute("problemTasks", cardsByCategories.get(TaskCategory.SomeProblems));
        model.addAttribute("doneTasks", cardsByCategories.get(TaskCategory.Done));

        return "board";
    }

    private Map<TaskCategory, List<TaskCard>> getCardsByCategories(Dashboard source) {
        var map = new HashMap<TaskCategory, List<TaskCard>>();

        map.put(TaskCategory.Planned, new ArrayList<>());
        map.put(TaskCategory.InProgress, new ArrayList<>());
        map.put(TaskCategory.SomeProblems, new ArrayList<>());
        map.put(TaskCategory.Done, new ArrayList<>());

        source.getTaskCards().stream().forEach(card -> {
            switch (card.getCategory()){
                case Planned:
                    map.get(TaskCategory.Planned).add(card);
                    break;
                case InProgress:
                    map.get(TaskCategory.InProgress).add(card);
                    break;
                case SomeProblems:
                    map.get(TaskCategory.SomeProblems).add(card);
                    break;
                case Done:
                    map.get(TaskCategory.Done).add(card);
                    break;
                default:
                    return;
            }
        });

        return map;
    }
}
