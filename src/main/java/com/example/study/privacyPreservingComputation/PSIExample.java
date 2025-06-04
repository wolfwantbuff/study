package com.example.study.privacyPreservingComputation;

import javax.crypto.Cipher;
import javax.crypto.KeyAgreement;
import javax.crypto.SecretKey;
import javax.crypto.interfaces.DHPublicKey;
import javax.crypto.spec.DHParameterSpec;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.util.HashSet;
import java.util.Set;

public class PSIExample {

    public static void main(String[] args) throws Exception {
        // 甲方生成DH密钥对
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("DH");
        keyPairGenerator.initialize(2048); // 设置密钥长度
        KeyPair keyPairA = keyPairGenerator.generateKeyPair();
        DHPublicKey publicKeyA = (DHPublicKey) keyPairA.getPublic();

        // 乙方使用甲方的公钥生成DH密钥对
        KeyFactory keyFactory = KeyFactory.getInstance("DH");
        DHParameterSpec dhParamSpec = ((DHPublicKey) publicKeyA).getParams();
        KeyPairGenerator keyPairGeneratorB = KeyPairGenerator.getInstance("DH");
        keyPairGeneratorB.initialize(dhParamSpec);
        KeyPair keyPairB = keyPairGeneratorB.generateKeyPair();
        DHPublicKey publicKeyB = (DHPublicKey) keyPairB.getPublic();

        // 甲方根据乙方的公钥生成共享密钥
        KeyAgreement keyAgreementA = KeyAgreement.getInstance("DH");
        keyAgreementA.init(keyPairA.getPrivate());
        keyAgreementA.doPhase(publicKeyB, true);
        SecretKey sharedKeyA = keyAgreementA.generateSecret("AES");

        // 乙方根据甲方的公钥生成共享密钥
        KeyAgreement keyAgreementB = KeyAgreement.getInstance("DH");
        keyAgreementB.init(keyPairB.getPrivate());
        keyAgreementB.doPhase(publicKeyA, true);
        SecretKey sharedKeyB = keyAgreementB.generateSecret("AES");

        // 生成随机集合
        Set<Integer> setA = new HashSet<>();
        Set<Integer> setB = new HashSet<>();

        for (int i = 0; i < 10; i++) {
            setA.add(i);
            setB.add(i * 2);
        }

        // 甲方对集合加密
        Set<Integer> encryptedSetA = encryptSet(setA, sharedKeyA);

        // 乙方对集合加密
        Set<Integer> encryptedSetB = encryptSet(setB, sharedKeyB);

        // 甲方发送加密后的集合给乙方，乙方解密后得到交集
        Set<Integer> intersectionB = getIntersection(encryptedSetA, sharedKeyB);
        System.out.println("Intersection at B: " + intersectionB);
    }

    private static Set<Integer> encryptSet(Set<Integer> set, SecretKey key) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, key);

        Set<Integer> encryptedSet = new HashSet<>();
        for (Integer elem : set) {
            byte[] encryptedElem = cipher.doFinal(elem.toString().getBytes());
            encryptedSet.add(new String(encryptedElem).hashCode());
        }

        return encryptedSet;
    }

    private static Set<Integer> getIntersection(Set<Integer> encryptedSet, SecretKey key) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, key);

        Set<Integer> intersection = new HashSet<>();
        for (Integer encryptedElem : encryptedSet) {
            byte[] decryptedElemBytes = cipher.doFinal(Integer.toString(encryptedElem).getBytes());
            String decryptedElem = new String(decryptedElemBytes);
            int elemValue = Integer.parseInt(decryptedElem);
            intersection.add(elemValue);
        }

        return intersection;
    }
}
