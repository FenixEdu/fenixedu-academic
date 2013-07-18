/*
 * Created on 25/Set/2003, 11:52:14 By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
package net.sourceforge.fenixedu.presentationTier.Action.Seminaries;

import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.Seminaries.ChangeCandidacyApprovanceStatus;
import net.sourceforge.fenixedu.applicationTier.Servico.Seminaries.SelectCandidaciesService;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.Seminaries.SelectCandidaciesDTO;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import pt.ist.fenixWebFramework.security.UserView;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

/**
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt Created at
 *         25/Set/2003, 11:52:14
 */
// TODO: this action IS NOT ready to handle multiple seminaries. It will need a
// select box to select which seminary's candidacies to view
@Mapping(module = "teacher", path = "/selectCandidacies", attribute = "selectCandidaciesForm",
        formBean = "selectCandidaciesForm", scope = "request", parameter = "method")
@Forwards(value = {
        @Forward(name = "prepareForm", path = "/selectCandidacies.do?method=prepare"),
        @Forward(name = "showSelectCandidacies", path = "/teacher/showSelectCandidacies.jsp", tileProperties = @Tile(
                navLocal = "/teacher/showSeminariesIndex_bd.jsp", title = "private.seminars.selectcandidate")) })
public class SelectCandidacies extends FenixDispatchAction {
    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws FenixActionException {
        IUserView userView = UserView.getUser();
        ActionForward destiny = null;
        String seminaryIDString = request.getParameter("seminaryID");
        Integer seminaryID = null;
        Integer wildcard = new Integer(-1);
        try {
            seminaryID = new Integer(seminaryIDString);
        } catch (NumberFormatException ex) {
            seminaryID = wildcard;
        }

        Object[] args = { new Boolean(false), seminaryID };
        try {
            SelectCandidaciesDTO serviceResult = SelectCandidaciesService.runSelectCandidaciesService(false, seminaryID);
            request.setAttribute("seminaries", serviceResult.getSeminaries());
            request.setAttribute("candidacies", serviceResult.getCandidacies());
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        destiny = mapping.findForward("showSelectCandidacies");
        return destiny;
    }

    public List getNewSelectedStudents(Integer[] selectedStudents, Integer[] previousUnselected) {
        List newSelectedStudents = new LinkedList();
        for (Integer selectedStudent : selectedStudents) {
            for (Integer element : previousUnselected) {
                if (selectedStudent.equals(element)) {
                    newSelectedStudents.add(selectedStudent);
                    break;
                }
            }
        }
        return newSelectedStudents;
    }

    public List getNewUnselectedStudents(Integer[] selectedStudents, Integer[] previousSelected) {
        List newUnselectedStudents = new LinkedList();
        for (Integer element : previousSelected) {
            newUnselectedStudents.add(element);
        }
        //
        //
        for (Integer element : previousSelected) {
            for (Integer selectedStudent : selectedStudents) {
                if (element.equals(selectedStudent)) {
                    newUnselectedStudents.remove(element);
                    break;
                }
            }
        }
        return newUnselectedStudents;
    }

    public ActionForward changeSelection(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {
        IUserView userView = getUserView(request);
        DynaActionForm selectCases = (DynaActionForm) form;
        Integer[] selectedStudents = null;
        Integer[] previousSelected = null;
        Integer[] previousUnselected = null;
        try {
            selectedStudents = (Integer[]) selectCases.get("selectedStudents");
            previousSelected = (Integer[]) selectCases.get("previousSelected");
            previousUnselected = (Integer[]) selectCases.get("previousUnselected");
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new FenixActionException();
        }
        if (selectedStudents == null || previousSelected == null || previousUnselected == null) {
            throw new FenixActionException();
        }
        List changedStatusCandidaciesIds = new LinkedList();
        changedStatusCandidaciesIds.addAll(this.getNewSelectedStudents(selectedStudents, previousUnselected));
        changedStatusCandidaciesIds.addAll(this.getNewUnselectedStudents(selectedStudents, previousSelected));

        ChangeCandidacyApprovanceStatus.run(changedStatusCandidaciesIds);

        // modified by Fernanda Quitério
        // destiny = mapping.findForward("prepareForm");
        // return destiny;
        return prepare(mapping, form, request, response);
    }
}