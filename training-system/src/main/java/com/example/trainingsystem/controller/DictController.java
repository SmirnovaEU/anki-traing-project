package com.example.trainingsystem.controller;


import com.example.trainingsystem.model.Dictionary;
import com.example.trainingsystem.repository.DictionaryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class DictController {
    final DictionaryRepository repository;

    @Autowired
    public DictController(DictionaryRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/")
    public String indexPage() {
        return "index";
    }

    @GetMapping("/dicts/all")
    public String dictionaryListPage(Model model) {

        List<Dictionary> dicts = repository.findAll();;
        model.addAttribute("dicts", dicts);
        return "dictList";
    }
}
