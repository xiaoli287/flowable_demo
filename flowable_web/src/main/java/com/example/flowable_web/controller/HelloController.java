package com.example.flowable_web.controller;


import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONUtil;
import com.example.flowable_web.vo.FinishedTaskVO;
import com.example.flowable_web.vo.ForLeaveVO;
import com.example.flowable_web.vo.TaskVO;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.engine.*;
import org.flowable.engine.runtime.Execution;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.image.ProcessDiagramGenerator;
import org.flowable.task.api.Task;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class HelloController {
    @Autowired
    RuntimeService runtimeService;

    @Autowired
    TaskService taskService;

    @Autowired
    RepositoryService repositoryService;

    @Autowired
    ProcessEngine processEngine;

    @Autowired
    private HistoryService historyService;


    /**
     * 查询流程当前情况
     * @param resp
     * @param staffId
     * @throws Exception
     */
    @GetMapping("/pic")
    public void showPic(HttpServletResponse resp, @RequestParam(value = "staffId")String staffId) throws Exception {
        ProcessInstance pi = runtimeService.createProcessInstanceQuery().processInstanceId(staffId).singleResult();
        if (pi == null) {
            return;
        }
        List<Execution> executions = runtimeService
                .createExecutionQuery()
                .processInstanceId(staffId)
                .list();

        List<String> activityIds = new ArrayList<>();
        List<String> flows = new ArrayList<>();
        for (Execution exe : executions) {
            List<String> ids = runtimeService.getActiveActivityIds(exe.getId());
            activityIds.addAll(ids);
        }

        /**
         * 生成流程图
         */
        BpmnModel bpmnModel = repositoryService.getBpmnModel(pi.getProcessDefinitionId());
        ProcessEngineConfiguration engconf = processEngine.getProcessEngineConfiguration();
        ProcessDiagramGenerator diagramGenerator = engconf.getProcessDiagramGenerator();
        InputStream in = diagramGenerator.generateDiagram(bpmnModel, "png", activityIds, flows, engconf.getActivityFontName(), engconf.getLabelFontName(), engconf.getAnnotationFontName(), engconf.getClassLoader(), 1.0, false);
        OutputStream out = null;
        byte[] buf = new byte[1024];
        int legth = 0;
        try {
            out = resp.getOutputStream();
            while ((legth = in.read(buf)) != -1) {
                out.write(buf, 0, legth);
            }
        } finally {
            if (in != null) {
                in.close();
            }
            if (out != null) {
                out.close();
            }
        }
    }


    private static final Logger log = LoggerFactory.getLogger(HelloController.class);

//    String staffId = "1000";

    /**
     * 先提交一个请求流程
     * @return
     */
    @PostMapping("/askForLeave")
    public  String askForLeave(@RequestBody ForLeaveVO forLeaveVO) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("leave", forLeaveVO.getStaffId());
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("ask_for_leave.bpmn20", map);
        runtimeService.setVariable(processInstance.getId(), "name", forLeaveVO.getName());
        runtimeService.setVariable(processInstance.getId(), "reason", forLeaveVO.getReason());
        runtimeService.setVariable(processInstance.getId(), "days", forLeaveVO.getDays());
        log.info("创建请假流程 processId：{}", processInstance.getId());
        return processInstance.getId();
    }

    /**
     * 查询我的待办列表
     * @param staffId 当前userid
     * @return
     */
    @GetMapping("/getAskForLeave")
    public  String getAskForLeave(@RequestParam(value = "staffId") String staffId) {
        List<Task> list = taskService.createTaskQuery().taskAssignee(staffId).orderByTaskId().desc().list();
        List<TaskVO> taskVOS = new ArrayList<>();
        for(Task task : list){
            //流程的变量
            Map<String,Object> variables = runtimeService.getVariables(task.getExecutionId());
            TaskVO vo = new TaskVO();
            vo.setId(task.getId());
            vo.setName(task.getName());
            vo.setAssignee(task.getAssignee());
            vo.setCreateTime(DateUtil.format(task.getCreateTime(),"yyyy-MM-dd hh:mm:ss"));
            vo.setExecutionId(task.getExecutionId());
            vo.setProcessInstanceId(task.getProcessInstanceId());
            vo.setVariables(JSONUtil.toJsonStr(variables));
            taskVOS.add(vo);
        }
        return JSONUtil.toJsonStr(taskVOS);
    }

    /**
     * 查询我的已办列表
     * @return
     */
    @GetMapping("/queryHistoryProcess")
    public  String queryHistoryProcess() {
        List<HistoricTaskInstance> list = historyService.createHistoricTaskInstanceQuery()
                .finished()
                .orderByHistoricTaskInstanceDuration().desc()
                .list();
        List<FinishedTaskVO> taskVOS = new ArrayList<>();
        for(HistoricTaskInstance task : list){
            //流程的变量
            Map<String,Object> variables = runtimeService.getVariables(task.getProcessInstanceId());
            FinishedTaskVO vo = new FinishedTaskVO();
            vo.setId(task.getId());
            vo.setName(task.getName());
            vo.setProcessInstanceId(task.getProcessDefinitionId());
            vo.setStartTime(DateUtil.format(task.getCreateTime(),"yyyy-MM-dd hh:mm:ss"));
            vo.setEndTime(DateUtil.format(task.getEndTime(),"yyyy-MM-dd hh:mm:ss"));
            vo.setVariables(JSONUtil.toJsonStr(variables));
            taskVOS.add(vo);
        }
        return JSONUtil.toJsonStr(taskVOS);
    }


    /**
     * 提交给组长审批我的请假流程
     * @return
     */
    @GetMapping("/submitToZuzhang")
    public  String submitToZuzhang(@RequestParam(value = "processInstanceId") String processInstanceId,
                                   @RequestParam(value = "zuzhangId") String zuzhangId) {
        //员工查找到自己的任务，然后提交给组长审批
        Task task = taskService.createTaskQuery().processInstanceId(processInstanceId).singleResult();
        log.info("任务 ID：{}；任务处理人：{}；任务是否挂起：{}", task.getId(), task.getAssignee(), task.isSuspended());
        Map<String, Object> map = new HashMap<>();
        //提交给组长的时候，需要指定组长的 id
        map.put("zuzhang_aduit", zuzhangId);
        map.put("checkResult", "通过");
        taskService.complete(task.getId(), map);
        return "审批成功";
    }

    /**
     * 提交给组长审批我的请假流程
     * @return
     */
    @GetMapping("/hr_audit")
    public  String hr_audit(@RequestParam(value = "processInstanceId") String processInstanceId,
                                   @RequestParam(value = "hrId") String hrId,@RequestParam("checkResult") String checkResult) {
        //员工查找到自己的任务，然后提交给组长审批
        Task task = taskService.createTaskQuery().processInstanceId(processInstanceId).singleResult();
        log.info("任务 ID：{}；任务处理人：{}；任务是否挂起：{}", task.getId(), task.getAssignee(), task.isSuspended());
        Map<String, Object> map = new HashMap<>();
        //提交给组长的时候，需要指定组长的 id
        map.put("hr_audit", hrId);
        map.put("checkResult",checkResult);
        taskService.complete(task.getId(), map);
        return "审批成功";
    }

}
