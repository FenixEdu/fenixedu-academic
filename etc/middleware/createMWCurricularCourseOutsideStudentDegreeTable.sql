drop table if exists mw_CURRICULAR_COURSE_OUTSIDE_STUDENT_DEGREE;
create table mw_CURRICULAR_COURSE_OUTSIDE_STUDENT_DEGREE(
  courseCode varchar(10) NOT NULL default '',
  degreeCode integer(20) NOT NULL default 0,
  keyCurricularCourse integer(11) NOT NULL default 0,
  idInternal integer(11) not null auto_increment,
  primary key (idInternal)
)type= InnoDB;
