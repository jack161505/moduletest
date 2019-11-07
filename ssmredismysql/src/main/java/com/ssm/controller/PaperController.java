package com.ssm.controller;


import java.util.List;

import com.ssm.pojo.Paper;
import com.ssm.redis.CacheUtil;
import com.ssm.service.PaperService;
import com.ssm.service.impl.RedisTemplateUtil;
import org.apache.ibatis.javassist.bytecode.stackmap.BasicBlock;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @Author: 李善玺
 * @Date: 2019/11/6 21:10
 * @Version 1.0
 */
@Controller
@RequestMapping("/paper")
public class PaperController {
    @Autowired
    private PaperService paperService;
//    @Resource
//    private RedisTemplate<String,Object> redisTemplate;
    @Resource
    private RedisTemplateUtil redisTemplateUtil;

    @RequestMapping("/allPaper")
    public String list(Model model, HttpServletRequest req) {

        List<Paper> paperList=CacheUtil.getList("paperList");
        if(paperList==null||paperList.size()<=0)
        {
            System.out.println("还没有缓存，将从数据库中查询。。");
            paperList= paperService.queryAllPaper();
            CacheUtil.setList("paperList",paperList);
        }else{
            System.out.println("已经有缓存了。。。");
        }
        req.getSession().setAttribute("paperList",paperList);
        model.addAttribute("list",paperList);
        return "allPaper";
    }

    @RequestMapping("/toAddPaper")
    public String toAddPaper() {
        return "addPaper";
    }

    @RequestMapping("/addPaper")
    public String addPaper(Paper paper) {
        paperService.addPaper(paper);
        CacheUtil.addList("paperList",paper);
        return "redirect:/paper/allPaper";
    }

    @RequestMapping("/del/{paperId}")
    public String deletePaper(@PathVariable("paperId") Long id) {
        paperService.deletePaperById(id);
//        CacheUtil.del
        return "redirect:/paper/allPaper";
    }

    @RequestMapping("/toUpdatePaper")
    public String toUpdatePaper(Model model, Long id) {
        model.addAttribute("paper", paperService.queryById(id));
        return "updatePaper";
    }

    @RequestMapping("/updatePaper")
    public String updatePaper(Model model, Paper paper) {
        paperService.updatePaper(paper);
        paper = paperService.queryById(paper.getPaperId());
        model.addAttribute("paper", paper);
        return "redirect:/paper/allPaper";
    }

    @RequestMapping("/redistest")
    public String redisTest(){
        System.out.println("测试开始！");
        try{
            boolean b= CacheUtil.setString("1234","redis");
            System.out.println(b);
            System.out.println(CacheUtil.getString("1234"));

        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
        System.out.println("测试结束！");
        return "redirect:/paper/allPaper";
    }
}
