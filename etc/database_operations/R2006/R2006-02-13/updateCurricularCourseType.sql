update DEGREE_MODULE set DEGREE_MODULE.TYPE = 'NORMAL_COURSE' where CLASS_NAME = 'net.sourceforge.fenixedu.domain.CurricularCourse' and CURRICULAR_STAGE != 'OLD';
