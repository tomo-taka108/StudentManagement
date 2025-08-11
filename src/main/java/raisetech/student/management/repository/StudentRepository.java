package raisetech.student.management.repository;

import java.util.List;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import raisetech.student.management.data.Student;
import raisetech.student.management.data.StudentCourses;

/**
 * 受講生情報を扱うリポジトリ。 全件検索や単一条件での検索、コース情報の検索が行えるクラスです。
 */
@Mapper
public interface StudentRepository {

  /**
   * 全件検索した受講生情報の一覧
   * 有効（非削除）の受講生情報の一覧
   * 特定idの受講生情報の一覧
   */
  @Select("SELECT * FROM students")
  List<Student> search();

  @Select("SELECT * FROM students WHERE id = #{id}")
  Student searchStudent(String id);

  /**
   * 全件検索した受講生コース情報の一覧
   * 特定idの受講生コース情報の一覧
   */
  @Select("SELECT * FROM student_courses")
  List<StudentCourses> searchStudentCoursesList();

  @Select("SELECT * FROM student_courses WHERE student_id = #{studentId}")
  List<StudentCourses> searchStudentCourses(String studentId);

  /**
   * 受講生情報を登録
   */
  @Insert("INSERT INTO students(name,kana_name,nickname,email,area,age,sex,remark,isDeleted)"
      + "VALUES(#{name},#{kanaName},#{nickname},#{email},#{area},#{age},#{sex},#{remark},false)")
  @Options(useGeneratedKeys = true, keyProperty = "id")
  // DBが自動採番の時に、その決まったIDをJavaオブジェクトの"id"にセットしてくれる
  void registerStudent(Student student);

  /**
   * 受講生コース情報を登録
   */
  @Insert("INSERT INTO student_courses(student_id,course_name,start_date,end_date)"
      + "VALUES(#{studentId},#{courseName},#{startDate},#{endDate})")
  @Options(useGeneratedKeys = true, keyProperty = "id")
  void registerStudentCourses(StudentCourses studentCourses);

  /**
   * 受講生情報を更新
   */
  @Update("UPDATE students SET name = #{name}, kana_name = #{kanaName}, nickname = #{nickname}, "
      + "email = #{email}, area = #{area}, age = #{age}, sex = #{sex}, remark = #{remark}, isDeleted = #{isDeleted} WHERE id = #{id}")
  void updateStudent(Student student);

  /**
   * 受講生コース情報を更新
   */
  @Update("UPDATE student_courses SET course_name = #{courseName} WHERE id = #{id}")
  void updateStudentCourses(StudentCourses studentCourses);

}
