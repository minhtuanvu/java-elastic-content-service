package de.funkedigital.fuzo.contentservice.admin.controller;

import de.funkedigital.fuzo.contentservice.admin.models.DlqRescueResult;
import de.funkedigital.fuzo.contentservice.admin.service.DlqRescueService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DlqRescueController {

    private final DlqRescueService dlqRescueService;

    DlqRescueController(DlqRescueService dlqRescueService) {
        this.dlqRescueService = dlqRescueService;
    }

    @GetMapping("/admin/dlq/rescue")
    DlqRescueResult redrive() {
        return dlqRescueService.rescue();
    }

}
