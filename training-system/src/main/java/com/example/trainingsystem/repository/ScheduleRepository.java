package com.example.trainingsystem.repository;

import com.example.trainingsystem.model.Dictionary;
import com.example.trainingsystem.model.Schedule;
import com.example.trainingsystem.model.Word;
import com.example.trainingsystem.model.WordStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    List<Schedule> findAllByDictionary(Dictionary dictionary);

    @Query(value = "select s from schedule s where s.dictionary = ?1 and " +
            "s.status <> ?2 and s.nextTrainDate < ?3 order by s.nextTrainDate") //and s.nextTrainDate < ?3
    List<Schedule> findWordsForRepeat(Dictionary dict, WordStatus newStatus, LocalDate currentDate);

    @Query(value = "select s from schedule s where s.dictionary = ?1 and " +
            "s.status = ?2 order by s.nextTrainDate")
    List<Schedule> findWordsForNewTraining(Dictionary dict, WordStatus newStatus);

    Schedule findByWord(Word word);
}
