package raisetech.student.management.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import raisetech.student.management.controller.converter.StudentConverter;
import raisetech.student.management.data.CourseStatus;
import raisetech.student.management.data.Student;
import raisetech.student.management.data.StudentCourse;
import raisetech.student.management.domain.StudentDetail;
import raisetech.student.management.exception.StudentNotFoundException;
import raisetech.student.management.repository.StudentRepository;

/**
 * 受講生情報を取り扱うサービスです。 受講生の検索や登録・更新処理を行います。
 */
@Service
public class StudentService {

  private StudentRepository repository;
  private StudentConverter converter;

  @Autowired
  public StudentService(StudentRepository repository, StudentConverter converter) {
    this.repository = repository;
    this.converter = converter;
  }

  /**
   * 受講生詳細の一覧検索です。全件検索を行うので、条件指定は行いません。
   *
   * @return 受講生詳細一覧（全件）
   */
  public List<StudentDetail> searchStudentList() {
    List<Student> studentList = repository.search();
    List<StudentCourse> studentCourseList = repository.searchStudentCourseList();
    List<CourseStatus> courseStatusList = repository.searchCourseStatusList();
    return converter.convertStudentDetails(studentList, studentCourseList, courseStatusList);
  }

  /**
   * 受講生詳細の検索です。IDに紐づく任意の受講生の情報を取得し、その受講生に紐づく受講生コース情報およびコース申込状況を取得して設定します。
   *
   * @param id 受講生ID
   * @return 受講生詳細
   * @throws StudentNotFoundException 指定されたIDの受講生が見つからない場合
   */
  public StudentDetail searchStudent(String id) {
    Student student = repository.searchStudent(id);

    // 受講生が存在しなければ StudentNotFoundException を投げる
    if (student == null) {
      throw new StudentNotFoundException("ID: " + id + " の受講生が見つかりません。");
    }

    // 受講生が存在すればコース情報を取得
    List<StudentCourse> studentCourse = repository.searchStudentCourse(student.getId());

    // 受講生が存在すればコース申込状況を取得
    List<CourseStatus> courseStatus = repository.searchCourseStatus(student.getId());

    return new StudentDetail(student, studentCourse, courseStatus);
  }

  /**
   * 受講生詳細の登録を行います。 受講生と受講生コース情報、コース申込状況を個別に登録し、受講生コース情報には受講生ID紐づけとコース開始日、コース終了日を設定します。
   * コース申込状況は、受講生IDとコース情報IDを各々紐づけの設定をします。
   *
   * @param studentDetail 受講生詳細
   * @return 登録情報を付与した受講生詳細
   */
  @Transactional // 登録・更新・削除したりする場合は、Service層で必ずつける！
  public StudentDetail registerStudent(StudentDetail studentDetail) {
    Student student = studentDetail.getStudent();

    List<StudentCourse> studentCourseList = studentDetail.getStudentCourseList();
    List<CourseStatus> courseStatusList = studentDetail.getCourseStatusList();

    // ★ バリデーション: courseIdの重複チェック
    Set<String> courseIds = new HashSet<>();
    for (StudentCourse course : studentCourseList) {
      if (!courseIds.add(course.getCourseId())) {
        throw new IllegalArgumentException("同じ courseId が複数あります：" + course.getCourseId());
      }
    }

    // ★ courseId の存在チェック
    Set<String> statusCourseIds = courseStatusList.stream()
        .map(CourseStatus::getCourseId)
        .collect(Collectors.toSet());
    if (!courseIds.equals(statusCourseIds)) {
      throw new IllegalArgumentException("studentCourseList と courseStatusList の courseId が一致していません。");
    }

    // 受講生の登録
    repository.registerStudent(student);

    // courseId をキーに Map にして lookup
    Map<String, CourseStatus> courseStatusMap = courseStatusList.stream()
        .collect(Collectors.toMap(CourseStatus::getCourseId, status -> status));

    // 受講生コース情報とコース申込状況を登録
    for (StudentCourse course : studentCourseList) {
      initStudentCourse(course, student); // studentId, start/end日を設定
      repository.registerStudentCourse(course);

      CourseStatus status = courseStatusMap.get(course.getCourseId());
      initCourseStatus(status, student, course); // studentId, studentCourse.id を設定
      repository.registerCourseStatus(status);
    }

    return studentDetail;
  }

  /**
   * 受講生コース情報を登録する際の初期情報を設定する。
   *
   * @param course  受講生コース情報
   * @param student 受講生
   */
  void initStudentCourse(StudentCourse course, Student student) {
    LocalDate now = LocalDate.now();

    course.setStudentId(student.getId());
    course.setStartDate(now);
    course.setEndDate(now.plusYears(1));
  }

  /**
   * コース申込状況を登録する際の初期情報を設定する。
   *
   * @param status  コース申込状況
   * @param student 受講生
   * @param course  受講生コース情報
   */
  void initCourseStatus(CourseStatus status, Student student, StudentCourse course) {
    status.setStudentId(student.getId());
    status.setCourseId(course.getCourseId());
  }

  /**
   * 受講生詳細の更新を行います。受講生の情報と受講生コース情報、コース申込状況をそれぞれ更新します。
   *
   * @param studentDetail 受講生詳細
   */
  @Transactional // 登録・更新・削除したりする場合は、Service層で必ずつける！
  // 更新用メソッド【受講生情報】
  public void updateStudent(StudentDetail studentDetail) {
    repository.updateStudent(studentDetail.getStudent());
    // 更新用メソッド【受講生コース情報】
    studentDetail.getStudentCourseList()
        .forEach(studentCourse -> repository.updateStudentCourse(studentCourse));
    // 更新用メソッド【コース申込状況情】
    studentDetail.getCourseStatusList()
        .forEach(courseStatus -> repository.updateCourseStatus(courseStatus));
  }

}
