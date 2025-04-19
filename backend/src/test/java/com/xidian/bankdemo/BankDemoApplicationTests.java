package com.xidian.bankdemo;

import com.xidian.bankdemo.mapper.UserMapper;
import com.xidian.bankdemo.util.TokenUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BankDemoApplicationTests {
    @Autowired
    private UserMapper userMapper;

    @Test
    void contextLoads() {
//        System.out.println();
        Type a = Type.INSERT;
        int b = Type.UPDATE.ordinal();
        System.out.println(a);
        System.out.println(b);
    }

}
enum Type {
    INSERT,DELETE,UPDATE,QUERY
}