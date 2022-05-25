package com.example.trainingsystem.mapper;

import com.example.trainingsystem.dto.NewWordDto;
import com.example.trainingsystem.model.Word;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper
public interface WordMapper {
    WordMapper MAPPER = Mappers.getMapper( WordMapper.class );

    @Mapping(target = "id", ignore = true)
    Word dtoToWord(NewWordDto dto);

    @Mapping(target = "id", ignore = true)
    void updateWordFromDto(NewWordDto dto, @MappingTarget Word word);
}
