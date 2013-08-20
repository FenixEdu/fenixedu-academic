/*
 * Created on 4/Ago/2003, 18:29:26
 *
 *By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
package net.sourceforge.fenixedu.presentationTier.Action.Seminaries;

import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.Seminaries.GetCaseStudiesByEquivalencyID;
import net.sourceforge.fenixedu.applicationTier.Servico.Seminaries.GetCaseStudiesByThemeID;
import net.sourceforge.fenixedu.applicationTier.Servico.Seminaries.GetEquivalency;
import net.sourceforge.fenixedu.applicationTier.Servico.Seminaries.GetThemeById;
import net.sourceforge.fenixedu.applicationTier.Servico.Seminaries.WriteCandidacy;
import net.sourceforge.fenixedu.applicationTier.Servico.student.ReadStudentByUsername;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;
import net.sourceforge.fenixedu.dataTransferObject.Seminaries.InfoCandidacy;
import net.sourceforge.fenixedu.dataTransferObject.Seminaries.InfoEquivalency;
import net.sourceforge.fenixedu.dataTransferObject.Seminaries.InfoTheme;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 * 
 * 
 *         Created at 4/Ago/2003, 18:29:26
 * 
 */
public class ShowCandidacySecondForm extends FenixAction {

    public InfoStudent readStudentByUserView(IUserView userView) throws FenixActionException {
        InfoStudent student = null;
        try {
            student = ReadStudentByUsername.runReadStudentByUsername(userView.getUtilizador());
        } catch (Exception e) {
            throw new FenixActionException();
        }
        return student;
    }

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws FenixActionException {
        IUserView userView = getUserView(request);
        // externalId is equivalency's ExternalId
        String equivalencyIDString = request.getParameter("externalId");
        String themeIDString = request.getParameter("themeID");
        String equivalencyID = null;
        String themeID = null;
        if (equivalencyIDString == null) {
            throw new FenixActionException(mapping.findForward("invalidQueryString"));
        }
        try {
            if (themeIDString != null) {
                themeID = themeIDString;
            }
            equivalencyID = equivalencyIDString;
        } catch (Exception ex) {
            throw new FenixActionException(mapping.findForward("invalidQueryString"));
        }
        InfoEquivalency equivalency = null;
        List cases = null;
        ActionForward destiny = null;
        try {
            equivalency = GetEquivalency.runGetEquivalency(equivalencyID);

            //
            if (themeID != null) // we want the cases of ONE theme
            {
                cases = GetCaseStudiesByThemeID.runGetCaseStudiesByThemeID(themeID);
            } else // we want ALL the cases of the equivalency (its a "Completa"
            // modality)
            {
                cases = GetCaseStudiesByEquivalencyID.runGetCaseStudiesByEquivalencyID(equivalencyID);
            }
        } catch (Exception e) {
            throw new FenixActionException();
        }
        if (equivalency.getHasCaseStudy().booleanValue()) {
            // String motivation = request.getParameter("motivation");
            request.setAttribute("equivalency", equivalency);
            request.setAttribute("unselectedCases", cases);
            request.setAttribute("selectedCases", new LinkedList());
            request.setAttribute("hiddenSelectedCases", new LinkedList());
            destiny = mapping.findForward("showCandidacyFormNonCompleteModalitySecondInfo");
        } else {
            InfoTheme theme = null;
            String motivation = request.getParameter("motivation");
            InfoCandidacy infoCandidacy = new InfoCandidacy();
            infoCandidacy.setCurricularCourse(equivalency.getCurricularCourse());
            infoCandidacy.setInfoModality(equivalency.getModality());
            infoCandidacy.setInfoSeminary(equivalency.getInfoSeminary());
            infoCandidacy.setSeminaryName(equivalency.getSeminaryName());
            infoCandidacy.setInfoStudent(this.readStudentByUserView(userView));
            infoCandidacy.setTheme(new InfoTheme(themeID));
            infoCandidacy.setMotivation(motivation);
            infoCandidacy.setCaseStudyChoices(new LinkedList());
            try {
                WriteCandidacy.runWriteCandidacy(infoCandidacy);
                theme = GetThemeById.runGetThemeById(themeID);
            } catch (Exception e) {
                throw new FenixActionException();
            }
            request.setAttribute("cases", new LinkedList());
            request.setAttribute("motivation", motivation);
            request.setAttribute("modalityName", equivalency.getModality().getName());
            request.setAttribute("theme", theme);
            request.setAttribute("seminaryName", infoCandidacy.getSeminaryName());
            destiny = mapping.findForward("candidacySubmited");
        }
        return destiny;
    }
}