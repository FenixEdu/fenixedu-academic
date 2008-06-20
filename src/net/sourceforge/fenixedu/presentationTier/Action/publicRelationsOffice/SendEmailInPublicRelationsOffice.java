package net.sourceforge.fenixedu.presentationTier.Action.publicRelationsOffice;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sourceforge.fenixedu.domain.accessControl.AllEmployeesByCampus;
import net.sourceforge.fenixedu.domain.accessControl.AllEmployeesGroup;
import net.sourceforge.fenixedu.domain.accessControl.AllResearchersGroup;
import net.sourceforge.fenixedu.domain.accessControl.AllStudentsByCampus;
import net.sourceforge.fenixedu.domain.accessControl.AllStudentsGroup;
import net.sourceforge.fenixedu.domain.accessControl.AllTeachersByCampus;
import net.sourceforge.fenixedu.domain.accessControl.AllTeachersGroup;
import net.sourceforge.fenixedu.domain.accessControl.ExecutionCourseResponsiblesGroup;
import net.sourceforge.fenixedu.domain.accessControl.InternalPersonGroup;
import net.sourceforge.fenixedu.domain.accessControl.groups.StudentsFromDegreeTypeGroup;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.space.Campus;
import net.sourceforge.fenixedu.domain.space.Space;
import net.sourceforge.fenixedu.injectionCode.IGroup;
import net.sourceforge.fenixedu.presentationTier.Action.cms.messaging.mailSender.SimpleMailSenderAction;
import net.sourceforge.fenixedu.presentationTier.struts.annotations.Forward;
import net.sourceforge.fenixedu.presentationTier.struts.annotations.Forwards;
import net.sourceforge.fenixedu.presentationTier.struts.annotations.Mapping;

@Mapping(path = "/sendEmail", module = "publicRelations")
@Forwards( {
	@Forward(name = "compose-mail", path = "publicRelationsOffice-sendMail"),
	@Forward(name = "success", path = "publicRelationsOffice-sendMail"),
	@Forward(name = "problem", path = "publicRelationsOffice-sendMail") })
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
	for ( Campus campus : Space.getAllActiveCampus()) {
	    groups.add(new AllStudentsByCampus(campus));
	    groups.add(new AllTeachersByCampus(campus));
	    groups.add(new AllEmployeesByCampus(campus));
	    
	}
	
	groups.add(new StudentsFromDegreeTypeGroup(DegreeType.BOLONHA_ADVANCED_FORMATION_DIPLOMA));
	groups.add(new StudentsFromDegreeTypeGroup(DegreeType.BOLONHA_DEGREE));
	groups.add(new StudentsFromDegreeTypeGroup(DegreeType.BOLONHA_INTEGRATED_MASTER_DEGREE));
	groups.add(new StudentsFromDegreeTypeGroup(DegreeType.BOLONHA_MASTER_DEGREE));
	groups.add(new StudentsFromDegreeTypeGroup(DegreeType.BOLONHA_PHD_PROGRAM));
	groups.add(new StudentsFromDegreeTypeGroup(DegreeType.BOLONHA_SPECIALIZATION_DEGREE));
	
	return groups;
    }
}
