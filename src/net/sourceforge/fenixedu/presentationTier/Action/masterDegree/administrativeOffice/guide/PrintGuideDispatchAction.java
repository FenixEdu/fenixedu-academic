package net.sourceforge.fenixedu.presentationTier.Action.masterDegree.administrativeOffice.guide;

import java.text.DateFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.candidate.ReadCandidateListByPersonAndExecutionDegree;
import net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.candidate.ReadMasterDegreeCandidate;
import net.sourceforge.fenixedu.dataTransferObject.InfoGuide;
import net.sourceforge.fenixedu.dataTransferObject.InfoMasterDegreeCandidate;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.masterDegree.GuideRequester;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.ServiceUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class PrintGuideDispatchAction extends FenixDispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	    throws Exception {

	InfoGuide infoGuide = (InfoGuide) request.getAttribute(PresentationConstants.GUIDE);
	String graduationType = (String) request.getAttribute("graduationType");
	if (graduationType == null) {
	    graduationType = request.getParameter("graduationType");
	}
	request.setAttribute("graduationType", graduationType);

	if (request.getParameter(PresentationConstants.REQUESTER_NUMBER) != null) {
	    Integer numberRequester = new Integer(request.getParameter(PresentationConstants.REQUESTER_NUMBER));
	    request.setAttribute(PresentationConstants.REQUESTER_NUMBER, numberRequester);
	}

	if (infoGuide.getGuideRequester().equals(GuideRequester.CANDIDATE.name())) {
	    // Read The Candidate
	    InfoMasterDegreeCandidate infoMasterDegreeCandidate = null;
	    try {
		if (request.getAttribute(PresentationConstants.REQUESTER_NUMBER) == null) {

		    infoMasterDegreeCandidate = (InfoMasterDegreeCandidate) ReadMasterDegreeCandidate.run(infoGuide.getInfoExecutionDegree(), infoGuide.getInfoPerson());
		} else {
		    Integer number = (Integer) request.getAttribute(PresentationConstants.REQUESTER_NUMBER);

		    infoMasterDegreeCandidate = (InfoMasterDegreeCandidate) ReadCandidateListByPersonAndExecutionDegree.run(infoGuide.getInfoExecutionDegree(), infoGuide.getInfoPerson(), number);
		}
	    } catch (FenixServiceException e) {
		throw new FenixActionException();
	    }
	    request.setAttribute(PresentationConstants.MASTER_DEGREE_CANDIDATE, infoMasterDegreeCandidate);
	}

	Locale locale = new Locale("pt", "PT");
	String formatedDate = "Lisboa, "
		+ DateFormat.getDateInstance(DateFormat.LONG, locale).format(infoGuide.getCreationDate());
	request.setAttribute(PresentationConstants.DATE, formatedDate);
	return mapping.findForward("PrintReady");

    }

    public ActionForward print(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	    throws Exception {
	Integer number = new Integer(request.getParameter("number"));
	Integer year = new Integer(request.getParameter("year"));
	Integer version = new Integer(request.getParameter("version"));
	request.removeAttribute(PresentationConstants.MASTER_DEGREE_CANDIDATE);
	Integer numberRequester = null;
	if (request.getParameter(PresentationConstants.REQUESTER_NUMBER) != null) {
	    numberRequester = new Integer(request.getParameter(PresentationConstants.REQUESTER_NUMBER));
	    request.setAttribute(PresentationConstants.REQUESTER_NUMBER, numberRequester);
	}

	InfoGuide infoGuide = null;
	try {
	    Object args[] = { number, year, version };

	    infoGuide = (InfoGuide) ServiceManagerServiceFactory.executeService("ChooseGuide", args);
	} catch (FenixServiceException e) {
	    throw new FenixActionException();
	}

	if (infoGuide.getGuideRequester().equals(GuideRequester.STUDENT)) {

	    InfoStudent infoStudent = null;
	    List infoStudents = null;

	    Object args2[] = { infoGuide.getInfoPerson() };

	    try {
		infoStudents = (List) ServiceUtils.executeService("ReadStudentsByPerson", args2);

		Iterator it = infoStudents.iterator();
		while (it.hasNext()) {
		    infoStudent = (InfoStudent) it.next();
		    if (infoStudent.getDegreeType().equals(DegreeType.MASTER_DEGREE))
			break;
		}
		request.setAttribute(PresentationConstants.STUDENT, infoStudent.getNumber());
	    } catch (FenixServiceException e) {
		throw new FenixActionException();
	    }

	} else {
	    request.setAttribute(PresentationConstants.STUDENT, numberRequester);
	}
	Locale locale = new Locale("pt", "PT");
	String formatedDate = "Lisboa, "
		+ DateFormat.getDateInstance(DateFormat.LONG, locale).format(infoGuide.getCreationDate());
	request.setAttribute(PresentationConstants.DATE, formatedDate);
	request.setAttribute(PresentationConstants.GUIDE, infoGuide);

	String copies = request.getParameter("copies");
	if (copies != null && copies.equals("2")) {
	    return mapping.findForward("PrintTwoGuides");
	}

	return mapping.findForward("PrintOneGuide");

    }

}