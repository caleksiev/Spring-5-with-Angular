package com.fmi.spring5.utils;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class FromToSlot {
    Date fromDate;

    Date toDate;
}
