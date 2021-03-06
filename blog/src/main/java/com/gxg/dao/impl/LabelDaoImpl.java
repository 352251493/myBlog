package com.gxg.dao.impl;

import com.gxg.dao.LabelDao;
import com.gxg.dao.rowmapper.LabelRowMapper;
import com.gxg.entities.Label;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

/**
 * @author 郭欣光
 * @date 2018/10/16 10:51
 */

@Repository(value = "labelDao")
public class LabelDaoImpl implements LabelDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 获取Label数量
     *
     * @return 标签数量
     * @author 郭欣光
     */
    @Override
    public int getCount() {
        String sql = "select count(1) from label";
        int rowCount = jdbcTemplate.queryForObject(sql, Integer.class);
        return rowCount;
    }

    /**
     * 获取指定范围Label，按照time降序排列
     *
     * @param start  开始坐标
     * @param length 获取个数
     * @return Label列表
     * @author 郭欣光
     */
    @Override
    public List<Label> getLabelByLimitOrderByTime(int start, int length) {
        String sql = "select * from label order by time desc limit ?, ?";
        List<Label> labelList = jdbcTemplate.query(sql, new LabelRowMapper(), start, length);
        return labelList;
    }

    /**
     * 获得指定id的标签个数
     *
     * @param id 标签id
     * @return 标签个数
     * @author 郭欣光
     */
    @Override
    public int getCountById(String id) {
        String sql = "select count(1) from label where id=?";
        int rowCount = jdbcTemplate.queryForObject(sql, Integer.class, id);
        return rowCount;
    }

    /**
     * 添加标签
     *
     * @param label 要添加的标签信息
     * @return 数据库改变的行数
     * @author 郭欣光
     */
    @Override
    public int addLabel(Label label) {
        String sql = "insert into label(id, name, time) values(?, ?, ?)";
        int changeCount = jdbcTemplate.update(sql, label.getId(), label.getName(), label.getTime());
        return changeCount;
    }

    /**
     * 删除指定id的标签
     *
     * @param id 要删除的标签id
     * @return 数据库改变行数
     */
    @Override
    public int deleteLabelById(String id) {
        String sql = "delete from label where id=?";
        int changeCount = jdbcTemplate.update(sql, id);
        return changeCount;
    }

    /**
     * 获取指定范围Label，按照time升序排列
     *
     * @param start  开始坐标
     * @param length 要获得的个数
     * @return 指定范围的label
     * @author 郭欣光
     */
    @Override
    public List<Label> getLabelByLimitOrderByTimeAsc(int start, int length) {
        String sql = "select * from label order by time limit ?, ?";
        List<Label> labelList = jdbcTemplate.query(sql, new LabelRowMapper(), start, length);
        return labelList;
    }

    /**
     * 根据id批量删除标签
     *
     * @param idList String[]要删除的标签的id数组
     * @throws Exception 执行时抛出的异常
     * @author 郭欣光
     */
    @Override
    public void delete(String[] idList) throws Exception {
        DefaultTransactionDefinition defaultTransactionDefinition = new DefaultTransactionDefinition();
        defaultTransactionDefinition.setIsolationLevel(TransactionDefinition.ISOLATION_READ_COMMITTED);
        defaultTransactionDefinition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        DataSourceTransactionManager transactionManager = new DataSourceTransactionManager(jdbcTemplate.getDataSource());
        TransactionStatus status = transactionManager.getTransaction(defaultTransactionDefinition);

        String sql = "delete from label where id=?";

        try {
            jdbcTemplate.execute(sql, new PreparedStatementCallback() {
                @Override
                public Object doInPreparedStatement(PreparedStatement preparedStatement) throws SQLException, DataAccessException {
                    for (int i = 0; i < idList.length; i++) {
                        preparedStatement.setString(1, idList[i]);
                        preparedStatement.addBatch();
                    }
                    preparedStatement.executeBatch();//数据不会持久化到数据库中
                    return null;
                }
            });
        } catch (Exception e) {
            transactionManager.rollback(status);
            throw e;
        }
        transactionManager.commit(status);
    }
}
