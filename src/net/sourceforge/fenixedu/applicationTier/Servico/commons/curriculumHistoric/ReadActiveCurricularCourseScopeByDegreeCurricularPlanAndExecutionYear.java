/*
 * Created on Oct 7, 2004
 */
package net.sourceforge.fenixedu.applicationTier.Servico.commons.curriculumHistoric;

import java.util.SortedSet;
import java.util.TreeSet;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.DegreeModuleScope;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;

/**
 * @author nmgo
 * @author lmre
 */
public class ReadActiveCurricularCourseScopeByDegreeCurricularPlanAndExecutionYear extends Service {

    public SortedSet<DegreeModuleScope> run(Integer degreeCurricularPlanID, Integer executioYearID)
	    throws FenixServiceException, ExcepcaoPersistencia {
	final DegreeCurricularPlan degreeCurricularPlan = rootDomainObject
		.readDegreeCurricularPlanByOID(degreeCurricularPlanID);
	final ExecutionYear executionYear = rootDomainObject.readExecutionYearByOID(executioYearID);

	final ComparatorChain comparator = new ComparatorChain();
	comparator.addComparator(new BeanComparator("curricularYear"));
	comparator.addComparator(new BeanComparator("curricularSemester"));
	comparator.addComparator(new BeanComparator("curricularCourse.idInternal"));
	comparator.addComparator(new BeanComparator("branch"));

	final SortedSet<DegreeModuleScope> scopes = new TreeSet<DegreeModuleScope>(comparator);

	for (DegreeModuleScope degreeModuleScope : degreeCurricularPlan.getDegreeModuleScopes()) {
	    if (degreeModuleScope.isActiveForExecutionYear(executionYear)) {
		scopes.add(degreeModuleScope);
	    }
	}

	return scopes;
    }

}
