package com.github.myazusa.astrolithabackend.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
public class BackendStatsResponseDTO {
    private double cpuUsage;
    private double memoryUsage;
    private double diskUsed;
    private long fileCount;
}
