package com.wds.codebook.telegram.utils;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.wds.codebook.common.utils.JacksonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Hex;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

/**
 * @author wds
 * @DateTime: 2024/11/27 11:00
 */
@Slf4j
public class ValidUtils {


    public static String botToken = "8116578715:AAE2NyMNK8j7ajEYDUoDCOC-vwemmFKK-5A";


    /**
     * 验证tg小程序数据
     * 1. 构建 data-check-string
     * 2. 计算【secret_key】
     *      = HMAC_SHA256(<bot_token>, "WebAppData")
     * 3. 计算参数【calcHash】
     *      = HMAC-SHA256(data-check-string, secret_key)
     * 4. 比较 hash和calcHash是否一致
     *
     * @param data tg文档中的initData
     * @param hash 收到的Hash
     * @return 校验结果
     */
    public static boolean validData(String data,String hash) {
        try {
            // 1. 构建 data-check-string
            String dataCheckString = buildDataCheckString(buildDataMap(data));

            // 2. 计算 secret_key = HMAC_SHA256(<bot_token>, "WebAppData")
            byte[] secretKey = encrypt(botToken, "WebAppData".getBytes(StandardCharsets.UTF_8));

            // 3. 计算 HMAC-SHA256(data-check-string, secret_key)
            String calcHash = encryptHex(dataCheckString, secretKey);
            // 4. 比较 hash 和 calcHash
            return hash.equalsIgnoreCase(calcHash);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 构建数据检查字符串
     * 1.按照字段名的字母顺序排序
     * 2.使用换行符"\n" 连接
     *
     * @param dataMap 参数map
     * @return 数据检查字符串 即tg文档中的data_check_string
     */

    private static String buildDataCheckString(Map<String, String> dataMap) {
        List<String> keys = new ArrayList<>(dataMap.keySet());
        Collections.sort(keys); // 字典序排序
        StringBuilder sb = new StringBuilder();
        for (String key : keys) {
            sb.append(key).append("=").append(dataMap.get(key)).append("\n");
        }
        // 移除最后一个换行符
        if (sb.length() > 0) {
            sb.setLength(sb.length() - 1);
        }
        return sb.toString();
    }

    /**
     * 构建参数map
     * 需要去除hash参数
     *
     * @param data tg文档中的initData
     * @return 返回参数map
     */

    private static Map<String, String> buildDataMap(String data) throws Exception {
        Map<String, String> params = new HashMap<>();
        String[] pairs = data.split("&");
        for (String pair : pairs) {
            int idx = pair.indexOf("=");
            if (idx > 0) {
                String key = URLDecoder.decode(pair.substring(0, idx), StandardCharsets.UTF_8.name());
                if (Arrays.asList("hash", "").contains(key)) {
                    continue;
                }
                String value = URLDecoder.decode(pair.substring(idx + 1), StandardCharsets.UTF_8.name());
                params.put(key, value);
            }
        }
        log.info("params:{}", JacksonUtils.toJson(params));
        return params;
    }





    /**
     * 计算HMAC-SHA256 16进制值
     * @param content 加密内容
     * @param key 密钥 [bytes]
     * @return
     */
    private static String encryptHex(String content, byte[] key) {
        return Hex.encodeHexString(encrypt(content, key));
    }

    /**
     * 计算 HMAC-SHA256
     * @param content 加密内容
     * @param key 密钥 [bytes]
     * @return
     */
    public static byte[] encrypt(String content, byte[] key) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKeySpec = new SecretKeySpec(key, "HmacSHA256");
            mac.init(secretKeySpec);
            return mac.doFinal(content.getBytes());
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException(e);
        }
    }



    public static void main(String[] args) throws Exception {
        String hash = "c6978f397b2f4f58afb770cc94475a36d2cce6b65a635a6464ba5a655545fade";  // 从 Telegram 接收到的哈希
        String data = "user=%7B%22id%22%3A5916162374%2C%22first_name%22%3A%22June%22%2C%22last_name%22%3A%22Double%22%2C%22language_code%22%3A%22zh-hans%22%2C%22allows_write_to_pm%22%3Atrue%2C%22photo_url%22%3A%22https%3A%5C%2F%5C%2Ft.me%5C%2Fi%5C%2Fuserpic%5C%2F320%5C%2FK3s4gI28di2N4c377dNAvSm55rCk-_e-0p3XtbRhopepeiUDqCBg2CmR0PeMzKDK.svg%22%7D&chat_instance=-7404315562752763869&chat_type=private&auth_date=1732677313&signature=nhoK6WbQfrinb4Adxk-vzV9ffc7Hw2BjRulbOpqEC-HeYMkRbdEBdkaFSQrVeOj3FNScMmVbYswos2zP8am0Bg&hash=c6978f397b2f4f58afb770cc94475a36d2cce6b65a635a6464ba5a655545fade";  // 从 Telegram 接收到的数据

        /**
        {
    "chat_type": "private",
    "signature": "nhoK6WbQfrinb4Adxk-vzV9ffc7Hw2BjRulbOpqEC-HeYMkRbdEBdkaFSQrVeOj3FNScMmVbYswos2zP8am0Bg",
    "auth_date": "1732677313",
    "user": "{\"id\":5916162374,\"first_name\":\"June\",\"last_name\":\"Double\",\"language_code\":\"zh-hans\",\"allows_write_to_pm\":true,\"photo_url\":\"https:\\/\\/t.me\\/i\\/userpic\\/320\\/K3s4gI28di2N4c377dNAvSm55rCk-_e-0p3XtbRhopepeiUDqCBg2CmR0PeMzKDK.svg\"}",
    "chat_instance": "-7404315562752763869"
}
         */


        // 验证数据
        boolean isValid = validData(data, hash);
        System.out.println("小程序参数校验结果: " + isValid);


        String json = "      {\n" +
                "    \"chat_type\": \"private\",\n" +
                "    \"signature\": \"nhoK6WbQfrinb4Adxk-vzV9ffc7Hw2BjRulbOpqEC-HeYMkRbdEBdkaFSQrVeOj3FNScMmVbYswos2zP8am0Bg\",\n" +
                "    \"auth_date\": \"1732677313\",\n" +
                "    \"user\": \"{\\\"id\\\":5916162374,\\\"first_name\\\":\\\"June\\\",\\\"last_name\\\":\\\"Double\\\",\\\"language_code\\\":\\\"zh-hans\\\",\\\"allows_write_to_pm\\\":true,\\\"photo_url\\\":\\\"https:\\\\/\\\\/t.me\\\\/i\\\\/userpic\\\\/320\\\\/K3s4gI28di2N4c377dNAvSm55rCk-_e-0p3XtbRhopepeiUDqCBg2CmR0PeMzKDK.svg\\\"}\",\n" +
                "    \"chat_instance\": \"-7404315562752763869\"\n" +
                "  }";

        ObjectMapper objectMapper = new ObjectMapper();
        // 解析整个 JSON 字符串为一个 Map
        Map<String, Object> data1 = objectMapper.readValue(json, Map.class);

        // 获取 user 字段中的 JSON 字符串并解析为一个 Map
        String userJson = (String) data1.get("user");

        // 解析 user 字段的 JSON 字符串为 Map
        Map<String, Object> userMap = objectMapper.readValue(userJson, Map.class);

        // 获取 first_name 和 last_name
        String firstName = (String) userMap.get("first_name");
        String lastName = (String) userMap.get("last_name");

        // 输出结果
        System.out.println("First Name: " + firstName);
        System.out.println("Last Name: " + lastName);

    }

}