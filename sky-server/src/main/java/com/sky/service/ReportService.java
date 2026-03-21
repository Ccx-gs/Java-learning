package com.sky.service;

import com.sky.vo.TurnoverReportVO;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface ReportService {
        /**
        * 营业额统计方法
        *
        * @param begin 起始时间
        * @param end   结束时间
        * @return 营业额统计结果
        */
        TurnoverReportVO turnoverStatistics(LocalDate begin, LocalDate end);
}
