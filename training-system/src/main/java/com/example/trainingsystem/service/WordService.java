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
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;


@Service
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
}
