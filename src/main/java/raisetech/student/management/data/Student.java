package raisetech.student.management.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

@Schema(description = "受講生を表すデータモデル")
@Getter
@Setter
@NoArgsConstructor // 引数なしコンストラクタを自動生成
@AllArgsConstructor // 全フィールドを引数に持つコンストラクタを自動生成

public class Student {

  @Schema(description = "受講生ID（自動付与）", example = "1")
  @Pattern(regexp = "^\\d+$", message = "数字のみ入力するようにしてください。")
  private String id;

  @Schema(description = "氏名", example = "佐藤太郎")
  @NotBlank(message = "【name】入力が必要です。")
  private String name;

  @Schema(description = "フリガナ", example = "サトウ　タロウ")
  @NotBlank(message = "【kanaName】入力が必要です。")
  private String kanaName;

  @Schema(description = "ニックネーム", example = "タロちゃん")
  @NotBlank(message = "【nickname】入力が必要です。")
  private String nickname;

  @Schema(description = "メールアドレス", example = "taro.sato@example.com")
  @NotBlank(message = "【email】入力が必要です。")
  @Email(message = "メールアドレスの形式が誤っています。")
  private String email;

  @Schema(description = "居住地域", example = "東京")
  @NotBlank(message = "【area】入力が必要です。")
  private String area;

  @Schema(description = "年齢", example = "18")
  @Range(min = 0, max = 150, message = "【age】0～150までの値を入力してください。")
  private int age;

  @Schema(description = "性別", example = "男性")
  @NotBlank(message = "【sex】入力が必要です。")
  private String sex;

  @Schema(description = "備考", example = "真面目な学生")
  private String remark;

  @Schema(description = "削除フラグ", example = "false")
  @JsonProperty("isDeleted") //JSONのキー名を固定（Javaの@Getter命名とJSON変換キー名を一致させる）
  private boolean isDeleted;
}
