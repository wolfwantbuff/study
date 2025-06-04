package com.example.study.code.dynamicTable;

public class DateShardingStrategy implements TableShardingStrategy {

    @Override
    public String getActualTable(String originalTable, Object shardingParam) {
        // 假设shardingParam是日期字符串，如"2023-01"
        return originalTable + "_" + shardingParam;
    }
}
