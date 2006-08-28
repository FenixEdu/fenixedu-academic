/*
 * Created on 8/Set/2003, 18:37:00
 * 
 * By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
package net.sourceforge.fenixedu.presentationTier.Action.Seminaries;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.dataTransferObject.Seminaries.InfoCandidacyDetails;
import net.sourceforge.fenixedu.dataTransferObject.Seminaries.InfoCaseStudy;
import net.sourceforge.fenixedu.dataTransferObject.Seminaries.InfoCaseStudyChoice;
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
 * Created at 8/Set/2003, 18:37:00
 *  
 */
public class DownloadCandidaciesTable extends FenixAction {
    static final String COLUMNS_HEADERS = "Nº\tNome\tMédia\tCadeiras Feitas\tAprovado\tE-Mail\tSeminário\tCurso\tDisciplina\tModalidade\tTema\tMotivação\tCaso1\tCaso2\tCaso3\tCaso4\tCaso5";

    Object[] getReadCandidaciesArgs(HttpServletRequest request) {
        Integer modalityID;
        Integer themeID;
        Integer case1Id;
        Integer case2Id;
        Integer case3Id;
        Integer case4Id;
        Integer case5Id;
        Integer curricularCourseID;
        Integer degreeID;
        Integer seminaryID;
        Boolean approved = null;
        //
        //
        String stringApproved = request.getParameter("approved");
        if (stringApproved != null && (stringApproved.equals("true") || stringApproved.equals("false")))
            approved = new Boolean(stringApproved);
        //
        //
        try {
            themeID = new Integer(request.getParameter("themeID"));
        } catch (NumberFormatException ex) {
            themeID = new Integer(-1);
        }
        try {
            modalityID = new Integer(request.getParameter("modalityID"));
        } catch (NumberFormatException ex) {
            modalityID = new Integer(-1);
        }
        try {
            seminaryID = new Integer(request.getParameter("seminaryID"));
        } catch (NumberFormatException ex) {
            seminaryID = new Integer(-1);
        }
        try {
            case1Id = new Integer(request.getParameter("case1ID"));
        } catch (NumberFormatException ex) {
            case1Id = new Integer(-1);
        }
        try {
            case2Id = new Integer(request.getParameter("case2ID"));
        } catch (NumberFormatException ex) {
            case2Id = new Integer(-1);
        }
        try {
            case3Id = new Integer(request.getParameter("case3ID"));
        } catch (NumberFormatException ex) {
            case3Id = new Integer(-1);
        }
        try {
            case4Id = new Integer(request.getParameter("case4ID"));
        } catch (NumberFormatException ex) {
            case4Id = new Integer(-1);
        }
        try {
            case5Id = new Integer(request.getParameter("case5ID"));
        } catch (NumberFormatException ex) {
            case5Id = new Integer(-1);
        }
        try {
            curricularCourseID = new Integer(request.getParameter("courseID"));
        } catch (NumberFormatException ex) {
            curricularCourseID = new Integer(-1);
        }
        try {
            degreeID = new Integer(request.getParameter("degreeID"));
        } catch (NumberFormatException ex) {
            degreeID = new Integer(-1);
        }
        Object[] arguments = { modalityID, seminaryID, themeID, case1Id, case2Id, case3Id, case4Id,
                case5Id, curricularCourseID, degreeID, approved };
        return arguments;
    }

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {
        String document = DownloadCandidaciesTable.COLUMNS_HEADERS + "\n";
        IUserView userView = getUserView(request);

        //
        List candidacies = new LinkedList();
        try {
            Object[] argsReadCandidacies = getReadCandidaciesArgs(request);
            candidacies = (List) ServiceManagerServiceFactory.executeService(userView,
                    "Seminaries.ReadCandidacies", argsReadCandidacies);
            for (Iterator iterator = candidacies.iterator(); iterator.hasNext();) {

                List casesChoices = null;

                List cases = new LinkedList();
                InfoCandidacyDetails candidacy = (InfoCandidacyDetails) iterator.next();

                casesChoices = candidacy.getCases();
                //
                for (Iterator casesIterator = casesChoices.iterator(); casesIterator.hasNext();) {
                    InfoCaseStudyChoice choice = (InfoCaseStudyChoice) casesIterator.next();
                    InfoCaseStudy infoCaseStudy = choice.getCaseStudy();
                    cases.add(infoCaseStudy);
                }
                candidacy.setCases(cases);
                //
                document += "\"" + candidacy.getStudent().getNumber() + "\"" + "\t";
                document += "\"" + candidacy.getStudent().getInfoPerson().getNome() + "\"" + "\t";
                //
                //
                //
                //
                //
                //

                document += "\"" + candidacy.getInfoClassification().getAritmeticClassification() + "\""
                        + "\t";
                document += "\"" + candidacy.getInfoClassification().getCompletedCourses() + "\"" + "\t";
                //
                //
                //
                String friendlyBoolean;
                if ((candidacy.getApproved() != null) && (candidacy.getApproved().booleanValue()))
                    friendlyBoolean = "Sim";
                else
                    friendlyBoolean = "Não";
                document += "\"" + friendlyBoolean + "\"" + "\t";
                document += "\"" + candidacy.getStudent().getInfoPerson().getEmail() + "\"" + "\t";
                document += "\"" + candidacy.getSeminary().getName() + "\"" + "\t";
                document += "\""
                        + candidacy.getCurricularCourse().getInfoDegreeCurricularPlan().getInfoDegree()
                                .getSigla() + "\"" + "\t";
                document += "\"" + candidacy.getCurricularCourse().getName() + "\"" + "\t";
                document += "\"" + candidacy.getModality().getName() + "\"" + "\t";
                if (candidacy.getTheme() != null)
                    document += "\"" + candidacy.getTheme().getName() + "\"" + "\t";
                else
                    document += "\"" + "N/A\"\t";
                document += "\"" + candidacy.getMotivation() + "\"" + "\t";
                for (Iterator casesIterator = cases.iterator(); casesIterator.hasNext();) {
                    InfoCaseStudy caseStudy = (InfoCaseStudy) casesIterator.next();
                    document += "\"" + caseStudy.getName() + "\"" + "\t";
                }
                document += "\n";
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new FenixActionException(e);
        }
        try {
            ServletOutputStream writer = response.getOutputStream();
            response.setContentType("application/vnd.ms-excel");
            writer.print(document);
            writer.flush();
            response.flushBuffer();
        } catch (IOException e1) {
            throw new FenixActionException();
        }
        return null;
    }
}