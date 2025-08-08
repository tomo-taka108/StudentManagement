package raisetech.student.management.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

  // 登録フォーム画面表示のURL
  @GetMapping("/newStudent")
  public String newStudent(Model model) {
    StudentDetail studentDetail = new StudentDetail();
    studentDetail.setStudentCourses(Arrays.asList(new StudentCourses()));
    model.addAttribute("studentDetail", studentDetail);
    return "registerStudent";
  }

  // 登録フォームのPOST送信先URL
  @PostMapping("/registerStudent")
  public String registerStudent(@ModelAttribute StudentDetail studentDetail, BindingResult result) {
    if (result.hasErrors()) {
      return "registerStudent";
    }
    service.registerStudent(studentDetail);
    return "redirect:/studentList";
  }

  // 更新フォーム画面表示のURL
  @GetMapping("/editStudent")
  public String editStudent(@RequestParam("id") String id, Model model) {
    Student student = service.searchById(id);
    List<StudentCourses> studentCourses = service.searchCoursesByStudentId(id);

    StudentDetail studentDetail = new StudentDetail();
    studentDetail.setStudent(student);
    studentDetail.setStudentCourses(studentCourses);

    model.addAttribute("studentDetail", studentDetail);
    return "editStudent";
  }

  // 更新フォームのPOST送信先URL
  @PostMapping("/updateStudent")
  public String updateStudent(@ModelAttribute StudentDetail studentDetail, BindingResult result){
    if (result.hasErrors()){
      return "editStudent";
    }
    service.updateStudent(studentDetail);
    return "redirect:/studentList";
  }


}
