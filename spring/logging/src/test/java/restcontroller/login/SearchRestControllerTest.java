package restcontroller.login;

import org.example.overview.config.DispatcherServletConfig;
import org.example.overview.config.WebAppConfig;
import org.example.overview.members.dao.MemberDAO;
import org.example.overview.members.dto.Password;
import org.example.overview.members.entity.Member;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class) // SpringJunit4ClassRunner.class는 spring-test에서 제공하는 단위테스트를 위한 클래스 러너
@ContextConfiguration(classes = {WebAppConfig.class, DispatcherServletConfig.class})
@WebAppConfiguration // WebApplicationContext 생성할 수 있도록 하는 어노테이션
public class SearchRestControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private MemberDAO memberDAO;


    private MockMvc mockMvc; // request 수행해주는 mock 객체


    @Before
    public void before() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        // test 하기 위한 MockMvc 객체 생성. 스프링이 로드한 WebApplicationContext 인스턴스로 작동.
    }

    @Before
    public void 테스트_위한_객체_생성() {
        Member member = Member.builder()
                .uId("test")
                .uPw(Password.of("test1234").getuPw())
                .uEmail("test@gmail.com")
                .build();
        memberDAO.insert(member);
    }

    @After
    public void 테스트_위한_객체_소멸() {
        memberDAO.delete("test");
    }

    @DisplayName("사용자 한명 검색 테스트")
    @Test
    public void 사용자_한명_검색_테스트() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/members/test"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("uId").value("test"))
                .andExpect(jsonPath("uPw").value(Password.of("test1234").getuPw()))
                .andExpect(jsonPath("uEmail").value("test@gmail.com"))
                .andDo(print());
    }


    @DisplayName("사용자 키워드 검색 테스트")
    @Test
    public void 사용자_키워드_검색_테스트() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/members")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("q", "com"))
                .andExpect(status().isOk())
                .andDo(print());
    }


}