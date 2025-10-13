package raisetech.student.management.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.http.MediaType;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import raisetech.student.management.data.CourseStatus;
import raisetech.student.management.data.Student;
import raisetech.student.management.data.StudentCourse;
import raisetech.student.management.data.StudentSearchCriteria;
import raisetech.student.management.domain.StudentDetail;
import raisetech.student.management.exception.StudentNotFoundException;
import raisetech.student.management.service.StudentService;

@WebMvcTest(StudentController.class)
class StudentControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private StudentService service;

  // 入力チェック（バリデーションテスト）に必要
  private Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

  /**
   * データモデルのバリデーションテスト
   */

  @Test
  void 受講生詳細の受講生で適切な値を入力した時に入力チェックに異常が発生しないこと() {
    Student student = new Student(
        "1",
        "佐藤太郎",
        "サトウタロウ",
        "タロちゃん",
        "taro.sato@exapmle.com",
        "東京",
        18,
        "男性",
        null,
        false);

    Set<ConstraintViolation<Student>> violations = validator.validate(student);

    assertThat(violations.size()).isEqualTo(0);
  }

  @Test
  void 受講生詳細の受講生でIDに数字以外を用いた時に入力チェックに掛かること() {
    Student student = new Student(
        "テストです",
        "佐藤太郎",
        "サトウタロウ",
        "タロちゃん",
        "taro.sato@exapmle.com",
        "東京",
        18,
        "男性",
        null,
        false);

    Set<ConstraintViolation<Student>> violations = validator.validate(student);

    assertThat(violations.size()).isEqualTo(1);
    assertThat(violations).extracting("message")
        .containsOnly("数字のみ入力してください。");
  }

  @Test
  void 受講生詳細のコース情報で適切な値を入力した時に入力チェックに異常が発生しないこと() {
    StudentCourse studentCourse = new StudentCourse();
    studentCourse.setCourseId("101");
    studentCourse.setCourseName("Java入門");

    Set<ConstraintViolation<StudentCourse>> violations = validator.validate(studentCourse);

    assertThat(violations.size()).isEqualTo(0);
  }

  @Test
  void 受講生詳細のコース申込状況で適切な値を入力した時に入力チェックに異常が発生しないこと() {
    CourseStatus courseStatus = new CourseStatus();
    courseStatus.setStatus("本申込");

    Set<ConstraintViolation<CourseStatus>> violations = validator.validate(courseStatus);

    assertThat(violations.size()).isEqualTo(0);
  }

  @Test
  void 受講生検索条件で適切な値を入力した時に入力チェックに異常が発生しないこと() {
    StudentSearchCriteria criteria = new StudentSearchCriteria();
    criteria.setId("1");
    criteria.setAgeMin(20);
    criteria.setAgeMax(50);
    criteria.setSex("男性");
    criteria.setStatus("仮申込");

    Set<ConstraintViolation<StudentSearchCriteria>> violations = validator.validate(criteria);

    assertThat(violations.size()).isEqualTo(0);
  }


  /**
   * Controllerのマッピングテスト
   */

  @Test
  public void 受講生詳細の一覧検索_条件指定なし_が実行できて空のリストが返ってくること()
      throws Exception {
    mockMvc.perform(get("/studentList"))
        .andExpect(status().isOk())
        .andExpect(content().json("[]"));

    verify(service, times(1)).searchStudentList();
  }

  @Test
  public void 受講生詳細の検索_受講生ID指定_が実行できること() throws Exception {
    String id = "123";
    mockMvc.perform(get("/student/{id}", id))
        .andExpect(status().isOk());

    verify(service, times(1)).searchStudent(id);
  }

  @Test
  void 受講生詳細の検索_受講生ID指定_で存在しないIDを指定した時に404エラーが返ってくること()
      throws Exception {
    // 存在しないIDを準備
    String id = "9999";

    // Serviceが例外を投げるように設定
    when(service.searchStudent(id)).thenThrow(
        new StudentNotFoundException("ID" + id + "の受講生が見つかりません。"));

    // GETリクエスト実行
    mockMvc.perform(get("/student/{id}", id))
        .andExpect(status().isNotFound()); // 404になることを確認

    verify(service, times(1)).searchStudent(id);
  }

  @Test
  void 受講生一覧の検索_検索条件を指定_が実行できること() throws Exception {
    // リクエストデータは適切に構築して入力チェックの検証も兼ねている。
    // 本来であれば返りは登録されたデータが入るが、モック化すると意味がないため、レスポンスは作らない。

    // ダミーのリクエストJSON
    String requestJson = """
        {
            "id": "1",
            "name": "佐藤",
            "kanaName": "サトウ",
            "nickname": "タロ",
            "area": "東京",
            "ageMin": 15,
            "ageMax": 50,
            "sex": "男性",
            "courseName": "Java",
            "status": "仮申込",
            "isDeleted": false
          }
        """;

    // POSTリクエストを実行
    mockMvc.perform(post("/studentList/criteria")
            .contentType(MediaType.APPLICATION_JSON) // リクエストがJSON形式であることを指定
            .content(requestJson))                                   // 実際に送るJSONデータ
        .andExpect(status().isOk());

    verify(service, times(1)).searchWithCriteria(any());
  }

  @Test
  void 受講生詳細の登録が実行できて空で返ってくること() throws Exception {
    // リクエストデータは適切に構築して入力チェックの検証も兼ねている。
    // 本来であれば返りは登録されたデータが入るが、モック化すると意味がないため、レスポンスは作らない。

    // ダミーのリクエストJSON
    String requestJson = """
        {
             "student":{
                 "name":"山田花子",
                 "kanaName":"ヤマダハナコ",
                 "nickname":"ハナちゃん",
                 "email":"hanako@example.com",
                 "area":"大阪",
                 "age":30,
                 "sex":"女性",
                 "remark":""
             },
             "studentCourseList":[
                 {
                     "courseId":"103",
                     "courseName":"Java応用"
                 }
             ],
             "courseStatusList":[
                  {
                      "courseId":"103",
                      "status":"受講中"
                  }
             ]
         }
        """;

    // POSTリクエストを実行
    mockMvc.perform(post("/registerStudent")
            .contentType(MediaType.APPLICATION_JSON) // リクエストがJSON形式であることを指定
            .content(requestJson))                                   // 実際に送るJSONデータ
        .andExpect(status().isOk());

    verify(service, times(1)).registerStudent(any());
  }

  @Test
  void 受講生詳細の更新が実行できて空で返ってくること() throws Exception {
    // リクエストデータは適切に構築して入力チェックの検証も兼ねている。

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
                 "age":30,
                 "sex":"女性",
                 "remark":"",
                 "isDeleted":true
             },
             "studentCourseList":[
                 {
                     "id":"11",
                     "courseId":"103",
                     "courseName":"Java応用"
                 }
             ],
             "courseStatusList":[
                 {
                     "id":"21",
                     "courseId":"103",
                     "status":"受講終了"
                 }
             ]
         }
        """;

    // PUTリクエストを実行
    mockMvc.perform(put("/updateStudent")
            .contentType(MediaType.APPLICATION_JSON) // リクエストがJSON形式であることを指定
            .content(requestJson))                                   // 実際に送るJSONデータ
        .andExpect(status().isOk());

    verify(service, times(1)).updateStudent(any());
  }

}