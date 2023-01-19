package com.wds.codebook.redis;

public interface RedisKeys {
    String SUBSCRIBER_UKEY_TOKEN_KEY = "subscriber:ukey:token:%s";
    String COMPANY_ACTIVE_STATUS = "company:active:status:%s";
    String SUBSCRIBER_LOGIN_KEY = "subscriber:login:%s";
    String SUBSCRIBER_USER_KEY = "subscriber:token:%s";
    String COMPANY_TOKEN_KEY = "subscriber:token:company:%s";
    String SUBSCRIBER_LOGIN_SMS = "subscriber:login:%s";
    String SUBSCRIBER_SMS_FREQUENCY_KEY = "subscriber:sms:frequency:%s";
    String SUBSCRIBER_SMS_DAY_KEY = "subscriber:sms:day:%s";
    String SUBSCRIBER_LOGIN_SMS_KEY = "subscriber:login:sms:%s";
    String SUBSCRIBER_LOGIN_VERIFY_SMS = "subscriber:login:verify:%s";
    String SUBSCRIBER_LOGIN_ERROR_COUNT_KEY = "subscriber:login:error:count:%s";
    String SUB_SHOW_TYPE = "sub:show:type:%s";
    String SUBSCRIBER_COMPLAINT_COMPANY = "subscriber:complaint:company:%s";
    String SUBSCRIBER_UNSUBSCRIBE_COMPANY = "subscriber:unsubscribe:company:%s";
    String LANDLINE_UNSUBSCRIBE_COMPANY = "landline:unsubscribe:company:%s";
    String SUBSCRIBER_CONTACT_COMPANY = "subscriber:contact:company:%s";
    /**
     * 登录临时token
     */
    String ACCOUNT_TEMPORARY_TOKEN = "account:temporary:token:%s";

    //----------------------sdk start---------------------
    /**
     * 第一次访问编码
     */
    String FIRST_TIME_VISIT_CODE = "sdk:oms:firstvisitcode:%s";
    String ACTIVE_CODE = "identity:register:%s:sessionNo:%s";
    String CERT_VERIFY_SESSIONS = "identity:cert:sessions:%s";
    String CERT_VERIFY_CODED = "identity:cert:codes:%s";
    /**
     * sdk 短信发送频率
     * 发送短信过于频繁，请%s分钟后再试
     */
    String SDK_SENDCODE_FREQUENCY_KEY = "sdk:omsSendcode:frequency:sendtype%s:%s";

    /**
     * sdk 短信发送频率   24小时内的发送量
     * 一天内发送短信超过限制次数，请1天后再试
     */
    String SDK_SENDCODE_DAY_KEY = "sdk:omsSendcode:day:sendtype%s:%s";

    /**
     * 验证码
     */
    String SDK_SEND_CODE = "sdk:omsSendcode:code:sendtype%s:%s";

    /**
     * 获取身份信息 手机号 次数限制
     */
    String LIMIT_IDENTITY_PHONE_COUNT = "sdk:limit:identity:phoneCount:%s:%s";

    /**
     * 获取身份信息 激活码 次数限制
     */
    String LIMIT_IDENTITY_ACTIVECIDE_COUNT = "sdk:limit:identity:activeCodeCount:%s:%s";
    //----------------------sdk end---------------------


    //固话绑定公司的语音验证码code
    String SUBSCRIBER_TELBINDCOM_VOICE_CODE = "subscriber:telbindcom:%s";
    /**
     * 流水号
     */
    String CALL_SERIAL_BATCH_DETAIL = "call:serial:batch:detail:%s";
    String CHARGING_IDEMPOTENTA = "call:serial:charging:%s";
    /**
     * 流水号(被叫先到)
     */
    String CALL_SERIAL_CALLED_SERIAL = "call:serial:called:serial:%s";

    /**
     * 流水分析总体概况redisKey
     */
    String SERIAL_EXPIRE_DATA = "call:serial:expire:data";

    /**
     * 流水号，闪信先到
     */
    String CALL_SERIAL_USSD_SERIAL = "call:serial:ussd:serial:%s";


    /**
     * 流水号,保存已插入的数据
     */
    String CALL_SERIAL_INSERT_BAK = "call:serial:ussd:insert:%s";

    /**
     * 流水号，计次先到
     */
    String CALL_SERIAL_USSD_SERIAL_CHARGING = "call:serial:ussd:serial:charging:%s";

    /**
     * ukey年审提醒
     */
    String REMIND_TYPE_UKEY = "remind:%s:%s:%s";

    /**
     * 企业退订关闭身份失败次数
     */
    String LIMIT_COUNT_OF_STOP_IDENTITY = "oms:fail:count:%s";

    /**
     * oms登录
     */
    //-----------------------用户登录key--------------------------

    String OMS_LOGIN_USER_TOKEN = "oms:%s:user:token:%s";
    String OMS_LOGIN_USER_ID = "oms:%s:user:userId:%s";
    String OMS_REQUEST_SIGN_USER_ID = "oms:%s:req:sign:userId:%s:%s";
    String SUBSCRIBER_FORGET_PWD = "subscriber:forget:%s";
    String OMS_CMS_FORGET_SMS = "cms:forget:%s";

    /**
     * 初始化公司闪信记录
     */
    String INIT_COMPANY_USSD = "cms:init:company:ussd";

    //-----------------------版本升级--------------------------
    /**
     * app 最新版本缓存
     */
    String OMS_APP_VERSION_NEWEST = "oms:app:version:appKey:%s";
    //    String OMS_APP_NEWEST_VERSION = "oms:app:version:newest:appKey:%s";
//    String OMS_APP_GRAY_NEWEST_VERSION = "oms:app:version:gray:newest:appKey:%s";
    String OMS_APP_GRAY_VERSION_NEWEST = "oms:app:version:gray:newest:appKey:%s";


//    String REDIS_VERSION_RELEASENUM =  "currReleaseNum";


    /**
     * app 最新强制更新版本缓存
     */
    String OMS_APP_FORCE_UPDATE_VERSION = "oms:app:force:version:appKey:%s";

    /**
     * app 灰度发布版本缓存
     */
    String OMS_APP_GRAY_LIST = "oms:app:gray:version:appKey:%s:*";

    String OMS_APP_GRAY_VERSION = "oms:app:gray:version:appKey:%s:versionCode:%s";

    String OMS_APP_GRAY_COMPANY_VERSION = "oms:app:gray:version:appKey:%s:company:%s";


    /**
     * 固话设备型号最新版本[model]
     */
    String OMS_TEL_VERSION_NEWEST = "oms:tel:version:model:%s";

    /**
     * 固话订户设备型号最新版本 [company]：[model]
     */
    String OMS_SUBSCRIBE_VERSION_NEWEST = "oms:tel:version:company:%s:%s";

    //--------------------------订单--------------------------------
    String UKEY_BUY_REPEAT = "oms:ukey:%s:batchNumber:%s:phone:%s";

    /**
     * 人脸头像实名比对次数限制
     */
    String FACE_VERIFY_TIME_KEY = "oms:face:verify:time:%s:%s";
    /**
     * 扣减次数
     */
    String DEDUTION_SERIAL_HASH_COMPANY = "oms:deduction:count:companyId:%s";

    String DEDUTION_SERIAL_PUSH_MQ = "oms:decution:pushmq:companyId:%s:serialNo:%s";
    String DEDUTION_SERIAL = "oms:decution:companyId:%s";
    String DEDUTION_SERIAL_DB = "oms:decution:db:companyId:%s";
    String DEDUTION_SERIAL_DELAY = "oms:decution:delay:companyId:%s";
    /**
     * 同步联通错误的key
     */
    String SYNC_UNICOM_DATA_EROR = "oms:sync:unicom:error";

}
