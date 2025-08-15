package raisetech.student.management.service;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import raisetech.student.management.controller.converter.StudentConverter;
import raisetech.student.management.data.Student;
import raisetech.student.management.data.StudentCourses;
import raisetech.student.management.domain.StudentDetail;
import raisetech.student.management.repository.StudentRepository;

/**
 * 受講生情報を取り扱うサービスです。
 * 受講生の検索や登録・更新処理を行います。
 */
@Service
public class StudentService {

  private StudentRepository repository;
  private StudentConverter converter;

  @Autowired
  public StudentService(StudentRepository repository,StudentConverter converter) {
    this.repository = repository;
    this.converter = converter;
  }

  /**
   * 受講生一覧検索です。
   * 全件検索を行うので、条件指定は行いません。
   *
   * @return 受講生一覧（全件）
   */
  public List<StudentDetail> searchStudentList() {
    List<Student> studentList = repository.search();
    List<StudentCourses> studentCoursesList = repository.searchStudentCoursesList();
    return converter.convertStudentDetails(studentList, studentCoursesList);
  }

  /**
   * 受講生検索です。
   * IDに紐づく任意の受講生の情報を取得し、その受講生に紐づく受講生コース情報を取得して設定します。
   * @param id 受講生ID
   * @return 受講生
   */
  public StudentDetail searchStudent(String id) {
    Student student = repository.searchStudent(id);
    List<StudentCourses> studentCourses = repository.searchStudentCourses(student.getId());
    return new StudentDetail(student,studentCourses);
  }


  @Transactional // 登録・更新・削除したりする場合は、Service層で必ずつける！
  // TODO:登録用メソッド【受講生情報】
  public StudentDetail registerStudent(StudentDetail studentDetail) {
    repository.registerStudent(studentDetail.getStudent());
    // TODO:登録用メソッド【受講生コース情報】
    for (StudentCourses studentCourse : studentDetail.getStudentCourses()) {
      studentCourse.setStudentId(studentDetail.getStudent().getId());
      studentCourse.setStartDate(LocalDateTime.now());
      studentCourse.setEndDate(LocalDateTime.now().plusYears(1));
      repository.registerStudentCourses(studentCourse);
    }
    return studentDetail;
  }

  @Transactional // 登録・更新・削除したりする場合は、Service層で必ずつける！
  // TODO:更新用メソッド【受講生情報】
  public void updateStudent(StudentDetail studentDetail) {
    repository.updateStudent(studentDetail.getStudent());
    // TODO:更新用メソッド【受講生コース情報】
    for (StudentCourses studentCourse : studentDetail.getStudentCourses()) {
      studentCourse.setStudentId(studentDetail.getStudent().getId());
      repository.updateStudentCourses(studentCourse);
    }
  }

}
