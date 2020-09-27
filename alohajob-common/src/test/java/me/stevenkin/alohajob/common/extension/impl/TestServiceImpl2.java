package me.stevenkin.alohajob.common.extension.impl;

import me.stevenkin.alohajob.common.extension.SpiImp;
import me.stevenkin.alohajob.common.extension.TestService;
import me.stevenkin.alohajob.common.extension.WareService;
import org.springframework.beans.factory.annotation.Autowired;

@SpiImp(name = "test2")
public class TestServiceImpl2 implements TestService {
    @Autowired
    private WareService wareService;

    @Override
    public String hello() {
        return wareService.getString("test2");
    }
}
