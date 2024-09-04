package com.xidian.bankdemo;


import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class EnumTest {


    @Test
    void test1()
    {
        System.out.println(season.spring);
        System.out.println(season.summer);
        System.out.println(season.autumn);
        System.out.println(season.winter);
        System.out.println("----------------------------------");
        for (season value : season.values()) {
            System.out.println(value);
        }
        System.out.println("----------------------------------");
        System.out.println(season.valueOf("spring"));
    }
}

enum season {
    spring("春天",1),
    summer("夏天",2),
    autumn("秋天",3),
    winter("冬天",4);
    private  String zw ;
    private  int index;

    season(String zw, int index) {
        this.zw = zw;
        this.index = index;
    }

    public String getZw() {
        return zw;
    }

    public void setZw(String zw) {
        this.zw = zw;
    }

    @Override
    public String toString() {
        return "season{" +
                "zw='" + zw + '\'' +
                ", index=" + index +
                '}';
    }
}
