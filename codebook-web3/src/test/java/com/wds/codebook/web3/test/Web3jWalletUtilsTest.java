package com.wds.codebook.web3.test;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.nacos.common.utils.JacksonUtils;
import com.wds.codebook.web3.utils.Web3jWalletUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author wds
 * @DateTime: 2024/5/8 13:57
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
class Web3jWalletUtilsTest {

    private static final String COMMON_WALLET_PATH = "C:\\Users\\wds_9\\Desktop\\wds\\wallet_home\\common_wallet";
    private static final String BIP39_WALLET_PATH = "C:\\Users\\wds_9\\Desktop\\wds\\wallet_home\\bip39_wallet";


    /**
     * 普通钱包生成和恢复 -- json
     *
     * @throws Exception e
     */
    @Test
     void generateCommonWalletTest() throws Exception {
        Web3jWalletUtils.CommonWallet commonWallet = Web3jWalletUtils.generateCommonWallet("wds123456");
        log.warn("生成的普通钱包: " + JacksonUtils.toJson(commonWallet));
        // 从json加载
        Web3jWalletUtils.CommonWallet commonWallet1 = Web3jWalletUtils.loadCommonWalletFromJson("wds123456", commonWallet.getJson());
        log.warn("加载的json: " + commonWallet1.getJson());
    }

    /**
     * 普通钱包生成和恢复 -- file
     *
     * @throws Exception e
     */
    @Test
    public void generateCommonWalletOnWalletPathTest() throws Exception {
        Web3jWalletUtils.CommonWallet commonWallet = Web3jWalletUtils.generateCommonWallet("", COMMON_WALLET_PATH);
        log.warn("生成的普通钱包: " + JacksonUtils.toJson(commonWallet));
        // 从文件加载
        Web3jWalletUtils.CommonWallet commonWallet1 = Web3jWalletUtils.loadCommonWalletFromFile("", commonWallet.getPath());
        log.warn("从文件加载的json: " + commonWallet1.getJson());
    }

    /**
     * bip39钱包生成和恢复 -- json
     *
     * @throws CipherException e
     */
    @Test
    public void bip39Wallet2Test() throws CipherException {
        Web3jWalletUtils.Bip39Wallet2 bip39Wallet2 = Web3jWalletUtils.generateBip39Wallet("");
        log.warn("生成的bip39钱包: " + JacksonUtils.toJson(bip39Wallet2));
        // 从json加载钱包
        Web3jWalletUtils.Bip39Wallet2 bip39Wallet21 = Web3jWalletUtils.loadBip39WalletFromJson("", bip39Wallet2.getMnemonic(), bip39Wallet2.getJson());
        log.warn("从json加载的bip39钱包: " + JacksonUtils.toJson(bip39Wallet21));
    }

    /**
     * bip39钱包生成和恢复 -- file
     *
     * @throws CipherException e
     * @throws IOException     e
     */
    @Test
    public void bip39Wallet2OnWalletPathTest() throws CipherException, IOException {
        Web3jWalletUtils.Bip39Wallet2 bip39Wallet2 = Web3jWalletUtils.generateBip39Wallet("", BIP39_WALLET_PATH);
        log.warn("生成的bip39钱包" + JacksonUtils.toJson(bip39Wallet2));
        Web3jWalletUtils.Bip39Wallet2 bip39Wallet21 = Web3jWalletUtils.loadBip39WalletFromFile("", bip39Wallet2.getMnemonic(), bip39Wallet2.getPath());
        log.warn("生成的bip39钱包" + JacksonUtils.toJson(bip39Wallet21));
    }

    /**
     * 生成随机密码
     */
    @Test
    public void generateRandomPasswordTest() {
        log.warn(Web3jWalletUtils.generateRandomPassword());
        log.warn(Web3jWalletUtils.generateRandomPassword(16));
    }

    /**
     * bip39钱包签名和验证交易
     */
    @Test
    public void bip39WalletSignAndVerifyTransaction() throws Exception {
        // TODO: 2020/9/24 生成bip39钱包
        Web3jWalletUtils.Bip39Wallet2 bip39Wallet2 = Web3jWalletUtils.generateBip39Wallet("123456", BIP39_WALLET_PATH);
        log.warn("生成的bip39钱包" + JacksonUtils.toJson(bip39Wallet2));
        String password = bip39Wallet2.getPassword();
        String mnemonic = bip39Wallet2.getMnemonic();
        log.warn("钱包密码: " + password);
        log.warn("钱包助记词: " + mnemonic);

        // TODO: 2020/9/24 获取原始数据
        JSONObject data = new JSONObject();
        data.put("fromWalletAddress", bip39Wallet2.getAddress());
        data.put("toWalletAddress", "0x565fe768c659259abn45cf4f1081a663d091bcb9");
        data.put("value", "99.4");
        data.put("chargeWalletAddress", "0xdd05e23c39eead942bcv63fd388ffa13a1a28307");
        data.put("chargeValue", "0.6");
        String rawData = data.toJSONString();
        log.warn("原始数据 : " + rawData);

        Credentials credentials = WalletUtils.loadBip39Credentials(password, mnemonic);
        // TODO: 2020/9/24 对原始数据进行签名
        String sign = Web3jWalletUtils.signTransaction(rawData, credentials.getEcKeyPair());
        // TODO: 2020/9/24 验证签名的数据
        boolean flag = Web3jWalletUtils.verifyTransaction(rawData, bip39Wallet2.getAddress(), sign);
        log.warn("验签结果: " + flag);
    }
}