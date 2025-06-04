package com.example.study.designpartten;


import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Constructor;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author xiaodong
 * @date 2022/7/15 13:51
 * @wiki
 */
public class LazySingleton {

    //private避免其它地方使用new创建实例
    private LazySingleton(){
        //避免别的地方使用反射创建实例
        if(LazySingletonHolder.lazySingleton != null){
            throw new RuntimeException("不允许创建多个实例");
        }
    }

    public static void main(String[] args) {
//        Map<String, String> getenv = System.getenv();
//        ReentrantReadWriteLock reentrantReadWriteLock = new ReentrantReadWriteLock();
//        ReentrantReadWriteLock.ReadLock readLock = reentrantReadWriteLock.readLock();
//        ReentrantReadWriteLock.WriteLock writeLock = reentrantReadWriteLock.writeLock();
//        writeLock.lock();
//        readLock.lock();
//        System.out.println();
//        readLock.unlock();
//        writeLock.unlock();
        long l=10000L;
        double d = 0.235612374321;
        long v = (long) (l * d);
        System.out.println(v);

    }

    public static long getMillis(long userId){
        long t1 = userId ^ 3 << 17;
        long t2 = t1 ^ 4 << 12;
        return (t2 >> 22) + 1275047176146L;
    }

    //避免序列化反序列化创建实例
    private Object readResolve(){
        return LazySingletonHolder.lazySingleton;
    }
    //final不能重写
    public static final LazySingleton getInstance(){
        return LazySingletonHolder.lazySingleton;
    }
    private static class LazySingletonHolder {
        //final不能修改
        private static final LazySingleton lazySingleton =
                new LazySingleton();
    }



    {
        try {
            //基于反射创建实例
            Class<LazySingleton> clazz = LazySingleton.class;
            Constructor<LazySingleton> constructor =
                    clazz.getDeclaredConstructor(null);
            constructor.setAccessible(true);
            constructor.newInstance();

            //基于反序列化创建实例
            //序列化
            LazySingleton lazySingleton = LazySingleton.getInstance();
            FileOutputStream fos = new FileOutputStream("LazySingleton.obj");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(lazySingleton);
            //反序列化
            FileInputStream fis = new FileInputStream("LazySingleton.obj");
            ObjectInputStream ois = new ObjectInputStream(fis);
            LazySingleton lazySingleton1 = (LazySingleton)ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
