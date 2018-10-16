package com.gxg.dao.impl;

import com.gxg.dao.ShortWordsDao;
import com.gxg.dao.rowmapper.ShortWordsMapper;
import com.gxg.entities.ShortWords;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 毒鸡汤信息与数据库交互
 * @author 郭欣光
 * @date 2018/10/16 15:41
 */

@Repository(value = "shortWordsDao")
public class ShortWordsDaoImpl implements ShortWordsDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 获取毒鸡汤个数
     *
     * @return 毒鸡汤个数
     * @author 郭欣光
     */
    @Override
    public int getCount() {
        String sql = "select count(1) from short_words";
        int rowCount = jdbcTemplate.queryForObject(sql, Integer.class);
        return rowCount;
    }

    /**
     * 获取指定开始位置及指定个数毒鸡汤信息
     *
     * @param start  指定的开始位置
     * @param length 指定获取个数
     * @return 毒鸡汤信息
     * @author 郭欣光
     */
    @Override
    public List<ShortWords> getShortWordsByLimitOrderByTime(int start, int length) {
        String sql = "select * from short_words order by time desc limit ?, ?";
        List<ShortWords> shortWordsList = jdbcTemplate.query(sql, new ShortWordsMapper(), start, length);
        return shortWordsList;
    }
}
