--
-- Table structure for table `mw_CURRICULAR_COURSE_OUTSIDE_STUDENT_DEGREE`
--

DROP TABLE IF EXISTS mw_CURRICULAR_COURSE_OUTSIDE_STUDENT_DEGREE;
CREATE TABLE mw_CURRICULAR_COURSE_OUTSIDE_STUDENT_DEGREE (
  courseCode varchar(10) NOT NULL default '',
  degreeCode int(20) NOT NULL default '0',
  keyCurricularCourse int(11) NOT NULL default '0',
  idInternal int(11) NOT NULL auto_increment,
  ACK_OPT_LOCK int(11) not null default '1',
  PRIMARY KEY  (idInternal)
) TYPE=InnoDB;

--
-- Dumping data for table `mw_CURRICULAR_COURSE_OUTSIDE_STUDENT_DEGREE`
--

INSERT INTO mw_CURRICULAR_COURSE_OUTSIDE_STUDENT_DEGREE VALUES ('PY',2,2700,1,1),
('UN',2,2733,2,1),
('P5',2,2689,3,1),
('QN',2,2707,4,1),
('SF',2,2717,5,1),
('AZ9',2,3101,6,1),
('A01',2,2762,7,1),
('AF2',2,2847,8,1),
('AZ8',2,3586,9,1),
('N5',2,2678,10,1),
('EX',2,2649,11,1),
('AT6',7,3014,12,1),
('6F',7,3670,13,1),
('ARI',8,2968,14,1),
('AE2',9,3383,15,1),
('AXB',9,3551,16,1),
('AF1',9,2845,17,1),
('IZ',9,3224,18,1),
('6L',9,3193,19,1),
('IV',9,3222,20,1),
('0M',9,3174,21,1),
('TU',9,3290,22,1),
('AJA',10,3247,23,1);

