package raisetech.student.management.service;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import raisetech.student.management.data.Student;
import raisetech.student.management.data.StudentCourses;
import raisetech.student.management.domain.StudentDetail;
import raisetech.student.management.repository.StudentRepository;

@Service
public class StudentService {

  private StudentRepository repository;

  @Autowired
  public StudentService(StudentRepository repository) {
    this.repository = repository;
  }

  // 一覧取得用メソッド【受講生情報】
  public List<Student> searchStudentList() {
    return repository.search();
  }

  // 一覧取得用メソッド【受講生コース情報】
  public List<StudentCourses> searchStudentCoursesList() {
    return repository.searchStudentCoursesList();
  }

  // 特定id取得用メソッド【受講生情報 ＆ 受講生コース情報】
  public StudentDetail searchStudent(String id) {
    Student student = repository.searchStudent(id);
    List<StudentCourses> studentCourses = repository.searchStudentCourses(student.getId());
    StudentDetail studentDetail = new StudentDetail();
    studentDetail.setStudent(student);
    studentDetail.setStudentCourses(studentCourses);
    return studentDetail;
  }


  @Transactional // 登録・更新・削除したりする場合は、Service層で必ずつける！
  // TODO:登録用メソッド【受講生情報】
  public void registerStudent(StudentDetail studentDetail) {
    repository.registerStudent(studentDetail.getStudent());
    // TODO:登録用メソッド【受講生コース情報】
    for (StudentCourses studentCourse : studentDetail.getStudentCourses()) {
      studentCourse.setStudentId(studentDetail.getStudent().getId());
      studentCourse.setStartDate(LocalDateTime.now());
      studentCourse.setEndDate(LocalDateTime.now().plusYears(1));
      repository.registerStudentCourses(studentCourse);
    }
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
