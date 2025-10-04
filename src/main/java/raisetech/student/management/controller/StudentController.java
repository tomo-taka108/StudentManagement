package raisetech.student.management.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import raisetech.student.management.data.StudentSearchCriteria;
import raisetech.student.management.domain.StudentDetail;
import raisetech.student.management.exception.ErrorResponse;
import raisetech.student.management.service.StudentService;

/**
 * 受講生の検索や登録、更新などを行うREST APIとして実行されるControllerです。
 */
@Validated
@RestController
public class StudentController {

  private StudentService service;

  @Autowired
  public StudentController(StudentService service) {
    this.service = service;
  }


  /**
   * 受講生詳細の一覧検索です。 全件検索を行うので、条件指定は行いません。
   *
   * @return 受講生詳細一覧（全件）
   */
  @Operation(
      summary = "受講生一覧の検索",
      description = "受講生詳細の一覧を検索します。全件検索を行うので、条件指定は行いません。",
      responses = {
          @ApiResponse(
              content = @Content(
                  mediaType = "application/json",
                  array = @ArraySchema(schema = @Schema(implementation = StudentDetail.class))
              )
          )}
  )
  @GetMapping("/studentList")
  public List<StudentDetail> getStudentList() {
    return service.searchStudentList();
  }


  /**
   * 受講生詳細の検索です。 IDに紐づく任意の受講生の情報を取得します。
   *
   * @param id 受講生ID
   * @return 受講生【単一】
   */
  @Operation(
      summary = "受講生の検索",
      description = "パスで指定されたIDに該当する受講生詳細を検索します。IDに紐づく受講生が存在しない場合はエラーメッセージを返します。",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "検索成功",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = StudentDetail.class)
              )),
          @ApiResponse(
              responseCode = "400",
              description = "IDの入力形式が誤っていた際のバリデーションエラー",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = ErrorResponse.class)
              )),
          @ApiResponse(
              responseCode = "404",
              description = "指定されたIDの受講生が存在しない場合のエラー",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = ErrorResponse.class)
              ))
      }
  )
  @GetMapping("/student/{id}")
  public StudentDetail getStudent(
      @PathVariable
      @NotBlank(message = "IDは必須です。")
      @Pattern(regexp = "^\\d+$", message = "IDは半角数字で入力してください。")
      String id) {
    return service.searchStudent(id);
  }


  /**
   * 受講生詳細の一覧検索（検索条件を指定）を行います。
   *
   * @param criteria 検索条件
   * @return 検索条件に一致する受講生詳細一覧
   */
  @Operation(
      summary = "条件指定での受講生一覧の検索",
      description = "検索条件に一致する受講生詳細の一覧を検索します。検索条件は任意に複数組み合わせ可能です。",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "検索成功",
              content = @Content(
                  mediaType = "application/json",
                  array = @ArraySchema(schema = @Schema(implementation = StudentDetail.class))
              )),
          @ApiResponse(
              responseCode = "400",
              description = "検索条件の入力内容のバリデーションエラー",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = ErrorResponse.class)
              ))
      }
  )
  @PostMapping("/studentList/Criteria")
  public ResponseEntity<List<StudentDetail>> getStudentListWithCriteria(
      @RequestBody @Valid StudentSearchCriteria criteria) {
    List<StudentDetail> responseStudentDetails = service.searchWithCriteria(criteria);
    return ResponseEntity.ok(responseStudentDetails);
  }


  /**
   * 受講生詳細の登録を行います。
   *
   * @param studentDetail 受講生詳細
   * @return 実行結果
   */
  @Operation(
      summary = "受講生一覧の検索（検索条件に一致）",
      description = "受講生情報の新規登録を行います。登録内容に不備があればエラーメッセージを返します",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "登録成功",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = StudentDetail.class)
              )),
          @ApiResponse(
              responseCode = "400",
              description = "入力内容のバリデーションエラー",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = ErrorResponse.class)
              ))
      }
  )
  @PostMapping("/registerStudent")
  public ResponseEntity<StudentDetail> registerStudent(
      @RequestBody @Valid StudentDetail studentDetail) {
    StudentDetail responseStudentDetail = service.registerStudent(studentDetail);
    return ResponseEntity.ok(responseStudentDetail);
  }


  /**
   * 受講生詳細の更新を行います。キャンセルフラグの更新もここで行います（論理削除）。
   *
   * @param studentDetail 受講生詳細
   * @return 実行結果
   */
  @Operation(
      summary = "受講生の更新",
      description = "受講生情報の更新を行います。更新内容に不備があればエラーメッセージを返します。",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "更新成功",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = StudentDetail.class)
              )),
          @ApiResponse(
              responseCode = "400",
              description = "入力内容のバリデーションエラー",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = ErrorResponse.class)
              ))
      }
  )
  @PutMapping("/updateStudent")
  public ResponseEntity<String> updateStudent(
      @RequestBody @Valid StudentDetail studentDetail) {
    service.updateStudent(studentDetail);
    return ResponseEntity.ok("更新処理が成功しました。");
  }

}
