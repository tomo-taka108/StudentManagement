package raisetech.student.management.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

@Getter
@Setter
public class Student {

  @Pattern(regexp = "^\\d+$")
  private String id;

  @NotBlank(message = "【name】入力が必要です。")
  private String name;

  @NotBlank(message = "【kanaName】入力が必要です。")
  private String kanaName;

  @NotBlank(message = "【nickname】入力が必要です。")
  private String nickname;

  @NotBlank(message = "【email】入力が必要です。")
  @Email(message = "メールアドレスの形式が誤っています。")
  private String email;

  @NotBlank(message = "【area】入力が必要です。")
  private String area;

  @Range(min = 0, max = 150, message = "【age】0～150までの値を入力してください。")
  private int age;

  @NotBlank(message = "【sex】入力が必要です。")
  private String sex;

  private String remark;

  @JsonProperty("isDeleted") //JSONのキー名を固定（Javaの@Getter命名とJSON変換キー名を一致させる）
  private boolean isDeleted;
}
