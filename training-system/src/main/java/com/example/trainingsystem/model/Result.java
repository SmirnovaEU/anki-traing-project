package com.example.trainingsystem.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "results")
public class Result {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(targetEntity = Training.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "training_id", nullable = false)
    private Training training;

    @ManyToOne(targetEntity = Word.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "word_id", nullable = false)
    private Word word;

    @Column(name = "success", nullable = false)
    private boolean success;

    public Result(Training training, Word word, boolean success) {
        this.training = training;
        this.word = word;
        this.success = success;
    }
}
