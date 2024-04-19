package org.example.dto;

import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class DistilleryVineDto {
    private List<Long> vineID;
    private DistilleryDto distillery;
}
