package student.management.student.management;

import java.util.Date;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class StudentCourses {

  private String id;
  private String studentId;
  private String courseName;
  private Date startDate;
  private Date endDate;

}
