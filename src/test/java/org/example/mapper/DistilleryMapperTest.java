package org.example.mapper;

import org.example.dto.DistilleryDto;
import org.example.entity.Distillery;
import org.example.mapper.impl.DistilleryMapperImpl;
import org.example.util.TestObjectInitializer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DistilleryMapperTest {


    @Mock
    private DistilleryMapperImpl distilleryMapper;

    private DistilleryDto expectedDistilleryDto;
    private Distillery expectedDistillery;

    @BeforeEach
    void setUp() {
        expectedDistilleryDto = TestObjectInitializer.initializeDistilleryDto();
        expectedDistillery = TestObjectInitializer.initializeDistillery();
    }

    @Test
    void toDto() {
        when(distilleryMapper.toDto(expectedDistillery)).thenReturn(expectedDistilleryDto);
        DistilleryDto distilleryDto = distilleryMapper.toDto(expectedDistillery);
        assertEquals(expectedDistilleryDto, distilleryDto);
    }

    @Test
    void toEntityTest() {
        when(distilleryMapper.toEntity(expectedDistilleryDto)).thenReturn(expectedDistillery);
        Distillery distillery = distilleryMapper.toEntity(expectedDistilleryDto);
        assertEquals(expectedDistillery, distillery);
    }


}
