package com.example.trainingsystem.controller;


import com.example.trainingsystem.model.Dictionary;
import com.example.trainingsystem.repository.DictionaryRepository;
import com.example.trainingsystem.service.DictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class DictController {

    private final DictService service;

    @Autowired
    public DictController(DictService service) {
        this.service = service;
    }

    @GetMapping("/")
    public String indexPage() {
        return "index";
    }

    @GetMapping("/dicts/all")
    public String dictionaryListPage(Model model) {

        List<Dictionary> dicts = service.getAll();
        if (dicts.isEmpty()) {
            return "dictNotAvailable";
        }
        model.addAttribute("dicts", dicts);
        return "dictList";
    }
}
