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

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.Seminaries.GetCandidaciesByStudentIDAndSeminaryID;
import net.sourceforge.fenixedu.applicationTier.Servico.Seminaries.GetSeminary;
import net.sourceforge.fenixedu.applicationTier.Servico.student.ReadCurricularCoursesByUsername;
import net.sourceforge.fenixedu.applicationTier.Servico.student.ReadStudentByUsername;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;
import net.sourceforge.fenixedu.dataTransferObject.Seminaries.InfoEquivalency;
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
 *         Created at 31/Jul/2003, 19:23:52
 * 
 */
public class ShowCandidacyOptions extends FenixAction {
    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws FenixActionException {
        IUserView userView = getUserView(request);
        String seminaryIDString = request.getParameter("objectCode");
        String seminaryID;
        if (seminaryIDString == null) {
            throw new FenixActionException(mapping.findForward("invalidQueryString"));
        }
        try {
            seminaryID = seminaryIDString;
        } catch (Exception ex) {
            throw new FenixActionException(mapping.findForward("invalidQueryString"));
        }
        InfoSeminaryWithEquivalencies seminary = null;
        InfoStudent student = null;
        List disciplines = null;
        ActionForward destiny = null;
        try {
            seminary = GetSeminary.runGetSeminary(seminaryID);
            student = ReadStudentByUsername.runReadStudentByUsername(userView.getUtilizador());
            disciplines = ReadCurricularCoursesByUsername.runReadCurricularCoursesByUsername(userView.getUtilizador());
            List avaliableEquivalencies = new LinkedList();
            for (Iterator iterator = disciplines.iterator(); iterator.hasNext();) {
                InfoCurricularCourse curricularCourse = (InfoCurricularCourse) iterator.next();
                for (Iterator equivalencyIterator = seminary.getEquivalencies().iterator(); equivalencyIterator.hasNext();) {
                    InfoEquivalency equivalency = (InfoEquivalency) equivalencyIterator.next();
                    if (equivalency.getCurricularCourse().getExternalId().equals(curricularCourse.getExternalId())) {
                        avaliableEquivalencies.add(equivalency);
                        break;
                    }
                }
            }
            seminary.setEquivalencies(avaliableEquivalencies);
            List candidacies =
                    GetCandidaciesByStudentIDAndSeminaryID.runGetCandidaciesByStudentIDAndSeminaryID(student.getExternalId(),
                            seminaryID);
            if (candidacies.size() >= seminary.getAllowedCandidaciesPerStudent().intValue()) {
                addErrorMessage(request, "error.seminaries.candidaciesLimitReached", "error.seminaries.candidaciesLimitReached",
                        String.valueOf(seminary.getAllowedCandidaciesPerStudent()));

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