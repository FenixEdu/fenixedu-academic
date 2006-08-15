/*
 * CriarTurma.java
 *
 * Created on 25 de Outubro de 2002, 18:34
 */

package net.sourceforge.fenixedu.applicationTier.Servico.sop;

/**
 * Servi�o CriarTurma
 * 
 * @author tfc130
 */
import java.util.Set;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoClass;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.SchoolClass;

public class CriarTurma extends Service {

	public Object run(final String className, final Integer curricularYear, final InfoExecutionDegree infoExecutionDegree,
			final InfoExecutionPeriod infoExecutionPeriod) throws ExistingServiceException {
        final ExecutionDegree executionDegree = rootDomainObject.readExecutionDegreeByOID(infoExecutionDegree.getIdInternal());
        final ExecutionPeriod executionPeriod = rootDomainObject.readExecutionPeriodByOID(infoExecutionPeriod.getIdInternal());
        final Set<SchoolClass> classes = executionDegree.findSchoolClassesByExecutionPeriodAndCurricularYear(executionPeriod, curricularYear);

        final DegreeCurricularPlan degreeCurricularPlan = executionDegree.getDegreeCurricularPlan();
        final Degree degree = degreeCurricularPlan.getDegree();
        final String schoolClassName = degree.constructSchoolClassPrefix(curricularYear) + className;

        for (final SchoolClass schoolClass : classes) {
            if (schoolClassName.equalsIgnoreCase(schoolClass.getNome())) {
        	throw new ExistingServiceException("Duplicate Entry: " + className);
            }
        }

        final SchoolClass schoolClass = new SchoolClass(executionDegree, executionPeriod, className, curricularYear);
        return InfoClass.newInfoFromDomain(schoolClass);
    }

}
