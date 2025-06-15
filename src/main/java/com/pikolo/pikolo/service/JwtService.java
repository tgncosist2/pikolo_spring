package com.pikolo.pikolo.service;

import io.jsonwebtoken.Claims;

public interface JwtService {
    String getToken(String key, Object value, int expireTime);
    Claims getClaims(String token);
}
