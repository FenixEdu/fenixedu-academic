/*
 * Created on 31/Jul/2003, 16:04:48
 *
 *By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
package net.sourceforge.fenixedu.presentationTier.Action.Seminaries;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pt.ist.bennu.core.domain.User;
import net.sourceforge.fenixedu.applicationTier.Servico.Seminaries.GetAllSeminaries;
import net.sourceforge.fenixedu.applicationTier.Servico.Seminaries.GetCandidaciesByStudentID;
import net.sourceforge.fenixedu.dataTransferObject.Seminaries.InfoCandidacy;
import net.sourceforge.fenixedu.dataTransferObject.Seminaries.InfoSeminaryWithEquivalencies;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 * 
 * 
 *         Created at 31/Jul/2003, 16:04:48
 * 
 */
public class ListAllSeminaries extends FenixAction {
    public List setCurrentCandidaciesInfo(ActionMapping mapping, HttpServletRequest request, User userView)
            throws FenixActionException {
        List currentCandidacies = null;
        try {
            currentCandidacies = GetCandidaciesByStudentID.runGetCandidaciesByStudentID(userView.getPerson());
        } catch (Exception e) {
            throw new FenixActionException(e);
        }
        return currentCandidacies;

    }

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws FenixActionException {
        User userView = getUserView(request);
        List seminaries = null;
        ActionForward destiny = null;
        List currentCandidacies = setCurrentCandidaciesInfo(mapping, request, userView);
        List candidaciesToDisplay = new LinkedList();
        try {
            seminaries = GetAllSeminaries.runGetAllSeminaries(new Boolean(true));
            for (Iterator iter = currentCandidacies.iterator(); iter.hasNext();) {
                InfoCandidacy infoCandidacy = (InfoCandidacy) iter.next();
                String seminaryID = infoCandidacy.getInfoSeminary().getExternalId();
                for (Iterator iterator = seminaries.iterator(); iterator.hasNext();) {
                    InfoSeminaryWithEquivalencies infoSeminary = (InfoSeminaryWithEquivalencies) iterator.next();
                    if (infoSeminary.getExternalId().equals(seminaryID)) {
                        candidaciesToDisplay.add(infoCandidacy);
                    }
                }
            }
        } catch (Exception e) {
            throw new FenixActionException(e);
        }
        request.setAttribute("currentCandidacies", candidaciesToDisplay);
        destiny = mapping.findForward("listSeminaries");
        request.setAttribute("seminaries", seminaries);
        return destiny;
    }
}