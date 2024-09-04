package com.xidian.bankdemo.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TokenUtil {
    private static final int EXPIRATION_TIME = 12 * 3600 * 1000;
    private static final SecretKey SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String AUTHORIZATION = "Authorization";
//    public static final KeyPair keyPair = Keys.keyPairFor(SignatureAlgorithm.ES256);
    private  static final  String privateKey = "MEECAQAwEwYHKoZIzj0CAQYIKoZIzj0DAQcEJzAlAgEBBCBQzIYsBvcuCFup2uAaaK_JThxQRtBJaisg7yLxzfV7ug";
    public  static  final  String publicKey = "MFkwEwYHKoZIzj0CAQYIKoZIzj0DAQcDQgAEfEby_DdZy2Y9qUOYxdu2foanh0HejNvtfCQRCBplADHvC7lc7kE0dGxFeoXfQ-4CkEFvqG1liodjzo_cn7miDQ";
    public static String generateToken(String username) {
        return TOKEN_PREFIX + Jwts.builder()
                .setSubject(username)
                .claim("username", username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SECRET_KEY)
                .compact();
    }

    public static String validateToken(String jwtToken) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(jwtToken.replace(TOKEN_PREFIX, ""))
                .getBody();
        return claims.get("username").toString();
    }

    public static String generateServerSignature(Map<String, Object> map) throws NoSuchAlgorithmException, InvalidKeySpecException {

        KeyFactory ec = KeyFactory.getInstance("EC");
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(Decoders.BASE64URL.decode(privateKey));
        PrivateKey privateKey = ec.generatePrivate(pkcs8EncodedKeySpec);
        return Jwts.builder()
                .signWith(privateKey)
                .setSubject("server")
                .claim("publicKey", publicKey)
                .setIssuedAt(new Date())
                .addClaims(map)
                .compact();

    }

    public static void main(String[] args) throws NoSuchAlgorithmException, InvalidKeySpecException {

        HashMap<String, Object> map = new HashMap<>();
        map.put("userName","hanha");
        String s = generateServerSignature(map);
        System.out.println(s);

        KeyFactory ec = KeyFactory.getInstance("EC");
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(Decoders.BASE64URL.decode(publicKey));
        PublicKey publicKey = ec.generatePublic(x509EncodedKeySpec);
        Claims body = Jwts.parserBuilder()
                .setSigningKey(publicKey)
                .build()
                .parseClaimsJws(s)
                .getBody();
        System.out.println(body.get("userName"));


    }
}
