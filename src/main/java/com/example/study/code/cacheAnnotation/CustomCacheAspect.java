//package com.example.study.code.cacheAnnotation;
//
//import cn.echaincity.base.framework.extension.storage.Storable;
//import cn.echaincity.base.scheduler.Scheduler;
//import cn.hutool.json.JSONUtil;
//import org.aspectj.lang.ProceedingJoinPoint;
//import org.aspectj.lang.annotation.Around;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Pointcut;
//import org.aspectj.lang.reflect.MethodSignature;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import java.lang.reflect.Field;
//import java.lang.reflect.Method;
//import java.lang.reflect.Parameter;
//import java.rmi.ServerException;
//
//@Component
//@Aspect
//public class CustomCacheAspect {
//
//    @Autowired
//    Storable storable;
//    //    @Resource(name = "delayedDoubleRemoveScheduler")
//    Scheduler scheduler;
//
//    // 1. 查缓存，key
//    @Pointcut("@annotation(cn.echaincity.base.storage.CacheableCustom)")
//    public void cacheableCustomFun() {
//    }
//
//    @Around("cacheableCustomFun()")
//    public Object getChache(ProceedingJoinPoint joinPoint) throws Throwable {
//        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
//        Class<?> returnType = method.getReturnType();
//
//        CacheableCustom cacheableCustom = method.getDeclaredAnnotation(CacheableCustom.class);
//        String key = getSaveKey(joinPoint, method, cacheableCustom, returnType.getSimpleName());
//
//        //优化 缓存击穿
//        Object result = null;
//        String value = storable.get(key);
//        if (value != null) {
//            return JSONUtil.toBean(value, returnType);
//        } else {
//            result = joinPoint.proceed();
//            if(result != null){
//                storable.set(key, JSONUtil.toJsonStr(result));
//            }
//        }
//
//        return result;
//    }
//
//    @Pointcut("@annotation(cn.echaincity.base.storage.CacheEvictCustom)")
//    public void cacheEvictCustomFun() {
//    }
//
//    @Around("cacheEvictCustomFun()")
//    public void removeCache(ProceedingJoinPoint joinPoint) throws Throwable {
//        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
//        Class<?> returnType = method.getReturnType();
//
//        CacheEvictCustom cacheEvictCustom = method.getDeclaredAnnotation(CacheEvictCustom.class);
//        String key = getRemoveKey(joinPoint, method, cacheEvictCustom, returnType.getSimpleName());
//
//        storable.remove(key);
//
//        joinPoint.proceed();
//
////        scheduler.schedule("delayedDoubleRemove", () -> storable.remove(key), 1000);
//    }
//
//    @Pointcut("@annotation(cn.echaincity.base.storage.CachePutCustom)")
//    public void cachePutCustomFun() {
//    }
//
//    @Around("cachePutCustomFun()")
//    public void updateCache(ProceedingJoinPoint joinPoint) throws Throwable {
//        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
//        Class<?> returnType = method.getReturnType();
//
//        CachePutCustom cachePutCustom = method.getDeclaredAnnotation(CachePutCustom.class);
//        String key = getUpdateKey(joinPoint, method, cachePutCustom, returnType.getSimpleName());
//
//        Object result = joinPoint.proceed();
//
//        if(result != null){
//            storable.set(key, JSONUtil.toJsonStr(result));
//        }
//    }
//
//    private String getSaveKey(ProceedingJoinPoint joinPoint, Method method, CacheableCustom fromCache, String simpleName) throws ServerException {
//        String key = fromCache.key();
//        Parameter[] parameters = method.getParameters();
//        Object[] args = joinPoint.getArgs();
//        String id = getId(key, parameters, args);
//        if (id == null || "".equals(id)) {
//            throw new ServerException("@GetCache注解解析id为空");
//        }
//
//        return fromCache.prefix() + ":" + getTableName(fromCache.tableName(), method) + ":" + getMethodName(fromCache.methodName(), method) + ":" + id;
//    }
//
//
//    private String getRemoveKey(ProceedingJoinPoint joinPoint, Method method, CacheEvictCustom cacheEvictCustom, String simpleName) throws ServerException {
//        String key = cacheEvictCustom.key();
//        Parameter[] parameters = method.getParameters();
//        Object[] args = joinPoint.getArgs();
//        String id = getId(key, parameters, args);
//
//        if (id == null || "".equals(id)) {
//            throw new ServerException("@RemoveCache注解解析id为空");
//        }
//
//        return cacheEvictCustom.prefix() + ":" + getTableName(cacheEvictCustom.tableName(), method) + ":" + getMethodName(cacheEvictCustom.methodName(), method) + ":" + id;
//    }
//
//    private String getUpdateKey(ProceedingJoinPoint joinPoint, Method method, CachePutCustom cachePutCustom, String simpleName) throws ServerException {
//        String key = cachePutCustom.key();
//        Parameter[] parameters = method.getParameters();
//        Object[] args = joinPoint.getArgs();
//        String id = getId(key, parameters, args);
//
//        if (id == null || "".equals(id)) {
//            throw new ServerException("@cachePutCustom注解解析id为空");
//        }
//
//        return cachePutCustom.prefix() + ":" + getTableName(cachePutCustom.tableName(), method) + ":" + getMethodName(cachePutCustom.methodName(), method) + ":" + id;
//    }
//
//    private String getId(String key, Parameter[] parameters, Object[] args) throws ServerException {
//        String id = null;
//        if ("".equals(key)) {
//            id = args[0].toString();
//        } else {
//            if (!key.startsWith("#")) {
//                throw new ServerException("@Cache***注解key必须以#开头");
//            }
//            if (!key.contains(".")) {
//                String name = key.substring(1);
//                for (int i = 0; i < parameters.length; i++) {
//                    if (parameters[i].getName().equals(name)) {
//                        id = args[i].toString();
//                        break;
//                    }
//                }
//            } else {
//                String[] s = key.substring(1).split("\\.");
//                for (int i = 0; i < parameters.length; i++) {
//                    if (parameters[i].getName().equals(s[0])) {
//                        try {
//                            Field field = parameters[i].getType().getDeclaredField(s[1]);
//                            field.setAccessible(true);
//                            id = field.get(args[i]).toString();
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }
//            }
//        }
//        return id;
//    }
//
//
//    private String getTableName(String tableName, Method method) {
//        if (tableName == null || "".equals(tableName)) {
//            String simpleName = method.getDeclaringClass().getSimpleName().toUpperCase();
//            int index = ClassTypeEnum.getIndex(simpleName);
//            return simpleName.substring(0, index);
//        } else {
//            return tableName.toUpperCase();
//        }
//    }
//
//
//    private String getMethodName(String methodName, Method method) {
//        if (methodName == null || "".equals(methodName)) {
//            String simpleName = method.getName().toUpperCase();
//            int index = simpleName.lastIndexOf(MethodPrefixEnum.GET.getCode());
//            if (index == -1) {
//                index = simpleName.lastIndexOf(MethodPrefixEnum.DELETE.getCode());
//                if (index == -1) {
//                    index = simpleName.lastIndexOf(MethodPrefixEnum.INSERT.getCode());
//                    if (index == -1) {
//                        index = simpleName.lastIndexOf(MethodPrefixEnum.UPDATE.getCode());
//                        if(index == -1){
//                            return simpleName;
//                        }else{
//                            return simpleName.replace(MethodPrefixEnum.UPDATE.getCode(), MethodPrefixEnum.GET.getCode());
//                        }
//                    }else {
//                        return simpleName.replace(MethodPrefixEnum.INSERT.getCode(), MethodPrefixEnum.GET.getCode());
//                    }
//                }else {
//                    return simpleName.replace(MethodPrefixEnum.DELETE.getCode(), MethodPrefixEnum.GET.getCode());
//                }
//            }else {
//                return simpleName;
//            }
//        } else {
//            return methodName.toUpperCase();
//        }
//    }
//}
