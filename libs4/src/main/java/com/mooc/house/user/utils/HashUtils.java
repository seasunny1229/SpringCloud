package com.mooc.house.user.utils;

import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;

import java.nio.charset.StandardCharsets;

public class HashUtils {

    private static final HashFunction FUNCTION = Hashing.md5();

    private static final HashFunction MURMUR_FUNC = Hashing.murmur3_128();

    private static final String SALT = "mooc.com";

    public static String encryptPassword(String password){
        HashCode code = FUNCTION.hashString(password + SALT, StandardCharsets.UTF_8);
        return code.toString();
    }

    public static String hashString(String input){
        HashCode hashCode = null;
        hashCode = MURMUR_FUNC.hashBytes(input.getBytes(StandardCharsets.UTF_8));
        return hashCode.toString();
    }


}
