//package com.xidian.bankdemo;
//
//import com.xidian.bankdemo.dto.QueryDTO;
//import com.xidian.bankdemo.dto.StatementDTO;
//import com.xidian.bankdemo.entity.*;
//import com.xidian.bankdemo.mapper.*;
//import com.xidian.bankdemo.security.XCiphersm;
//import net.olymtech.javakgc.base.utils.exception.CryptoException;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.security.crypto.password.PasswordEncoder;
//
//import java.io.UnsupportedEncodingException;
//import java.math.BigDecimal;
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.List;
//import java.util.Random;
//
//@SpringBootTest
//public class MapperTests {
//    @Autowired
//    private UserMapper userMapper;
//    @Autowired
//    private AccountMapper accountMapper;
//    @Autowired
//    private FriendMapper friendMapper;
//    @Autowired
//    private TransactionMapper transactionMapper;
//
//    @Autowired
//    private LogMapper logMapper;
//
//    @Autowired
//    PasswordEncoder passwordEncoder;
//
//
//    @Test
//    void testUserMapper() throws UnsupportedEncodingException, CryptoException {
//        User ww = userMapper.findByName("wx");
//        String payPassword = ww.getPayPassword();
//        boolean flag = passwordEncoder.matches("123456",payPassword);
//        System.out.println(flag);
//
//
//    }
//
//    @Test
//    void testGetInfoByName(){
//        User user = userMapper.getInfoByName("wx");
//        System.out.println(user);
//    }
//    @Test
//    void testUpdateInfoByName(){
//        User info = new User();
//        info.setUsername("张看");
//        info.setAge(18);
//        info.setGender(0);
//        info.setEmail("zj@gmail.com");
//        int r = userMapper.setInfoByName(info);
//        System.out.println(r);
//    }
//    @Test
//    void setPwdByName(){
//        System.out.println(userMapper.setPwdByName("管理员", "12345678"));
//    }
//    @Test
//    void testGetAccountList() {
//        List<Account> accounts = accountMapper.findByOwnerId(2L);
//        System.out.println(accounts);
//    }
//    @Test
//    void testExistsByAccountId(){
//        boolean b = accountMapper.existsById(1234567L);
//        System.out.println(b);
//    }
//    @Test
//    void testAddAccount() {
//        Account account = new Account();
//        account.setId(11111111L);
//        account.setBankName("中国银行");
//        account.setPhone("12311111111");
//        account.setBalance(BigDecimal.valueOf(0.00));
//        account.setStatus(0);
//        account.setOwnerId(2L);
//
//        int result = accountMapper.insertAccount(account);
//        System.out.println(result);
//    }
//    @Test
//    void testUpdateCode() {
//        int code = new Random().nextInt(999999);
//        System.out.println(code);
//        System.out.println(userMapper.updateCode(2L, code));
//    }
//    @Test
//    void testFindFriends() {
//        List<Friend> friends = friendMapper.findByOwnerId(2L);
//        System.out.println(friends);
//    }
//    @Test
//    void testGetTransactionList() {
//        QueryDTO queryDTO = new QueryDTO();
//        queryDTO.setMyAccount(6222604444444444444L);
////        queryDTO.setObjAccount(123456789L);
////        queryDTO.setMinAmount(BigDecimal.valueOf(2));
////        queryDTO.setMaxAmount(BigDecimal.valueOf(2));
////        queryDTO.setOffset(0);
////        queryDTO.setLimit(2);
////        List<StatementDTO> list = transactionMapper.findByQuery(queryDTO);
////        System.out.println(list);
////        System.out.println("转入："+ list.size());
//        List<StatementDTO> incomeByQuery = transactionMapper.findIncomeByQuery(queryDTO);
//        List<StatementDTO> outcomeByQuery = transactionMapper.findOutcomeByQuery(queryDTO);
//        System.out.println(incomeByQuery);
//        System.out.println(outcomeByQuery);
//
//
//    }
//    @Test
//    void testUpdateAmountById(){
//        int i = accountMapper.updateBalanceById(1234567L, BigDecimal.valueOf(4).negate());
//        System.out.println(i);
//    }
//    @Test
//    void existsByNameAndCode(){
//        boolean b = userMapper.existsByNameAndCode("张看", 479937);
//        System.out.println(b);
//    }
//    @Test
//    void existsByAccount(){
//        boolean b = friendMapper.existsByAccount(123456789L, 2L);
//        System.out.println(b);
//    }
//    @Test
//    void insertFriend(){
//        Friend friend = new Friend(123456789L,
//                "张2看",
//                "交通银行",
//                2L);
//        int i = friendMapper.insertFriend(friend);
//        System.out.println(i);
//    }
//    @Test
//    void insertTransaction(){
//        Transaction transaction = new Transaction(123456789L,
//                6222601111111111112L,
//                "张看",
//                "张看",
//                BigDecimal.valueOf(3),
//                "测试转账",
//                0);
//        System.out.println(transactionMapper.insertTransaction(transaction));
//    }
//    @Test
//    void testFindAllUser(){
//        List<User> all = userMapper.findAll();
//        System.out.println(all);
//    }
//    @Test
//    void test1() throws CryptoException, UnsupportedEncodingException {
//
//    }
//
//
//    @Test
//    void test2() throws ParseException {
//        String s ="20210901071221";
//        SimpleDateFormat sDate = new SimpleDateFormat("yyyyMMddHHmmss");
//        Date parse = sDate.parse(s);
//        System.out.println(parse);
//
//
//
//    }
//
//    @Test
//    void testInsertLog()
//    {
//        Log log = new Log();
//        log.setUsername("han");
//        log.setObj("用户名和密码");
//        log.setDescription("用户登录");
//        log.setDeleted(0);
//        log.setType(3);
//        log.setResult(1);
//        log.setGmtCreate(new Date());
//        log.setGmtModified(new Date());
//        System.out.println("本地时间戳："+new Date());
//        logMapper.insertLog(log);
//
//    }
//
//}
