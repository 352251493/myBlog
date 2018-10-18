package com.gxg.dao.rowmapper;

import com.gxg.entities.ShortWords;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author 郭欣光
 * @date 2018/10/16 15:46
 */
public class ShortWordsMapper implements RowMapper<ShortWords> {

    @Override
    public ShortWords mapRow(ResultSet resultSet, int i) throws SQLException {
        ShortWords shortWords = new ShortWords();
        shortWords.setId(resultSet.getString("id"));
        shortWords.setWord(resultSet.getString("word"));
        shortWords.setTime(resultSet.getTimestamp("time"));
        return shortWords;
    }
}
