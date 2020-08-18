package com.jackeroo.oauth.common.utils;

import org.springframework.util.StringUtils;

import java.util.HashSet;
import java.util.Set;

/**
 * @author alex
 * @date 2020/07/23
 */
public class CollectionUtil {

    private static final String SEPARATOR_COMMA = ",";

    public static Set<String> getSetBySplit(String content){
        return getSetBySplit(content, SEPARATOR_COMMA);
    }

    public static Set<String> getSetBySplit(String content, String separator){
        Set<String> set = new HashSet<>();
        if(StringUtils.isEmpty(content)){
            return set;
        }
        for (String item : content.split(separator)) {
            set.add(item);
        }
        return set;
    }
}
