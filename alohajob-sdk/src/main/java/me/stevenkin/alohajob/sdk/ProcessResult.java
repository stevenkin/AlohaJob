package me.stevenkin.alohajob.sdk;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ProcessResult {
    private ProcessResultType type;
    private String msg;
}
