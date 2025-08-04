package raisetech.student.management.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
    return repository.searchCourses();
  }

  // 登録用メソッド【受講生情報】
  public void insertStudent(StudentDetail studentDetail) {
    repository.insertStudent(studentDetail.getStudent());
  }

  // 登録用メソッド【受講生コース情報】
  public void insertCourse(List<StudentCourses> courses) {
    for (StudentCourses course : courses) {
      repository.insertCourse(course);
    }
  }

}
