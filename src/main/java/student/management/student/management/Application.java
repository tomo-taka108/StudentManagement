package student.management.student.management;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication //SpringBootアプリの起動（SpringBootが使える）
@RestController
public class Application {

  // 【講義19】StudentRepositoryというDB操作用のインターフェースを変数repositoryに注入してほしい（newしなくてよい）
  @Autowired
  private StudentRepository repository;


  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }

  @GetMapping("/studentList")
  public List<Student> getStudentList() {
    return repository.search();
  }

  @GetMapping("/studentCoursesList")
  public List<StudentCourses> getStudentCoursesList() {
    return repository.searchCourses();
  }

}
