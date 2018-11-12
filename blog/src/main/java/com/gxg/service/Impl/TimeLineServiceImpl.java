package com.gxg.service.Impl;

import com.gxg.dao.ArticleDao;
import com.gxg.entities.Article;
import com.gxg.service.TimeLineService;
import com.gxg.utils.TimeUtil;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 时间轴页面相关业务处理
 * @author 郭欣光
 * @date 2018/11/12 11:27
 */

@Service(value = "timeLineService")
public class TimeLineServiceImpl implements TimeLineService {

    @Autowired
    private ArticleDao articleDao;

    @Value("${timeline.page.record.number}")
    private int timeLinePageRecordNumber;

    /**
     * 获取时间轴页面要显示的文章信息
     *
     * @param pageNumber 页数
     * @return 页面显示相关信息
     * @author 郭欣光
     */
    @Override
    public JSONObject getTimeLine(String pageNumber) {
        if (articleDao.getCount() == 0) {
            return null;
        } else {
            int pageNumberInt = 0;
            try {
                pageNumberInt = Integer.parseInt(pageNumber);
            } catch (Exception e) {
                System.out.println("访问时间轴页面的页码不正确：" + e);
                return null;
            }
            if (pageNumberInt <= 0) {
                System.out.println("访问时间轴页面的页码不正确：" + pageNumberInt);
                return null;
            } else if (articleDao.getCount() <= (pageNumberInt - 1) * timeLinePageRecordNumber) {
                System.out.println("访问时间轴页面的页码超页：" + pageNumberInt);
                return null;
            } else {
                List<Article> articleList = articleDao.getArticleByLimitOrderByModificationTime(timeLinePageRecordNumber * (pageNumberInt - 1), timeLinePageRecordNumber);
                JSONObject timeLine = new JSONObject();
                List<String> yearList = new ArrayList<>();
                String lastYear = "";
                String lastMonth = "";
                Map<String, List<String>> monthMap = new HashMap<>();
                Map<String, List<Article>> articleMap = new HashMap<>();
                for (Article article : articleList) {
                    Timestamp time = article.getModificationTime();
                    String year = TimeUtil.getYear(time);
                    List<String> monthList = null;
                    String month = TimeUtil.getMonth(time);
                    List<Article> list = null;
                    if (lastYear.equals(year)) {
                        if (lastMonth.equals(month)) {
                            list = articleMap.get(year + month);
                        } else {
                            lastMonth = month;
                            monthList = monthMap.get(year);
                            monthList.add(month);
                            monthMap.put(year, monthList);
                            list = new ArrayList<>();
                        }
                    } else {
                        yearList.add(year);
                        lastYear = year;
                        lastMonth = month;
                        monthList = new ArrayList<>();
                        monthList.add(month);
                        monthMap.put(year, monthList);
                        list = new ArrayList<>();
                    }
                    list.add(article);
                    articleMap.put(year + month, list);
                }
                timeLine.accumulate("yearList", yearList);
                timeLine.accumulate("monthMap", monthMap);
                timeLine.accumulate("articleMap", articleMap);
                timeLine.accumulate("pageNumber", pageNumberInt);
                int allPageNumber = articleDao.getCount() % timeLinePageRecordNumber == 0 ? articleDao.getCount() / timeLinePageRecordNumber : articleDao.getCount() / timeLinePageRecordNumber + 1;
                timeLine.accumulate("allPageNumber", allPageNumber);
                if (pageNumberInt <= 1) {
                    timeLine.accumulate("preNumber", 0);
                } else {
                    int preNumber = pageNumberInt - 1;
                    timeLine.accumulate("preNumber", preNumber);
                }
                if (articleDao.getCount() > pageNumberInt * timeLinePageRecordNumber) {
                    int nextNumber = pageNumberInt + 1;
                    timeLine.accumulate("nextNumber", nextNumber);
                } else {
                    timeLine.accumulate("nextNumber", 0);
                }
                return timeLine;
            }
        }
    }
}
