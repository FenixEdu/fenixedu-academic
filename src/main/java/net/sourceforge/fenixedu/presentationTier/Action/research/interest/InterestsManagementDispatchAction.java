package net.sourceforge.fenixedu.presentationTier.Action.research.interest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.research.interest.ChangeResearchInterestOrder;
import net.sourceforge.fenixedu.applicationTier.Servico.research.interest.DeleteResearchInterest;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.research.ResearchInterest;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

@Mapping(module = "researcher", path = "/interests/interestsManagement", scope = "session", parameter = "method")
@Forwards(value = {
        @Forward(name = "InsertNewInterest", path = "/researcher/interests/insertNewInterest.jsp", tileProperties = @Tile(
                title = "private.operator.personnelmanagement.managementfaculty.teacherevaluation.researchinterests")),
        @Forward(name = "EditInterest", path = "/researcher/interests/editInterest.jsp", tileProperties = @Tile(
                title = "private.operator.personnelmanagement.managementfaculty.teacherevaluation.researchinterests")),
        @Forward(name = "Success", path = "/researcher/interests/interestsManagement.jsp", tileProperties = @Tile(
                head = "/commons/renderers/treeRendererHeader.jsp",
                title = "private.operator.personnelmanagement.managementfaculty.teacherevaluation.researchinterests")) })
public class InterestsManagementDispatchAction extends FenixDispatchAction {

    private static final int UP = -1;

    private static final int DOWN = 1;

    public ActionForward delete(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        DeleteResearchInterest.run(request.getParameter("oid"));

        return prepare(mapping, form, request, response);
    }

    public ActionForward up(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        changeOrder(request, UP);
        return prepare(mapping, form, request, response);
    }

    public ActionForward down(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        changeOrder(request, DOWN);
        return prepare(mapping, form, request, response);
    }

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        List<ResearchInterest> orderedInterests = getOrderedInterests(request);
        RenderUtils.invalidateViewState();
        request.setAttribute("researchInterests", orderedInterests);

        return mapping.findForward("Success");
    }

    public ActionForward alterOrder(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        request.setAttribute("alterOrder", "alterOrder");
        return prepare(mapping, form, request, response);
    }

    public ActionForward prepareInsertInterest(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        List<ResearchInterest> orderedInterests = getOrderedInterests(request);

        if (orderedInterests.size() > 0) {
            request.setAttribute("lastOrder", orderedInterests.get(orderedInterests.size() - 1).getOrder() + 1);
        } else {
            request.setAttribute("lastOrder", 1);
        }

        request.setAttribute("party", getUserView(request).getPerson());

        return mapping.findForward("InsertNewInterest");
    }

    public ActionForward prepareEditInterest(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        ResearchInterest researchInterest = AbstractDomainObject.fromExternalId(request.getParameter("oid"));

        request.setAttribute("interest", researchInterest);

        return mapping.findForward("EditInterest");
    }

    private void changeOrder(HttpServletRequest request, int direction) throws FenixServiceException {
        IUserView userView = getUserView(request);
        Person person = userView.getPerson();

        ResearchInterest interest = AbstractDomainObject.fromExternalId(request.getParameter("oid"));
        List<ResearchInterest> orderedInterests = getOrderedInterests(request);

        int index = orderedInterests.indexOf(interest);
        if (index + direction >= 0) {
            orderedInterests.remove(interest);
            if (index + direction > orderedInterests.size()) {
                orderedInterests.add(index, interest);
            } else {
                orderedInterests.add(index + direction, interest);
            }

            ChangeResearchInterestOrder.run(person, orderedInterests);
        }
    }

    public ActionForward changeOrderUsingAjaxTree(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        String treeStructure = (String) getFromRequest(request, "tree");
        List<ResearchInterest> newInterestsOrder = reOrderInterests(treeStructure, getOrderedInterests(request));
        ChangeResearchInterestOrder.run(getLoggedPerson(request), newInterestsOrder);

        return prepare(mapping, form, request, response);
    }

    private List<ResearchInterest> reOrderInterests(String treeStructure, List<ResearchInterest> oldOrder) {
        List<ResearchInterest> newInterestsOrder = new ArrayList<ResearchInterest>();
        List<ResearchInterest> oldInterestsOrder = oldOrder;
        String[] nodes = treeStructure.split(",");

        for (String node : nodes) {
            String[] parts = node.split("-");

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

    private List<ResearchInterest> getOrderedInterests(HttpServletRequest request) throws FenixServiceException {

        List<ResearchInterest> researchInterests = getUserView(request).getPerson().getResearchInterests();

        List<ResearchInterest> orderedInterests = new ArrayList<ResearchInterest>(researchInterests);
        Collections.sort(orderedInterests, new Comparator<ResearchInterest>() {
            @Override
            public int compare(ResearchInterest researchInterest1, ResearchInterest researchInterest2) {
                return researchInterest1.getOrder().compareTo(researchInterest2.getOrder());
            }
        });
        return orderedInterests;
    }

}