package net.sourceforge.fenixedu.presentationTier.Action.publicRelationsOffice;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sourceforge.fenixedu.domain.accessControl.AllEmployeesGroup;
import net.sourceforge.fenixedu.domain.accessControl.AllResearchersGroup;
import net.sourceforge.fenixedu.domain.accessControl.AllStudentsGroup;
import net.sourceforge.fenixedu.domain.accessControl.AllTeachersGroup;
import net.sourceforge.fenixedu.domain.accessControl.ExecutionCourseResponsiblesGroup;
import net.sourceforge.fenixedu.domain.accessControl.InternalPersonGroup;
import net.sourceforge.fenixedu.injectionCode.IGroup;
import net.sourceforge.fenixedu.presentationTier.Action.cms.messaging.mailSender.SimpleMailSenderAction;

public class SendEmailInPublicRelationsOffice extends SimpleMailSenderAction {

    @Override
    protected List<IGroup> getPossibleReceivers(HttpServletRequest request) {
	List<IGroup> groups = new ArrayList<IGroup>();
	groups.add(new AllTeachersGroup());
	groups.add(new AllStudentsGroup());
	groups.add(new AllEmployeesGroup());
	groups.add(new AllResearchersGroup());
	groups.add(new ExecutionCourseResponsiblesGroup());
	groups.add(new InternalPersonGroup());
	return groups;
    }
}
