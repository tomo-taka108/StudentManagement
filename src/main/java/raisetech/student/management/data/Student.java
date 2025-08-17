package raisetech.student.management.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Student {

  private String id;

  @NotBlank(message = "名前は必須です") // Validation（Controller および StudentDetail で@Validを付けて有効化する）
  private String name;

  private String kanaName;

  private String nickname;

  @Email(message = "メール形式が正しくありません") // Validation（Controller および StudentDetail で@Validを付けて有効化する）
  private String email;

  private String area;

  @Min(value=12, message = "年齢は12歳以上にしてください") // Validation（Controller および StudentDetail で@Validを付けて有効化する）
  @Max(value=120, message = "年齢は120歳以下にしてください") // Validation（Controller および StudentDetail で@Validを付けて有効化する）
  private Integer age;

  private String sex;

  private String remark;

  @JsonProperty("isDeleted") //JSONのキー名を固定（Javaの@Getter命名とJSON変換キー名を一致させる）
  private boolean isDeleted;
}
