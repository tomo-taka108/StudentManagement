package student.management.student.management;

import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class Application {

  Map<String, String> studentMap = new HashMap<String, String>();

  // 学生情報の初期登録データ
  public Application() {
    studentMap.put("田中一郎", "20");
    studentMap.put("山田花子", "24");
    studentMap.put("鈴木次郎", "27");
    studentMap.put("佐藤三郎", "30");
  }

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }

  // 学生情報を取得
  @GetMapping("/studentInfoMap")
  public String getStudentInfoMap() {
    StringBuilder sb = new StringBuilder();
    for (String key : studentMap.keySet()) {
      String age = studentMap.get(key);
      sb.append(key).append(" ").append(age).append("歳\n");
    }
    return sb.toString();
  }

  // 学生情報を追加登録
  @PostMapping("/studentInfoMap")
  public void setStudentInfoMap(String name,String age) {
    if (!studentMap.containsKey(name)) {
      studentMap.put(name, age);
    }
  }

}
