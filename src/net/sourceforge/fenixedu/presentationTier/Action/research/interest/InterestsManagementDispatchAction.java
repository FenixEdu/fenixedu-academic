package net.sourceforge.fenixedu.presentationTier.Action.research.interest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;
import net.sourceforge.fenixedu.domain.research.ResearchInterest;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class InterestsManagementDispatchAction extends FenixDispatchAction {

	private static final int UP = -1;

	private static final int DOWN = 1;

	public ActionForward delete(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		Integer oid = Integer.parseInt(request.getParameter("oid"));
		IUserView userView = getUserView(request);

		ServiceUtils.executeService(userView, "DeleteResearchInterest",
				new Object[] { oid });

		return prepare(mapping, form, request, response);
	}

	public ActionForward up(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		changeOrder(request, UP);
		return prepare(mapping, form, request, response);
	}

	public ActionForward down(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		changeOrder(request, DOWN);
		return prepare(mapping, form, request, response);
	}

	public ActionForward prepare(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		List<ResearchInterest> orderedInterests = getOrderedInterests(request);
		RenderUtils.invalidateViewState();
		request.setAttribute("researchInterests", orderedInterests);

		return mapping.findForward("Success");
	}

	public ActionForward alterOrder(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		request.setAttribute("alterOrder", "alterOrder");
		return prepare(mapping, form, request, response);
	}

	public ActionForward prepareInsertInterest(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		List<ResearchInterest> orderedInterests = getOrderedInterests(request);

		if (orderedInterests.size() > 0) {
			request.setAttribute("lastOrder", orderedInterests.get(
					orderedInterests.size() - 1).getOrder() + 1);
		} else {
			request.setAttribute("lastOrder", 1);
		}

		request.setAttribute("party", (Party) getUserView(request).getPerson());

		return mapping.findForward("InsertNewInterest");
	}

	public ActionForward prepareEditInterest(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {

		Integer interestOid = Integer.parseInt(request.getParameter("oid"));
		ResearchInterest researchInterest = rootDomainObject
				.readResearchInterestByOID(interestOid);

		request.setAttribute("interest", researchInterest);

		return mapping.findForward("EditInterest");
	}

	private void changeOrder(HttpServletRequest request, int direction)
			throws FenixFilterException, FenixServiceException {
		Integer oid = Integer.parseInt(request.getParameter("oid"));

		IUserView userView = getUserView(request);
		Person person = userView.getPerson();

		ResearchInterest interest = rootDomainObject
				.readResearchInterestByOID(oid);
		List<ResearchInterest> orderedInterests = getOrderedInterests(request);

		int index = orderedInterests.indexOf(interest);
		if (index + direction >= 0) {
			orderedInterests.remove(interest);
			if (index + direction > orderedInterests.size()) {
				orderedInterests.add(index, interest);
			} else {
				orderedInterests.add(index + direction, interest);
			}

			ServiceUtils.executeService(userView,
					"ChangeResearchInterestOrder", new Object[] { person,
							orderedInterests });
		}
	}

	public ActionForward changeOrderUsingAjaxTree(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String treeStructure = (String) getFromRequest(request, "tree");
		List<ResearchInterest> newInterestsOrder = reOrderInterests(treeStructure, getOrderedInterests(request));
		executeService(request, "ChangeResearchInterestOrder", new Object[] {
				getLoggedPerson(request), newInterestsOrder });

		return prepare(mapping, form, request, response);
	}

	
	private List<ResearchInterest> reOrderInterests(String treeStructure,
			List<ResearchInterest> oldOrder) {
		List<ResearchInterest> newInterestsOrder = new ArrayList<ResearchInterest>();
		List<ResearchInterest> oldInterestsOrder = oldOrder;
		String[] nodes = treeStructure.split(",");

		for (int i = 0; i < nodes.length; i++) {
			String[] parts = nodes[i].split("-");

			Integer index = getId(parts[0]) - 1;
			ResearchInterest interest = oldInterestsOrder.get(index);
			newInterestsOrder.add(interest);
		}
		return newInterestsOrder;
	}

	private Integer getId(String id) {
		if (id == null) {
			return null;
		}
		try {
			return new Integer(id);
		} catch (NumberFormatException e) {
			e.printStackTrace();
			return null;
		}
	}

	private List<ResearchInterest> getOrderedInterests(
			HttpServletRequest request) throws FenixFilterException,
			FenixServiceException {

		List<ResearchInterest> researchInterests = getUserView(request)
				.getPerson().getResearchInterests();

		List<ResearchInterest> orderedInterests = new ArrayList<ResearchInterest>(
				researchInterests);
		Collections.sort(orderedInterests, new Comparator<ResearchInterest>() {
			public int compare(ResearchInterest researchInterest1,
					ResearchInterest researchInterest2) {
				return researchInterest1.getOrder().compareTo(
						researchInterest2.getOrder());
			}
		});
		return orderedInterests;
	}

}