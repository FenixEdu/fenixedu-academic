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
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.SchoolClass;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class CriarTurma extends Service {

    public Object run(final InfoClass infoClass) throws ExcepcaoPersistencia, ExistingServiceException {
        final ExecutionDegree executionDegree = rootDomainObject.readExecutionDegreeByOID(infoClass.getInfoExecutionDegree().getIdInternal());
        final ExecutionPeriod executionPeriod = rootDomainObject.readExecutionPeriodByOID(infoClass.getInfoExecutionPeriod().getIdInternal());
        final Set<SchoolClass> classes = executionDegree.findSchoolClassesByExecutionPeriodAndCurricularYear(executionPeriod, infoClass.getAnoCurricular());

        final Integer curricularYear = infoClass.getAnoCurricular();
        final DegreeCurricularPlan degreeCurricularPlan = executionDegree.getDegreeCurricularPlan();
        final Degree degree = degreeCurricularPlan.getDegree();
        final String schoolClassName = degree.constructSchoolClassPrefix(curricularYear) + infoClass.getNome();

        for (final SchoolClass schoolClass : classes) {
            if (schoolClassName.equalsIgnoreCase(schoolClass.getNome())) {
        	throw new ExistingServiceException("Duplicate Entry: " + infoClass.getNome());
            }
        }

        final SchoolClass schoolClass = new SchoolClass(executionDegree, executionPeriod, infoClass.getNome(), curricularYear);
        return InfoClass.newInfoFromDomain(schoolClass);
    }

}
