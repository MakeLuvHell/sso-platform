package com.sso.common.core.util;

import com.sso.common.core.constant.SecurityConstants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * JWT 工具类
 */
public final class JwtUtils {

    private JwtUtils() {
    }

    /**
     * 生成密钥
     */
    public static SecretKey generateKey(String secret) {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 生成访问令牌
     *
     * @param subject   主题 (通常是用户ID)
     * @param claims    自定义声明
     * @param secret    密钥
     * @param expirationMs 过期时间(毫秒)
     * @return JWT Token
     */
    public static String generateAccessToken(String subject, Map<String, Object> claims,
                                             String secret, long expirationMs) {
        SecretKey key = generateKey(secret);
        Date now = new Date();
        Date expiration = new Date(now.getTime() + expirationMs);

        return Jwts.builder()
                .subject(subject)
                .claims(claims)
                .issuer(SecurityConstants.JWT_ISSUER)
                .issuedAt(now)
                .expiration(expiration)
                .signWith(key)
                .compact();
    }

    /**
     * 解析令牌
     *
     * @param token  JWT Token
     * @param secret 密钥
     * @return Claims
     */
    public static Claims parseToken(String token, String secret) {
        SecretKey key = generateKey(secret);
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * 获取用户ID
     */
    public static String getUserId(String token, String secret) {
        Claims claims = parseToken(token, secret);
        return claims.getSubject();
    }

    /**
     * 获取用户名
     */
    public static String getUsername(String token, String secret) {
        Claims claims = parseToken(token, secret);
        return claims.get(SecurityConstants.CLAIM_USERNAME, String.class);
    }

    /**
     * 获取租户ID
     */
    public static Long getTenantId(String token, String secret) {
        Claims claims = parseToken(token, secret);
        return claims.get(SecurityConstants.CLAIM_TENANT_ID, Long.class);
    }

    /**
     * 获取角色列表
     */
    @SuppressWarnings("unchecked")
    public static List<String> getRoles(String token, String secret) {
        Claims claims = parseToken(token, secret);
        return claims.get(SecurityConstants.CLAIM_ROLES, List.class);
    }

    /**
     * 判断令牌是否过期
     */
    public static boolean isTokenExpired(String token, String secret) {
        try {
            Claims claims = parseToken(token, secret);
            return claims.getExpiration().before(new Date());
        } catch (Exception e) {
            return true;
        }
    }

    /**
     * 验证令牌
     */
    public static boolean validateToken(String token, String secret) {
        try {
            parseToken(token, secret);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
