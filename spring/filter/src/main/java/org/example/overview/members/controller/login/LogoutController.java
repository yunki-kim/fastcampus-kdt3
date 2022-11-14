package org.example.overview.members.controller.login;

import org.example.overview.cookies.CookieMgr;
import org.example.overview.sessions.SessionMgr;
import org.example.overview.utils.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/members")
public class LogoutController { // 로그아웃 컨트롤러

    private SessionMgr sessionMgr; // = SessionMgr.getInstance();
    private CookieMgr cookieMgr; // = CookieMgr.getInstance();


    @Autowired
    public LogoutController(SessionMgr sessionMgr, CookieMgr cookieMgr) {
        this.sessionMgr = sessionMgr;
        this.cookieMgr = cookieMgr;
    }

    @GetMapping("/logout")
    public String doLogout(Model model,
                           HttpServletRequest request,
                           HttpSession session, HttpServletResponse response) {
        String view = "redirect:/"; // redirect:/ => index.jsp


//        if (session.getAttribute("SESSION_ID") == null) {
//            session.setAttribute("logout", Status.FAIL);
//            return view; // login 되어있는 상태로 redirect
//        }

        cookieMgr.delete(request, response);
        sessionMgr.delete(session);

        session = request.getSession(); // 새로운 세션 생성 (새로운 세션 만들어 redirect 하기 위함)
        session.setAttribute("logout", Status.SUCCESS);

        return view; // login 끊긴 상태로 redirect
    }
}
