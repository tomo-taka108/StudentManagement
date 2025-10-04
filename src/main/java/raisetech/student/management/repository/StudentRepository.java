package raisetech.student.management.repository;

import java.util.List;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import raisetech.student.management.data.CourseStatus;
import raisetech.student.management.data.Student;
import raisetech.student.management.data.StudentCourse;
import raisetech.student.management.data.StudentSearchCriteria;
import raisetech.student.management.domain.StudentDetail;

/**
 * 受講生テーブルと受講生コース情報テーブルに紐づくRepositoryです。
 */
@Mapper
public interface StudentRepository {

  /**
   * 受講生の全件検索を行います。
   *
   * @return 受講生一覧（全件）
   */
  List<Student> search();

  /**
   * IDに紐づく受講生の検索を行います。
   *
   * @param id 受講生ID
   * @return 受講生
   */
  Student searchStudent(String id);

  /**
   * 受講生コース情報の全件検索を行います。
   *
   * @return 受講生のコース情報（全件）
   */
  List<StudentCourse> searchStudentCourseList();

  /**
   * 受講生IDに紐づく受講生コース情報の検索を行います。
   *
   * @param studentId 受講生ID
   * @return 受講生IDに紐づく受講生コース情報
   */
  List<StudentCourse> searchStudentCourse(String studentId);

  /**
   * コース申込状況の全件検索を行います。
   *
   * @return コース申込状況（全件）
   */
  List<CourseStatus> searchCourseStatusList();

  /**
   * 受講生IDに紐づくコース申込状況の検索を行います。
   *
   * @param studentId 受講生ID
   * @return 受講生IDに紐づくコース申込状況
   */
  List<CourseStatus> searchCourseStatus(String studentId);

  /**
   * 検索条件を指定して受講生の検索を行います。
   *
   * @param criteria 検索条件
   * @return 検索条件に一致する受講生一覧
   */
  List<Student> searchWithCriteria(StudentSearchCriteria criteria);


  /**
   * 受講生を新規登録します。* IDに関しては自動採番を行う。
   *
   * @param student 受講生
   */
  void registerStudent(Student student);

  /**
   * 受講生コース情報を新規登録します。IDに関しては自動採番を行う。
   *
   * @param studentCourse 受講生コース情報
   */
  void registerStudentCourse(StudentCourse studentCourse);

  /**
   * コース申込状況を新規登録します。IDに関しては自動採番を行う。
   *
   * @param courseStatus コース申込状況
   */
  void registerCourseStatus(CourseStatus courseStatus);


  /**
   * 受講生を更新します。
   *
   * @param student 受講生
   */
  void updateStudent(Student student);

  /**
   * 受講生コース情報のコース名を更新します。
   *
   * @param studentCourse 受講生コース情報
   */
  void updateStudentCourse(StudentCourse studentCourse);

  /**
   * コース申込状況を更新します。
   *
   * @param courseStatus コース申込状況
   */
  void updateCourseStatus(CourseStatus courseStatus);

}
