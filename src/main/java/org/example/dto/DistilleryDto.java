package org.example.dto;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString

public class DistilleryDto {
    private long id;
    private String name;
}
