package com.webapp.BoreConnect.BoreConnect.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import com.webapp.BoreConnect.BoreConnect.service.BoreService;

@Controller
@RequestMapping("/api")
public class BoreController {

    @Autowired
    private BoreService boreService;

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @PostMapping("/start")
    @ResponseBody
    public String startBore(@RequestParam int port) {
        try {
            return boreService.startBoreLocal(port).get();
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    @PostMapping("/stop")
    @ResponseBody
    public String stopBore() {
        boreService.stopBore();
        return "Bore service stopped";
    }

    @GetMapping("/output")
    @ResponseBody
    public String getOutput() {
        return boreService.getCurrentUrl();
    }
}