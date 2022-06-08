package com.example.trainingsystem.controller;

import com.example.trainingsystem.model.Dictionary;
import com.example.trainingsystem.model.Result;
import com.example.trainingsystem.model.Training;
import com.example.trainingsystem.repository.DictionaryRepository;
import com.example.trainingsystem.service.DictService;
import com.example.trainingsystem.service.TrainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class TrainingController {
    private final TrainService service;

    @Autowired
    public TrainingController(TrainService service) {
        this.service = service;
    }

    @GetMapping("/trainings/new")
    public String getNewTraining(@RequestParam("dictId") long dictId, Model model) {

        Training training = service.newTraining(dictId);
        model.addAttribute("training", training);
        model.addAttribute("dictionary", training.getDictionary());
        if (training.getWords().isEmpty())
            return "nowords";
        return "training";
    }

    @GetMapping("/trainings/repeat")
    public String getRepeatTraining(@RequestParam("dictId") long dictId, Model model) {

        Training training = service.repeatTraining(dictId);
        model.addAttribute("training", training);
        model.addAttribute("dictionary", training.getDictionary());
        if (training.getWords().isEmpty()) return "nowords";
        return "training";
    }

    @PostMapping("/trainings")
    public String createRepeatTraining(long id, Model model) {
        Training training = service.getTraining(id);
        List<Result> results = service.doTraining(training);

        model.addAttribute("dictionary", training.getDictionary());
        model.addAttribute("results", results);
        return "result";
    }


}
