package com.example.trainingsystem.service;

import com.example.trainingsystem.controller.NotFoundException;
import com.example.trainingsystem.dto.NewWordDto;
import com.example.trainingsystem.mapper.WordMapper;
import com.example.trainingsystem.model.Dictionary;
import com.example.trainingsystem.model.Schedule;
import com.example.trainingsystem.model.Word;
import com.example.trainingsystem.repository.DictionaryRepository;
import com.example.trainingsystem.repository.ScheduleRepository;
import com.example.trainingsystem.repository.WordRepository;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import lombok.extern.log4j.Log4j2;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Service
@Log4j2
public class WordService {
    private final WordRepository repository;
    private final DictionaryRepository dictRepository;
    private final ScheduleRepository scheduleRepository;
    private static final WordMapper wordMapper = Mappers.getMapper(WordMapper.class);

    @Autowired
    public WordService(WordRepository repository, DictionaryRepository dictRepository, ScheduleRepository scheduleRepository) {
        this.repository = repository;
        this.dictRepository = dictRepository;
        this.scheduleRepository = scheduleRepository;
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    public void createWord(NewWordDto wordDto, long dictId) {
        Word word = new Word();
        wordMapper.updateWordFromDto(wordDto, word);

        Dictionary dictionary = dictRepository.findById(dictId).orElseThrow(NotFoundException::new);
        word.setDictionary(dictionary);
        word.setAddedDate(LocalDate.now());
        repository.save(word);
        Schedule schedule = new Schedule(word);
        scheduleRepository.save(schedule);
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    public Word editWord(Word wordDto) {
        Word word = repository.findById(wordDto.getId()).orElseThrow(NotFoundException::new);
        word.setTranslation(wordDto.getTranslation());
        word.setContext(wordDto.getContext());
        word.setExample(wordDto.getExample());
        return repository.save(word);
    }

    @HystrixCommand(fallbackMethod = "fallbackGetAllWords")
    public List<Word> getAllByDictionary(Dictionary dict) {
        return repository.findAllByDictionary(dict);
    }

    @HystrixCommand(fallbackMethod = "fallbackGetWord")
    public Word getById(long dictId) {
        return repository.findById(dictId).orElseThrow(NotFoundException::new);
    }

    @HystrixCommand(fallbackMethod = "fallbackRemoveWord")
    public void removeById(long dictId) {
        repository.deleteById(dictId);
    }

    public Word fallbackGetWord(long dictId) {
        log.error(String.format("Dictionary id = %d is not available", dictId));
        return null;
    }

    public List<Word> fallbackGetAllWords(Dictionary dict) {
        log.error(String.format("Dictionary %s is not available", dict.getName()));
        return new ArrayList<Word>();
    }

    public void fallbackRemoveWord(long dictId) {
        log.error(String.format("Dictionary id = %d is not available", dictId));
    }
}
