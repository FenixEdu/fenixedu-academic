/*
 * Created on Feb 18, 2004
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.gesdis.student;

import net.sourceforge.fenixedu.applicationTier.Servico.framework.EditDomainObjectService;
import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.dataTransferObject.student.InfoStudentCourseReport;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.DomainFactory;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.gesdis.StudentCourseReport;
import net.sourceforge.fenixedu.domain.gesdis.StudentCourseReport;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCurricularCourse;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;

/**
 * @author <a href="mailto:lesa@mega.ist.utl.pt">Leonor Almeida </a>
 * @author <a href="mailto:shmc@mega.ist.utl.pt">Sergio Montelobo </a>
 * 
 */
public class EditStudentCourseReport extends EditDomainObjectService {

    @Override
	protected void copyInformationFromInfoToDomain(ISuportePersistente sp, InfoObject infoObject, DomainObject domainObject) throws ExcepcaoPersistencia {
        InfoStudentCourseReport infoStudentCourseReport = (InfoStudentCourseReport) infoObject;
        StudentCourseReport studentCourseReport = (StudentCourseReport) domainObject;

        IPersistentCurricularCourse persistentCurricularCourse = sp.getIPersistentCurricularCourse();
        CurricularCourse curricularCourse = (CurricularCourse) persistentCurricularCourse.readByOID(
                CurricularCourse.class, infoStudentCourseReport.getInfoCurricularCourse()
                        .getIdInternal());
        studentCourseReport.setCurricularCourse(curricularCourse);

        studentCourseReport.setLastModificationDate(infoStudentCourseReport.getLastModificationDate());
        studentCourseReport.setStrongPoints(infoStudentCourseReport.getStrongPoints());
        studentCourseReport.setStudentReport(infoStudentCourseReport.getStudentReport());
        studentCourseReport.setWeakPoints(infoStudentCourseReport.getWeakPoints());

    }

	@Override
    protected DomainObject createNewDomainObject(InfoObject infoObject) {
        return DomainFactory.makeStudentCourseReport();
    }

	@Override
    protected Class getDomainObjectClass() {
        return StudentCourseReport.class;
    }

    @Override
    protected IPersistentObject getIPersistentObject(ISuportePersistente sp) {
        return sp.getIPersistentStudentCourseReport();
    }

}
