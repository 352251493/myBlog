package com.gxg.service.Impl;

import com.gxg.dao.LeaveMessageDao;
import com.gxg.entities.Blog;
import com.gxg.entities.LeaveMessage;
import com.gxg.service.BlogService;
import com.gxg.service.LeaveMessageService;
import com.gxg.service.MailService;
import com.gxg.utils.NumberUtil;
import com.gxg.utils.RegularExpressionUtil;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.sql.Timestamp;
import java.util.*;

/**
 * @author 郭欣光
 * @date 2018/11/13 14:17
 */

@Service(value = "leaveMessageService")
public class LeaveMessageServiceImpl implements LeaveMessageService {

    @Autowired
    private LeaveMessageDao leaveMessageDao;

    @Value("${leave.message.each.page.number}")
    private int leaveMessageEachPageNumber;

    @Autowired
    private BlogService blogService;

    @Autowired
    Configuration configuration;//这里注入的是freeMarker的configuration

    @Autowired
    private MailService mailService;

    @Value("${leave.message.head_img.list}")
    private String[] leaveMessageHeadImgList;

    /**
     * 获得留言列表
     *
     * @param messagePage 页数
     * @param request     用户请求信息
     * @return 留言列表
     * @author 郭欣光
     */
    @Override
    public String getLeaveMessageList(String messagePage, HttpServletRequest request) {
        JSONObject result = new JSONObject();
        String status = "false";
        String content = "获取失败！";
        String isUser = "false";
        String isHasMessage = "false";
        int messagePageInt = 0;
        List<LeaveMessage> leaveMessageList = null;
        try {
            messagePageInt = Integer.parseInt(messagePage);
        } catch (Exception e) {
            System.out.println("获取留言列表时，传入的页数不是整数：" + messagePage);
            content = "传入的页数不是整型数字！";
        }
        if (messagePageInt <= 0) {
            if ("获取失败！".equals(content)) {
                content = "传入的页数小于或等于0！";
                System.out.println("获取留言列表时，传入的页数小于或等于0：" + messagePage);
            }
        } else if (leaveMessageDao.getCount() == 0) {
            content = "没有留言！";
        } else if (leaveMessageDao.getCount() <= (messagePageInt - 1) * leaveMessageEachPageNumber) {
            status = "true";
            content = "没有更多留言了";
        } else {
            leaveMessageList = leaveMessageDao.getLeaveMessageByLimitOrderByCreateTime(leaveMessageEachPageNumber * (messagePageInt - 1), leaveMessageEachPageNumber);
            status = "true";
            isHasMessage = "true";
            HttpSession session = request.getSession();
            if (session.getAttribute("user") != null) {
                isUser = "true";
            }
        }
        result.accumulate("status", status);
        result.accumulate("isUser", isUser);
        result.accumulate("isHasMessage", isHasMessage);
        if (leaveMessageList != null) {
            result.accumulate("content", leaveMessageList);
        } else {
            result.accumulate("content", content);
        }
        return result.toString();
    }

    /**
     * 发送留言验证码
     *
     * @param leaveMessageName 留言者昵称
     * @param email            邮箱
     * @param request          用户请求信息
     * @author 郭欣光
     */
    @Override
    public String sendEmailCheckCdoe(String leaveMessageName, String email, HttpServletRequest request) {
        JSONObject result = new JSONObject();
        String status = "false";
        String content = "发送验证码失败！";
        if (email == null || "".equals(email) || email.length() == 0) {
            content = "邮箱信息为空！";
        } else if (RegularExpressionUtil.checkEmail(email)) {
            String leaveMessageEmailCheckCode = NumberUtil.makeNumber(6);
            HttpSession session = request.getSession();
            session.setAttribute("leaveMessageEmailCheckCode", leaveMessageEmailCheckCode);
            Blog blog = blogService.getBlog(request);
            String fromUserName = "随遇而安。";
            if (blog != null && blog.getOwnerName() != null && !"".equals(blog.getOwnerName())) {
                fromUserName = blog.getOwnerName();
            }
            String toUserName = "有缘人";
            if (leaveMessageName != null && leaveMessageName.length() != 0 && !"".equals(leaveMessageName)) {
                toUserName = leaveMessageName;
            }
            Map<String, Object> model = new HashMap<String, Object>();
            model.put("time", new Date());
            model.put("articleCommentEmailCheckCode", leaveMessageEmailCheckCode);
            model.put("toUserName", toUserName);
            model.put("fromUserName", fromUserName);
            try {
                Template t = configuration.getTemplate("email_check_code.ftl");
                String emailContent = FreeMarkerTemplateUtils.processTemplateIntoString(t, model);
                JSONObject sendEmailResult = mailService.sendHtmlMail(email, "邮箱验证", emailContent);
                status = sendEmailResult.getString("status");
                content = sendEmailResult.getString("content");
            } catch (Exception e) {
                System.out.println("发送邮件失败，失败原因：" + e);
                content = "发送邮件失败！";
            }
        } else {
            content = "邮箱格式不正确！";
        }
        result.accumulate("status", status);
        result.accumulate("content", content);
        return result.toString();
    }

    /**
     * 验证邮箱验证码并发布留言
     *
     * @param leaveMessageName           留言者昵称
     * @param leaveMessageEmail          留言者邮箱
     * @param leaveMessageComment        留言内容
     * @param leaveMessageEmailCheckCode 邮箱验证码
     * @param request                    用户请求信息
     * @return 处理结果
     * @author 郭欣光
     */
    @Override
    public synchronized String checkEmailCheckCodeAndPublish(String leaveMessageName, String leaveMessageEmail, String leaveMessageComment, String leaveMessageEmailCheckCode, HttpServletRequest request) {
        JSONObject result = new JSONObject();
        String status = "false";
        String content = "发表留言出错！";
        if (leaveMessageName == null ||"".equals(leaveMessageName) || leaveMessageName.length() == 0) {
            content = "请留下您的昵称";
        } else if (leaveMessageEmail == null || "".equals(leaveMessageEmail) || leaveMessageEmail.length() == 0) {
            content = "请留下您的邮箱，您的邮箱信息将不会被其他人看到，该邮箱仅用于对您提出的意见进行探讨";
        } else if (leaveMessageComment == null || "".equals(leaveMessageComment) || leaveMessageComment.length() == 0) {
            content = "说点什么呗~";
        } else if (leaveMessageEmailCheckCode == null || leaveMessageEmailCheckCode.length() == 0 || "".equals(leaveMessageEmailCheckCode)) {
            content = "请输入验证码！";
        } else if (leaveMessageName.length() > 20) {
            content = "昵称长度不能大于20字符";
        } else if (!RegularExpressionUtil.checkEmail(leaveMessageEmail)) {
            content = "您输入的邮箱格式不正确，您的邮箱信息将不会被其他人看到，该邮箱仅用于对您提出的意见进行探讨";
        } else if (leaveMessageEmail.length() > 50) {
            content = "邮箱长度不能大于50字符";
        } else if (leaveMessageComment.length() > 300) {
            content = "评论内容不能超过300字符";
        } else if (leaveMessageHeadImgList.length == 0) {
            System.out.println("系统内留言头像列表为空，无法分配头像！");
            content = "系统内留言头像列表为空，无法分配头像！";
        } else {
            HttpSession session = request.getSession();
            if (session.getAttribute("leaveMessageEmailCheckCode") == null) {
                content = "验证码信息已失效！";
            } else {
                String systemLeaveMessageEmailCheckCode = (String)session.getAttribute("leaveMessageEmailCheckCode");
                if (leaveMessageEmailCheckCode.equals(systemLeaveMessageEmailCheckCode)) {
                    LeaveMessage leaveMessage = new LeaveMessage();
                    Timestamp time = new Timestamp(System.currentTimeMillis());
                    String timeString = time.toString();
                    String id = timeString.split(" ")[0].split("-")[0] + timeString.split(" ")[0].split("-")[1] + timeString.split(" ")[0].split("-")[2] + timeString.split(" ")[1].split(":")[0] + timeString.split(" ")[1].split(":")[1] + timeString.split(" ")[1].split(":")[2].split("\\.")[0] + timeString.split(" ")[1].split(":")[2].split("\\.")[1];//注意，split是按照正则表达式进行分割，.在正则表达式中为特殊字符，需要转义。
                    while (leaveMessageDao.getCountById(id) != 0) {
                        long idLong = Long.parseLong(id);
                        Random random = new Random();
                        idLong += random.nextInt(100);
                        id = idLong + "";
                        if (id.length() > 17) {
                            id = id.substring(0, 17);
                        }
                    }
                    leaveMessage.setId(id);
                    leaveMessage.setComment(leaveMessageComment);
                    leaveMessage.setName(leaveMessageName);
                    leaveMessage.setEmail(leaveMessageEmail);
                    leaveMessage.setCreateTime(time);
                    Random random = new Random();
                    int headImgIndex = random.nextInt(leaveMessageHeadImgList.length);
                    String headImg = leaveMessageHeadImgList[headImgIndex];
                    leaveMessage.setHeadImg(headImg);
                    try {
                        if (leaveMessageDao.createLeaveMessage(leaveMessage) == 0) {
                            System.out.println("留言添加数据库出错！");
                            content = "操作数据库出错！";
                        } else {
                            status = "true";
                            content = "发表成功！";
                        }
                    } catch (Exception e) {
                        System.out.println("留言添加数据库出错！错误原因：" + e);
                        content = "操作数据库出错！";
                    }
                } else {
                    content = "验证码不正确！";
                }
            }
        }
        result.accumulate("status", status);
        result.accumulate("content", content);
        return result.toString();
    }
}
