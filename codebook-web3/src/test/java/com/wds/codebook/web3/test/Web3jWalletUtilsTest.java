package com.wds.codebook.web3.test;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.nacos.common.utils.JacksonUtils;
import com.google.common.collect.ImmutableList;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

    private final static ImmutableList<ChildNumber> BIP44_ETH_ACCOUNT_ZERO_PATH =
            ImmutableList.of(new ChildNumber(44, true), new ChildNumber(60, true),
                    ChildNumber.ZERO_HARDENED, ChildNumber.ZERO);
    /**
     * bip39钱包签名和验证交易
     */
    @Test
    public void signAndVerify1() throws Exception {
        //12位助记词

        List<String> str= new ArrayList<>() ;
        //length shaft sponsor ankle during bullet coil sketch connect another stock digital
        str.add("length");
        str.add("shaft");
        str.add("sponsor");
        str.add("ankle");
        str.add("during");
        str.add("bullet");
        str.add("coil");
        str.add("sketch");
        str.add("connect");
        str.add("another");
        str.add("stock");
        str.add("digital");


        //使用助记词生成钱包种子
        byte[] seed = MnemonicCode.toSeed(str, "");
        DeterministicKey masterPrivateKey = HDKeyDerivation.createMasterPrivateKey(seed);
        DeterministicHierarchy deterministicHierarchy = new DeterministicHierarchy(masterPrivateKey);
        DeterministicKey deterministicKey = deterministicHierarchy
                .deriveChild(BIP44_ETH_ACCOUNT_ZERO_PATH, false, true, new ChildNumber(0));
        byte[] bytes = deterministicKey.getPrivKeyBytes();
        ECKeyPair keyPair = ECKeyPair.create(bytes);
        //通过公钥生成钱包地址
        String address = Keys.getAddress(keyPair.getPublicKey());


        log.warn("助记词：");
        log.warn(str.toString());

        log.warn("地址：");
        log.warn("0x"+address);

        log.warn("私钥：");
        log.warn("0x"+keyPair.getPrivateKey().toString(16));

        log.warn("公钥：");
        log.warn(keyPair.getPublicKey().toString(16));


    }   /**
     * bip39钱包签名和验证交易
     */
    @Test
    public void signAndVerify() throws Exception {


//
//        助记词：length, shaft, sponsor, ankle, during, bullet, coil, sketch, connect, another, stock, digital
//        私钥：0x47ae411da5eb66894aa0dae8ca888128205cc7cb23b38675904fda5545c6b2d5
//        公钥：bed6ad054f6bacfa1a042a33bb89b51bdfedd883d24558cc3563ebf0cb25cc274fb4bf299f3684a8eb36f4b6c9b5b8add3b1bd6fc1b1178269995bf935d8becb
//        钱包地址：0x4c78c36e376eb1d701b415eaffc15c29c2170981

        String password = "";
        String mnemonic ="length, shaft, sponsor, ankle, during, bullet, coil, sketch, connect, another, stock, digital";
        log.warn("钱包助记词: " + mnemonic);

        String addr = "0x4c78c36e376eb1d701b415eaffc15c29c2170981";
        String priKey = "0x47ae411da5eb66894aa0dae8ca888128205cc7cb23b38675904fda5545c6b2d5";
        String pubKey = "bed6ad054f6bacfa1a042a33bb89b51bdfedd883d24558cc3563ebf0cb25cc274fb4bf299f3684a8eb36f4b6c9b5b8add3b1bd6fc1b1178269995bf935d8becb";


        String rawData = "123456";

        Credentials credentials = Credentials.create(priKey,pubKey);

        String sign = Web3jWalletUtils.signTransaction(rawData, credentials.getEcKeyPair());

        log.warn("地址 : " + credentials.getAddress());
        log.warn("私钥 : " + credentials.getEcKeyPair().getPrivateKey() );
        log.warn("公钥 : " + credentials.getEcKeyPair().getPublicKey());
        log.warn("sign : " + sign);

        String sign2 = "{\"r\":\"653fc7afb50e0e323b2c2bbc58475bd2e7a594aa33f32ccafa3084f91156ae5c\",\"s\":\"53dfc52b60ea9f29f057aae7777d2237237ebd0761e178d279515cf4609540c2\",\"v\":\"1b\"}";
        boolean flag = Web3jWalletUtils.verifyTransaction(rawData,addr, sign2);
        log.warn("验签结果: " + flag);
    }








}