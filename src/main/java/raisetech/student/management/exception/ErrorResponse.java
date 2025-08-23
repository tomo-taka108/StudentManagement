package raisetech.student.management.exception;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data // getter/setter, toString, equals, hashCode を自動生成
@NoArgsConstructor // 引数なしコンストラクタ
@AllArgsConstructor // 全フィールドのコンストラクタ

// エラー時のレスポンスを表すクラス
public class ErrorResponse {

  @Schema(example = "400")
  private int status; // HTTPステータスコード

  @Schema(example = "エラーが発生しました。")
  private String message; // エラーメッセージ

}
