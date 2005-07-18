/*
 * Created on Feb 18, 2004
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.student.delegate;

import net.sourceforge.fenixedu.applicationTier.Servico.framework.EditDomainObjectService;
import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.dataTransferObject.student.InfoStudentCourseReport;
import net.sourceforge.fenixedu.domain.Country;
import net.sourceforge.fenixedu.domain.ICurricularCourse;
import net.sourceforge.fenixedu.domain.IDomainObject;
import net.sourceforge.fenixedu.domain.gesdis.IStudentCourseReport;
import net.sourceforge.fenixedu.domain.gesdis.StudentCourseReport;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;

/**
 * @author <a href="mailto:lesa@mega.ist.utl.pt">Leonor Almeida </a>
 * @author <a href="mailto:shmc@mega.ist.utl.pt">Sergio Montelobo </a>
 * 
 */
public class EditStudentCourseReport extends EditDomainObjectService {

    protected IPersistentObject getIPersistentObject(ISuportePersistente sp) {
        return sp.getIPersistentStudentCourseReport();
    }

    @Override
    protected void copyInformationFromIntoToDomain(ISuportePersistente sp, InfoObject infoObject,
            IDomainObject domainObject) throws ExcepcaoPersistencia {
        InfoStudentCourseReport infoStudentCourseReport = (InfoStudentCourseReport) infoObject;
        IStudentCourseReport studentCourseReport = (StudentCourseReport) domainObject;
        if (infoStudentCourseReport.getInfoCurricularCourse() != null) {
            ICurricularCourse curricularCourse = (ICurricularCourse) sp.getIPersistentCurricularCourse()
                    .readByOID(Country.class,
                            infoStudentCourseReport.getInfoCurricularCourse().getIdInternal());
            studentCourseReport.setCurricularCourse(curricularCourse);
            studentCourseReport.setKeyCurricularCourse(curricularCourse.getIdInternal());
        }
        studentCourseReport.setLastModificationDate(infoStudentCourseReport.getLastModificationDate());
        studentCourseReport.setStrongPoints(infoStudentCourseReport.getStrongPoints());
        studentCourseReport.setStudentReport(infoStudentCourseReport.getStudentReport());
        studentCourseReport.setWeakPoints(infoStudentCourseReport.getWeakPoints());
    }

    @Override
    protected IDomainObject createNewDomainObject(InfoObject infoObject) {
        return new StudentCourseReport();
    }

    @Override
    protected Class getDomainObjectClass() {
        return StudentCourseReport.class;
    }

}
