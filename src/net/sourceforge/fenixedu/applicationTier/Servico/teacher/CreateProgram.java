package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.CurricularCourse;

public class CreateProgram extends Service {

    public void run(final Integer executionCourseID, final CurricularCourse curricularCourse, final String program, final String programEn) {
    	curricularCourse.insertCurriculum(program, programEn, "", "", "", "");
   }

}