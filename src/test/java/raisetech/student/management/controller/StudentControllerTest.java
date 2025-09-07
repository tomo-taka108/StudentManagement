package raisetech.student.management.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.extension.MediaType.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import raisetech.student.management.data.Student;
import raisetech.student.management.data.StudentCourse;
import raisetech.student.management.domain.StudentDetail;
import raisetech.student.management.exception.StudentNotFoundException;
import raisetech.student.management.service.StudentService;

@WebMvcTest(StudentController.class)
class StudentControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private StudentService service;

  private Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

  @Test
  public void 受講生詳細の一覧検索が実行てきて空のリストが返ってくること() throws Exception {
    mockMvc.perform(get("/studentList"))
        .andExpect(status().isOk());

    verify(service, times(1)).searchStudentList();
  }

  @Test
  public void 受講生詳細の検索が実行できて空のリストが返ってくること() throws Exception {
    String id = "123";
    mockMvc.perform(get("/student/{id}", id))
        .andExpect(status().isOk());

    verify(service, times(1)).searchStudent(id);
  }

  @Test
  void 受講生詳細の受講生で適切な値を入力した時に入力チェックに異常が発生しないこと() {
    Student student = new Student();
    student.setId("1");
    student.setName("佐藤太郎");
    student.setKanaName("サトウタロウ");
    student.setNickname("タロちゃん");
    student.setEmail("taro.sato@exapmle.com");
    student.setArea("東京");
    student.setAge(18);
    student.setSex("男性");

    Set<ConstraintViolation<Student>> violations = validator.validate(student);

    assertThat(violations.size()).isEqualTo(0);
  }

  @Test
  void 受講生詳細の受講生でIDに数字以外を用いた時に入力チェックに掛かること() {
    Student student = new Student();
    student.setId("テストです。");
    student.setName("佐藤太郎");
    student.setKanaName("サトウタロウ");
    student.setNickname("タロちゃん");
    student.setEmail("taro.sato@exapmle.com");
    student.setArea("東京");
    student.setAge(18);
    student.setSex("男性");

    Set<ConstraintViolation<Student>> violations = validator.validate(student);

    assertThat(violations.size()).isEqualTo(1);
    assertThat(violations).extracting("message")
        .containsOnly("数字のみ入力するようにしてください。");
  }

  @Test
  void 受講生詳細のコース情報で適切な値を入力した時に入力チェックに異常が発生しないこと() {
    StudentCourse studentCourse = new StudentCourse();
    studentCourse.setCourseName("Java入門");

    Set<ConstraintViolation<StudentCourse>> violations = validator.validate(studentCourse);

    assertThat(violations.size()).isEqualTo(0);
  }

  @Test
  void 受講生詳細の検索で存在しないIDを指定した時に404エラーが返ってくること() throws Exception {
    // 存在しないIDを準備
    String id = "9999";

    // Serviceが例外を投げるように設定
    when(service.searchStudent(id)).thenThrow(
        new StudentNotFoundException("ID" + id + "の受講生が見つかりません。"));

    // GETリクエスト実行
    mockMvc.perform(get("/student/{id}", id)
            .contentType(String.valueOf(APPLICATION_JSON)))
        .andExpect(status().isNotFound()); // 404になることを確認

    verify(service, times(1)).searchStudent(id);
  }

  @Test
  void 受講生詳細の登録が実行できてサービスが呼ばれること() throws Exception {
    // ダミーのリクエストJSON
    String requestJson = """
        {
             "student":{
                 "name":"山田花子",
                 "kanaName":"ヤマダハナコ",
                 "nickname":"ハナちゃん",
                 "email":"hanako@example.com",
                 "area":"大阪",
                 "age":"30",
                 "sex":"女性",
                 "remark":""
             },
             "studentCourseList":[
                 {
                     "courseName":"Java応用"
                 }
             ]
         }
        """;

    // Serviceの戻り値をモック化
    StudentDetail studentDetail = new StudentDetail();
    when(service.registerStudent(any(StudentDetail.class))).thenReturn(studentDetail);

    // POSTリクエストを実行
    mockMvc.perform(post("/registerStudent")
            .contentType(String.valueOf(APPLICATION_JSON)) // リクエストがJSON形式であることを指定
            .content(requestJson))                                   // 実際に送るJSONデータ
        .andExpect(status().isOk());

    verify(service, times(1)).registerStudent(any(StudentDetail.class));
  }

  @Test
  void 受講生詳細の更新が実行できてサービスが呼ばれること() throws Exception {
    // ダミーのリクエストJSON
    String requestJson = """
        {
             "student":{
                 "id":"123",
                 "name":"山田花子",
                 "kanaName":"ヤマダハナコ",
                 "nickname":"ハナちゃん",
                 "email":"hanako@example.com",
                 "area":"大阪",
                 "age":"30",
                 "sex":"女性",
                 "remark":"",
                 "isDeleted":true
             },
             "studentCourseList":[
                 {
                     "courseName":"Java応用"
                 }
             ]
         }
        """;

    // PUTリクエストを実行
    mockMvc.perform(put("/updateStudent")
            .contentType(String.valueOf(APPLICATION_JSON)) // リクエストがJSON形式であることを指定
            .content(requestJson))                                   // 実際に送るJSONデータ
        .andExpect(status().isOk());

    verify(service, times(1)).updateStudent(any(StudentDetail.class));
  }

}