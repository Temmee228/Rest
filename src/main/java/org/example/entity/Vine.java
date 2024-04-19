package org.example.entity;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Vine {
    private long id;
    private String name;
    private Long storeID;
}
