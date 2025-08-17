package raisetech.student.management.domain;

import jakarta.validation.Valid;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import raisetech.student.management.data.Student;
import raisetech.student.management.data.StudentCourse;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StudentDetail {

  @Valid // StudentクラスのValidationアノテーションを有効化する
  private Student student;

  @Valid // StudentCourseクラスのValidationアノテーションを有効化する
  private List<StudentCourse> studentCourseList;
}
