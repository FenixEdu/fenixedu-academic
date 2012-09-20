/*
 * Created on Oct 25, 2004
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.publico;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.dto.SchoolClassDTO;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.SchoolClass;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import pt.ist.fenixWebFramework.services.Service;

/**
 * @author João Mota
 * 
 */
public class ReadSchoolClassByNameInCurrentExecutionPeriod extends FenixService {

    @Service
    public static SchoolClassDTO run(final String schoolClassName) throws FenixServiceException {
	final ExecutionSemester executionSemester = ExecutionSemester.readActualExecutionSemester();
	SchoolClass schoolClass = (SchoolClass) CollectionUtils.find(executionSemester.getSchoolClasses(), new Predicate() {

	    public boolean evaluate(Object arg0) {
		SchoolClass schoolClass = (SchoolClass) arg0;
		return schoolClass.getNome().equalsIgnoreCase(schoolClassName);
	    }
	});
	if (schoolClass != null) {
	    DegreeCurricularPlan degreeCurricularPlan = schoolClass.getExecutionDegree().getDegreeCurricularPlan();
	    Degree degree = degreeCurricularPlan.getDegree();
	    SchoolClassDTO schoolClassDTO = new SchoolClassDTO();
	    schoolClassDTO.setExecutionPeriodId(executionSemester.getIdInternal());
	    schoolClassDTO.setSchoolClassId(schoolClass.getIdInternal());
	    schoolClassDTO.setSchoolClassName(schoolClass.getNome());
	    schoolClassDTO.setDegreeCurricularPlanId(degreeCurricularPlan.getIdInternal());
	    schoolClassDTO.setDegreeCurricularPlanName(degreeCurricularPlan.getName());
	    schoolClassDTO.setDegreeId(degree.getIdInternal());
	    schoolClassDTO.setDegreeInitials(degree.getSigla());
	    return schoolClassDTO;
	}
	return null;

    }

}