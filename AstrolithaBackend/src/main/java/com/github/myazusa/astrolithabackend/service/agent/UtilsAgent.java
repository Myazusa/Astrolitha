package com.github.myazusa.astrolithabackend.service.agent;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class UtilsAgent {
    @Tool(name = "queryLocalTime", description = "查询当前时间的工具方法。返回当前时间的年月日时分秒。")
    public String queryLocalTime(){
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy年MM月dd日HH时mm分ss秒");

        return "当前时间是：" + now.format(formatter);
    }

    @Tool(name = "queryLocalZone", description = "查询当前所属地区的工具方法。返回当前地区国家名称。")
    public String queryLocalZone(){
        return "当前时区是：" + Locale.getDefault().getDisplayCountry();
    }
}
