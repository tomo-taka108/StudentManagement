package raisetech.student.management.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import raisetech.student.management.controller.converter.StudentConverter;
import raisetech.student.management.data.Student;
import raisetech.student.management.data.StudentCourses;
import raisetech.student.management.domain.StudentDetail;
import raisetech.student.management.service.StudentService;

@Controller
public class StudentController {

  private StudentService service;
  private StudentConverter converter;

  @Autowired
  public StudentController(StudentService service, StudentConverter converter) {
    this.service = service;
    this.converter = converter;
  }

  @GetMapping("/studentList")
  public String getStudentList(Model model) {
    List<Student> students = service.searchStudentList();
    List<StudentCourses> studentCourses = service.searchStudentCoursesList();

    model.addAttribute("studentList", converter.convertstudentdetails(students, studentCourses));
    return "studentList";
  }

  @GetMapping("/studentCourseList")
  public String getCourseList(Model model) {
    List<StudentCourses> studentCourses = service.searchStudentCoursesList();
    model.addAttribute("studentCourseList", studentCourses);
    return "studentCourseList";
  }

  @GetMapping("/newStudent") // 登録フォーム画面表示のURL
  public String newStudent(Model model) {
    model.addAttribute("studentDetail", new StudentDetail());
    return "registerStudent";
  }

  @PostMapping("/registerStudent") // 登録フォームのPOST送信先URL
  public String registerStudent(@ModelAttribute StudentDetail studentDetail, BindingResult result) {
    if (result.hasErrors()) {
      return "registerStudent";
    }

    // 課題28①　新規受講生情報を登録する処理を実装する。→登録された受講生情報がstudentListとして画面表示されるところまで確認する。
    // 課題28②　コース情報も一緒に登録できるように実装する。コースは単体でよい。→同様に画面表示まで確認する。

    // 学生情報を登録
    service.registerStudent(studentDetail);
    return "redirect:/studentList";
  }


}
