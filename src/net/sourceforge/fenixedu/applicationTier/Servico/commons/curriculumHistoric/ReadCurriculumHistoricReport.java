package net.sourceforge.fenixedu.applicationTier.Servico.commons.curriculumHistoric;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.commons.curriculumHistoric.InfoCurriculumHistoricReport;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.ExecutionYear;

public class ReadCurriculumHistoricReport extends Service {

    public InfoCurriculumHistoricReport run(final Integer curricularCourseOID, final Integer semester, final Integer executionYearOID) {
	final ExecutionYear executionYear = rootDomainObject.readExecutionYearByOID(executionYearOID);
        final ExecutionPeriod executionPeriod = executionYear.readExecutionPeriodForSemester(semester);
        
        final CurricularCourse curricularCourse = (CurricularCourse) rootDomainObject.readDegreeModuleByOID(curricularCourseOID);
	
	return new InfoCurriculumHistoricReport(executionPeriod, curricularCourse);
    }

}
