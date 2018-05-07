package com.nchu.easyword.controller;

import com.nchu.easyword.dao.model.DailyTask;
import com.nchu.easyword.dao.model.MemoryRecord;
import com.nchu.easyword.dao.model.User;
import com.nchu.easyword.dto.ResponseDTO;
import com.nchu.easyword.exception.ServiceException;
import com.nchu.easyword.exception.StatusCode;
import com.nchu.easyword.service.impl.NotificationServiceImpl;
import com.nchu.easyword.service.inface.*;
import com.nchu.easyword.utils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 2018-4-7 16:08:54
 *
 * @author xujw
 * 单词背诵任务相关控制器
 */
@RestController
@CrossOrigin(allowCredentials = "true")
@RequestMapping("/task")
public class TaskController {
    @Autowired
    WordsService wordsService;
    @Autowired
    UserSessionService sessionService;
    @Autowired
    TaskService taskService;
    @Autowired
    UserService userService;
    @Autowired
    NotificationService notificationService;

    /**
     * 获取今日任务单词
     *
     * @return
     */
    @RequestMapping(value = "/getDailyTask", method = RequestMethod.GET)
    public ResponseDTO<DailyTask> getDailyTask(@RequestParam("refresh") boolean refresh, HttpServletRequest request,
                                               @RequestAttribute("responseDTO") ResponseDTO responseDTO) {
        User user = sessionService.getUser(request);
        responseDTO.setMessage("今日任务");
        /*先读取用户今日任务记录,如果没有则新建当日任务*/
        DailyTask dailyTask = taskService.getUserTask(user.getId(), DateUtil.getCurrentTimestamp());
        if (dailyTask != null && !refresh) {
            responseDTO.setData(dailyTask);
            return responseDTO;
        } else if (dailyTask != null && refresh) {
            responseDTO.setData(taskService.refreshDailyTask(dailyTask));
            return responseDTO;
        } else {
            /*dailyTask为null则创建新的今日任务*/
            responseDTO.setData(taskService.createDailyTask(user.getId()));
            return responseDTO;
        }
    }

    /**
     * 更新任务进度信息
     *
     * @param dailyTask   新任务信息
     * @param responseDTO 响应结果
     * @param request     Http请求
     * @return
     */
    @RequestMapping(value = "/updateTaskProgress", method = RequestMethod.POST)
    public ResponseDTO updateTaskProgress(@RequestBody DailyTask dailyTask, @RequestParam("wordId") long wordId, @RequestParam("word") String word,
                                          @RequestAttribute("responseDTO") ResponseDTO responseDTO, HttpServletRequest request) throws ServiceException {
        User user = sessionService.getUser(request);
        if (user != null && user.getId() != dailyTask.getUserId()) {
            throw new ServiceException(StatusCode.REQUEST_FAILED, "非法操作!");
        }
        /*新增单词背诵记录*/
        wordsService.addMemoryRecord(new MemoryRecord(user.getId(), wordId, word));
        responseDTO.setData(taskService.updateTaskProgress(dailyTask)).setMessage("更新任务进度");
        /*如果今日任务已经完成则给用户增加200积分*/
        if (dailyTask.getTodayProgress() == dailyTask.getWordNum()) {
            user.setPoints(user.getPoints() + 200);
            userService.updateUserInfo(user);
            responseDTO.setUserUpdate(true);
            notificationService.newNotification(NotificationServiceImpl.genSimpleNotification(user.getId(), "完成今日任务获得积分 200 点,请继续坚持哦!"));
        }
        return responseDTO;
    }

    /**
     * 更新复习任务进度信息
     *
     * @param dailyTask   新任务信息
     * @param responseDTO 响应结果
     * @param request     Http请求
     * @return
     */
    @RequestMapping(value = "/updateReviewTask", method = RequestMethod.POST)
    public ResponseDTO updateReviewTask(@RequestBody DailyTask dailyTask, @RequestAttribute("responseDTO") ResponseDTO responseDTO, HttpServletRequest request) throws ServiceException {
        User user = sessionService.getUser(request);
        responseDTO.setData(taskService.updateTaskProgress(dailyTask)).setMessage("更新复习任务进度");
        /*如果今日任务已经完成则给用户增加100积分*/
        if (dailyTask.getReviewProgress() == dailyTask.getWordNum()) {
            user.setPoints(user.getPoints() + 100);
            userService.updateUserInfo(user);
            responseDTO.setUserUpdate(true);
            notificationService.newNotification(NotificationServiceImpl.genSimpleNotification(user.getId(), "完成复习任务获得积分 100 点,请继续坚持哦!"));
        }
        return responseDTO;
    }

    /**
     * 获取复习任务,即昨天的任务单词列表
     *
     * @param responseDTO
     * @param request
     * @return
     */
    @RequestMapping(value = "/getReviewTask", method = RequestMethod.GET)
    public ResponseDTO getReviewTask(@RequestAttribute("responseDTO") ResponseDTO responseDTO, HttpServletRequest request) {
        return responseDTO.setData(
                taskService.getReviewTask(sessionService.getUser(request).getId())
        ).setMessage("复习任务");
    }
}
