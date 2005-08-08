update DEGREE_CURRICULAR_PLAN set CLASS_NAME = "net.sourceforge.fenixedu.domain.DegreeCurricularPlanLET", ACK_OPT_LOCK = ACK_OPT_LOCK + 1 where ID_INTERNAL = 91;

update DEGREE_CURRICULAR_PLAN set CLASS_NAME = "net.sourceforge.fenixedu.domain.DegreeCurricularPlanLEA", ACK_OPT_LOCK = ACK_OPT_LOCK + 1 where ID_INTERNAL = 110;

update DEGREE_CURRICULAR_PLAN set CLASS_NAME_FOR_STUDENT_CURRICULAR_PLANS = "net.sourceforge.fenixedu.domain.StudentCurricularPlanLEM", ACK_OPT_LOCK = ACK_OPT_LOCK + 1 where ID_INTERNAL = 50;