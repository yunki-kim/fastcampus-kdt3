package org.example.overview.members.dto;

import org.example.overview.members.entity.Member;
import org.example.overview.members.vo.MemberVO;

public class MemberDTO {
    private String uId = "";

    private Password uPw = null;

    private Password uNewPw = null;

    private String uEmail = "";



    public MemberDTO() {
    }

    public MemberDTO(String uId) {
        this.uId = uId;
    }

    public MemberDTO(String uId, String uEmail) {
        this.uId = uId;
        this.uEmail = uEmail;
    }

    public MemberDTO(String uId, String uPw, boolean needEncode) {
        this.uId = uId;
        this.uPw = needEncode ? Password.of(uPw, true) : Password.of(uPw, false);
    }

    public MemberDTO(String uId, Password uPw) {
        this.uId = uId;
        this.uPw = uPw;
    }

    public MemberDTO(String uId, Password uPw, Password uNewPw) {
        this.uId = uId;
        this.uPw = uPw;
        this.uNewPw = uNewPw;
    }

    public MemberDTO(String uId, String uPw, String uEmail, boolean needEncode) {
        this.uId = uId;
        this.uPw = needEncode ? Password.of(uPw, true) : Password.of(uPw, false);
        this.uEmail = uEmail;
    }

    public MemberDTO(String uId, String uPw, String uNewPw, String uEmail, boolean needEncode) {
        this.uId = uId;
        this.uPw = needEncode ? Password.of(uPw, true) : Password.of(uPw, false);
        this.uNewPw = needEncode ? Password.of(uNewPw, true) : Password.of(uNewPw, false);
        this.uEmail = uEmail;
    }

    public MemberDTO(String uId, Password uPw, String uEmail) {
        this.uId = uId;
        this.uPw = uPw;
        this.uEmail = uEmail;
    }

    public MemberDTO(String uId, Password uPw, Password uNewPw, String uEmail) {
        this.uId = uId;
        this.uPw = uPw;
        this.uNewPw = uNewPw;
        this.uEmail = uEmail;
    }

    public Member toEntity() {
        return new Member(uId, getuPwStr(), uEmail);
    }

    public MemberVO toVO() {
        return new MemberVO(uId, uEmail);
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public Password getuPw() {
        return uPw;
    }

    public String getuPwStr() {
        return uPw.getuPw();
    }


    public void setuPw(Password uPw) {
        this.uPw = uPw;
    }

    public Password getuNewPw() {
        return uNewPw;
    }

    public String getuNewPwStr() {
        return uNewPw.getuPw();
    }



    public void setuNewPw(Password uNewPw) {
        this.uNewPw = uNewPw;
    }

    public String getuEmail() {
        return uEmail;
    }

    public void setuEmail(String uEmail) {
        this.uEmail = uEmail;
    }

    @Override
    public String toString() {
        return "MemberDTO{" +
                "uId='" + uId + '\'' +
                ", uPw=" + uPw +
                ", uNewPw=" + uNewPw +
                ", uEmail='" + uEmail + '\'' +
                '}';
    }
}
