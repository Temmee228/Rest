package org.example.dto;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class VineDto {
    private long id;
    private String name;
    private long storeID;
}
