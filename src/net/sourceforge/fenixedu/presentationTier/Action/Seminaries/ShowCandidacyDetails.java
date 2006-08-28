/*
 * Created on 26/Ago/2003, 14:48:56
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
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;
import net.sourceforge.fenixedu.dataTransferObject.Seminaries.InfoCandidacy;
import net.sourceforge.fenixedu.dataTransferObject.Seminaries.InfoCaseStudy;
import net.sourceforge.fenixedu.dataTransferObject.Seminaries.InfoCaseStudyChoice;
import net.sourceforge.fenixedu.dataTransferObject.Seminaries.InfoModality;
import net.sourceforge.fenixedu.dataTransferObject.Seminaries.InfoSeminary;
import net.sourceforge.fenixedu.dataTransferObject.Seminaries.InfoTheme;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 * 
 * 
 * Created at 26/Ago/2003, 14:48:56
 *  
 */
public class ShowCandidacyDetails extends FenixAction {
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {
        IUserView userView = getUserView(request);
        String candidacyIDString = request.getParameter("objectCode");
        Integer candidacyID;
        if (candidacyIDString == null)
            throw new FenixActionException(mapping.findForward("invalidQueryString"));
        try {
            candidacyID = new Integer(candidacyIDString);
        } catch (Exception ex) {
            throw new FenixActionException(mapping.findForward("invalidQueryString"));
        }
        InfoCandidacy candidacy = null;
        InfoStudent student = null;
        InfoCurricularCourse curricularCourse = null;
        InfoTheme theme = null;
        InfoModality modality = null;
        String motivation = null;
        InfoSeminary seminary = null;
        List casesChoices = null;
        List cases = new LinkedList();

        ActionForward destiny = null;
        try {
            Object[] argsReadCandidacy = { candidacyID };
            candidacy = (InfoCandidacy) ServiceManagerServiceFactory.executeService(userView,
                    "Seminaries.GetCandidacyById", argsReadCandidacy);

            student = candidacy.getInfoStudent();
            curricularCourse = candidacy.getCurricularCourse();
            theme = candidacy.getTheme();
            modality = candidacy.getInfoModality();
            motivation = candidacy.getMotivation();
            casesChoices = candidacy.getCaseStudyChoices();
            seminary = candidacy.getInfoSeminary();

            //
            for (Iterator iterator = casesChoices.iterator(); iterator.hasNext();) {
                InfoCaseStudyChoice choice = (InfoCaseStudyChoice) iterator.next();

                InfoCaseStudy infoCaseStudy = choice.getCaseStudy();
                cases.add(infoCaseStudy);
            }
            //              

        } catch (Exception e) {
            throw new FenixActionException();
        }

        destiny = mapping.findForward("showCandidacyDetails");
        request.setAttribute("cases", cases);
        request.setAttribute("student", student);
        request.setAttribute("curricularCourse", curricularCourse);
        request.setAttribute("theme", theme);
        request.setAttribute("motivation", motivation);
        request.setAttribute("seminary", seminary);
        request.setAttribute("modality", modality);
        return destiny;
    }
}