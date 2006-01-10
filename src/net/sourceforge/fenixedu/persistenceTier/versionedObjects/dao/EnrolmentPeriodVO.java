package net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.EnrolmentPeriodInClasses;
import net.sourceforge.fenixedu.domain.EnrolmentPeriodInCurricularCourses;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentEnrolmentPeriod;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.VersionedObjectsBase;
import net.sourceforge.fenixedu.util.PeriodState;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;

public class EnrolmentPeriodVO extends VersionedObjectsBase implements IPersistentEnrolmentPeriod {

    public EnrolmentPeriodInCurricularCourses readActualEnrolmentPeriodForDegreeCurricularPlan(
            Integer degreeCurricularPlanId) throws ExcepcaoPersistencia {
		
		Collection<EnrolmentPeriodInCurricularCourses> enrolmentPeriods = readAll(EnrolmentPeriodInCurricularCourses.class);
		DegreeCurricularPlan degreeCurricularPlan = (DegreeCurricularPlan)readByOID(DegreeCurricularPlan.class, degreeCurricularPlanId);
		Date date = new Date();
		
		for (EnrolmentPeriodInCurricularCourses enrolmentPeriod : enrolmentPeriods){
			if (enrolmentPeriod.getDegreeCurricularPlan().getName().equals(degreeCurricularPlan.getName()) &&
				enrolmentPeriod.getDegreeCurricularPlan().getDegree().getSigla().equals(degreeCurricularPlan.getDegree().getSigla()) &&
				enrolmentPeriod.getStartDate().before(date) &&
				enrolmentPeriod.getEndDate().after(date))
				
				return enrolmentPeriod;
		}
		return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorPersistente.OJB.IPersistentEnrolmentPeriod#readNextEnrolmentPeriodForDegreeCurricularPlan(Dominio.DegreeCurricularPlan)
     */
    public EnrolmentPeriodInCurricularCourses readNextEnrolmentPeriodForDegreeCurricularPlan(
            Integer degreeCurricularPlanId) throws ExcepcaoPersistencia {
		
		Collection<EnrolmentPeriodInCurricularCourses> enrolmentPeriods = readAll(EnrolmentPeriodInCurricularCourses.class);
		
		ComparatorChain comparatorChain = new ComparatorChain(new BeanComparator("startDate"), false);
        Collections.sort((List)enrolmentPeriods, comparatorChain);

		DegreeCurricularPlan degreeCurricularPlan = (DegreeCurricularPlan)readByOID(DegreeCurricularPlan.class, degreeCurricularPlanId);
		Date date = new Date();
		
		for (EnrolmentPeriodInCurricularCourses enrolmentPeriod : enrolmentPeriods){
			if (enrolmentPeriod.getDegreeCurricularPlan().getName().equals(degreeCurricularPlan.getName()) &&
				enrolmentPeriod.getDegreeCurricularPlan().getDegree().getSigla().equals(degreeCurricularPlan.getDegree().getSigla()) &&
				enrolmentPeriod.getStartDate().after(date))
				
				return enrolmentPeriod;
		}
		
		return null;

    }



	
	
    public EnrolmentPeriodInClasses readCurrentClassesEnrollmentPeriodForDegreeCurricularPlan(
            Integer degreeCurricularPlanId) throws ExcepcaoPersistencia {
		
		Collection<EnrolmentPeriodInClasses> enrolmentPeriods = readAll(EnrolmentPeriodInClasses.class);
		
		for (EnrolmentPeriodInClasses enrolmentPeriod : enrolmentPeriods){
			if (enrolmentPeriod.getDegreeCurricularPlan().getIdInternal().equals(degreeCurricularPlanId) &&
				enrolmentPeriod.getExecutionPeriod().getState().equals(PeriodState.CURRENT))
				return enrolmentPeriod;
		}
		
		return null;
    }
	
	
	
	
	

    public List readAllEnrollmentPeriodsInCourses() throws ExcepcaoPersistencia {
        return (List)readAll(EnrolmentPeriodInCurricularCourses.class);
    }
}
