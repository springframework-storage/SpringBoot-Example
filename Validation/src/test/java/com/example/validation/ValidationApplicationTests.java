package com.example.validation;

import com.example.validation.controller.MemberController;
import com.example.validation.domain.ErrorMessage;
import com.example.validation.domain.Member;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SuppressWarnings("Duplicates")
@RunWith(SpringRunner.class)
@WebMvcTest(MemberController.class)
public class ValidationApplicationTests {

  private static final Logger logger = LoggerFactory.getLogger(ValidationApplicationTests.class);

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  private static final String TEST_END_POINT = "/member";

  private static final String TEST_NAME = "gitflow";
  private static final int TEST_AGE = 27;
  private static final String TEST_PHONE = "01012345678";

  private static final String TEST_INVALID_NAME = null;
  private static final int TEST_INVALID_AGE = 13;
  private static final String TEST_INVALID_PHONE = "010xxxxxxxx";

  private static final String INVALID_NAME_MSG = "name null";
  private static final String INVALID_AGE_MSG = "min 14";
  private static final String INVALID_PHONE_MSG = "Invalid phone number";

  private void mockRequest(String memberToJson, ResultMatcher matcher, String result) throws Exception {
    mockMvc.perform(post(TEST_END_POINT)
            .content(memberToJson)
            .contentType(MediaType.APPLICATION_JSON_UTF8)
            .accept(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(matcher)
            .andExpect(content().json(result));
  }

  @Test
  public void test_success() throws Exception {
    Member member = new Member(TEST_NAME, TEST_AGE, TEST_PHONE);
    String memberToJson = objectMapper.writeValueAsString(member);

    mockRequest(memberToJson, status().isOk(), memberToJson);
    logger.info("### {} / {} / {}", memberToJson, status().isOk(), memberToJson);
  }

  @Test
  public void test_InvalidByName() throws Exception {
    Member member = new Member(TEST_INVALID_NAME, TEST_AGE, TEST_PHONE);
    String memberToJson = objectMapper.writeValueAsString(member);

    String errorMessageToJson = objectMapper.writeValueAsString(new ErrorMessage(HttpStatus.BAD_REQUEST.value(), INVALID_NAME_MSG));

    mockRequest(memberToJson, status().isBadRequest(), errorMessageToJson);
    logger.info("### {} / {} / {}", memberToJson, status().isBadRequest(), errorMessageToJson);
  }

  @Test
  public void test_InvalidByAge() throws Exception {
    Member member = new Member(TEST_NAME, TEST_INVALID_AGE, TEST_PHONE);
    String memberToJson = objectMapper.writeValueAsString(member);

    String errorMessageToJson = objectMapper.writeValueAsString(new ErrorMessage(HttpStatus.BAD_REQUEST.value(), INVALID_AGE_MSG));

    mockRequest(memberToJson, status().isBadRequest(), errorMessageToJson);
    logger.info("### {} / {} / {}", memberToJson, status().isBadRequest(), errorMessageToJson);
  }

  @Test
  public void test_InvalidByPhone() throws Exception {
    Member member = new Member(TEST_NAME, TEST_AGE, TEST_INVALID_PHONE);
    String memberToJson = objectMapper.writeValueAsString(member);

    String errorMessageToJson = objectMapper.writeValueAsString(new ErrorMessage(HttpStatus.BAD_REQUEST.value(), INVALID_PHONE_MSG));

    mockRequest(memberToJson, status().isBadRequest(), errorMessageToJson);
    logger.info("### {} / {} / {}", memberToJson, status().isBadRequest(), errorMessageToJson);
  }

}
