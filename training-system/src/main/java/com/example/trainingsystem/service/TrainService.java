package com.example.trainingsystem.service;

import com.example.trainingsystem.controller.NotFoundException;
import com.example.trainingsystem.model.*;
import com.example.trainingsystem.repository.*;
import com.example.trainingsystem.security.UserSecurity;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class TrainService {
    private final ScheduleRepository scheduleRepository;
    private final ResultRepository resultRepository;
    private final DictionaryRepository dictRepository;
    private final WordRepository wordRepository;
    private final SettingsRepository setRepository;
    private final TrainingRepository trainingRepository;

    @Autowired
    public TrainService(ScheduleRepository scheduleRepository, ResultRepository resultRepository, DictionaryRepository dictRepository, WordRepository wordRepository, SettingsRepository setRepository, TrainingRepository trainingRepository) {
        this.scheduleRepository = scheduleRepository;
        this.resultRepository = resultRepository;
        this.dictRepository = dictRepository;
        this.wordRepository = wordRepository;
        this.setRepository = setRepository;
        this.trainingRepository = trainingRepository;
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    public Training newTraining(long dictId) {
        Dictionary dict = dictRepository.findById(dictId).orElseThrow(NotFoundException::new);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = ((UserSecurity) authentication.getPrincipal()).getUser();
        int newWordsQuantity = setRepository.findFirstByUser(user).getNewWordsInTrain();
        List<Schedule> schedules = scheduleRepository.findWordsForNewTraining(dict, WordStatus.NEW);
        List<Word> wordList = schedules.stream().map(Schedule::getWord).limit(newWordsQuantity).collect(Collectors.toList());
        Training training = new Training(dict, false, wordList, LocalDate.now());
        Training saved = trainingRepository.save(training);

        return saved;
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    public Training repeatTraining(long dictId) {
        Dictionary dict = dictRepository.findById(dictId).orElseThrow(NotFoundException::new);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = ((UserSecurity) authentication.getPrincipal()).getUser();
        int repeatWordsQuantity = setRepository.findFirstByUser(user).getRepeatWordsInTrain();
        List<Schedule> schedules = scheduleRepository.findWordsForRepeat(dict, WordStatus.NEW, LocalDate.now());
        List<Word> wordList = schedules.stream().map(Schedule::getWord).limit(repeatWordsQuantity).collect(Collectors.toList());
        Training training = new Training(dict, true, wordList, LocalDate.now());
        Training saved = trainingRepository.save(training);

        return saved;
    }

    public List<Result> doTraining(Training training) {
        List<Result> results = trainWords(training);
        formSchedule(training);
        return results;
    }

    public Training getTraining(long id) {
        return trainingRepository.findById(id).orElseThrow();
    }

    private List<Result> trainWords(Training training) {
        val random = new Random();
        List<Word> words = training.getWords();
        List<Result> results = new ArrayList<>();
        for (Word word : words) {
            Result result = new Result(training, word, random.nextBoolean());
            results.add(result);
            resultRepository.save(result);
        }
        return results;
    }

    private void formSchedule(Training training) {
        List<Result> results = resultRepository.findAllByTraining(training);
        for (Result result : results) {
            Word word = result.getWord();
            Schedule schedule = scheduleRepository.findByWord(word);
            //обновить статус и стадию изучения слова
            updateWordStatus(schedule, result, training.isRepeat());
            //обновить даты и количество тренировок
            updateSchedule(schedule, training);
            scheduleRepository.save(schedule);
        }
    }

    private void updateWordStatus(Schedule schedule, Result result, Boolean isRepeat) {
        if (isRepeat) {
            if (result.isSuccess()) {

                if (schedule.getStage().equals(LearningStage.STAGE6)) {
                    //если этап последний, то переводим в статус "выучено"
                    schedule.setStatus(WordStatus.LEARNT);
                    schedule.setLearntDate(LocalDate.now());
                } else {
                    //если этап не последний, переводим на следующий этап
                    int nextStageIndex = schedule.getStage().ordinal() + 2;
                    LearningStage nextStage = LearningStage.valueOf("STAGE" + nextStageIndex);
                    schedule.setStage(nextStage);
                }
            }
        } else {
            //если новая тренировка, меняем статус
            schedule.setStatus(WordStatus.IS_LEARNING);
        }
    }

    private Schedule updateSchedule(Schedule schedule, Training training) {
        schedule.setTotalTrainNumber(schedule.getTotalTrainNumber() + 1);
        schedule.setLastTrainDate(training.getTrainingDate());
        int daysTillNextTrain = schedule.getStage().getDaysTillNextTrain();
        schedule.setNextTrainDate(training.getTrainingDate().plusDays(daysTillNextTrain));
        return schedule;
    }
}
