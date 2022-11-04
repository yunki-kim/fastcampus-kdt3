package org.example.overview.members.restcontroller.login;

import org.example.overview.members.dto.MemberDTO;
import org.example.overview.members.service.MemberService;
import org.example.overview.members.vo.MemberVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/members")
public class SearchRestController { // 유저 검색 페이지 컨트롤러
    private MemberService memberService; // = MemberService.getInstance();

    @Autowired
    public SearchRestController(MemberService memberService) {
        this.memberService = memberService;
    }


    @PostMapping(value = "/search",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<MemberVO>> findByUserIdOrEmail(@RequestParam(required = false) String q) {
        if (q == null || q.equals("")) {
            List<MemberDTO> memberDTOList = memberService.getAllUsers();
            List<MemberVO> memberVOList = memberDTOList.stream().map(m -> m.toVO()).collect(Collectors.toList());
            return new ResponseEntity<>(memberVOList, HttpStatus.OK);
        }

        List<MemberDTO> memberDTOList = memberService.findByUserIdOrEmail(q);
        List<MemberVO> memberVOList = memberDTOList.stream().map(m -> m.toVO()).collect(Collectors.toList());
        return new ResponseEntity<>(memberVOList, HttpStatus.OK);
    }
}
