package com.example.trainingsystem.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "results")
@NamedEntityGraph(name = "training-results-entity-graph",
        attributeNodes = {
                @NamedAttributeNode("training")
        }
)
public class Result {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(targetEntity = Training.class, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "training_id", nullable = false)
    private Training training;

    @BatchSize(size = 10)
    @ManyToOne(targetEntity = Word.class, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
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
