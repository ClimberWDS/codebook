package com.wds.codebook.leetcode.simple;

import com.google.common.collect.Lists;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author wds
 * @DateTime: 2024/4/24 10:58
 */
public class Question100 {

    /**
     * 给你一个字符串数组，请你将 字母异位词 组合在一起。可以按任意顺序返回结果列表。
     *
     * 字母异位词 是由重新排列源单词的所有字母得到的一个新单词。
     *
     *
     *
     * 示例 1:
     *
     * 输入: strs = ["eat", "tea", "tan", "ate", "nat", "bat"]
     * 输出: [["bat"],["nat","tan"],["ate","eat","tea"]]
     * 示例 2:
     *
     * 输入: strs = [""]
     * 输出: [[""]]
     * 示例 3:
     *
     * 输入: strs = ["a"]
     * 输出: [["a"]]
     *
     */
    public static List<List<String>> groupAnagrams(String[] strArr) {
//        Arrays.stream(strArr).collect(Collectors.toMap());

        return null;
    }


    public static void main(String[] args) {
        String[] strArr = {"eat", "tea", "tan", "ate", "nat", "bat"};
        List<List<String>> lists = groupAnagrams(strArr);
        System.out.println(lists);
    }

}