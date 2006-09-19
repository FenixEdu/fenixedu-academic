/*
 * Created on Oct 7, 2004
 */
package net.sourceforge.fenixedu.applicationTier.Servico.commons.curriculumHistoric;

import java.util.Iterator;
import java.util.SortedSet;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.DegreeModuleScope;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author nmgo
 * @author lmre
 */
public class ReadActiveCurricularCourseScopeByDegreeCurricularPlanAndExecutionYear extends Service {

    public SortedSet<DegreeModuleScope> run(Integer degreeCurricularPlanID, Integer executioYearID)
            throws FenixServiceException, ExcepcaoPersistencia {
        final DegreeCurricularPlan degreeCurricularPlan = rootDomainObject.readDegreeCurricularPlanByOID(degreeCurricularPlanID);
        final ExecutionYear executionYear = rootDomainObject.readExecutionYearByOID(executioYearID);
        final SortedSet<DegreeModuleScope> degreeModuleScopes = degreeCurricularPlan.getDegreeModuleScopes();
        for (final Iterator<DegreeModuleScope> degreeModuleScopeIterator = degreeModuleScopes.iterator();
                degreeModuleScopeIterator.hasNext(); ) {
            final DegreeModuleScope degreeModuleScope = degreeModuleScopeIterator.next();
            if (!degreeModuleScope.isActiveForExecutionYear(executionYear)) {
                degreeModuleScopeIterator.remove();
            }
        }
        return degreeModuleScopes;
    }

}
