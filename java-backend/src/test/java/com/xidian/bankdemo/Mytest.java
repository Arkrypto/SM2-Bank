package com.xidian.bankdemo;


import com.xidian.bankdemo.dto.QueryDTO;
import com.xidian.bankdemo.entity.Transaction;
import com.xidian.bankdemo.mapper.AccountMapper;
import com.xidian.bankdemo.mapper.LogMapper;
import com.xidian.bankdemo.mapper.TransactionMapper;
import com.xidian.bankdemo.mapper.UserMapper;
import com.xidian.bankdemo.service.AccountService;
import net.olymtech.javakgc.base.sm2.cert.CertInfoTem;
import net.olymtech.javakgc.base.sm2.cert.PemCertParser;
import org.bouncycastle.asn1.x509.Time;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.*;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.Security;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.*;

@SpringBootTest
public class Mytest {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private LogMapper logMapper;

    @Autowired
    private TransactionMapper transactionMapper;

    @Autowired
    private AccountService accountService;

    static {
        Security.removeProvider("SunEC");
    }

    @Test
    void test(){
        QueryDTO queryDTO = new QueryDTO();
        queryDTO.setMyAccount(6222603333333333333L);
        List<Transaction> byQuery = transactionMapper.findByQuery(queryDTO);

    }

    @Test
    void test1() throws Exception {
        String message = "-----BEGIN CERTIFICATE-----\n" +
                "MIIM3zCCCsegAwIBAgITawAMbdcfI5Nyb1TMVAAAAAxt1zANBgkqhkiG9w0BAQsF\n" +
                "ADBPMQswCQYDVQQGEwJVUzEeMBwGA1UEChMVTWljcm9zb2Z0IENvcnBvcmF0aW9u\n" +
                "MSAwHgYDVQQDExdNaWNyb3NvZnQgUlNBIFRMUyBDQSAwMTAeFw0yMTA0MTIwMjAx\n" +
                "MTBaFw0yMTEwMTIwMjAxMTBaMBcxFTATBgNVBAMTDHd3dy5iaW5nLmNvbTCCASIw\n" +
                "DQYJKoZIhvcNAQEBBQADggEPADCCAQoCggEBANwoLj/ulH8Rr+6rPbHda6eRo/eC\n" +
                "/SW1TI3s1NkTHcIF4XXgyNGMVbtH5StbSfXfGzpWToppgTpeox6R3PnfQ9YwwEMJ\n" +
                "zbzFsvvIUKxCrOzmmSEB9aML8l+Oxn9Z87iyBtjVYXaKlgmoQQDb8bYWbJ7NHWSv\n" +
                "6mDj9G/t97hYnZVMfqJsWUBFSrAu5WlBop/+1aQ++2KK2PNdqAdt4qpY8LbTTNUU\n" +
                "KIcIZW5hkan4/TSMqaU/og2THagc0J8Jo7w77knZ8A24OJxBSBQPIO1Ok5SpxucW\n" +
                "FWDR257gmwC8igbRB9db85sMHrIvpywzC2p1kPOfec+xFFDxvfOTlMGWSMUCAwEA\n" +
                "AaOCCOowggjmMIIBAwYKKwYBBAHWeQIEAgSB9ASB8QDvAHUAfT7y+I//iFVoJMLA\n" +
                "yp5SiXkrxQ54CX8uapdomX4i8NcAAAF4w9joHgAABAMARjBEAiAQjusD9By8AkZJ\n" +
                "1QD5gXjRbEXe4gfrJHAQsJlbsH5lXQIgEfa2jm93Xt9gIjNAVYqrpXg1j9ucIUGs\n" +
                "sw6Vr5D8ex0AdgBElGUusO7Or8RAB9io/ijA2uaCvtjLMbU/0zOWtbaBqAAAAXjD\n" +
                "2OhtAAAEAwBHMEUCIQCWxZHw8JGJDzNPDB+45SFRZdN6m75jEeDihLiOaGRXygIg\n" +
                "VYw695kMy78/1176ru5/LPPzOT9CsrRpzpF+JGHiuT4wJwYJKwYBBAGCNxUKBBow\n" +
                "GDAKBggrBgEFBQcDATAKBggrBgEFBQcDAjA+BgkrBgEEAYI3FQcEMTAvBicrBgEE\n" +
                "AYI3FQiH2oZ1g+7ZAYLJhRuBtZ5hhfTrYIFdhYaOQYfCmFACAWQCAScwgYcGCCsG\n" +
                "AQUFBwEBBHsweTBTBggrBgEFBQcwAoZHaHR0cDovL3d3dy5taWNyb3NvZnQuY29t\n" +
                "L3BraS9tc2NvcnAvTWljcm9zb2Z0JTIwUlNBJTIwVExTJTIwQ0ElMjAwMS5jcnQw\n" +
                "IgYIKwYBBQUHMAGGFmh0dHA6Ly9vY3NwLm1zb2NzcC5jb20wHQYDVR0OBBYEFGwz\n" +
                "qsSUKx0F8IpiLsTyNf02XrqTMA4GA1UdDwEB/wQEAwIEsDCCBW0GA1UdEQSCBWQw\n" +
                "ggVgggx3d3cuYmluZy5jb22CEGRpY3QuYmluZy5jb20uY26CEyoucGxhdGZvcm0u\n" +
                "YmluZy5jb22CCiouYmluZy5jb22CCGJpbmcuY29tghZpZW9ubGluZS5taWNyb3Nv\n" +
                "ZnQuY29tghMqLndpbmRvd3NzZWFyY2guY29tghljbi5pZW9ubGluZS5taWNyb3Nv\n" +
                "ZnQuY29tghEqLm9yaWdpbi5iaW5nLmNvbYINKi5tbS5iaW5nLm5ldIIOKi5hcGku\n" +
                "YmluZy5jb22CGGVjbi5kZXYudmlydHVhbGVhcnRoLm5ldIINKi5jbi5iaW5nLm5l\n" +
                "dIINKi5jbi5iaW5nLmNvbYIQc3NsLWFwaS5iaW5nLmNvbYIQc3NsLWFwaS5iaW5n\n" +
                "Lm5ldIIOKi5hcGkuYmluZy5uZXSCDiouYmluZ2FwaXMuY29tgg9iaW5nc2FuZGJv\n" +
                "eC5jb22CFmZlZWRiYWNrLm1pY3Jvc29mdC5jb22CG2luc2VydG1lZGlhLmJpbmcu\n" +
                "b2ZmaWNlLm5ldIIOci5iYXQuYmluZy5jb22CECouci5iYXQuYmluZy5jb22CEiou\n" +
                "ZGljdC5iaW5nLmNvbS5jboIPKi5kaWN0LmJpbmcuY29tgg4qLnNzbC5iaW5nLmNv\n" +
                "bYIQKi5hcHBleC5iaW5nLmNvbYIWKi5wbGF0Zm9ybS5jbi5iaW5nLmNvbYINd3Au\n" +
                "bS5iaW5nLmNvbYIMKi5tLmJpbmcuY29tgg9nbG9iYWwuYmluZy5jb22CEXdpbmRv\n" +
                "d3NzZWFyY2guY29tgg5zZWFyY2gubXNuLmNvbYIRKi5iaW5nc2FuZGJveC5jb22C\n" +
                "GSouYXBpLnRpbGVzLmRpdHUubGl2ZS5jb22CDyouZGl0dS5saXZlLmNvbYIYKi50\n" +
                "MC50aWxlcy5kaXR1LmxpdmUuY29tghgqLnQxLnRpbGVzLmRpdHUubGl2ZS5jb22C\n" +
                "GCoudDIudGlsZXMuZGl0dS5saXZlLmNvbYIYKi50My50aWxlcy5kaXR1LmxpdmUu\n" +
                "Y29tghUqLnRpbGVzLmRpdHUubGl2ZS5jb22CCzNkLmxpdmUuY29tghNhcGkuc2Vh\n" +
                "cmNoLmxpdmUuY29tghRiZXRhLnNlYXJjaC5saXZlLmNvbYIVY253ZWIuc2VhcmNo\n" +
                "LmxpdmUuY29tggxkZXYubGl2ZS5jb22CDWRpdHUubGl2ZS5jb22CEWZhcmVjYXN0\n" +
                "LmxpdmUuY29tgg5pbWFnZS5saXZlLmNvbYIPaW1hZ2VzLmxpdmUuY29tghFsb2Nh\n" +
                "bC5saXZlLmNvbS5hdYIUbG9jYWxzZWFyY2gubGl2ZS5jb22CFGxzNGQuc2VhcmNo\n" +
                "LmxpdmUuY29tgg1tYWlsLmxpdmUuY29tghFtYXBpbmRpYS5saXZlLmNvbYIObG9j\n" +
                "YWwubGl2ZS5jb22CDW1hcHMubGl2ZS5jb22CEG1hcHMubGl2ZS5jb20uYXWCD21p\n" +
                "bmRpYS5saXZlLmNvbYINbmV3cy5saXZlLmNvbYIcb3JpZ2luLmNud2ViLnNlYXJj\n" +
                "aC5saXZlLmNvbYIWcHJldmlldy5sb2NhbC5saXZlLmNvbYIPc2VhcmNoLmxpdmUu\n" +
                "Y29tghJ0ZXN0Lm1hcHMubGl2ZS5jb22CDnZpZGVvLmxpdmUuY29tgg92aWRlb3Mu\n" +
                "bGl2ZS5jb22CFXZpcnR1YWxlYXJ0aC5saXZlLmNvbYIMd2FwLmxpdmUuY29tghJ3\n" +
                "ZWJtYXN0ZXIubGl2ZS5jb22CE3dlYm1hc3RlcnMubGl2ZS5jb22CFXd3dy5sb2Nh\n" +
                "bC5saXZlLmNvbS5hdYIUd3d3Lm1hcHMubGl2ZS5jb20uYXUwgbAGA1UdHwSBqDCB\n" +
                "pTCBoqCBn6CBnIZNaHR0cDovL21zY3JsLm1pY3Jvc29mdC5jb20vcGtpL21zY29y\n" +
                "cC9jcmwvTWljcm9zb2Z0JTIwUlNBJTIwVExTJTIwQ0ElMjAwMS5jcmyGS2h0dHA6\n" +
                "Ly9jcmwubWljcm9zb2Z0LmNvbS9wa2kvbXNjb3JwL2NybC9NaWNyb3NvZnQlMjBS\n" +
                "U0ElMjBUTFMlMjBDQSUyMDAxLmNybDBXBgNVHSAEUDBOMEIGCSsGAQQBgjcqATA1\n" +
                "MDMGCCsGAQUFBwIBFidodHRwOi8vd3d3Lm1pY3Jvc29mdC5jb20vcGtpL21zY29y\n" +
                "cC9jcHMwCAYGZ4EMAQIBMB8GA1UdIwQYMBaAFLV2DDARzseSQk1Mx1wsyKkM6Atk\n" +
                "MB0GA1UdJQQWMBQGCCsGAQUFBwMBBggrBgEFBQcDAjANBgkqhkiG9w0BAQsFAAOC\n" +
                "AgEAco89TAB1sCvQLroHS/xq5X+7QOIEa3+BQDho6pdRfQ4W1sgBJPJE9oe84V3i\n" +
                "bTME8bAGsURyu1SSFZ++JPFkcdggIR155kU8dZEjRDPAlwcEYX8AkZJnCE1HW8x1\n" +
                "asVMF978B9cyq35tgS9UPQu9j0bR1cbLZC8CzmnMVgUYjex/tZqcv0d1M+c2pIMT\n" +
                "Tl3SJQ8h6gE2YyWhQRdj3WxqyJKxVQOeGLN4g0GKm2iTfaXPAN3A7L0Okgl4JCI/\n" +
                "VQTz/tTaG/f9mM3+vgtY9QIQKC0Swx5abPTMJW6YJiK8QoiM98L+e+xvuVHmcG2D\n" +
                "Wjeph1515z/kb+zXrHjUGsr3rwuEXSWGFFmzwzYdSZLSGwAucUskg8dKkU9OpAPm\n" +
                "GA0dXfLbL9imaw/50PhLDo6XrEBjQUihArxDRRGEd7YGBU0W08povMRIE6tbxQZJ\n" +
                "sXREWlACD/SBlSx5pAmE7feAS7T82HrH4jm08/07zAnyh9WNqQH5flBjvHHHN9oC\n" +
                "fP6/q9LcSqSx2KLskGfpaCq7RQpaYKhj9wVdHWnfAUcMTiiQgTl2heWLtfEbIUDf\n" +
                "IGSg9oSdjpP8bxRgTcISZEcGeJLfJWqMJclDiseusW9mAqs0NY0/VvXmyjnL2eZ2\n" +
                "ZKVj0GlyGE1bYkFlXlJ1DbRLrg7xJ+kl9iT/nv84uN+lfgg=\n" +
                "-----END CERTIFICATE-----";
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        byte[] bytes = message.getBytes(StandardCharsets.UTF_8);
        PemCertParser pemCertParser = new PemCertParser();
        CertInfoTem certInfoTem = pemCertParser.parseCert(bytes);
        String algorithm = certInfoTem.getAlgorithm();
        String issuer = certInfoTem.getIssuer();
        int version = certInfoTem.getVersion();
        String serialNumber = certInfoTem.getSerialNumber();
        BitSet signature = certInfoTem.getSignature();
        byte[] publicKey = certInfoTem.getPublicKey();
        String subject = certInfoTem.getsubject();
        Time endTime = certInfoTem.getEndTime();

        System.out.println(endTime);

    }

    @Test
    void test2() throws FileNotFoundException, CertificateException {
        File file = new File("C:\\Users\\89248\\Desktop\\密评管理\\bank-demo\\bank-demo\\backend\\test.pem..cer");
        FileInputStream fileInputStream = new FileInputStream(file);
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        X509Certificate certificate =(X509Certificate) cf.generateCertificate(fileInputStream);
        BigInteger serialNumber = certificate.getSerialNumber();
        String sigAlgName = certificate.getSigAlgName();
        System.out.println(sigAlgName);
        System.out.println(serialNumber);


    }


}
