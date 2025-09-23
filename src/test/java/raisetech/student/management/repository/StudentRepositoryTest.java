package raisetech.student.management.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import raisetech.student.management.data.CourseStatus;
import raisetech.student.management.data.Student;
import raisetech.student.management.data.StudentCourse;

@MybatisTest
class StudentRepositoryTest {

  @Autowired // Repositoryはインターフェースでありnewでインスタンス化できないので、@Autowiredを使う
  private StudentRepository sut;

  @Test
    // List<Student> search()
  void 受講生の全件検索が行えること() {
    List<Student> result = sut.search();
    assertThat(result.size()).isEqualTo(5);
  }

  @ParameterizedTest
  @ValueSource(strings = {"1", "2", "3", "4", "5"})
    // Student searchStudent(String id)
  void IDに紐づく受講生の検索が行えること(String id) {
    Student result = sut.searchStudent(id);

    switch (id) {
      case "1" -> assertThat(result.getName()).isEqualTo("佐藤 太郎");
      case "2" -> assertThat(result.getName()).isEqualTo("鈴木 花子");
      case "3" -> assertThat(result.getName()).isEqualTo("田中 一郎");
      case "4" -> assertThat(result.getName()).isEqualTo("高橋 美咲");
      case "5" -> assertThat(result.getName()).isEqualTo("山本 大輔");
    }
  }

  @Test
    // Student searchStudent(String id)
  void 登録されていないIDの受講生検索が行えないこと() {
    Student result = sut.searchStudent("6");
    assertThat(result).isNull(); // 単一検索のためisNullを使う（isEmptyではない）
  }

  @Test
    // List<StudentCourse> searchStudentCourseList()
  void 受講生コース情報の全件検索が行えること() {
    List<StudentCourse> result = sut.searchStudentCourseList();
    assertThat(result.size()).isEqualTo(8);
  }

  @ParameterizedTest
  @ValueSource(strings = {"1", "2", "3", "4", "5"})
    // List<StudentCourse> searchStudentCourse(String studentId)
  void 受講生IDに紐づく受講生コース情報の検索が行えること(String studentId) {
    List<StudentCourse> result = sut.searchStudentCourse(studentId);

    switch (studentId) {
      case "1" -> {
        assertThat(result.get(0).getCourseName()).isEqualTo("Java入門");
        assertThat(result.get(1).getCourseName()).isEqualTo("Spring Boot実践");
      }
      case "2" -> assertThat(result.get(0).getCourseName()).isEqualTo("Java入門");
      case "3" -> {
        assertThat(result.get(0).getCourseName()).isEqualTo("データベース基礎");
        assertThat(result.get(1).getCourseName()).isEqualTo("Python超入門");
      }
      case "4" -> assertThat(result.get(0).getCourseName()).isEqualTo("データベース基礎");
      case "5" -> {
        assertThat(result.get(0).getCourseName()).isEqualTo("ネットワーク基礎");
        assertThat(result.get(1).getCourseName()).isEqualTo("Java応用");
      }
    }
  }

  @Test // List<StudentCourse> searchStudentCourse(String studentId)
  void 登録されていない受講生IDで受講生コース情報の検索が行えないこと(){
    List<StudentCourse> result = sut.searchStudentCourse("6");
    assertThat(result).isEmpty(); // 複数検索のためisEmptyを使う（「リスト」という入れ物は返すが中身は「空」。isNullではない）
  }

  @Test
    // List<CourseStatus> searchCourseStatusList()
  void コース申込状況の全件検索が行えること(){
    List<CourseStatus>result=sut.searchCourseStatusList();
    assertThat(result.size()).isEqualTo(8);
  }

  @ParameterizedTest
  @ValueSource(strings = {"1", "2", "3", "4"})
    // List<CourseStatus> searchCourseStatus(String courseId)
  void コースIDに紐づくコース申込状況の検索が行えること(String courseId) {
    List<CourseStatus> result = sut.searchCourseStatus(courseId);

    switch (courseId) {
      case "1" -> assertThat(result.get(0).getStatus()).isEqualTo("仮申込");
      case "2" -> assertThat(result.get(0).getStatus()).isEqualTo("本申込");
      case "3" -> assertThat(result.get(0).getStatus()).isEqualTo("受講中");
      case "4" -> assertThat(result.get(0).getStatus()).isEqualTo("受講終了");
    }
  }

  @Test // List<CourseStatus> searchCourseStatus(String courseId)
  void 登録されていないコースIDでコース申込状況の検索が行えないこと(){
    List<CourseStatus> result = sut.searchCourseStatus("9");
    assertThat(result).isEmpty(); // 複数検索のためisEmptyを使う（「リスト」という入れ物は返すが中身は「空」。isNullではない）
  }

  @Test
    // void registerStudent(Student student)
  void 受講生の登録が行えること() {
    Student student = new Student();
    student.setName("山田花子");
    student.setKanaName("ヤマダハナコ");
    student.setNickname("ハナちゃん");
    student.setEmail("hanako@example.com");
    student.setArea("東京");
    student.setAge(30);
    student.setSex("女性");
    student.setRemark("");
    student.setDeleted(false);

    sut.registerStudent(student);

    List<Student> result = sut.search();
    assertThat(result.size()).isEqualTo(6);
  }

  @Test
    // void registerStudentCourse(StudentCourse studentCourse)
  void 受講生コース情報の登録が行えること() {
    StudentCourse studentCourse = new StudentCourse();
    studentCourse.setStudentId("3");
    studentCourse.setCourseName("Java超応用");
    studentCourse.setStartDate(LocalDate.of(2025, 9, 1));
    studentCourse.setEndDate(LocalDate.of(2025, 12, 15));

    sut.registerStudentCourse(studentCourse);

    List<StudentCourse> result = sut.searchStudentCourseList();
    assertThat(result.size()).isEqualTo(9);
  }

  @Test
    // void registerCourseStatus(CourseStatus courseStatus)
  void コース申込状況の登録が行えること() {
    // 先に新しいコースを登録
    StudentCourse studentCourse = new StudentCourse();
    studentCourse.setStudentId("3");
    studentCourse.setCourseName("Java超応用");
    studentCourse.setStartDate(LocalDate.of(2025, 9, 1));
    studentCourse.setEndDate(LocalDate.of(2025, 12, 15));
    sut.registerStudentCourse(studentCourse);

    // 自動採番されたコースIDを取得
    int newCourseId = sut.searchStudentCourseList()
        .stream()
        .mapToInt(c -> Integer.parseInt(c.getId()))
        .max()
        .orElseThrow();

    // 新しい申込状況を登録
    CourseStatus courseStatus = new CourseStatus();
    courseStatus.setCourseId(String.valueOf(newCourseId));
    courseStatus.setStudentId("3");
    courseStatus.setStatus("仮申込");

    sut.registerCourseStatus(courseStatus);

    List<CourseStatus> result = sut.searchCourseStatusList();
    assertThat(result.size()).isEqualTo(9);
  }

  @Test
    // void updateStudent(Student student)
  void 受講生の更新が行えること() {
    Student student = new Student(
        "3",
        "田中一郎",
        "タナカイチロウ",
        "イチくん",
        "ichiro.tanaka@exapmle.com",
        "群馬",
        21,
        "男性",
        "成績優秀",
        false);
    // areaを「秋田」➡「群馬」、ageを「20」➡「21」に更新

    sut.updateStudent(student);

    Student result = sut.searchStudent("3");
    assertThat(result.getArea()).isEqualTo("群馬");
    assertThat(result.getAge()).isEqualTo(21);
  }

  @Test
    // void updateStudentCourse(StudentCourse studentCourse)
  void 受講生コース情報のコース名の更新が行えること() {
    StudentCourse studentCourse = new StudentCourse();
    studentCourse.setId("3");
    studentCourse.setCourseName("データベース応用");
    // id:3のコース情報について、「データベース基礎」➡「データベース応用」へ更新

    sut.updateStudentCourse(studentCourse);

    List<StudentCourse> result = sut.searchStudentCourseList();
    assertThat(result.get(2).getCourseName()).isEqualTo("データベース応用");
  }

  @Test
    // void updateCourseStatus(CourseStatus courseStatus)
  void コース申込状況の申込状況の更新が行えること() {
    CourseStatus courseStatus = new CourseStatus();
    courseStatus.setId("5");
    courseStatus.setStatus("本申込");
    // id:5のコース申込状況について、「仮申込」➡「本申込」へ更新

    sut.updateCourseStatus(courseStatus);

    List<CourseStatus> result = sut.searchCourseStatusList();
    assertThat(result.get(4).getStatus()).isEqualTo("本申込");
  }

}