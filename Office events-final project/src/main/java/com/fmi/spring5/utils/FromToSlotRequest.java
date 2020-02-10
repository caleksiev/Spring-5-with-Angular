package com.fmi.spring5.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@Data
@NoArgsConstructor
public class FromToSlotRequest {
    String fromDate;

    String toDate;
}
