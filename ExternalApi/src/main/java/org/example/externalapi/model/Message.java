package org.example.externalapi.model;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Message {
    public Long id;
    public String message;
}
