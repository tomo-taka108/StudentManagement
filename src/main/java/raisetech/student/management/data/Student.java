package raisetech.student.management.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Student {

  private String id;
  private String name;
  private String kanaName;
  private String nickname;
  private String email;
  private String area;
  private Integer age;
  private String sex;
  private String remark;

  @JsonProperty("isDeleted") //JSONのキー名を固定（Javaの@Getter命名とJSON変換キー名を一致させる）
  private boolean isDeleted;
}
