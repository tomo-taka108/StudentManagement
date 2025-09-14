CREATE TABLE students (
    id int AUTO_INCREMENT,
    name varchar(50) NOT NULL,
    kana_name varchar(50) NOT NULL,
    nickname varchar(50) NOT NULL,
    email varchar(50) NOT NULL,
    area varchar(50) NOT NULL,
    age int NOT NULL,
    sex varchar(50) NOT NULL,
    remark varchar(255) DEFAULT NULL,
    isDeleted boolean DEFAULT FALSE,
    PRIMARY KEY (id)
  );

CREATE TABLE student_courses (
    id int AUTO_INCREMENT,
    student_id int NOT NULL,
    course_name varchar(50) NOT NULL,
    start_date timestamp DEFAULT NULL,
    end_date timestamp DEFAULT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (student_id) REFERENCES students(id)
  );