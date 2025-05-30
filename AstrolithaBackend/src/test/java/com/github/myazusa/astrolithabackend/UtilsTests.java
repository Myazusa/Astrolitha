package com.github.myazusa.astrolithabackend;

import com.github.myazusa.astrolithabackend.common.util.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

@Slf4j
@SpringBootTest
public class UtilsTests {
    // 成功
    @Test
    void testUUID(){
        UUID uuid = UUID.randomUUID();
        log.info(uuid.toString());
    }
    // 成功
    @Test
    void testCalculateBCryptHash(){
        String calculateBCryptHash = SecurityUtils.calculateBCryptHash("114514At1919810");
        log.info(calculateBCryptHash);
    }
}
