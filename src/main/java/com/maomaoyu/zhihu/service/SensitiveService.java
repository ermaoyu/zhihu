package com.maomaoyu.zhihu.service;

import org.apache.commons.lang.CharUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * maomaoyu    2018/12/22_19:16
 **/
@Service
public class SensitiveService implements InitializingBean {
    private static final Logger logger = LoggerFactory.getLogger(SensitiveService.class);

    /**
     *  默认敏感词替换符
     * */

    private static final  String DEFAULT_REPLACEMENT = "**";

    @Override
    public void afterPropertiesSet() throws Exception {
        rootNode = new TrieNode();
        try {
            InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("SensitiveWords.txt");
            InputStreamReader read = new InputStreamReader(is);
            BufferedReader bufferedReader = new BufferedReader(read);
            String lineTxt;
            while((lineTxt = bufferedReader.readLine())!= null){
                lineTxt = lineTxt.trim();
                addWord(lineTxt);
            }
            read.close();
        } catch (Exception e) {
           logger.error("读取敏感词文件失败" + e.getMessage());
        }
    }

    /**
     *  构造字典树
     * */
    private class TrieNode{
        /**
         *  true 关键词的终结 : false表示往后还有
         * */
        private boolean end  = false;

        /**
         *  key下一个字符,value是对应的节点
         * */
        private Map<Character,TrieNode> subNodes = new HashMap<>();

        /**
         *  向指定位置添加节点数
         * */
        void addSubNode(Character key,TrieNode node){
               subNodes.put(key,node);
        }

        /**
         *  获取下一个节点
         * */
        TrieNode getSubNode(Character key){
            return subNodes.get(key);
        }

        boolean isKeywordEnd(){
            return end;
        }

        void setKeywordEnd(boolean end){
            this.end = end;
        }

        public int getSubNodeCount(){
            return subNodes.size();
        }
    }

    /**
     *  根节点
     * */
    private TrieNode rootNode = new TrieNode();

    /**
     * 判断是否是一个符号
     */
    private boolean isSymbol(char c) {
        int ic = (int) c;
        // 0x2E80-0x9FFF 东亚文字范围
        return !CharUtils.isAsciiAlphanumeric(c) && (ic < 0x2E80 || ic > 0x9FFF);
    }

    private void addWord(String lineTxt){
        TrieNode tempNode = rootNode;//根节点
        //循环每个字节
        for (int i = 0;i < lineTxt.length();++i){
            Character c = lineTxt.charAt(i);
            //过滤空格
            if (isSymbol(c)){
                continue;
            }

            TrieNode node = tempNode.getSubNode(c);
            if (node == null){//没初始化
                node = new TrieNode();
                tempNode.addSubNode(c,node);
            }
            tempNode = node;

            if ( i == lineTxt.length() - 1){
                //关键词结束,设置结束标志
                tempNode.setKeywordEnd(true);
            }
        }
    }

    /**
     *  过滤敏感词
     * */
    public String filter(String text) {
        if (StringUtils.isBlank(text)) {
            return text;
        }
        String replacement = DEFAULT_REPLACEMENT;
        StringBuilder res = new StringBuilder();

        TrieNode tempNode = rootNode;
        int begin = 0;//回滚数
        int position = 0;//当前比价的位置

        while (position < text.length()) {
            char c = text.charAt(position);
            //空格直接跳过
            if (isSymbol(c)) {
                if (tempNode == rootNode) {
                    res.append(c);
                    ++begin;
                }
                ++position;
                continue;
            }

            tempNode = tempNode.getSubNode(c);
            if (tempNode == null) {//当前位置匹配结束
                res.append(text.charAt(begin));//以begin开始的字符串不存在敏感词
                position = begin + 1;//调到下一字符开始测试
                begin = position;
                tempNode = rootNode;//回到初始起点
            } else if (tempNode.isKeywordEnd()) {
                //发现敏感词,从begin到position的位置用replacement替换掉
                res.append(replacement);
                position = position + 1;
                begin = position;
                tempNode = rootNode;
            } else {
                ++position;
            }
        }
        res.append(text.substring(begin));
        return res.toString();
    }

    public static void main(String[] argv) {
        SensitiveService s = new SensitiveService();
        s.addWord("色情");
        s.addWord("好色");
        System.out.print(s.filter("你好*色情XX"));
    }

}
