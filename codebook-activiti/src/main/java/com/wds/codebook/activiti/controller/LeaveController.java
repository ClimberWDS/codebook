package com.wds.codebook.activiti.controller;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Deployment;
import org.junit.Test;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("/leave")
public class LeaveController {
    @GetMapping("/send")
    public void send(){
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        RepositoryService repositoryService = processEngine.getRepositoryService();
        Deployment deploy = repositoryService.createDeployment().addClasspathResource("bpmn/demo.bpmn20.xml").deploy();
        System.out.println(deploy.getId());
    }

    @Test
    public void test02() {
        //使用classpath下的activiti.cfg.xml中的配置创建processEngine
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        RepositoryService repositoryService = processEngine.getRepositoryService();
        Deployment deploy = repositoryService.createDeployment().addClasspathResource("bpmn/demo.bpmn20.xml").deploy();
        System.out.println(deploy.getId());
    }

    @Test
    public void test03() {
        //使用classpath下的activiti.cfg.xml中的配置创建processEngine
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        TaskService taskService = processEngine.getTaskService();
        Deployment deploy = taskService.complete(processEngine.getName().replace());
        System.out.println(deploy.getId());
    }

}
