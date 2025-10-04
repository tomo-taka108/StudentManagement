package raisetech.student.management.data;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Schema(description = "コースの申込状況を表すデータモデル")
@Getter
@Setter
@NoArgsConstructor // 引数なしコンストラクタを自動生成
@AllArgsConstructor // 全フィールドを引数に持つコンストラクタを自動生成

public class CourseStatus {

  @Schema(description = "コース申込状況ID（自動付与）", example = "1")
  private String id;

  @Schema(description = "受講生ID", example = "1")
  private String studentId;

  @Schema(description = "コースID", example = "1")
  private String courseId;

  @Schema(description = "コース申込状況", example = "本申込")
  @NotBlank(message = "【status】「仮申込」「本申込」「受講中」「受講終了」のいずれかを入力してください。")
  @Pattern(regexp = "仮申込|本申込|受講中|受講終了", message = "【status】は「仮申込」「本申込」「受講中」「受講終了」のいずれかを入力してください。")
  private String status;

}
