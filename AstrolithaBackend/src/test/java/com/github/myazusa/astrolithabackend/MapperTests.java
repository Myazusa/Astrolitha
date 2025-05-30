package com.github.myazusa.astrolithabackend;

import com.github.myazusa.astrolithabackend.service.micro.RagSqlService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@SpringBootTest
public class MapperTests {
    @Autowired
    private RagSqlService ragSqlService;

    // 成功
    @Test
    void testQueryParsingStatus(){
        Boolean parsingStatus = ragSqlService.queryParsedStatus("公务员录用体检考生须知.doc");
        log.info("解析状态是：{}",parsingStatus);
    }

    // 成功
    @Test
    void testAddNewFile(){
        boolean state = ragSqlService.addNewFile("aaa.doc","20eb190b-f8c3-4dfc-812e-c784dd58bdae");
        log.info("是否插入成功：{}",state);
    }

    // 成功
    @Test
    void testFileNameExists(){
        boolean state = ragSqlService.fileNameExist("bbb.doc");
        log.info("是否存在：{}",state);
    }

    // 成功
    @Test
    void testDeleteByFileName(){
        boolean state = ragSqlService.deleteByFileName("aaa.doc");
        log.info("是否删除成功：{}",state);
    }

    // 成功
    @Test
    void testAddNewFiles(){
        List<String> fileNames = new ArrayList<>();
        fileNames.add("aaa.doc");
        fileNames.add("bbb.doc");
        fileNames.add("ccc.doc");
        Boolean state = ragSqlService.addNewFiles(fileNames, "20eb190b-f8c3-4dfc-812e-c784dd58bdae");
        log.info("是否插入成功{}",state);
    }

    // 成功
    @Test
    void testUpdateFileName(){
        Boolean state = ragSqlService.updateFileName("aaa.doc", "bbb.doc");
        log.info("是否修改成功{}",state);
    }
}
