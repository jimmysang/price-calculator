package com.web.controller;

import com.model.User;
import com.service.UserService;
import com.util.ResponseUtil;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Sang on 15/10/17.
 */
@Controller
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;
    @RequestMapping("/login")
    private void login(HttpServletRequest request,HttpServletResponse response){
        String userName=request.getParameter("userName");
        String password=request.getParameter("password");
        User user=new User();
        user.setName(userName);
        user.setPassword(password);
        JSONObject result=new JSONObject();
        try {
            if(userService.login(user)){
                result.put("result","true");
                request.getSession().setAttribute("user", user);
                System.out.println("创建Session"+request.getSession());
            }else {
                result.put("result","false");
            }
        }catch (Exception e){
            result.put("result","false");
        }
        ResponseUtil.write(response,result,null);
    }
}
