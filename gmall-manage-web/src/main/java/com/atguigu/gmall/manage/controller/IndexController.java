package com.atguigu.gmall.manage.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by Administrator on 2018/9/4/004.
 */
@Controller
public class IndexController {

    @RequestMapping("/index")
    public String index() {
        System.out.println("jj");
        return "index";
    }
    @RequestMapping("/attrListPage")
    public String attrListPage() {
        System.out.println("attrlist界面");
        return "attrListPage";
    }
    @RequestMapping("/spuListPage")
    public String spuManage() {
        System.out.println("spuListPage界面");
        return "spuListPage";
    }

    @RequestMapping("/skuListPage")
    public String skuListPage() {
        System.out.println("skuListPage界面");
        return "skuListPage";
    }


}

