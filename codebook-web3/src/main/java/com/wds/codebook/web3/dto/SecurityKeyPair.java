package com.wds.codebook.web3.dto;

import lombok.Data;
import org.bouncycastle.util.encoders.Hex;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Keys;

import java.math.BigInteger;

/**
 * @author wds
 * @DateTime: 2024/4/28 16:53
 */
@Data
public class SecurityKeyPair {

    /**
     * 私钥
     * 16进制
     */
    private final String privateKey;
    /**
     * 公钥
     * 16进制
     */
    private final String publicKey;

    public SecurityKeyPair(String privateKey, String publicKey) {
        this.privateKey = privateKey;
        this.publicKey = publicKey;
    }

    public static SecurityKeyPair generateKeyPair() {
        try {
            // 生成一个随机的以太坊密钥对
            ECKeyPair ecKeyPair = Keys.createEcKeyPair();

            // 获取私钥和公钥
            BigInteger privateKey = ecKeyPair.getPrivateKey();
            BigInteger publicKey = ecKeyPair.getPublicKey();

            // 将私钥和公钥转换为十六进制字符串
//            String privateKeyHex = privateKey.toString(16);
//            String publicKeyHex = publicKey.toString(16);
            String privateKeyHex = Hex.toHexString(privateKey.toByteArray());
            String publicKeyHex = Hex.toHexString(publicKey.toByteArray());

            // 输出私钥和公钥
//            System.out.println("Private Key: " + privateKeyHex);
//            System.out.println("Private Key: " + Hex.toHexString(privateKey.toByteArray()));
//            System.out.println("Public Key: " + publicKeyHex);
//            System.out.println("Public Key: " + Hex.toHexString(publicKey.toByteArray()));

            return new SecurityKeyPair(privateKeyHex, publicKeyHex);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        generateKeyPair();

    }
}