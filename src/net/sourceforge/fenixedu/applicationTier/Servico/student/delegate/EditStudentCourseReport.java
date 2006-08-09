/*
 * Created on Feb 18, 2004
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.student.delegate;

import net.sourceforge.fenixedu.applicationTier.Servico.framework.EditDomainObjectService;
import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.dataTransferObject.student.InfoStudentCourseReport;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.gesdis.StudentCourseReport;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author <a href="mailto:lesa@mega.ist.utl.pt">Leonor Almeida </a>
 * @author <a href="mailto:shmc@mega.ist.utl.pt">Sergio Montelobo </a>
 * 
 */
public class EditStudentCourseReport extends EditDomainObjectService {

    @Override
    protected void copyInformationFromInfoToDomain(InfoObject infoObject, DomainObject domainObject)
            throws ExcepcaoPersistencia {
        InfoStudentCourseReport infoStudentCourseReport = (InfoStudentCourseReport) infoObject;
        StudentCourseReport studentCourseReport = (StudentCourseReport) domainObject;
        if (infoStudentCourseReport.getInfoCurricularCourse() != null) {
            CurricularCourse curricularCourse = (CurricularCourse) rootDomainObject.readDegreeModuleByOID(infoStudentCourseReport.getInfoCurricularCourse().getIdInternal());
            studentCourseReport.setCurricularCourse(curricularCourse);
        }
        studentCourseReport.setLastModificationDate(infoStudentCourseReport.getLastModificationDate());
        studentCourseReport.setStrongPoints(infoStudentCourseReport.getStrongPoints());
        studentCourseReport.setStudentReport(infoStudentCourseReport.getStudentReport());
        studentCourseReport.setWeakPoints(infoStudentCourseReport.getWeakPoints());
    }

    @Override
    protected DomainObject createNewDomainObject(InfoObject infoObject) {
        return new StudentCourseReport();
    }

	@Override
	protected DomainObject readDomainObject(final Integer idInternal) {
		return rootDomainObject.readStudentCourseReportByOID(idInternal);
	}

}
