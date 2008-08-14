/*
 * Created on Dec 19, 2003 by jpvl
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher.professorship;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoProfessorship;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.Teacher;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

/**
 * @author jpvl
 */
public class ResponsibleForValidator {
    /**
     * @author jpvl
     */
    public class InvalidCategory extends FenixServiceException {

    }

    /**
     * @author jpvl
     */
    public class MaxResponsibleForExceed extends FenixServiceException {
	private InfoExecutionCourse infoExecutionCourse;

	private List infoResponsiblefors;

	public MaxResponsibleForExceed(InfoExecutionCourse infoExecutionCourse, List infoResponsiblefors) {
	    this.infoResponsiblefors = infoResponsiblefors;
	    this.infoExecutionCourse = infoExecutionCourse;
	}

	/**
	 * @return Returns the infoExecutionCourse.
	 */
	public InfoExecutionCourse getInfoExecutionCourse() {
	    return this.infoExecutionCourse;
	}

	/**
	 * @return Returns the infoResponsiblefors.
	 */
	public List getInfoResponsiblefors() {
	    return this.infoResponsiblefors;
	}
    }

    private static ResponsibleForValidator validator = new ResponsibleForValidator();

    public static ResponsibleForValidator getInstance() {
	return validator;
    }

    private final int MAX_RESPONSIBLEFOR_BY_EXECUTION_COURSE = 3;

    private ResponsibleForValidator() {
    }

    public void validateResponsibleForList(Teacher teacher, ExecutionCourse executionCourse, Professorship responsibleForAdded)
	    throws MaxResponsibleForExceed, InvalidCategory {

	if (!teacher.getCategory().getCanBeExecutionCourseResponsible().booleanValue()) {
	    throw new InvalidCategory();
	}

	List responsibleFors = executionCourse.responsibleFors();

	if ((!responsibleFors.contains(responsibleForAdded))
		&& (responsibleFors.size() >= MAX_RESPONSIBLEFOR_BY_EXECUTION_COURSE)) {
	    List infoResponsibleFors = (List) CollectionUtils.collect(responsibleFors, new Transformer() {

		public Object transform(Object input) {
		    Professorship responsibleFor = (Professorship) input;
		    InfoProfessorship infoResponsibleFor = InfoProfessorship.newInfoFromDomain(responsibleFor);
		    return infoResponsibleFor;
		}
	    });

	    InfoExecutionCourse infoExecutionCourse = InfoExecutionCourse.newInfoFromDomain(executionCourse);
	    throw new MaxResponsibleForExceed(infoExecutionCourse, infoResponsibleFors);
	}
    }
}