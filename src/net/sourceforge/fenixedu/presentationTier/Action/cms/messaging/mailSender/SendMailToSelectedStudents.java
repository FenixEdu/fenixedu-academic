/**
 * 
 */

package net.sourceforge.fenixedu.presentationTier.Action.cms.messaging.mailSender;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.commons.CollectionUtils;
import net.sourceforge.fenixedu.dataTransferObject.teacher.executionCourse.SearchExecutionCourseAttendsBean;
import net.sourceforge.fenixedu.dataTransferObject.teacher.executionCourse.SearchExecutionCourseAttendsBean.StudentAttendsStateType;
import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.accessControl.FixedSetGroup;
import net.sourceforge.fenixedu.injectionCode.IGroup;
import net.sourceforge.fenixedu.presentationTier.Action.cms.messaging.SendMailForm;
import net.sourceforge.fenixedu.util.WorkingStudentSelectionType;

import org.apache.commons.collections.Transformer;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt">Goncalo Luiz</a> <br/> <br/>
 *         <br/> Created on 15:59:15,23/Mar/2006
 * @version $Id: SendMailToSelectedStudents.java,v 1.1 2006/03/29 17:15:55 gedl
 *          Exp $
 */
public class SendMailToSelectedStudents extends ExecutionCourseSendMail {

    private class AttendsPersonTransformer implements Transformer {
	public Object transform(Object input) {
	    return ((Attends) input).getRegistration().getStudent().getPerson();
	}

    }

    @Override
    protected IGroup[] getAllowedGroups(HttpServletRequest request, IGroup[] selectedGroups) throws FenixFilterException,
	    FenixServiceException {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    protected IGroup[] getGroupsToSend(IUserView userView, SendMailForm form, Map parameters) {

	SearchExecutionCourseAttendsBean attendsBean = getBean(parameters);
	attendsBean.getExecutionCourse().searchAttends(attendsBean);

	IGroup group = new FixedSetGroup(CollectionUtils.collect(attendsBean.getAttendsResult(), new AttendsPersonTransformer()));

	return new IGroup[] { group };

    }

    private SearchExecutionCourseAttendsBean getBean(Map parameters) {
	SearchExecutionCourseAttendsBean attendsBean = new SearchExecutionCourseAttendsBean(rootDomainObject
		.readExecutionCourseByOID(Integer.valueOf((String) parameters.get("objectCode"))));

	String checkedCoursesIds[] = (String[]) parameters.get("coursesIDs");
	if (checkedCoursesIds != null) {
	    Collection<DegreeCurricularPlan> dcps = new HashSet<DegreeCurricularPlan>();
	    for (String dcpID : checkedCoursesIds) {
		dcps.add(rootDomainObject.readDegreeCurricularPlanByOID(Integer.valueOf(dcpID)));
	    }
	    attendsBean.setDegreeCurricularPlans(dcps);
	}
	attendsBean.setDegreeCurricularPlans(Collections.EMPTY_LIST);

	String enrollmentTypes[] = (String[]) parameters.get("enrollmentType");
	if (enrollmentTypes != null) {
	    Collection<StudentAttendsStateType> attendsStateTypes = new HashSet<StudentAttendsStateType>();
	    for (String enrolmentType : enrollmentTypes) {
		attendsStateTypes.add(StudentAttendsStateType.valueOf(enrolmentType));
	    }
	    attendsBean.setAttendsStates(attendsStateTypes);
	} else {
	    attendsBean.setAttendsStates(Collections.EMPTY_LIST);
	}

	String checkedShiftIds[] = (String[]) parameters.get("shiftIDs");
	if (checkedShiftIds != null) {
	    Collection<Shift> shifts = new HashSet<Shift>();
	    for (String shiftID : checkedShiftIds) {
		shifts.add(rootDomainObject.readShiftByOID(Integer.valueOf(shiftID)));
	    }
	    attendsBean.setShifts(shifts);
	} else {
	    attendsBean.setShifts(Collections.EMPTY_LIST);
	}

	String[] wsSelected = (String[]) parameters.get("workingStudentType");
	if (wsSelected != null) {
	    Collection<WorkingStudentSelectionType> wsSelectionTypes = new HashSet<WorkingStudentSelectionType>();
	    for (String ws : wsSelected) {
		wsSelectionTypes.add(WorkingStudentSelectionType.valueOf(ws));
	    }
	    attendsBean.setWorkingStudentTypes(wsSelectionTypes);
	} else {
	    attendsBean.setWorkingStudentTypes(Collections.EMPTY_LIST);
	}

	return attendsBean;
    }

}
