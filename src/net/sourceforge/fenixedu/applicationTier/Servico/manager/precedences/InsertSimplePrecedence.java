/*
 * Created on 30/Jul/2004
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager.precedences;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.precedences.Precedence;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

/**
 * @author Tânia Pousão
 * 
 */
public class InsertSimplePrecedence extends FenixService {

    @Checked("RolePredicates.MANAGER_OR_OPERATOR_PREDICATE")
    @Service
    public static void run(String className, Integer curricularCourseToAddPrecedenceID, Integer precedentCurricularCourseID,
	    Integer number) throws FenixServiceException {
	CurricularCourse curricularCourseToAddPrecedence = (CurricularCourse) rootDomainObject
		.readDegreeModuleByOID(curricularCourseToAddPrecedenceID);
	if (curricularCourseToAddPrecedence == null) {
	    throw new FenixServiceException("curricularCourseToAddPrecedence.NULL");
	}

	CurricularCourse precedentCurricularCourse = null;
	if (precedentCurricularCourseID != null) {
	    precedentCurricularCourse = (CurricularCourse) rootDomainObject.readDegreeModuleByOID(precedentCurricularCourseID);
	    if (precedentCurricularCourse == null) {
		throw new FenixServiceException("precedentCurricularCourse.NULL");
	    }
	}

	new Precedence(curricularCourseToAddPrecedence, className, precedentCurricularCourse, number);
    }

}