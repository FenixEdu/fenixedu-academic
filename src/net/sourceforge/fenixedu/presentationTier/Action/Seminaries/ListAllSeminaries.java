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
import javax.servlet.http.HttpSession;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;
import net.sourceforge.fenixedu.dataTransferObject.Seminaries.InfoCandidacy;
import net.sourceforge.fenixedu.dataTransferObject.Seminaries.InfoSeminaryWithEquivalencies;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 * 
 * 
 * Created at 31/Jul/2003, 16:04:48
 *  
 */
public class ListAllSeminaries extends FenixAction {
    public List setCurrentCandidaciesInfo(ActionMapping mapping, HttpServletRequest request,
            IUserView userView) throws FenixActionException {
        List currentCandidacies = null;
        InfoStudent student = null;
        try {
            Object[] argsReadStudent = { userView.getUtilizador() };
            student = (InfoStudent) ServiceManagerServiceFactory.executeService(userView,
                    "ReadStudentByUsername", argsReadStudent);
            Object[] argsReadCandidacies = { student.getIdInternal() };
            currentCandidacies = (List) ServiceManagerServiceFactory.executeService(userView,
                    "Seminaries.GetCandidaciesByStudentID", argsReadCandidacies);
        } catch (Exception e) {
            throw new FenixActionException();
        }
        return currentCandidacies;

    }

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {
        HttpSession session = this.getSession(request);
        IUserView userView = getUserView(request);
        List seminaries = null;
        ActionForward destiny = null;
        List currentCandidacies = setCurrentCandidaciesInfo(mapping, request, userView);
        List candidaciesToDisplay = new LinkedList();
        try {
            Object[] args = { new Boolean(true) };
            seminaries = (List) ServiceManagerServiceFactory.executeService(userView,
                    "Seminaries.GetAllSeminaries", args);
            for (Iterator iter = currentCandidacies.iterator(); iter.hasNext();) {
                InfoCandidacy infoCandidacy = (InfoCandidacy) iter.next();
                Integer seminaryID = infoCandidacy.getInfoSeminary().getIdInternal();
                for (Iterator iterator = seminaries.iterator(); iterator.hasNext();) {
                    InfoSeminaryWithEquivalencies infoSeminary = (InfoSeminaryWithEquivalencies) iterator.next();
                    if (infoSeminary.getIdInternal().equals(seminaryID))
                        candidaciesToDisplay.add(infoCandidacy);
                }
            }
        } catch (Exception e) {
            throw new FenixActionException();
        }
        request.setAttribute("currentCandidacies", candidaciesToDisplay);
        destiny = mapping.findForward("listSeminaries");
        request.setAttribute("seminaries", seminaries);
        return destiny;
    }
}