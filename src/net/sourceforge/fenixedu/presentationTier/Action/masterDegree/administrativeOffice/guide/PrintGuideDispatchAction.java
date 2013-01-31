package net.sourceforge.fenixedu.presentationTier.Action.masterDegree.administrativeOffice.guide;

import java.text.DateFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoGuide;
import net.sourceforge.fenixedu.dataTransferObject.InfoGuideWithPersonAndExecutionDegreeAndContributor;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;
import net.sourceforge.fenixedu.domain.Guide;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
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

		Locale locale = new Locale("pt", "PT");
		String formatedDate =
				"Lisboa, " + DateFormat.getDateInstance(DateFormat.LONG, locale).format(infoGuide.getCreationDate());
		request.setAttribute(PresentationConstants.DATE, formatedDate);
		return mapping.findForward("PrintReady");

	}

	public ActionForward printByNumberAndYear(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {

		final InfoGuide infoGuide =
				InfoGuideWithPersonAndExecutionDegreeAndContributor.newInfoFromDomain(Guide.readLastVersionByNumberAndYear(
						getIntegerFromRequest(request, "number"), getIntegerFromRequest(request, "year")));

		request.setAttribute(PresentationConstants.GUIDE, infoGuide);

		final Locale locale = new Locale("pt", "PT");
		final String formatedDate =
				"Lisboa, " + DateFormat.getDateInstance(DateFormat.LONG, locale).format(infoGuide.getCreationDate());
		request.setAttribute(PresentationConstants.DATE, formatedDate);

		return mapping.findForward("PrintTwoGuides");

	}

	public ActionForward print(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Integer number = new Integer(request.getParameter("number"));
		Integer year = new Integer(request.getParameter("year"));
		Integer version = new Integer(request.getParameter("version"));

		InfoGuide infoGuide = null;
		try {
			Object args[] = { number, year, version };

			infoGuide = (InfoGuide) ServiceManagerServiceFactory.executeService("ChooseGuide", args);
		} catch (FenixServiceException e) {
			throw new FenixActionException();
		}

		InfoStudent infoStudent = null;
		List infoStudents = null;

		Object args2[] = { infoGuide.getInfoPerson() };

		try {
			infoStudents = (List) ServiceUtils.executeService("ReadStudentsByPerson", args2);

			Iterator it = infoStudents.iterator();
			while (it.hasNext()) {
				infoStudent = (InfoStudent) it.next();
				if (infoStudent.getDegreeType().equals(DegreeType.MASTER_DEGREE)) {
					break;
				}
			}
			request.setAttribute(PresentationConstants.STUDENT, infoStudent.getNumber());
		} catch (FenixServiceException e) {
			throw new FenixActionException();
		}

		Locale locale = new Locale("pt", "PT");
		String formatedDate =
				"Lisboa, " + DateFormat.getDateInstance(DateFormat.LONG, locale).format(infoGuide.getCreationDate());
		request.setAttribute(PresentationConstants.DATE, formatedDate);
		request.setAttribute(PresentationConstants.GUIDE, infoGuide);

		String copies = request.getParameter("copies");
		if (copies != null && copies.equals("2")) {
			return mapping.findForward("PrintTwoGuides");
		}

		return mapping.findForward("PrintOneGuide");

	}

}