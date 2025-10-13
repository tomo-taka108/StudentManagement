package raisetech.student.management.data;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Schema(description = "受講生コース情報を表すデータモデル")
@Getter
@Setter
@NoArgsConstructor // 引数なしコンストラクタを自動生成
@AllArgsConstructor // 全フィールドを引数に持つコンストラクタを自動生成

public class StudentCourse {

  @Schema(description = "コースID（自動付与）", example = "1")
  private String id; // DB自動採番

  @Schema(description = "受講生ID", example = "1")
  private String studentId;

  @Schema(description = "コースそのものの識別子", example = "101")
  @NotBlank(message = "コースIDは入力が必要です。")
  private String courseId;

  @Schema(description = "コース名", example = "Java入門")
  @NotBlank(message = "コース名は入力が必要です。")
  private String courseName;

  @Schema(description = "受講開始日", example = "2025-04-01")
  private LocalDate startDate;

  @Schema(description = "受講終了日（予定日）", example = "2025-06-30")
  private LocalDate endDate;

}
