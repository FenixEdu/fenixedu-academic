/*
 * Created on 31/Jul/2003, 19:23:52
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
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;
import net.sourceforge.fenixedu.dataTransferObject.Seminaries.InfoEquivalency;
import net.sourceforge.fenixedu.dataTransferObject.Seminaries.InfoSeminaryWithEquivalencies;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 * 
 * 
 * Created at 31/Jul/2003, 19:23:52
 *  
 */
public class ShowCandidacyOptions extends FenixAction {
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {
        IUserView userView = getUserView(request);
        String seminaryIDString = request.getParameter("objectCode");
        Integer seminaryID;
        if (seminaryIDString == null)
            throw new FenixActionException(mapping.findForward("invalidQueryString"));
        try {
            seminaryID = new Integer(seminaryIDString);
        } catch (Exception ex) {
            throw new FenixActionException(mapping.findForward("invalidQueryString"));
        }
        InfoSeminaryWithEquivalencies seminary = null;
        InfoStudent student = null;
        List disciplines = null;
        ActionForward destiny = null;
        try {
            Object[] argsReadSeminary = { seminaryID };
            Object[] argsReadStudent = { userView.getUtilizador() };
            seminary = (InfoSeminaryWithEquivalencies) ServiceManagerServiceFactory.executeService(userView,
                    "Seminaries.GetSeminary", argsReadSeminary);
            student = (InfoStudent) ServiceManagerServiceFactory.executeService(userView,
                    "ReadStudentByUsername", argsReadStudent);
            Object[] ReadCurricularCoursesByUsername = { userView.getUtilizador() };
            disciplines = (List) ServiceManagerServiceFactory.executeService(userView,
                    "student.ReadCurricularCoursesByUsername", ReadCurricularCoursesByUsername);
            List avaliableEquivalencies = new LinkedList();
            for (Iterator iterator = disciplines.iterator(); iterator.hasNext();) {
                InfoCurricularCourse curricularCourse = (InfoCurricularCourse) iterator.next();
                for (Iterator equivalencyIterator = seminary.getEquivalencies().iterator(); equivalencyIterator
                        .hasNext();) {
                    InfoEquivalency equivalency = (InfoEquivalency) equivalencyIterator.next();
                    if (equivalency.getCurricularCourse().getIdInternal().equals(
                            curricularCourse.getIdInternal())) {
                        avaliableEquivalencies.add(equivalency);
                        break;
                    }
                }
            }
            seminary.setEquivalencies(avaliableEquivalencies);
            Object[] argsReadCandidacies = { student.getIdInternal(), seminaryID };
            List candidacies = (List) ServiceManagerServiceFactory.executeService(userView,
                    "Seminaries.GetCandidaciesByStudentIDAndSeminaryID", argsReadCandidacies);
            if (candidacies.size() >= seminary.getAllowedCandidaciesPerStudent().intValue()) {
                ActionErrors actionErrors = new ActionErrors();

                ActionError actionError = new ActionError("error.seminaries.candidaciesLimitReached",
                        seminary.getAllowedCandidaciesPerStudent());
                actionErrors.add("error.seminaries.candidaciesLimitReached", actionError);
                saveErrors(request, actionErrors);
                destiny = mapping.findForward("candidaciesLimitReached");
            } else {
                destiny = mapping.findForward("showCandidacyOptions");
                request.setAttribute("seminary", seminary);
            }

        } catch (Exception e) {
            throw new FenixActionException(e);
        }

        return destiny;
    }
}