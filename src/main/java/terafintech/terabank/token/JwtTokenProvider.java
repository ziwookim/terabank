package terafintech.terabank.token;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Getter;
import org.hibernate.cfg.Environment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import terafintech.terabank.config.ConfigProperties;

import java.util.Date;

@Controller
public class JwtTokenProvider {

    @Autowired
    ConfigProperties configProperties;

    public JwtTokenProvider() {
        System.out.println("configProperties = " + configProperties);
        System.out.println(configProperties.getSecretkey());
    }

//    private long tokenValidTime = 30000000 * 60000000 * 1000L;

    public String createToken(String userId) {
        Claims claims = Jwts.claims().setSubject(userId);

        Date now = new Date();
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
//                .setExpiration(new Date(now.getTime() + tokenValidTime))
                .signWith(SignatureAlgorithm.HS256, configProperties.getSecretkey())
                .compact();
    }

//    public boolean validateToken(String jwtToken) {
//        try {
//            Jws<Claims> claims = Jwts.parser().setSigningKey(secretCode).parseClaimsJws(jwtToken);
//            return !claims.getBody().getExpiration().before(new Date());
//        } catch (Exception e) {
//            return false;
//        }
//    }
}
