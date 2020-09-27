package me.stevenkin.alohajob.common.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
public class Holder<T> {
    @Getter
    @Setter
    private volatile T object;
}
