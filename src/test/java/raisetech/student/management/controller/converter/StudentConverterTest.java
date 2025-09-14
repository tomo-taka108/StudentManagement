package raisetech.student.management.controller.converter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import raisetech.student.management.data.Student;
import raisetech.student.management.data.StudentCourse;
import raisetech.student.management.domain.StudentDetail;

class StudentConverterTest {

  private StudentConverter sut;

  @BeforeEach
  void before() {
    sut = new StudentConverter();
  }

  // 【受講生およびコース情報の各項目が漏れなく作成されるかのテスト】
  @Test
  void 受講生のリストと受講生コース情報のリストを渡して受講生詳細のリストが作成できること() {
    // 準備
    Student student = createStudent(); // Studentの各項目は共通のためメソッド化

    StudentCourse studentCourse = new StudentCourse(
        "1",
        "1",
        "Java入門",
        LocalDateTime.now(),
        LocalDateTime.now().plusYears(1));

    List<Student> studentList = List.of(student);
    List<StudentCourse> studentCourseList = List.of(studentCourse);

    // 実行
    List<StudentDetail> result = sut.convertStudentDetails(studentList, studentCourseList);

    // 検証
    assertThat(result.get(0).getStudent()).isEqualTo(student);
    assertThat(result.get(0).getStudentCourseList()).isEqualTo(studentCourseList);
  }

  @Test
  void 受講生のリストと受講生コース情報のリストを渡した時に紐づかない受講生コース情報は除外されること() {
    // 準備
    Student student = createStudent(); // Studentの各項目は共通のためメソッド化

    StudentCourse studentCourse = new StudentCourse(
        "1",
        "2",
        "Java入門",
        LocalDateTime.now(),
        LocalDateTime.now().plusYears(1));

    List<Student> studentList = List.of(student);
    List<StudentCourse> studentCourseList = List.of(studentCourse);

    // 実行
    List<StudentDetail> result = sut.convertStudentDetails(studentList, studentCourseList);

    // 検証
    assertThat(result.get(0).getStudent()).isEqualTo(student);
    assertThat(result.get(0).getStudentCourseList()).isEmpty(); // コースは空のはず
  }

  // メソッド化したStudent
  private static Student createStudent() {
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
    return student;
  }

  // 【受講生が複数、コースが複数の作成に対応できているかのテスト】
  // ケース①【受講生が１人＋コースが複数】
  @Test
  void 受講生1人に対しコース2つが作成されること() {
    // 準備
    Student student = new Student();
    student.setId("123");
    StudentCourse course1 = new StudentCourse();
    course1.setStudentId("123");
    course1.setCourseName("Java入門");
    StudentCourse course2 = new StudentCourse();
    course2.setStudentId("123");
    course2.setCourseName("データベース基礎");

    List<Student> studentList = List.of(student);
    List<StudentCourse> studentCourseList = List.of(course1, course2);

    // 実行
    List<StudentDetail> result = sut.convertStudentDetails(studentList, studentCourseList);

    // 検証
    StudentDetail detail = result.get(0);
    // ①返り値StudentDetailが1件返ってくること
    assertEquals(1, result.size());
    // ②返り値StudentDetailのstudentに元のStudentが入っていること
    assertEquals(student, detail.getStudent());
    // ③返り値StudentDetailのstudentCourseListに2件コースが入っていること
    assertEquals(2, detail.getStudentCourseList().size());
    // ④その2件のコースの名前が正しいこと
    assertEquals("Java入門", detail.getStudentCourseList().get(0).getCourseName());
    assertEquals("データベース基礎", detail.getStudentCourseList().get(1).getCourseName());
  }

  // ケース②【受講生が複数＋コースが複数】
  @Test
  void 受講生2人に対しコース1つおよびコース2つが作成されること() {
    // 準備
    Student studentA = new Student();
    studentA.setId("123");
    Student studentB = new Student();
    studentB.setId("456");

    StudentCourse courseA1 = new StudentCourse();
    courseA1.setStudentId("123");
    courseA1.setCourseName("Java入門");
    StudentCourse courseA2 = new StudentCourse();
    courseA2.setStudentId("123");
    courseA2.setCourseName("データベース基礎");
    StudentCourse courseB3 = new StudentCourse();
    courseB3.setStudentId("456");
    courseB3.setCourseName("ネットワーク基礎");
    StudentCourse courseX99 = new StudentCourse(); // 無視されることを確認するために準備
    courseX99.setStudentId("789"); // 存在しない受講生ID、無視されるはず
    courseX99.setCourseName("Java応用");

    List<Student> studentList = List.of(studentA, studentB);
    List<StudentCourse> studentCourseList = List.of(courseA1, courseA2, courseB3, courseX99);

    // 実行
    List<StudentDetail> result = sut.convertStudentDetails(studentList, studentCourseList);

    // 検証
    StudentDetail detail1 = result.get(0);
    StudentDetail detail2 = result.get(1);
    // ①返り値StudentDetailが2件返ってくること
    assertEquals(2, result.size());
    // ②返り値StudentDetailのstudentに元のStudentが入っていること
    assertEquals(studentA, detail1.getStudent());
    assertEquals(studentB, detail2.getStudent());
    // ③返り値StudentDetailのstudentCourseListに正しいコース数が入っていること
    assertEquals(2, detail1.getStudentCourseList().size());
    assertEquals(1, detail2.getStudentCourseList().size());
    // ④各コースの名前が正しいこと
    assertEquals("Java入門", detail1.getStudentCourseList().get(0).getCourseName());
    assertEquals("データベース基礎", detail1.getStudentCourseList().get(1).getCourseName());
    assertEquals("ネットワーク基礎", detail2.getStudentCourseList().get(0).getCourseName());
  }

}