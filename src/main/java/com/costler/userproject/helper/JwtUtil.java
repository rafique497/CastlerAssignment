package com.costler.userproject.helper;

import com.costler.userproject.entity.User;
import com.costler.userproject.repo.UserRepo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.Function;

@Component
public class JwtUtil 
{
    @Autowired
    private UserRepo userRepo;
	private static final long serialVersionUID=-2550185165626007488L;
	private static final long JWT_TOKEN_VALIDITY = 5 * 60 * 60 * 1000 * 60;
	private String secret = "rafique";

  public String getUsernameFromToken(String token) {
      return getClaimFromToken(token,Claims::getSubject);
  }
    public String getRoleFromToken(String token) {
        Claims claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
        ArrayList<Object> abc = (ArrayList<Object>) claims.get("role");
        LinkedHashMap<String,String> ab = (LinkedHashMap<String, String>) abc.get(0);
        return ab.get("authority");
    }
	public Date getExpirationDateFromToken(String token) {
      return getClaimFromToken(token,Claims::getExpiration);
  }

  public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
      final Claims claims = getAllClaimsFromToken(token);
      return claimsResolver.apply(claims);
  }
  private Claims getAllClaimsFromToken(String token) {
      return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
  }

  private Boolean isTokenExpired(String token) {
      final Date expiration=getExpirationDateFromToken(token);
      return expiration.before(new Date());
  }

  public String generateToken(UserDetails userDetails) {
      Map<String, Object> claims = new HashMap<>();
      claims.put("role",userDetails.getAuthorities());
      User user = userRepo.findByEmail(userDetails.getUsername());
      claims.put("city",user.getCity());
      claims.put("company",user.getCompanyName());
      return doGenerateToken(claims, userDetails.getUsername(),user.getRole(),user.getName());
  }

  private String doGenerateToken(Map<String, Object> claims, String subject,String role,String name) {
      return Jwts.builder().setClaims(claims).setSubject(subject).setId(name).setIssuer(role).setIssuedAt(new Date(System.currentTimeMillis()))
              .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY))
              .signWith(SignatureAlgorithm.HS256, secret).compact();
  }

  public Boolean validateToken(String token, UserDetails userDetails) {
      final String username = getUsernameFromToken(token);
      return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
  }
}
