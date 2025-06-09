package com.github.myazusa.astrolithabackend.service.micro;

import com.github.myazusa.astrolithabackend.dto.BackendStatsResponseDTO;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.GlobalMemory;
import oshi.software.os.FileSystem;
import oshi.software.os.OSFileStore;
import oshi.software.os.OperatingSystem;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SystemStatsService {
    private final SystemInfo systemInfo = new SystemInfo();

    public BackendStatsResponseDTO getSystemStats() throws InterruptedException {
        BackendStatsResponseDTO stats = new BackendStatsResponseDTO();

        // CPU 使用率
        CentralProcessor processor = systemInfo.getHardware().getProcessor();
        long[] preSystemCpuLoadTicks = processor.getSystemCpuLoadTicks();
        Thread.sleep(1000);
        double cpuLoad = processor.getSystemCpuLoadBetweenTicks(preSystemCpuLoadTicks) * 100; // 百分比
        stats.setCpuUsage(round(cpuLoad, 2));

        // 内存使用率
        GlobalMemory memory = systemInfo.getHardware().getMemory();
        double memoryUsed = (double) (memory.getTotal() - memory.getAvailable()) / memory.getTotal() * 100;
        stats.setMemoryUsage(round(memoryUsed, 2));

        // 系统盘使用量
        OperatingSystem os = systemInfo.getOperatingSystem();
        FileSystem fileSystem = os.getFileSystem();
        List<OSFileStore> fileStores = fileSystem.getFileStores();

        // 找出系统盘（一般是根目录）
        for (OSFileStore store : fileStores) {
            if (store.getMount().equals("/") || store.getMount().toLowerCase().startsWith("c:\\")) {
                long usedBytes = store.getTotalSpace() - store.getUsableSpace();
                double usedGB = usedBytes / 1.0 / 1024 / 1024 / 1024;
                stats.setDiskUsed(round(usedGB, 2));
                break;
            }
        }

        return stats;
    }

    private double round(double value, int places) {
        return Math.round(value * Math.pow(10, places)) / Math.pow(10, places);
    }
}
