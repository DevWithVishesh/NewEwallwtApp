package com.icsd.annotation;


import lombok.AllArgsConstructor;
import lombok.Data;
@Data
@AllArgsConstructor
@ValidateCondition(message = "Day can't be null when frequency is monthly")
public class FrequencyDTO {
    private Frequency frequency;
    private int day;
}
