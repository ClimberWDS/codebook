package com.wds.codebook.common;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * 智能云OpenApi请求示例Demo
 */
public class OpenApiDemo {

    public static void main1(String[] args) {
        // ak/sk密钥，可以在商户端界面的左下角’个人中心-个人信息‘里获取
        String accessKey = "f16fad233e0c48b393921be83244fde0";
        String accessSecretKey = "b79dd30d335ad0d199d828fc2e6f6b8f";

        // 参数
        Map<String, Object> param = new HashMap<>();
        param.put("pageNo", 1);
        param.put("pageSize", 10);
        param.put("instanceNos", Arrays.asList("INS-5A613FA8FA1845BD", "INS-63722E4929BD416B"));

        // url
        String url = "https://yshub.armcloudtest.top/open-api/v1/instance/list";

        // 请求
        String response = requestWithAuth(url, param, accessKey, accessSecretKey);
        System.out.println("responseDate: " + response);
    }

    /**
     * openAPI加密请求
     * <p>签名计算方式 sign=HexEncode(HMAC-SHA256(AccessSecretKey, timestamp + "#" + HexEncode(Sha256(bodyStr)))</p>
     */
    public static String requestWithAuth(String url, Map<String, Object> param, String accessKey, String accessSecretKey) {
        long timestamp = System.currentTimeMillis();
        // todo 请替换成您熟悉的Json序列化框架来对参数进行序列化，此处只是为了不引入第三方依赖而简单实现
        String jsonStr = mapToJson(param);
        String sha256HexValue = sha256Hex(jsonStr);
        String sign = hmacSha256Hex(timestamp + "#" + sha256HexValue, accessSecretKey);
        Map<String, String> headers = new HashMap<>();
        headers.put("paas-access-key", accessKey);
        headers.put("paas-request-sign", sign);
        headers.put("paas-request-timestamp", String.valueOf(timestamp));
        System.out.println("url: " + url);
        System.out.println("header: " + mapToJson(headers));
        System.out.println("param: " + jsonStr);
        String response = post(url, jsonStr, headers);
        System.out.println("response: " + response);
        return response;
    }

    /**
     * http请求
     */
    private static String post(String url, String jsonStr, Map<String, String> headers) {
        String response = "";
        try {
            URL apiUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) apiUrl.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            headers.forEach(conn::setRequestProperty);
            conn.setDoOutput(true);
            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonStr.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }
            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                    response = br.readLine();
                }
            } else {
                System.out.println("request error，http status: " + responseCode);
            }
            conn.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    /**
     * 对象转json辅助，此处为了减少引入的依赖所以简单实现，生产环境建议替换为开源工具，例如Jackson
     */
    private static String mapToJson(Map param) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("{");
        param.forEach((k, v) -> {
            stringBuilder.append("\"").append(k).append("\":");
            if (v instanceof Number) {
                stringBuilder.append(v);
            } else if (v instanceof CharSequence) {
                stringBuilder.append("\"").append(v).append("\"");
            } else if (v instanceof Map) {
                stringBuilder.append(mapToJson((Map) v));
            } else if (v instanceof Collection) {
                stringBuilder.append(listToJson((Collection) v));
            } else {
                stringBuilder.append("\"").append(v).append("\"");
            }
            stringBuilder.append(",");
        });
        if (stringBuilder.charAt(stringBuilder.length() - 1) == ',') {
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        }
        stringBuilder.append("}");
        return stringBuilder.toString();
    }

    /**
     * 集合转json辅助，此处为了减少引入的依赖所以简单实现，生产环境建议替换为开源工具，例如Jackson
     */
    private static String listToJson(Collection collection) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("[");
        collection.forEach(v -> {
            if (v instanceof Number) {
                stringBuilder.append(v);
            } else if (v instanceof CharSequence) {
                stringBuilder.append("\"").append(v).append("\"");
            } else if (v instanceof Map) {
                stringBuilder.append(mapToJson((Map) v));
            } else if (v instanceof Collection) {
                stringBuilder.append(listToJson((Collection) v));
            } else {
                stringBuilder.append("\"").append(v).append("\"");
            }
            stringBuilder.append(",");
        });
        if (stringBuilder.charAt(stringBuilder.length() - 1) == ',') {
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        }
        stringBuilder.append("]");
        return stringBuilder.toString();
    }

    /**
     * sha256加密算法
     */
    public static String sha256Hex(String input) {
        try {
            MessageDigest sha256Digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = sha256Digest.digest(input.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * hmac-Sha256加密算法
     */
    public static String hmacSha256Hex(String input, String key) {
        try {
            Mac hmacSha256 = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(), "HmacSHA256");
            hmacSha256.init(secretKey);
            byte[] hash = hmacSha256.doFinal(input.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        BigDecimal a = new BigDecimal(6143.51);
        int i = a.add(new BigDecimal(0.5)).compareTo(new BigDecimal(6144));
        System.out.println(i);
    }
}
