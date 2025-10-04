package raisetech.student.management.data;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

/**
 * 受講生検索条件を表すデータモデル すべてのフィールドは任意項目です。nullの場合はその条件で絞り込みを行いません。
 */

@Schema(description = "受講生検索条件（すべて任意項目）")
@Getter
@Setter
@NoArgsConstructor // 引数なしコンストラクタを自動生成
@AllArgsConstructor // 全フィールドを引数に持つコンストラクタを自動生成

public class StudentSearchCriteria {

  @Schema(description = "受講生ID", example = "1")
  @Pattern(regexp = "^\\d+$", message = "数字のみ入力してください。")
  private String id;

  @Schema(description = "氏名（部分一致）", example = "佐藤")
  private String name;

  @Schema(description = "フリガナ（部分一致）", example = "サトウ")
  private String kanaName;

  @Schema(description = "ニックネーム（部分一致）", example = "タロ")
  private String nickname;

  @Schema(description = "居住地域", example = "東京")
  private String area;

  @Schema(description = "年齢（最小）", example = "18")
  @Min(value = 0, message = "年齢の最小値は0以上を指定してください。")
  @Max(value = 150, message = "年齢の最小値は150以下を指定してください。")
  private Integer ageMin;

  @Schema(description = "年齢（最大）", example = "50")
  @Min(value = 0, message = "年齢の最大値は0以上を指定してください。")
  @Max(value = 150, message = "年齢の最大値は150以下を指定してください。")
  private Integer ageMax;

  @Schema(description = "性別", example = "男性")
  @Pattern(regexp = "男性|女性|その他", message = "性別は「男性」「女性」「その他」のいずれかを入力してください。")
  private String sex;

  @Schema(description = "コース名（部分一致）", example = "Java")
  private String courseName;

  @Schema(description = "コース申込状況", example = "受講中")
  @Pattern(regexp = "仮申込|本申込|受講中|受講終了", message = "【status】は「仮申込」「本申込」「受講中」「受講終了」のいずれかを入力してください。")
  private String status;

  @Schema(description = "削除フラグ（true: 削除済み, false: 有効）", example = "false")
  private Boolean isDeleted;

  // 将来的に条件を追加する場合は、ここにフィールドを追加する

}
