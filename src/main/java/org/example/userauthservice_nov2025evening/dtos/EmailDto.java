package org.example.userauthservice_nov2025evening.dtos;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class EmailDto {
    private String to;
    private String from;
    private String subject;
    private String body;
}
