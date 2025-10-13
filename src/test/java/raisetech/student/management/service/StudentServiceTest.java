package raisetech.student.management.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
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
import raisetech.student.management.data.CourseStatus;
import raisetech.student.management.data.Student;
import raisetech.student.management.data.StudentCourse;
import raisetech.student.management.data.StudentSearchCriteria;
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
    List<CourseStatus> courseStatusList = new ArrayList<>();
    when(mockRepository.search()).thenReturn(studentList);
    when(mockRepository.searchStudentCourseList()).thenReturn(studentCourseList);
    when(mockRepository.searchCourseStatusList()).thenReturn(courseStatusList);

    // 実行
    sut.searchStudentList();

    // 検証
    verify(mockRepository, times(1)).search();
    verify(mockRepository, times(1)).searchStudentCourseList();
    verify(mockRepository, times(1)).searchCourseStatusList();
    verify(mockConverter, times(1)).convertStudentDetails(studentList, studentCourseList,
        courseStatusList);
  }

  @Test
    // 正常系のテスト
  void 受講生詳細の検索_IDが存在する場合_リポジトリの処理が適切に呼び出せて受講生詳細が返ること() {
    // 準備
    String id = "123";
    Student student = new Student();
    student.setId(id);
    // StudentCourseを準備
    StudentCourse studentCourse1 = new StudentCourse();
    studentCourse1.setId("11");
    studentCourse1.setStudentId(id);
    List<StudentCourse> studentCourseList = List.of(studentCourse1);
    // CourseStatusを準備
    CourseStatus status1 = new CourseStatus();
    status1.setId("51");
    status1.setStudentId(id);
    List<CourseStatus> courseStatusList = List.of(status1);

    // モックの戻り値を設定
    when(mockRepository.searchStudent(id)).thenReturn(student);
    when(mockRepository.searchStudentCourse(id)).thenReturn(studentCourseList);
    when(mockRepository.searchCourseStatus(id)).thenReturn(courseStatusList);

    // 実行
    StudentDetail result = sut.searchStudent(id);

    // 検証
    assertNotNull(result);
    assertEquals(student, result.getStudent());
    assertEquals(studentCourseList, result.getStudentCourseList());
    assertEquals(courseStatusList, result.getCourseStatusList());

    // リポジトリが正しく呼ばれたかを検証
    verify(mockRepository, times(1)).searchStudent(id);
    verify(mockRepository, times(1)).searchStudentCourse(id);
    verify(mockRepository, times(1)).searchCourseStatus(id);
  }

  @Test
    // 例外系のテスト
  void 受講生詳細の検索_IDが存在しない場合_例外StudentNotFoundExceptionが発生すること() {
    // 準備
    String id = "9999";
    when(mockRepository.searchStudent(id)).thenReturn(null);

    // 実行
    StudentNotFoundException exception = assertThrows(StudentNotFoundException.class,
        () -> sut.searchStudent(id));

    // 検証
    assertEquals("ID: " + id + " の受講生が見つかりません。", exception.getMessage());

    verify(mockRepository, times(1)).searchStudent(id);
    verify(mockRepository, never()).searchStudentCourse(anyString());
    verify(mockRepository, never()).searchCourseStatus(anyString());
  }

  @Test
  void 検索条件を指定して受講生詳細一覧を取得できること() {
    // 準備
    StudentSearchCriteria criteria = new StudentSearchCriteria();
    criteria.setArea("東京");

    List<Student> studentList = List.of(new Student());
    List<StudentCourse> courseList = List.of(new StudentCourse());
    List<CourseStatus> statusList = List.of(new CourseStatus());

    when(mockRepository.searchWithCriteria(criteria)).thenReturn(studentList);
    when(mockRepository.searchStudentCourseList()).thenReturn(courseList);
    when(mockRepository.searchCourseStatusList()).thenReturn(statusList);
    when(mockConverter.convertStudentDetails(studentList, courseList, statusList))
        .thenReturn(List.of(new StudentDetail()));

    // 実行
    List<StudentDetail> result = sut.searchWithCriteria(criteria);

    // 検証
    assertNotNull(result);
    verify(mockRepository, times(1)).searchWithCriteria(criteria);
    verify(mockRepository, times(1)).searchStudentCourseList();
    verify(mockRepository, times(1)).searchCourseStatusList();
    verify(mockConverter, times(1)).convertStudentDetails(studentList, courseList, statusList);
  }

  @Test
  void コース名で検索した場合にコースがフィルタリングされること() {
    // 準備
    StudentSearchCriteria criteria = new StudentSearchCriteria();
    criteria.setCourseName("Java");

    StudentCourse course1 = new StudentCourse();
    course1.setCourseName("Java入門");

    StudentCourse course2 = new StudentCourse();
    course2.setCourseName("Python基礎");

    when(mockRepository.searchWithCriteria(criteria)).thenReturn(List.of(new Student()));
    when(mockRepository.searchStudentCourseList()).thenReturn(List.of(course1, course2));
    when(mockRepository.searchCourseStatusList()).thenReturn(List.of());

    // 実行
    sut.searchWithCriteria(criteria);

    // 検証：「Java」を含むコース名のみでconvertが呼ばれること
    verify(mockConverter).convertStudentDetails(
        any(), // 受講生リストは何でもいい
        argThat(courses -> courses.size() == 1 && courses.get(0).getCourseName().contains("Java")),
        // コースリストは「Java」を含むものだけ
        any() // ステータスリストは何でもいい
    );
  }

  @Test
  void ステータスで検索した場合にステータスがフィルタリングされること() {
    // 準備
    StudentSearchCriteria criteria = new StudentSearchCriteria();
    criteria.setStatus("受講中");

    CourseStatus status1 = new CourseStatus();
    status1.setStatus("受講中");

    CourseStatus status2 = new CourseStatus();
    status2.setStatus("仮申込");

    when(mockRepository.searchWithCriteria(criteria)).thenReturn(List.of(new Student()));
    when(mockRepository.searchStudentCourseList()).thenReturn(List.of());
    when(mockRepository.searchCourseStatusList()).thenReturn(List.of(status1, status2));

    // 実行
    sut.searchWithCriteria(criteria);

    // 検証：受講中のみでconvertが呼ばれること
    verify(mockConverter).convertStudentDetails(
        any(), // 受講生リストは何でもいい
        any(), // コースリストは何でもいい
        argThat(statuses -> statuses.size() == 1 && statuses.get(0).getStatus().equals("受講中"))
        // 申込状況リストは「受講中」だけ
    );
  }

  @Test
  void 受講生詳細の登録_受講生とコース情報とコース申込状況が登録されること() {
    // 準備
    String id = "123";
    Student student = new Student();
    student.setId(id);

    // コースを2件用意
    StudentCourse studentCourse1 = new StudentCourse();
    studentCourse1.setCourseId("101");
    StudentCourse studentCourse2 = new StudentCourse();
    studentCourse2.setCourseId("102");
    List<StudentCourse> studentCourseList = List.of(studentCourse1, studentCourse2);

    // 各コースに紐づく申込状況を用意
    CourseStatus status1 = new CourseStatus();
    status1.setCourseId("101");
    CourseStatus status2 = new CourseStatus();
    status2.setCourseId("102");
    List<CourseStatus> courseStatusList = List.of(status1, status2);

    StudentDetail studentDetail = new StudentDetail(student, studentCourseList, courseStatusList);

    // 実行
    StudentDetail result = sut.registerStudent(studentDetail);

    // 検証
    // ①repository.registerStudent が1回呼ばれること
    verify(mockRepository, times(1)).registerStudent(student);
    // ②各studentCourseについて repository.registerStudentCourse が呼ばれること
    verify(mockRepository, times(1)).registerStudentCourse(studentCourse1);
    verify(mockRepository, times(1)).registerStudentCourse(studentCourse2);
    // ③各courseStatusについて repository.registerCourseStatus が呼ばれること
    verify(mockRepository, times(1)).registerCourseStatus(status1);
    verify(mockRepository, times(1)).registerCourseStatus(status2);
    // ④戻り値が元の studentDetail と同じであること
    assertSame(studentDetail, result);
    // ⑤初期化処理が行われていることを確認（IDの紐づけなど）
    assertEquals(id, studentCourse1.getStudentId());
    assertEquals(id, studentCourse2.getStudentId());
    assertEquals(id, status1.getStudentId());
    assertEquals(id, status2.getStudentId());
    assertEquals("101", status1.getCourseId());
    assertEquals("102", status2.getCourseId());
  }

  @Test
  void 受講生コース情報を登録する際の初期情報の設定_初期情報が正しく設定されること() {
    // 準備
    String id = "123";
    Student student = new Student();
    student.setId(id);
    StudentCourse studentCourse = new StudentCourse();
    LocalDate now = LocalDate.now();

    // 実行
    sut.initStudentCourse(studentCourse, student);

    // 検証
    assertEquals(id, studentCourse.getStudentId());
    assertEquals(now, studentCourse.getStartDate());
    assertEquals(now.plusYears(1), studentCourse.getEndDate());
  }

  @Test
  void コース申込状況を登録する際の初期情報の設定_初期情報が正しく設定されること() {
    // 準備
    String studentId = "123";
    String courseId = "101";

    Student student = new Student();
    student.setId(studentId);

    StudentCourse studentCourse = new StudentCourse();
    studentCourse.setCourseId(courseId);

    CourseStatus courseStatus = new CourseStatus();

    // 実行
    sut.initCourseStatus(courseStatus, student, studentCourse);

    // 検証
    // studentId と courseId が正しくセットされていること
    assertEquals(studentId, courseStatus.getStudentId());
    assertEquals(courseId, courseStatus.getCourseId());
  }

  @Test
  void コースに紐づいたコース申込状況を登録しない場合は例外が発生すること() {
    Student student = new Student();
    student.setId("123");

    StudentCourse course = new StudentCourse();
    course.setId("12");

    // コース1件、コース申込状況0件 → 不一致
    StudentDetail studentDetail = new StudentDetail(
        student,
        List.of(course),
        List.of()
    );

    assertThrows(IllegalArgumentException.class, () -> sut.registerStudent(studentDetail));
  }

  @Test
  void 受講生詳細の更新_受講生とコース情報と申込状況が更新されること() {
    // 準備
    String id = "123";
    Student student = new Student();
    student.setId(id);

    StudentCourse studentCourse1 = new StudentCourse();
    StudentCourse studentCourse2 = new StudentCourse();
    List<StudentCourse> studentCourseList = List.of(studentCourse1, studentCourse2);

    CourseStatus courseStatus1 = new CourseStatus();
    CourseStatus courseStatus2 = new CourseStatus();
    List<CourseStatus> courseStatusList = List.of(courseStatus1, courseStatus2);

    StudentDetail studentDetail = new StudentDetail(student, studentCourseList, courseStatusList);

    // 実行
    sut.updateStudent(studentDetail);

    // 検証
    // ①repository.updateStudent が1回呼ばれること
    verify(mockRepository, times(1)).updateStudent(student);
    // ②各studentCourseについて repository.updateStudentCourse が呼ばれること
    verify(mockRepository, times(1)).updateStudentCourse(studentCourse1);
    verify(mockRepository, times(1)).updateStudentCourse(studentCourse2);
    // ③各courseStatus について repository.updateCourseStatus が呼ばれること
    verify(mockRepository, times(1)).updateCourseStatus(courseStatus1);
    verify(mockRepository, times(1)).updateCourseStatus(courseStatus2);
  }

}