package me.junbyoung.gateway_server.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;

@Component
public class JwtTokenUtil {
	private static final Logger LOGGER = LoggerFactory.getLogger(JwtTokenUtil.class);

	private final SecretKey secretKey;

	public JwtTokenUtil(@Value("${app.jwtSecret}") String jwtSecret){
		this.secretKey = Keys.hmacShaKeyFor(jwtSecret.getBytes());
	}

	public Claims getClaimsFromToken(String token) {
		return Jwts.parser()
				.verifyWith(secretKey) // 서명 키 설정
				.build()
				.parseSignedClaims(token)
				.getPayload();
	}

	public boolean validateToken(String authToken) {
		try {
			Jws<Claims> jws = Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(authToken);
			return true;
		} catch (SecurityException ex) {
			LOGGER.error("Invalid JWT signature");
		} catch (MalformedJwtException ex) {
			LOGGER.error("Invalid JWT token");
		} catch (ExpiredJwtException ex) {
			LOGGER.error("Expired JWT token");
		} catch (UnsupportedJwtException ex) {
			LOGGER.error("Unsupported JWT token");
		} catch (IllegalArgumentException ex) {
			LOGGER.error("JWT claims string is empty");
		}
		return false;
	}
}
