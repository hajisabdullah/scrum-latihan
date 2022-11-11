package com.example.sitodo;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Tag("unit")
class SitodoApplicationTest {

    @Test
    void contextLoads() {
        SitodoApplication.main(new String[]{});

        // There is nothing to assert because the SUT (i.e. the method) is a
        // void method. This test simply invokes it.
    }
}
