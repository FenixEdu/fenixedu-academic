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
import javax.servlet.http.HttpSession;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;
import net.sourceforge.fenixedu.dataTransferObject.Seminaries.InfoCandidacy;
import net.sourceforge.fenixedu.dataTransferObject.Seminaries.InfoEquivalency;
import net.sourceforge.fenixedu.dataTransferObject.Seminaries.InfoTheme;
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
 * Created at 4/Ago/2003, 18:29:26
 *  
 */
public class ShowCandidacySecondForm extends FenixAction {

    public InfoStudent readStudentByUserView(IUserView userView) throws FenixActionException {
        InfoStudent student = null;
        try {
            Object[] argsReadStudent = { userView.getUtilizador() };
            student = (InfoStudent) ServiceManagerServiceFactory.executeService(userView,
                    "ReadStudentByUsername", argsReadStudent);
        } catch (Exception e) {
            throw new FenixActionException();
        }
        return student;
    }

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {
        HttpSession session = this.getSession(request);
        IUserView userView = getUserView(request);
        //idInternal is equivalency's IdInternal
        String equivalencyIDString = request.getParameter("idInternal");
        String themeIDString = request.getParameter("themeID");
        Integer equivalencyID = null;
        Integer themeID = null;
        if (equivalencyIDString == null)
            throw new FenixActionException(mapping.findForward("invalidQueryString"));
        try {
            if (themeIDString != null)
                themeID = new Integer(themeIDString);
            equivalencyID = new Integer(equivalencyIDString);
        } catch (Exception ex) {
            throw new FenixActionException(mapping.findForward("invalidQueryString"));
        }
        InfoEquivalency equivalency = null;
        List cases = null;
        ActionForward destiny = null;
        try {
            Object[] argsReadEquivalency = { equivalencyID };
            equivalency = (InfoEquivalency) ServiceManagerServiceFactory.executeService(userView,
                    "Seminaries.GetEquivalency", argsReadEquivalency);

            //
            if (themeID != null) // we want the cases of ONE theme
            {
                Object[] argsReadCases = { themeID };
                cases = (List) ServiceManagerServiceFactory.executeService(userView,
                        "Seminaries.GetCaseStudiesByThemeID", argsReadCases);
            } else // we want ALL the cases of the equivalency (its a "Completa"
            // modality)
            {
                Object[] argsReadCases = { equivalencyID };
                cases = (List) ServiceManagerServiceFactory.executeService(userView,
                        "Seminaries.GetCaseStudiesByEquivalencyID", argsReadCases);
            }
        } catch (Exception e) {
            throw new FenixActionException();
        }
        if (equivalency.getHasCaseStudy().booleanValue()) {
            //   String motivation = request.getParameter("motivation");
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
                Object[] argsWriteCandidacy = { infoCandidacy };
                ServiceManagerServiceFactory.executeService(userView, "Seminaries.WriteCandidacy",
                        argsWriteCandidacy);
                Object[] argsReadTheme = { themeID };
                theme = (InfoTheme) ServiceManagerServiceFactory.executeService(userView,
                        "Seminaries.GetThemeById", argsReadTheme);
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