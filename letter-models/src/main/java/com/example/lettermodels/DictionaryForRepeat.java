package com.example.lettermodels;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "repeat_letter")
public class DictionaryForRepeat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "name")
    private String name;

    @Column(name = "words_count")
    private long wordsCount;

    public DictionaryForRepeat(String name, long wordsCount) {
        this.name = name;
        this.wordsCount = wordsCount;
    }
}
