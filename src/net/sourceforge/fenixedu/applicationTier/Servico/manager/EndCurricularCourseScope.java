package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourseScopeEditor;
import net.sourceforge.fenixedu.domain.CurricularCourseScope;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

/**
 * @author Fernanda Quitério 28/10/2003
 * 
 */
public class EndCurricularCourseScope extends FenixService {

    @Checked("RolePredicates.MANAGER_OR_OPERATOR_PREDICATE")
    @Service
    public static void run(InfoCurricularCourseScopeEditor newInfoCurricularCourseScope) throws FenixServiceException {

	if (!newInfoCurricularCourseScope.getEndDate().after(newInfoCurricularCourseScope.getBeginDate())) {
	    throw new InvalidArgumentsServiceException();
	}

	CurricularCourseScope oldCurricularCourseScope = rootDomainObject
		.readCurricularCourseScopeByOID(newInfoCurricularCourseScope.getIdInternal());
	if (oldCurricularCourseScope == null) {
	    throw new NonExistingServiceException("message.non.existing.curricular.course.scope", null);
	}

	oldCurricularCourseScope.setEndDate(newInfoCurricularCourseScope.getEndDate());
    }

}