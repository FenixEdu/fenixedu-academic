/*
 * Created on Feb 18, 2004
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.gesdis.student;

import net.sourceforge.fenixedu.applicationTier.Servico.framework.ReadDomainObjectService;
import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.dataTransferObject.student.InfoStudentCourseReport;
import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.gesdis.StudentCourseReport;

/**
 * @author <a href="mailto:lesa@mega.ist.utl.pt">Leonor Almeida </a>
 * @author <a href="mailto:shmc@mega.ist.utl.pt">Sergio Montelobo </a>
 *  
 */
public class ReadStudentCourseReport extends ReadDomainObjectService {

    protected InfoObject newInfoFromDomain(DomainObject domainObject) {
        return InfoStudentCourseReport.newInfoFromDomain((StudentCourseReport) domainObject);
    }

	@Override
	protected DomainObject readDomainObject(final Integer idInternal) {
		return rootDomainObject.readStudentCourseReportByOID(idInternal);
	}

}
