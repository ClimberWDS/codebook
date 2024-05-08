package com.wds.codebook.web3.utils;

import com.wds.codebook.web3.dto.SecurityKeyPair;
import org.web3j.crypto.*;
import org.web3j.utils.Numeric;
import org.web3j.crypto.Sign.SignatureData;

import java.math.BigInteger;
import java.security.KeyPair;
import java.util.Arrays;

import org.web3j.crypto.Credentials;
import org.web3j.crypto.Sign;

import static com.wds.codebook.web3.dto.SecurityKeyPair.generateKeyPair;

/**
 * @author wds
 * @DateTime: 2024/4/28 16:19
 */
public class CryptoUtils {
    /**
     * 以太坊自定义的签名消息都以以下字符开头
     * 参考 eth_sign in https://github.com/ethereum/wiki/wiki/JSON-RPC
     */
    public static final String PERSONAL_MESSAGE_PREFIX = "\u0019Ethereum Signed Message:\n";


//
//    /**
//     * 对签名消息，原始消息，账号地址三项信息进行认证，判断签名是否有效
//     *
//     * @param message
//     * @return
//     */
//    public static String signMessage(String message, Credentials credentials) {
//        byte[] messageBytes = message.getBytes();
//        byte[] messageHash = org.web3j.crypto.Hash.sha3(messageBytes);
//
//        Sign.SignatureData signatureData = Sign.signMessage(messageHash, credentials.getEcKeyPair());
//
//        BigInteger v = new BigInteger(signatureData.getV()); // 将 V 值转换为 BigInteger
//        byte[] vBytes = Numeric.toBytesPadded(v, 1);
//        byte[] signature = new byte[65];
//        System.arraycopy(signatureData.getR(), 0, signature, 0, 32);
//        System.arraycopy(signatureData.getS(), 0, signature, 32, 32);
//        System.arraycopy(vBytes, 0, signature, 64, 1);
//
//        return Numeric.toHexString(signature);
//    }
//
//    public static boolean validate(String signature, String message, String address) {
//        // 参考 eth_sign in https://github.com/ethereum/wiki/wiki/JSON-RPC
//        // eth_sign
//        // The sign method calculates an Ethereum specific signature with:
//        //    sign(keccak256("\x19Ethereum Signed Message:\n" + len(message) + message))).
//        //
//        // By adding a prefix to the message makes the calculated signature recognisable as an Ethereum specific signature.
//        // This prevents misuse where a malicious DApp can sign arbitrary data (e.g. transaction) and use the signature to
//        // impersonate the victim.
//        String prefix = PERSONAL_MESSAGE_PREFIX + message.length();
//        byte[] msgHash = Hash.sha3((prefix + message).getBytes());
//
//        byte[] signatureBytes = Numeric.hexStringToByteArray(signature);
//        byte v = signatureBytes[64];
//        if (v < 27) {
//            v += 27;
//        }
//
//        SignatureData sd = new SignatureData(
//                v,
//                Arrays.copyOfRange(signatureBytes, 0, 32),
//                Arrays.copyOfRange(signatureBytes, 32, 64));
//
//        String addressRecovered = null;
//        boolean match = false;
//
//        // Iterate for each possible key to recover
//        for (int i = 0; i < 4; i++) {
//            BigInteger publicKey = Sign.recoverFromSignature(
//                    (byte) i,
//                    new ECDSASignature(new BigInteger(1, sd.getR()), new BigInteger(1, sd.getS())),
//                    msgHash);
//
//            if (publicKey != null) {
//                addressRecovered = "0x" + Keys.getAddress(publicKey);
//
//                if (addressRecovered.equals(address)) {
//                    match = true;
//                    break;
//                }
//            }
//        }
//        return match;
//    }
//
//
//
//    private static final String privateKey = "a1e797e7dc80704c90bd2e07c7db5ae94ed913a78e7fae91e96c7d08017b1a96";
//
//    private static final String publicKey = "5ce18b16f0dfbf424457c7f46ca83400a78201eff3fcb705c5354f23da3fe15afd535dfdddc244bb5f5b24b7a5555aa961e33fdf5ddea1bcbe1e9af9d9329661";
//    public static void main(String[] args) {
//
//        // 用于测试的私钥（替换为你自己的私钥）
//
//        // 使用私钥创建凭据对象
//        Credentials credentials = Credentials.create(privateKey);
//
//        // 要签名的消息
//        String message = "Hello, Ethereum!";
//
//        // 生成签名
//        String signature = signMessage(message, credentials);
//
//        // 输出签名
//        System.out.println("Message: " + message);
//        System.out.println("Signature: " + signature);
//
//        // 验证签名
//        boolean isValid = validate(signature, message, credentials.getAddress());
//        System.out.println("Signature is valid: " + isValid);
//    }



    public static void main(String[] args) {
        // 生成公私钥对
        SecurityKeyPair keyPair = generateKeyPair();
        if (keyPair != null) {
            // 使用私钥创建凭据对象
            Credentials credentials = Credentials.create(keyPair.getPrivateKey());
            // 要签名的消息
            String message = "1";
            // 生成签名
            String signature = signMessage(message, credentials);
            // 输出签名
            System.out.println("Generated Signature: " + signature);
            // 验证签名
            boolean isValid = validate(signature, message, credentials.getAddress());
            System.out.println("Signature is valid: " + isValid);
        }
    }


    // 对消息进行签名
    public static String signMessage(String message, Credentials credentials) {
        byte[] messageBytes = message.getBytes();
        byte[] messageHash = org.web3j.crypto.Hash.sha3(messageBytes);
        Sign.SignatureData signatureData = Sign.signMessage(messageHash, credentials.getEcKeyPair());
        BigInteger v = new BigInteger(signatureData.getV()); // 将 V 值转换为 BigInteger
        byte[] vBytes = Numeric.toBytesPadded(v, 1);
        byte[] signature = new byte[65];
        System.arraycopy(signatureData.getR(), 0, signature, 0, 32);
        System.arraycopy(signatureData.getS(), 0, signature, 32, 32);
        System.arraycopy(vBytes, 0, signature, 64, 1);

        return Numeric.toHexString(signature);
    }

    // 验证签名
    public static boolean validate(String signature, String message, String publicKey) {

        String prefix = "Ethereum Signed Message:" + message.length();
        byte[] msgHash = org.web3j.crypto.Hash.sha3((prefix + message).getBytes());

        byte[] signatureBytes = Numeric.hexStringToByteArray(signature);
        byte v = signatureBytes[64];
        if (v < 27) {
            v += 27;
        }

        ECDSASignature sig = new ECDSASignature(
                new BigInteger(1, Arrays.copyOfRange(signatureBytes, 0, 32)),
                new BigInteger(1, Arrays.copyOfRange(signatureBytes, 32, 64))
        );
        boolean match = false;

        // Recover public key from signature
        BigInteger pubKeyRecovered = Sign.recoverFromSignature(
                (byte) (v - 27), // It is either 0 or 1
                sig,
                msgHash);

        if (pubKeyRecovered != null) {
            String publicKeyRecovered = Numeric.toHexStringWithPrefix(pubKeyRecovered);
            if (publicKeyRecovered.equals(publicKey)) {
                match = true;
            }
        }

        return match;
    }


}

