package raisetech.student.management.exception;

// 「受講生が見つからなかった」ことを表す独自例外
public class StudentNotFoundException extends RuntimeException {

  public StudentNotFoundException(String message) {       // コンストラクタでメッセージを受け取る
    super(message);                                     // 親クラス(RuntimeException)に渡す
  }
}
