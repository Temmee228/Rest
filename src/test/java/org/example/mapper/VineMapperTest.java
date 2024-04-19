package org.example.mapper;

import org.example.dto.VineDto;
import org.example.entity.Vine;
import org.example.mapper.impl.VineMapperImpl;
import org.example.util.TestObjectInitializer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class VineMapperTest {
    @Mock
    private VineMapperImpl vineMapper;

    private VineDto expectedVineDto;
    private Vine expectedVine;

    @BeforeEach
    void setUp() {
        expectedVineDto = TestObjectInitializer.initializeVineDto();
        expectedVine = TestObjectInitializer.initializeVine();
    }

    @Test
    void toDto() {
        when(vineMapper.toDto(expectedVine)).thenReturn(expectedVineDto);
        VineDto vineDto = vineMapper.toDto(expectedVine);
        assertEquals(expectedVineDto, vineDto);
    }

    @Test
    void toEntityTest() {
        when(vineMapper.toEntity(expectedVineDto)).thenReturn(expectedVine);
        Vine vine = vineMapper.toEntity(expectedVineDto);
        assertEquals(expectedVine, vine);
    }
}
