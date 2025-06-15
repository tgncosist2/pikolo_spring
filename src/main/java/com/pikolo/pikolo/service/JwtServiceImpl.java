package com.pikolo.pikolo.service;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service("jwtService")
public class JwtServiceImpl implements JwtService {

    private final String secretKey = "$!@N4WEpfn$@!xkvSe1Gtq$!@$$@!rzNXnP2Npimm7JRw@#";

    /**
     * SecretKey 생성 메서드 (권장 방식)
     */
    private SecretKey getSigningKey() {
        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    @Override
    public String getToken(String key, Object value, int expireTime) {
        Date expTime = new Date();
        expTime.setTime(System.currentTimeMillis() + expireTime);
        
        SecretKey signKey = getSigningKey();

        Map<String, Object> headerMap = new HashMap<>();
        headerMap.put("typ", "JWT");
        headerMap.put("alg", "HS256");

        Map<String, Object> map = new HashMap<>();
        map.put(key, value);

        JwtBuilder builder = Jwts.builder()
                .header()
                .add(headerMap)
                .and()
                .claims()
                .add(map)
                .issuedAt(new Date())
                .expiration(expTime)
                .and()
                .signWith(signKey);

        return builder.compact();
    }

    @Override
    public Claims getClaims(String token) {
        if (token != null && !token.isEmpty()) {
            try {
                SecretKey signKey = getSigningKey();
                Claims claims = Jwts.parser()
                        .verifyWith(signKey)
                        .build()
                        .parseSignedClaims(token)
                        .getPayload();
                return claims;
            } catch (ExpiredJwtException e) {
                System.out.println("토큰이 만료되었습니다: " + e.getMessage());
                return null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}