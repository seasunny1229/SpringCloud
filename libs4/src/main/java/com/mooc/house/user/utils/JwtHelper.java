package com.mooc.house.user.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.common.collect.Maps;

import org.apache.commons.lang3.time.DateUtils;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Map;

public class JwtHelper {

    private static final String SECRET = "session_secret";

    private static final String ISSUER = "mooc_user";

    public static String genToken(Map<String, String> claims){

        try {

            // algorithm
            Algorithm algorithm = Algorithm.HMAC256(SECRET);

            // JWT creation builder
            JWTCreator.Builder builder = JWT.create()
                    .withIssuer(ISSUER)
                    .withExpiresAt(DateUtils.addDays(new Date(), 1));
            claims.forEach(builder::withClaim);

            // create JWT and sign
            String jwtToken = builder.sign(algorithm);

            return jwtToken;


        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static Map<String, String> verifyToken(String token){

        Algorithm algorithm = null;

        try {
            algorithm = Algorithm.HMAC256(SECRET);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }

        // jwt verifier
        JWTVerifier jwtVerifier = JWT.require(algorithm).withIssuer(ISSUER).build();

        // verify token
        DecodedJWT decodedJWT = jwtVerifier.verify(token);

        // get claims
        Map<String, Claim> map = decodedJWT.getClaims();

        // result
        Map<String, String> resultMap = Maps.newHashMap();
        map.forEach((s, claim) -> resultMap.put(s, claim.asString()));

        return resultMap;
    }




}
