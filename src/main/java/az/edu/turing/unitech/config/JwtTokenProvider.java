package az.edu.turing.unitech.config;

import az.edu.turing.unitech.domain.entity.MyUserDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.xml.bind.DatatypeConverter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;


import java.util.Date;

@Component
public class JwtTokenProvider {


    private final String SECRET_KEY = "thisissecretkeyhugogoguprt9gh9ptyedgypegf9p3rg6ujioyjoijiojhoyijhiouhoiujhuuit7ctuxtvftxeszrway";
    private final long EXPIRATION_TIME = 86400000;


    public String generateToken(Authentication authentication) {
        MyUserDetails userDetails = (MyUserDetails) authentication.getPrincipal();

        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setId(userDetails.getId().toString())
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, DatatypeConverter.parseBase64Binary(SECRET_KEY))
                .compact();
    }


    public String getUserIdFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(DatatypeConverter.parseBase64Binary(SECRET_KEY))
                .build()
                .parseClaimsJws(token)
                .getBody();

        // The ID was set in the token when it was created, so we retrieve it here
        return claims.getId();
    }

    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(DatatypeConverter.parseBase64Binary(SECRET_KEY))
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        System.out.println("Authorization Header: " + bearerToken);
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }


    public boolean validateToken(String token, UserDetails userDetails) {
        String username = getUsernameFromToken(token);
        System.out.println("Token Username: " + username);
        System.out.println("UserDetails Username: " + userDetails.getUsername());

        boolean isTokenExpired = isTokenExpired(token);
        System.out.println("Is Token Expired: " + isTokenExpired);

        return (username.equals(userDetails.getUsername()) && !isTokenExpired);
    }


    private boolean isTokenExpired(String token) {
        Date expirationDate = Jwts.parserBuilder()
                .setSigningKey(DatatypeConverter.parseBase64Binary(SECRET_KEY))
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
        System.out.println("Token Expiration Date: " + expirationDate);
        return expirationDate.before(new Date());
    }


}
