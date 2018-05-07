package top.yjzloveyzh.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import top.yjzloveyzh.common.Constants;
import top.yjzloveyzh.common.exception.UserException;
import top.yjzloveyzh.common.pojo.User;
import top.yjzloveyzh.services.UserService;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    @Qualifier(value = "userServiceImpl")
    UserService userService;

    @RequestMapping("/toLogin")
    public String toLogin(RedirectAttributes redirectAttributes, HttpSession session) {

        if (session.getAttribute(Constants.User.SESSION_USER_KEY) != null) {

            return "redirect:/lab/toContent";
        }

        return Constants.UserControllerUrl.USER_CONTROLLER_LOGIN_PAGE_URL;
    }

    @RequestMapping("/register")
    public String register(User user, HttpServletResponse response, HttpSession session) {
        PrintWriter out = null;

        try {
            out = response.getWriter();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (out == null) {

            return null;
        }

        if (user == null) {
            out.println("<script>alert(\"" + "注册失败,请重新注册\"  );history.back();</script>");
        } else {

            try {
                userService.register(user);
                session.setAttribute(Constants.User.SESSION_USER_KEY, user);

                return "redirect:toContent";
            } catch (UserException e) {
                out.println("<script>alert(\"" + e.getMessage() + "注册失败,请重新注册\");history.back();</script>");
            }
        }

        return null;
    }

    @RequestMapping("/login")
    public String login(User labUser, HttpSession session, HttpServletResponse response, RedirectAttributes redirectAttributes) {
        String errorMessage = "";

        try {
            User user = userService.login(labUser);
            session.setAttribute(Constants.User.SESSION_USER_KEY, user);

            return "redirect:/lab/toContent";
        } catch (UserException userException) {
            errorMessage = userException.getMessage();
            redirectAttributes.addFlashAttribute("error", errorMessage);

            return "redirect:toLogin";
        }
    }

    @RequestMapping("/toUserInfo")
    public String toUserInfo(HttpSession session, HttpServletRequest request) {

        User user = (User) session.getAttribute(Constants.User.SESSION_USER_KEY);

        if (user != null) {
            User userInfo = userService.getUserById(user.getId());
            userInfo.setPassword("");
            request.setAttribute("USER_INFO", userInfo);
            System.out.println(userInfo);
        }

        return "user/userInfo";
    }

    @RequestMapping(value="/logout")
    public String logout(HttpSession session, RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute(Constants.User.SESSION_USER_KEY);

        if (user != null) {
            session.setAttribute(Constants.User.SESSION_USER_KEY, null);
            redirectAttributes.addFlashAttribute("error", "登出成功。");

            return "redirect:/user/toLogin";
        } else {
            redirectAttributes.addFlashAttribute("error", "您还未登陆，请登录。");

            return "redirect:/user/toLogin";
        }
    }
}