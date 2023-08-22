package com.ead.course.dtos;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
public class SubscritionDTO {

    @NotNull
    private UUID userId;
}
