package com.fmi.spring5.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class FromToSlot {
    LocalDateTime from;
    LocalDateTime to;
}
