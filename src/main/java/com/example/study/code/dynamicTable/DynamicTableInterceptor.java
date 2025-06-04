//package com.example.study.code.dynamicTable;
//
//import com.baomidou.mybatisplus.core.toolkit.PluginUtils;
//import org.apache.ibatis.executor.statement.StatementHandler;
//import org.apache.ibatis.mapping.BoundSql;
//import org.apache.ibatis.mapping.MappedStatement;
//import org.apache.ibatis.plugin.*;
//import org.apache.ibatis.reflection.MetaObject;
//import org.apache.ibatis.reflection.SystemMetaObject;
//
//import java.sql.Connection;
//import java.util.Properties;
//
//@Intercepts({@Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class, Integer.class})})
//public class DynamicTableInterceptor implements Interceptor {
//
//    @Override
//    public Object intercept(Invocation invocation) throws Throwable {
//        StatementHandler statementHandler = PluginUtils.realTarget(invocation.getTarget());
//        MetaObject metaObject = SystemMetaObject.forObject(statementHandler);
//
//        // 获取MappedStatement
//        MappedStatement mappedStatement = (MappedStatement) metaObject.getValue("delegate.mappedStatement");
//
//        // 获取方法上的DynamicTable注解
//        DynamicTable dynamicTable = getDynamicTableAnnotation(mappedStatement);
//        if (dynamicTable == null) {
//            return invocation.proceed();
//        }
//
//        // 获取原始SQL
//        BoundSql boundSql = statementHandler.getBoundSql();
//        String originalSql = boundSql.getSql();
//
//        // 获取分表参数
//        Object shardingParam = boundSql.getParameterObject();
//
//        // 创建分表策略实例
//        TableShardingStrategy strategy = dynamicTable.strategy().newInstance();
//
//        // 替换SQL中的表名
//        String newSql = replaceTableNames(originalSql, strategy, shardingParam);
//
//        // 修改SQL
//        metaObject.setValue("delegate.boundSql.sql", newSql);
//
//        return invocation.proceed();
//    }
//
//    private DynamicTable getDynamicTableAnnotation(MappedStatement mappedStatement) {
//        try {
//            String id = mappedStatement.getId();
//            String className = id.substring(0, id.lastIndexOf("."));
//            String methodName = id.substring(id.lastIndexOf(".") + 1);
//
//            Class<?> clazz = Class.forName(className);
//            // 先检查方法上的注解
//            DynamicTable methodAnnotation = clazz.getMethod(methodName,
//                    mappedStatement.getParameterMap().getType()).getAnnotation(DynamicTable.class);
//            if (methodAnnotation != null) {
//                return methodAnnotation;
//            }
//            // 再检查类上的注解
//            return clazz.getAnnotation(DynamicTable.class);
//        } catch (Exception e) {
//            return null;
//        }
//    }
//
//    private String replaceTableNames(String originalSql, TableShardingStrategy strategy, Object shardingParam) {
//        // 这里需要根据实际情况解析SQL并替换表名
//        // 简单实现：假设原始表名为"order"，替换为"order_202301"等形式
//        // 实际项目中可能需要更复杂的SQL解析
//        return originalSql.replaceAll("\\border\\b", strategy.getActualTable("order", shardingParam));
//    }
//
//    @Override
//    public Object plugin(Object target) {
//        return Plugin.wrap(target, this);
//    }
//
//    @Override
//    public void setProperties(Properties properties) {
//    }
//}