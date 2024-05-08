package com.wds.codebook.web3.controller;

import cn.hutool.core.util.NumberUtil;
import com.wds.codebook.common.model.RespResult;
import com.wds.codebook.web3.dto.UserVO;
import com.wds.codebook.web3.utils.CryptoUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.web3j.crypto.WalletUtils;

import java.util.HashMap;

/**
 * @author wds
 * @DateTime: 2024/4/28 13:57
 */

@Slf4j
@RestController
public class LoginController {

    @RequestMapping(value = "/oauth/metamask", method = RequestMethod.POST)
    @ResponseBody
    public RespResult<Void> login(@RequestBody HashMap requestObject) {
        final String publicAddress = (String) requestObject.get("publicAddress");
        final String signature = (String) requestObject.get("signature");
        final String message = (String) requestObject.get("message");

        // 地址合法性校验
        if (!WalletUtils.isValidAddress(publicAddress)) {
            // 不合法直接返回错误
            return RespResult.fail("009", "地址格式非法！");
        }

        // 校验签名信息
        if (!CryptoUtils.validate(signature, message, publicAddress)) {
            return RespResult.fail("", "签名校验失败！");
        }

        // 校验通过，publicAddress 相当于就是OAuth的openid, 根据该账号做其他业务处理
        //
        // JWT token
        return RespResult.success();
    }
}