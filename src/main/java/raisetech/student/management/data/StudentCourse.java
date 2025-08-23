package raisetech.student.management.data;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class StudentCourse {

  private String id;

  private String studentId;

  @NotBlank(message = "【courseName】入力が必要です。")
  private String courseName;

  private LocalDateTime startDate;

  private LocalDateTime endDate;

}
