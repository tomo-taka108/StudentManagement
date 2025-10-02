package raisetech.student.management.data;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Schema(description = "コース名に紐づくコースIDを定義するデータモデル（コースマスタ）")
@Getter
@Setter
@NoArgsConstructor // 引数なしコンストラクタを自動生成
@AllArgsConstructor // 全フィールドを引数に持つコンストラクタを自動生成

public class Course {

  @Schema(description = "コースID（識別子）", example = "101")
  @NotBlank(message = "【courseId】入力が必要です。")
  private String courseId; // VARCHAR(50) に対応

  @Schema(description = "コース名", example = "Java入門")
  @NotBlank(message = "【courseName】入力が必要です。")
  private String courseName; // course_name カラムに対応

}
