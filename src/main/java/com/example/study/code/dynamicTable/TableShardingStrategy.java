package com.example.study.code.dynamicTable;

public interface TableShardingStrategy {
    /**
     * 获取实际表名
     * @param originalTable 原始表名
     * @param shardingParam 分表参数
     * @return 实际表名
     */
    String getActualTable(String originalTable, Object shardingParam);
}
