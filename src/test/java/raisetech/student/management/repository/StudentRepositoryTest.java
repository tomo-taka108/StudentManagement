package raisetech.student.management.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import raisetech.student.management.data.Student;
import raisetech.student.management.data.StudentCourse;

@MybatisTest
class StudentRepositoryTest {

  @Autowired // Repositoryはインターフェースでありnewでインスタンス化できないので、@Autowiredを使う
  private StudentRepository sut;

  @Test
    // search()
  void 受講生の全件検索が行えること() {
    List<Student> result = sut.search();
    assertThat(result.size()).isEqualTo(5);
  }

  @Test
    // searchStudent(String id)
  void IDに紐づく受講生の検索が行えること() {
    Student result = sut.searchStudent("3");
    assertThat(result.getName()).isEqualTo("田中 一郎");
  }

  @Test
    // searchStudentCourseList()
  void 受講生コース情報の全件検索が行えること() {
    List<StudentCourse> result = sut.searchStudentCourseList();
    assertThat(result.size()).isEqualTo(8);
  }

  @Test
    // searchStudentCourse(String studentId)
  void 受講生IDに紐づく受講生コース情報の検索が行えること() {
    List<StudentCourse> result = sut.searchStudentCourse("3");
    assertThat(result.size()).isEqualTo(2);
    assertThat(result.get(0).getCourseName()).isEqualTo("データベース基礎");
    assertThat(result.get(1).getCourseName()).isEqualTo("Python超入門");
  }

  @Test
    // registerStudent(Student student)
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
    // registerStudentCourse(StudentCourse studentCourse)
  void 受講生コース情報の登録が行えること() {
    StudentCourse studentCourse = new StudentCourse();
    studentCourse.setStudentId("3");
    studentCourse.setCourseName("Java超応用");
    studentCourse.setStartDate(LocalDateTime.of(2025, 9, 1, 0, 0, 0));
    studentCourse.setEndDate(LocalDateTime.of(2025, 12, 15, 0, 0, 0));

    sut.registerStudentCourse(studentCourse);

    List<StudentCourse> result = sut.searchStudentCourseList();
    assertThat(result.size()).isEqualTo(9);
  }

  @Test
    // updateStudent(Student student)
  void 受講生の更新が行えること() {
    Student student = new Student(
        "3",
        "田中一郎",
        "タナカイチロウ",
        "イチくん",
        "ichiro.tanaka@exapmle.com",
        "群馬",
        19,
        "男性",
        null,
        false);
    // areaを「東京」➡「群馬」、ageを「18」➡「19」に更新

    sut.updateStudent(student);

    Student result = sut.searchStudent("3");
    assertThat(result.getArea()).isEqualTo("群馬");
    assertThat(result.getAge()).isEqualTo(19);
  }

  @Test
    // updateStudentCourse(StudentCourse studentCourse)
  void 受講生コース情報のコース名の更新が行えること() {
    StudentCourse studentCourse = new StudentCourse();
    studentCourse.setId("3");
    studentCourse.setCourseName("データベース応用");
    // id:3のコース情報について、「データベース基礎」➡「データベース応用」へ更新

    sut.updateStudentCourse(studentCourse);

    List<StudentCourse> result = sut.searchStudentCourseList();
    assertThat(result.get(2).getCourseName()).isEqualTo("データベース応用");
  }

}