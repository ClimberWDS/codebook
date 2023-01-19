package com.wds.codebook.common.model;

public enum ExtraCodeEnum {

    /**
     * 成功的响应码
     */
    NORMAL("0000", "OK"),
    NO_AUTH("9999", "无权限请求"),
    NO_BTN_AUTH("4003", "没有此操作权限"),
    NOT_LOGIN("1000", "账号未登录或无权限访问"),
    ERROR_PARAMS("1001", "请求参数错误或缺失"),
    NO_DATA("1002", "未找到相应的数据"),
    QUERY_ERROR("1003", "查询身份票据失败"),

    SQL_ERROR("1004", "操作数据库失败"),

    ERROR_REQUEST_PARAM("1005", "非法参数请求"),

    DATA_DEAL_ERROR("1006", "数据处理失败"),

    QUERY_ROOT_CERT_ERROR("1007", "查询根证书失败"),

    PUSH_ROOT_CERT_ERROR("1007", "保存根证书失败"),

    EMP_IDENTITY_DISABLED("1008", "身份已被服务管理系统禁用"),

    AUTH_SIGN_IVALID("1009", "签名值错误");

    public String code;
    public String msg;

    ExtraCodeEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static String findMsgByCode(String code) {
        for (ExtraCodeEnum enumObj : ExtraCodeEnum.values()) {
            if (enumObj.getCode().equals(code)) {
                return enumObj.getMsg();
            }
        }
        return "";
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
