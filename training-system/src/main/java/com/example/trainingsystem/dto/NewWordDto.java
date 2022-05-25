package com.example.trainingsystem.dto;

import com.example.trainingsystem.model.Dictionary;
import lombok.Data;

@Data
public class NewWordDto {

    private String name;
    private String translation;
    private String context;
    private String example;
//    private LocalDate addedDate;
//    private WordStatus state;
    private Dictionary dictionary;
}
