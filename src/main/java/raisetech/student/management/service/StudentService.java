package raisetech.student.management.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import raisetech.student.management.data.Student;
import raisetech.student.management.data.StudentCourses;
import raisetech.student.management.repository.StudentRepository;

@Service
public class StudentService {

  private StudentRepository repository;

  @Autowired
  public StudentService(StudentRepository repository) {
    this.repository = repository;
  }

  public List<Student> searchStudentList() {
    // 絞り込みをする。年齢が20代の人のみを抽出する。
    // その抽出したリストをコントローラーに返す。

    return repository.search().stream()
        .filter(s->s.getAge()>=20 && s.getAge()<30)
        .collect(Collectors.toList());
  }

  public List<StudentCourses> searchStudentCoursesList() {
    // 絞り込み検索で「Java入門」のコース情報のみを抽出する。
    // 抽出したリストをコントローラーに返す。

    return repository.searchCourses().stream()
        .filter(c->"Java入門".equals(c.getCourseName()))
        .collect(Collectors.toList());
  }

}
