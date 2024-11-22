package com.wds.codebook.web3.test;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.nacos.common.utils.JacksonUtils;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.wds.codebook.web3.utils.Web3jWalletUtils;
import lombok.extern.slf4j.Slf4j;
import org.bitcoinj.crypto.*;
import org.bouncycastle.util.encoders.Hex;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.web3j.crypto.*;

import java.io.IOException;
import java.util.*;

import static com.wds.codebook.web3.utils.Web3jWalletUtils.getAddrByPriKey;


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
    private final static ImmutableList<ChildNumber> BIP44_ETH_ACCOUNT_ZERO_PATH =
            ImmutableList.of(new ChildNumber(44, true), new ChildNumber(60, true),
                    ChildNumber.ZERO_HARDENED, ChildNumber.ZERO);


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


    /**
     * 生成钱包
     * @throws Exception
     */
    @Test
    public void testGenerateBip39Wallet() throws Exception {
        Web3jWalletUtils.Bip39Wallet2 bip39Wallet2 = Web3jWalletUtils.generateBip39Wallet("", BIP39_WALLET_PATH);
        log.warn("生成的bip39钱包" + JacksonUtils.toJson(bip39Wallet2));
        String password = bip39Wallet2.getPassword();
        String mnemonic = bip39Wallet2.getMnemonic();
        log.warn("钱包密码: " + password);
        log.warn("钱包助记词: " + mnemonic);
        //12位助记词
        Web3jWalletUtils.getKeyPairByMnemonic(mnemonic);
    }

    /**
     * 通过助记词获得公私钥
     */
    @Test
    public void testGetKeyPairByMnemonic()  {
        //12位助记词
        String mnemonic ="acoustic arch oppose hunt allow hole phrase hammer output subject gorilla hour";
        Web3jWalletUtils.getKeyPairByMnemonic(mnemonic);


    }

    /**
     * 通过私钥获得公钥及地址
     */
    @Test
    public void testGetAddrByPriKey()  {
        Web3jWalletUtils.getAddrByPriKey("47ae411da5eb66894aa0dae8ca888128205cc7cb23b38675904fda5545c6b2d5");
    }



   /**
     * 签名
     */
    @Test
    public void testSign() {
        String addr = "0xb25ea788f17df3d3f3981ab095c4e7adc118322d";
        String priKey = "0x4d490a92e4be62071b9a9ca9f4548c4a0f41302094eb5db066b06c9393f8f79a";
        String pubKey = "3f4fe7e67f5938bb3351f07366568c7e2fdb87df5f9fd3ca623cbce320651724613ddde8410ae98a06f2ed83e4784809da8f48306e8c4a877485f33fe9f1bf11";
        String rawData = "2024052239BGY7L7E4M9C5HP";
        Credentials credentials = Credentials.create(priKey,pubKey);
        String sign = Web3jWalletUtils.signTransaction(rawData, credentials.getEcKeyPair());
        log.warn("sign : " + sign);
    }  /**
     * bip39钱包签名和验证交易
     */
    @Test
    public void signAndVerify() throws Exception {

        String mnemonic ="length, shaft, sponsor, ankle, during, bullet, coil, sketch, connect, another, stock, digital";
        log.warn("钱包助记词: " + mnemonic);

        String addr = "0x4c78c36e376eb1d701b415eaffc15c29c2170981";
        String priKey = "0x47ae411da5eb66894aa0dae8ca888128205cc7cb23b38675904fda5545c6b2d5";
        String pubKey = "bed6ad054f6bacfa1a042a33bb89b51bdfedd883d24558cc3563ebf0cb25cc274fb4bf299f3684a8eb36f4b6c9b5b8add3b1bd6fc1b1178269995bf935d8becb";
        String rawData = "2024052024ALN6RB9W8UHONX";
        Credentials credentials = Credentials.create(priKey,pubKey);
        String sign = Web3jWalletUtils.signTransaction(rawData, credentials.getEcKeyPair());

        log.warn("地址 : " + credentials.getAddress());
        log.warn("私钥 : " + credentials.getEcKeyPair().getPrivateKey() );
        log.warn("公钥 : " + credentials.getEcKeyPair().getPublicKey());
        boolean flag1 = Web3jWalletUtils.verifyTransaction(rawData,addr, sign);
        log.warn("验签结果: " + flag1);

        log.warn("sign : " + sign);
        String sign2 = "{\"r\":\"3fd652c0c652bf358af20e88c95f19b09361abfacb0afcd58336a87d207809df\",\"s\":\"1935e8581de7d45110d2bdc817ac2234defb3bfafa1409621178cece4a042caa\",\"v\":\"1c\"}";
        boolean flag = Web3jWalletUtils.verifyTransaction(rawData,addr, sign2);
        log.warn("验签结果: " + flag);
    }

    @Test
    public void testVerify() throws Exception {
        String addr = "0x9789747c737438395Bf0FF7e12c851cDFf5dCA10";
        String rawData = "20240612A4ZS4HSCA1C2C1VY";
        String sign = "{\"v\":\"1b\",\"s\":\"75758c4babb356874b854927fcaf6b96c653c4c78b3a8992da406657b5c28210\",\"r\":\"9bd5177162c3ee24111bb33559bf1d691e5e57605dc8a7b4d1708ee743253138\"}";
        boolean flag3 = Web3jWalletUtils.verifyTransaction(rawData,addr, sign);
        log.warn("验签结果: " + flag3);
    }

    /**
     * bip39钱包签名和验证交易
     */
    @Test
    public void signAndVerify2() throws Exception {

//        String[] mnemonicList = {"unaware", "swallow", "fitness", "marine", "slogan", "anxiety", "stem", "top", "unique", "choose", "squirrel", "beef"};
//        Web3jWalletUtils.getKeyPairByMnemonic(mnemonicList);



        String addr = "0xff530e8268694c80982f81cd079b9824557a4b7f";
//        String priKey =  "06c144350c32ec267f98fd7ef21121b90f386c5b1fa7f122dc696ba170ed48df";
        String priKey = "0x6c144350c32ec267f98fd7ef21121b90f386c5b1fa7f122dc696ba170ed48df";
        String pubKey = "e80b01374a2f9621831939a07679b6456adf262396a32b60d374838cb6906b2b2f60d9c49bf0e00b95cbd6c1db1853e9aa73ef3fc6226aa8815fe237440df76e";
        String rawData = "123";
        Credentials credentials = Credentials.create(priKey,pubKey);
        String sign = Web3jWalletUtils.signTransaction(rawData, credentials.getEcKeyPair());

        log.warn("地址 : " + credentials.getAddress());
        log.warn("私钥 : " + credentials.getEcKeyPair().getPrivateKey() );
        log.warn("公钥 : " + credentials.getEcKeyPair().getPublicKey());
        boolean flag1 = Web3jWalletUtils.verifyTransaction(rawData,addr, sign);
        log.warn("验签结果: " + flag1);

        log.warn("sign : " + sign);
        String sign2 = "{\"r\":\"fa0ec91188aa348e86b76745d115d6dbacb7d226fd7ebca1dc4807bc1dc0897a\",\"s\":\"67511c8f7ea115775d7b36154a001fba5b7eb528a1edd8575379303f5dae2387\",\"v\":\"1b\"}";

        boolean flag = Web3jWalletUtils.verifyTransaction(rawData,addr, sign2);
        log.warn("验签结果: " + flag);
    }


}