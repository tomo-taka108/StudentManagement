package raisetech.student.management.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import raisetech.student.management.data.CourseStatus;
import raisetech.student.management.data.Student;
import raisetech.student.management.data.StudentCourse;

@Schema(description = "受講生詳細（受講生情報およびコース情報、コース申込状況）を表すデータモデル")
@Getter
@Setter
@NoArgsConstructor // 引数なしコンストラクタを自動生成
@AllArgsConstructor // 全フィールドを引数に持つコンストラクタを自動生成
public class StudentDetail {

  @Valid // StudentクラスのValidationアノテーションを有効化する
  private Student student;

  @Valid // StudentCourseクラスのValidationアノテーションを有効化する
  private List<StudentCourse> studentCourseList;

  @Valid // CourseStatusクラスのValidationアノテーションを有効化する
  private List<CourseStatus> courseStatusList;
}
