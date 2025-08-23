package raisetech.student.management;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@OpenAPIDefinition(info = @Info(
    title = "受講生管理システム",
    description = "受講生情報を管理するためのシステムです。各受講生の個人情報と受講しているコース情報について、閲覧・新規登録・更新・削除を行います。"
))
@SpringBootApplication //SpringBootアプリの起動（SpringBootが使える）
public class Application {

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }

}
