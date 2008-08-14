package net.sourceforge.fenixedu.presentationTier.Action.cms.messaging.mailSender;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.accessControl.AllStudentsGroup;
import net.sourceforge.fenixedu.domain.accessControl.AllTeachersGroup;
import net.sourceforge.fenixedu.domain.accessControl.DegreeStudentsGroup;
import net.sourceforge.fenixedu.domain.accessControl.DegreeTeachersGroup;
import net.sourceforge.fenixedu.injectionCode.IGroup;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class DegreeMailSenderAction extends SimpleMailSenderAction {

    private static final String fromName = "Coordenação do curso";

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	request.setAttribute("degree", getDegree(request));
	return super.execute(mapping, actionForm, request, response);
    }

    private Degree getDegree(HttpServletRequest request) {
	String degreeId = request.getParameter("degreeId");
	return (degreeId != null) ? RootDomainObject.getInstance().readDegreeByOID(Integer.valueOf(degreeId)) : null;
    }

    @Override
    protected List<IGroup> getPossibleReceivers(HttpServletRequest request) {
	Degree degree = getDegree(request);
	List<IGroup> groups = new ArrayList<IGroup>();
	groups.add(new DegreeTeachersGroup(degree));
	groups.add(new DegreeStudentsGroup(degree));
	groups.add(new AllTeachersGroup());
	groups.add(new AllStudentsGroup());
	return groups;
    }

    @Override
    protected String getFromName(HttpServletRequest request) {
	StringBuffer buffer = new StringBuffer(fromName);
	buffer.append(" ");
	buffer.append(getDegree(request).getName());
	return buffer.toString();
    }
}
