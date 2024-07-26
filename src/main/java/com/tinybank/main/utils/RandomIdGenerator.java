package com.tinybank.main.utils;

import org.apache.commons.lang3.RandomStringUtils;

public class RandomIdGenerator {
    public static String newUserId() {
        return "user_" + RandomStringUtils.randomAlphanumeric(10);
    }
}
