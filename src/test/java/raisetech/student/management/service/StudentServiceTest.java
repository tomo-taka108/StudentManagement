package raisetech.student.management.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import raisetech.student.management.controller.converter.StudentConverter;
import raisetech.student.management.data.Student;
import raisetech.student.management.data.StudentCourse;
import raisetech.student.management.domain.StudentDetail;
import raisetech.student.management.exception.StudentNotFoundException;
import raisetech.student.management.repository.StudentRepository;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

  @Mock
  private StudentRepository mockRepository;

  @Mock
  private StudentConverter mockConverter;

  private StudentService sut;

  @BeforeEach
  void before() {
    sut = new StudentService(mockRepository, mockConverter);
  }

  @Test
  void 受講生詳細の一覧検索_リポジトリとコンバーターの処理が適切に呼び出せていること() {
    // 準備
    List<Student> studentList = new ArrayList<>();
    List<StudentCourse> studentCourseList = new ArrayList<>();
    when(mockRepository.search()).thenReturn(studentList);
    when(mockRepository.searchStudentCourseList()).thenReturn(studentCourseList);

    // 実行
    sut.searchStudentList();

    // 検証
    verify(mockRepository, times(1)).search();
    verify(mockRepository, times(1)).searchStudentCourseList();
    verify(mockConverter, times(1)).convertStudentDetails(studentList, studentCourseList);
  }

  @Test
    // 正常系のテスト
  void 受講生詳細の検索_IDが存在する場合_リポジトリの処理が適切に呼び出せて受講生詳細が返ること() {
    // 準備
    String id = "123";
    Student student = new Student();
    String studentId = student.getId();
    List<StudentCourse> studentCourse = new ArrayList<>();
    when(mockRepository.searchStudent(id)).thenReturn(student);
    when(mockRepository.searchStudentCourse(studentId)).thenReturn(studentCourse);

    // 実行
    StudentDetail result = sut.searchStudent(id);

    // 検証
    assertNotNull(result);
    assertEquals(student, result.getStudent());
    assertEquals(studentCourse, result.getStudentCourseList());

    verify(mockRepository, times(1)).searchStudent(id);
    verify(mockRepository, times(1)).searchStudentCourse(studentId);
  }

  @Test
    // 例外系のテスト
  void 受講生詳細の検索_IDが存在しない場合_例外が発生すること() {
    // 準備
    String id = "9999";
    when(mockRepository.searchStudent(id)).thenReturn(null);

    // 実行
    Exception exception = assertThrows(StudentNotFoundException.class,
        () -> sut.searchStudent(id));

    // 検証
    assertEquals("ID: " + id + " の受講生が見つかりません。", exception.getMessage());

    verify(mockRepository, times(1)).searchStudent(id);
    verify(mockRepository, never()).searchStudentCourse(anyString());
  }

  @Test
  void 受講生詳細の登録_受講生とコース情報が登録されること() {
    // 準備
    Student student = new Student();
    student.setId("123");

    StudentCourse studentCourse1 = new StudentCourse();
    StudentCourse studentCourse2 = new StudentCourse();
    List<StudentCourse> studentCourseList = List.of(studentCourse1, studentCourse2);

    StudentDetail studentDetail = new StudentDetail(student, studentCourseList);

    // 実行
    StudentDetail result = sut.registerStudent(studentDetail);

    // 検証
    // ①repository.registerStudent が1回呼ばれること
    verify(mockRepository, times(1)).registerStudent(student);
    // ②各studentCourseについて repository.registerStudentCourse が呼ばれること
    verify(mockRepository, times(1)).registerStudentCourse(studentCourse1);
    verify(mockRepository, times(1)).registerStudentCourse(studentCourse2);
    // 戻り値が元の studentDetail と同じであること
    assertSame(studentDetail, result);
  }

  @Test
  void 受講生コース情報を登録する際の初期情報の設定_初期情報が正しく設定されること() {
    // 準備
    Student student = new Student();
    student.setId("123");
    StudentCourse studentCourse = new StudentCourse();
    LocalDateTime now = LocalDateTime.now();

    // 実行
    sut.initStudentCourse(studentCourse, student);

    // 検証
    assertEquals(student.getId(), studentCourse.getStudentId());
    assertEquals(now, studentCourse.getStartDate());
    assertEquals(now.plusYears(1), studentCourse.getEndDate());
  }

  @Test
  void 受講生詳細の更新_受講生とコース情報が更新されること() {
    // 準備
    Student student = new Student();
    student.setId("123");

    StudentCourse studentCourse1 = new StudentCourse();
    StudentCourse studentCourse2 = new StudentCourse();
    List<StudentCourse> studentCourseList = List.of(studentCourse1, studentCourse2);

    StudentDetail studentDetail = new StudentDetail(student, studentCourseList);

    // 実行
    sut.updateStudent(studentDetail);

    // 検証
    // ①repository.updateStudent が1回呼ばれること
    verify(mockRepository, times(1)).updateStudent(student);
    // ②各studentCourseについて repository.updateStudentCourse が呼ばれること
    verify(mockRepository, times(1)).updateStudentCourse(studentCourse1);
    verify(mockRepository, times(1)).updateStudentCourse(studentCourse2);
  }

}