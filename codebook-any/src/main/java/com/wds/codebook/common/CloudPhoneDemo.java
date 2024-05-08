package com.wds.codebook.common;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 智能云云手机业务场景，绑定、获取鉴权信息、解绑接口调用示例Demo
 */
public class CloudPhoneDemo {

    // ak/sk密钥，可以在商户端界面的左下角’个人中心-个人信息‘里获取
    private static final String ACCESS_KEY = "4869390fc8a847b48103b6de35346cad";
    private static final String ACCESS_SECRET_KEY = "b1fe4f07701bd1215c88c3bab23b836b";
    //    private static final String OPEN_API_HOST = "https://yshub.armcloudtest.top";
    private static final String OPEN_API_HOST = "http://paas.ys.com:8888";

    public static void main(String[] args) {
        // 用户唯一标识，由商户自己定义的每个用户对应的一个标识，绑定实例、获取实例鉴权信息、以及调用sdk推流时传的uid需要保持一致
        String uid = "sdk-test";

        // 绑定(购买)实例
        String instanceNo = bindInstance(uid);
        System.out.println("instance bind. instanceNo: " + instanceNo);

        // 获取实例获取连接鉴权信息
        getSdkAccessKeyAndAccessSecretKey(uid);

        // 主动解绑实例
        unbindInstance(instanceNo);
    }

    // 绑定(购买)实例
    private static String bindInstance(String uid) {
        System.out.println("start to bindInstance ===================================================================");
        // 参数
        Map<String, Object> param = new HashMap<>();
        // 用户唯一标识，由商户自己定义的每个用户对应的一个标识，与下面‘获取连接鉴权信息’，以及调用sdk推流时传的uid需要保持一致
        param.put("uid", uid);
        // 申请时长，单位秒
        param.put("onlineDuration", 60 * 60);
        // 绑定实例个数
        param.put("bindCount", 1);
        // 指定实例分组编号，1是默认的公共池
        param.put("instanceGroupNo", 1);

        // 可以通过指定实例规格(实例开数)，从而在指定的实例分组下随机查找一台符合该规格的可用实例进行绑定
        // param.put("instanceType", "4");

        // 可以通过传递用户的客户端IP，这样通过随机查找时，会根据地理位置，优先分配就近机房的实例给到用户，保障用户推流网络质量
        // param.put("clientIp", "113.111.113.24");

        // 不指定实例编号，则会在指定的实例分组内随机选择一台可用的实例去绑定
        // 也可以通过指定实例编号，来直接绑定某一台实例
        // param.put("instanceNos", Arrays.asList("INS-5A613FA8FA1845BD"));

        // url
        String url = OPEN_API_HOST + "/open-api/v1/instance/cloud-phone-bind";
        // 请求
        String responseStr = OpenApiDemo.requestWithAuth(url, param, ACCESS_KEY, ACCESS_SECRET_KEY);
        Map<String, Object> response = parseJsonToMap(responseStr);
        // 判断响应编码是否为0，非0则代表请求异常
        if (!String.valueOf(response.get("code")).equals("0")) {
            System.err.println("request error");
            throw new RuntimeException("request error");
        }

        // 获取路径 => response.data.instanceList[0].instanceNo
        String instanceNo = ((Map<String, Object>) ((Map<String, Object>) ((Map<String, Object>) response.get("data")).get("instanceList")).get("0")).get("instanceNo").toString();
        return instanceNo;
    }

    // 获取实例获取连接鉴权信息
    private static void getSdkAccessKeyAndAccessSecretKey(String uid) {
        System.out.println("start to getSdkAccessKeyAndAccessSecretKey =============================================");
        // 参数
        Map<String, Object> param = new HashMap<>();
        // 用户唯一标识，由商户自己定义的每个用户对应的一个标识，与上面‘绑定(购买)实例’，以及调用sdk推流时传的uid需要保持一致
        param.put("uid", uid);
        // 客户端票据，从SDK获取
        param.put("clientTicket", "YTgyYzA5ZTQ1OWY0NGM2NTliOGIxNGJjMTAzZmU0N2UsMSwxLjAuNCxzYWZhcmksMTMuMC4zLDE2OTc3ODE0MTc0Mjc=");

        // url
        String url = OPEN_API_HOST + "/open-api/v1/instance/cloud-phone-security-token";
        // 请求
        String responseStr = OpenApiDemo.requestWithAuth(url, param, ACCESS_KEY, ACCESS_SECRET_KEY);
        Map<String, Object> response = parseJsonToMap(responseStr);
        // 判断响应编码是否为0，非0则代表请求异常
        if (!String.valueOf(response.get("code")).equals("0")) {
            System.err.println("request error, " + response);
            throw new RuntimeException("request error, " + response);
        }

        // 获取临时accessKey => response.data.accessKey
        String accessKey = ((Map<String, Object>) response.get("data")).get("accessKey").toString();
        // 获取临时accessSecretKey => response.data.accessSecretKey
        String accessSecretKey = ((Map<String, Object>) response.get("data")).get("accessSecretKey").toString();
        System.out.println("sdk ak/sk ===================================================");
        System.out.println("accessKey: " + accessKey);
        System.out.println("accessSecretKey: " + accessSecretKey);
    }

    // 主动解绑实例
    private static void unbindInstance(String instanceNo) {
        System.out.println("start to unbindInstance ================================================================");
        // 参数
        Map<String, Object> param = new HashMap<>();
        // 要解绑的实例编号，每次最多传200个
        param.put("instanceNos", Arrays.asList(instanceNo));

        // url
        String url = OPEN_API_HOST + "/open-api/v1/instance/cloud-phone-unbind";
        // 请求
        String responseStr = OpenApiDemo.requestWithAuth(url, param, ACCESS_KEY, ACCESS_SECRET_KEY);
        Map<String, Object> response = parseJsonToMap(responseStr);
        // 判断响应编码是否为0，非0则代表请求异常
        if (!String.valueOf(response.get("code")).equals("0")) {
            System.err.println("request error, " + response);
            throw new RuntimeException("request error, " + response);
        }
        System.out.println("unbind success");
    }

    /**
     * json转对象辅助方法，此处为了减少引入的依赖所以简单实现，生产环境建议替换为开源工具，例如Jackson
     */
    private static Map<String, Object> parseJsonToMap(String text) {
        ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
        ScriptEngine scriptEngine = scriptEngineManager.getEngineByName("nashorn");
        scriptEngine.put("json", text);
        try {
            scriptEngine.eval("var data=JSON.parse(json)");
            Object map = scriptEngine.get("data");
            return (Map<String, Object>) map;
        } catch (ScriptException e) {
            throw new RuntimeException("parse error", e);
        }
    }

}
