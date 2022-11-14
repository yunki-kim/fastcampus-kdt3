package org.example.overview.members.restcontroller.login;

import exception.InputEmptyException;
import org.example.overview.cookies.CookieMgr;
import org.example.overview.members.dto.Password;
import org.example.overview.members.service.MemberService;
import org.example.overview.sessions.SessionMgr;
import org.example.overview.utils.Status;
import org.example.overview.utils.UtilsMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;

@RestController
@RequestMapping("/members")
public class PrivateRestController { // 개인 설정 페이지 컨트롤러
    private MemberService memberService;
    private CookieMgr cookieMgr;
    private SessionMgr sessionMgr;

    @Autowired
    public PrivateRestController(MemberService memberService, CookieMgr cookieMgr, SessionMgr sessionMgr) {
        this.memberService = memberService;
        this.cookieMgr = cookieMgr;
        this.sessionMgr = sessionMgr;
    }

    /* PK를 제외한 모든 개인정보를 수정하는 함수 추가 (22.11.04) */
    @PutMapping("/private/{uId}")
    public ResponseEntity<Status> updateUserEmail(@PathVariable(value = "uId") String uId,
                                                        @RequestBody Map<String, String> map) throws InputEmptyException { // uEmail 말고 더 많은 사용자 정보 수정이 있을 수 있음
        if (UtilsMethod.isNullOrEmpty(map.get("uNewEmail"))) throw new InputEmptyException();


        if (memberService.updateUserEmail(uId, map.get("uNewEmail"))) {
            return new ResponseEntity<>(Status.SUCCESS, HttpStatus.OK);
        }
        return new ResponseEntity<>(Status.FAIL, HttpStatus.BAD_REQUEST);
    }


    /* uPw와 uNewPw가 같으면 패스워드 업데이트 불가능 기능 추가 (22.11.03) */
    @PatchMapping("/private/{uId}")
    public ResponseEntity<Status> updateUserPassword(@PathVariable("uId") String uId,
                                                       @RequestParam String uPw, // required
                                                       @RequestParam String uNewPw) { // required
        if (memberService.updateUserPassword(uId, Password.of(uPw), Password.of(uNewPw))) {
            return new ResponseEntity<>(Status.SUCCESS, HttpStatus.OK);
        }
        return new ResponseEntity<>(Status.FAIL, HttpStatus.BAD_REQUEST);
    }


    @PostMapping(value = "/private/checkPwd")
    public ResponseEntity<Status> checkPassword(@RequestBody Map<String, String> map) {
        if (UtilsMethod.isNullOrEmpty(map.get("uId"))) return new ResponseEntity<>(Status.NULL, HttpStatus.BAD_REQUEST);
        if (UtilsMethod.isNullOrEmpty(map.get("uPw"))) return new ResponseEntity<>(Status.NULL, HttpStatus.BAD_REQUEST);


        if (memberService.checkPassword(map.get("uId"), Password.of(map.get("uPw")))) {
            return new ResponseEntity<>(Status.SUCCESS, HttpStatus.OK);
        }
        return new ResponseEntity<>(Status.FAIL, HttpStatus.BAD_REQUEST);
    }

    @PostMapping(value = "/private/checkNewPwd")
    public ResponseEntity<Status> checkNewPassword(@RequestBody Map<String, String> map) { // uId, uNewPw
        if (UtilsMethod.isNullOrEmpty(map.get("uId"))) throw new InputEmptyException();
        if (UtilsMethod.isNullOrEmpty(map.get("uNewPw"))) throw new InputEmptyException();

        if (memberService.checkNewPassword(map.get("uId"), Password.of(map.get("uNewPw")))) {
            return new ResponseEntity<>(Status.SUCCESS, HttpStatus.OK);
        }
        return new ResponseEntity<>(Status.FAIL, HttpStatus.BAD_REQUEST);
    }


    @DeleteMapping("/private/{uId}")
    public ResponseEntity<Status> removeByUserId(@PathVariable String uId,
                                                 @RequestParam String uPw,
                                                 @RequestParam(required = false) String agree,
                                                 HttpServletRequest request, HttpSession session, HttpServletResponse response) {
        if (agree == null || !agree.equals("yes")) throw new InputEmptyException();

        if (memberService.removeByUserId(uId, Password.of(uPw))) {
            cookieMgr.delete(request, response);
            sessionMgr.delete(session);

            return new ResponseEntity<>(Status.SUCCESS, HttpStatus.OK);
        }
        return new ResponseEntity<>(Status.FAIL, HttpStatus.BAD_REQUEST);
    }
}
