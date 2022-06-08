package com.example.trainingsystem.controller;

import com.example.trainingsystem.dto.NewWordDto;
import com.example.trainingsystem.model.Dictionary;
import com.example.trainingsystem.model.Schedule;
import com.example.trainingsystem.model.Word;
import com.example.trainingsystem.repository.DictionaryRepository;
import com.example.trainingsystem.repository.ScheduleRepository;
import com.example.trainingsystem.repository.WordRepository;
import com.example.trainingsystem.service.DictService;
import com.example.trainingsystem.service.WordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;


@Controller
public class WordController {

    private final DictService dictService;
    private final WordService service;
    private final ScheduleRepository scheduleRepository;


    @Autowired
    public WordController(DictService dictService, WordService service, ScheduleRepository scheduleRepository) {
        this.dictService = dictService;
        this.service = service;
        this.scheduleRepository = scheduleRepository;
    }

    @GetMapping("/words/all")
    public String dictionaryPage(@RequestParam("dictId") long dictId, Model model) {
        Dictionary dict = dictService.getById(dictId);
        List<Word> dictWords = service.getAllByDictionary(dict);
        model.addAttribute("dictionary", dict);
        model.addAttribute("words", dictWords);
        return "dict";
    }

    @GetMapping("words/edit")
    public String editPage(@RequestParam("id") long id, Model model) {
        Word word = service.getById(id);
        model.addAttribute("word", word);
        return "edit";
    }

    @GetMapping("/words/addWord")
    public String editNewPage(@RequestParam("id") long dictId, Model model) {
        NewWordDto wordForm = new NewWordDto();
        Dictionary dictionary = dictService.getById(dictId);
        model.addAttribute("wordForm", wordForm);
        model.addAttribute("dictionary", dictionary);
        return "add";
    }

    @PostMapping("/words/edit")
    public String editWord(Word wordForm, Model model, RedirectAttributes redirectAttributes) {
        Word saved = service.editWord(wordForm);
        model.addAttribute(saved);
        redirectAttributes.addAttribute("dictId", saved.getDictionary().getId());
        return "redirect:/words/all";
    }

    @PostMapping("/words/addWord")
    public String createWord(@RequestParam("dictId") long dictId, NewWordDto wordForm, RedirectAttributes redirectAttributes) {
        service.createWord(wordForm, dictId);
        redirectAttributes.addAttribute("dictId", dictId);
        return "redirect:/words/all";
    }

    @GetMapping("/words/delete")
    public String deleteWord(@RequestParam Long id, @RequestParam long dictId, RedirectAttributes redirectAttributes) {
        service.removeById(id);
        redirectAttributes.addAttribute("dictId", dictId);
        return "redirect:/words/all";
    }

    @GetMapping("/words/schedule")
    public String schedulePage(@RequestParam("dictId") long dictId, Model model, RedirectAttributes redirectAttributes) {
        Dictionary dict = dictService.getById(dictId);
        List<Schedule> dictSchedule = scheduleRepository.findAllByDictionary(dict);
        model.addAttribute("dictionary", dict);
        model.addAttribute("schedules", dictSchedule);
        redirectAttributes.addAttribute("dictId", dictId);
        return "schedule";
    }

    @ExceptionHandler(NotFoundException.class)
    public ModelAndView handleNFE(NotFoundException e) {
        ModelAndView modelAndView = new ModelAndView("error");
        modelAndView.addObject("message", e.getMessage());
        return modelAndView;
    }

}
