CREATE DATABASE IF NOT EXISTS CBA_Code_Check;
USE CBA_Code_Check;

CREATE TABLE IF NOT EXISTS user
(
  id INT NOT NULL AUTO_INCREMENT,
  first_name VARCHAR(32) NOT NULL,
  last_name VARCHAR(32) NOT NULL,
  mobile_phone VARCHAR(8),
  email VARCHAR(128) NOT NULL,
  passwd_hash VARCHAR(64) NOT NULL,
  PRIMARY KEY (id),
  UNIQUE(email) 
);

CREATE TABLE IF NOT EXISTS role
(
  id INT NOT NULL AUTO_INCREMENT,
  name VARCHAR(32) NOT NULL,
  PRIMARY KEY (id),
  UNIQUE(name) 
);

CREATE TABLE IF NOT EXISTS user_role
(
  user_id INT NOT NULL,
  role_id INT NOT NULL,
  PRIMARY KEY (user_id, role_id),
  FOREIGN KEY fk_user_role_user(user_id)
  REFERENCES user(id)
  ON UPDATE CASCADE
  ON DELETE CASCADE,
  FOREIGN KEY fk_user_role_role(role_id)
  REFERENCES role(id)
  ON UPDATE CASCADE
  ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS course
(
  id INT NOT NULL AUTO_INCREMENT,
  name VARCHAR(32) NOT NULL,
  PRIMARY KEY (id),
  UNIQUE(name)
);

CREATE TABLE IF NOT EXISTS user_course
(
  user_id INT NOT NULL,
  course_id INT NOT NULL,
  PRIMARY KEY (user_id, course_id),
  FOREIGN KEY fk_user_course_user(user_id)
  REFERENCES user(id)
  ON UPDATE CASCADE
  ON DELETE CASCADE,
  FOREIGN KEY fk_user_course_course(course_id)
  REFERENCES course(id)
  ON UPDATE CASCADE
  ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS assignment
(
  id INT NOT NULL AUTO_INCREMENT,
  name VARCHAR(32) NOT NULL,
  description TEXT NOT NULL,
  PRIMARY KEY (id),
  UNIQUE(name)
);

CREATE TABLE IF NOT EXISTS assignment_test
(
  id INT NOT NULL AUTO_INCREMENT,
  assignment_id INT NOT NULL,
  input TEXT NOT NULL,
  expected TEXT NOT NULL,
  timeout INT NOT NULL DEFAULT 5000,
  hidden BOOL,
  test_order INT NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY fk_assignment_test_assignment(assignment_id)
  REFERENCES assignment(id)
  ON UPDATE CASCADE
  ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS task
(
  id INT NOT NULL AUTO_INCREMENT,
  user_id INT NOT NULL,
  assignment_id INT NOT NULL,
  begin TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  end TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  max_handins INT NOT NULL DEFAULT -1,
  status VARCHAR(16) NOT NULL DEFAULT "NOT HANDED IN",
  PRIMARY KEY (id),
  UNIQUE(user_id, assignment_id),
  FOREIGN KEY fk_task_user(user_id)
  REFERENCES user(id)
  ON UPDATE CASCADE
  ON DELETE CASCADE,
  FOREIGN KEY fk_task_assignment(assignment_id)
  REFERENCES assignment(id)
  ON UPDATE CASCADE
  ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS report
(
  id INT NOT NULL AUTO_INCREMENT,
  task_id INT NOT NULL,
  status VARCHAR(16) NOT NULL DEFAULT "RUNNING",
  created TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  FOREIGN KEY fk_report_task(task_id)
  REFERENCES task(id)
  ON UPDATE CASCADE
  ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS report_line
(
  id INT NOT NULL AUTO_INCREMENT,
  report_id INT NOT NULL,  
  assignment_test_id INT NOT NULL,
  output TEXT NULL,
  status VARCHAR(16) NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY fk_report_line_report(report_id)
  REFERENCES report(id)
  ON UPDATE CASCADE
  ON DELETE CASCADE,
  FOREIGN KEY fk_report_line_assignment_test(assignment_test_id)
  REFERENCES assignment_test(id)
  ON UPDATE CASCADE
  ON DELETE CASCADE
);