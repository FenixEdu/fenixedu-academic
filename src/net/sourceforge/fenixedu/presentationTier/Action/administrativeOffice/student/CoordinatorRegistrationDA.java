package net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.student;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.presentationTier.Action.masterDegree.coordinator.CoordinatedDegreeInfo;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(module = "coordinator", path = "/registration", scope = "request", parameter = "method")
@Forwards(value = { @Forward(name = "view-registration-curriculum", path = "view-registration-curriculum"),
		@Forward(name = "chooseCycleForViewRegistrationCurriculum", path = "chooseCycleForViewRegistrationCurriculum") })
public class CoordinatorRegistrationDA extends RegistrationDA {

	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		CoordinatedDegreeInfo.setCoordinatorContext(request);
		return super.execute(mapping, actionForm, request, response);
	}

}
