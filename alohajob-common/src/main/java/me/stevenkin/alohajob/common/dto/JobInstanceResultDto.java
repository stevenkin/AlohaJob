package me.stevenkin.alohajob.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobInstanceResultDto {
    private Integer status;

    private String msg;
}
