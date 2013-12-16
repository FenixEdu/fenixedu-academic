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

import pt.ist.bennu.core.domain.User;
import net.sourceforge.fenixedu.applicationTier.Servico.Seminaries.ReadCandidacies;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.dataTransferObject.Seminaries.InfoCandidacyDetails;
import net.sourceforge.fenixedu.dataTransferObject.Seminaries.InfoCaseStudy;
import net.sourceforge.fenixedu.dataTransferObject.Seminaries.InfoCaseStudyChoice;
import net.sourceforge.fenixedu.presentationTier.Action.Seminaries.Exceptions.BDException;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Mapping;

/**
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 * 
 * 
 *         Created at 8/Set/2003, 18:37:00
 * 
 */
@Mapping(module = "teacher", path = "/getTabSeparatedCandidacies", scope = "request")
public class DownloadCandidaciesTable extends FenixAction {
    static final String COLUMNS_HEADERS =
            "Nº\tNome\tMédia\tCadeiras Feitas\tAprovado\tE-Mail\tSeminário\tCurso\tDisciplina\tModalidade\tTema\tMotivação\tCaso1\tCaso2\tCaso3\tCaso4\tCaso5";

    List doReadCandidacies(HttpServletRequest request) throws NotAuthorizedException, BDException {
        String modalityID;
        String themeID;
        String case1Id;
        String case2Id;
        String case3Id;
        String case4Id;
        String case5Id;
        String curricularCourseID;
        String degreeID;
        String seminaryID;
        Boolean approved = null;
        //
        //
        String stringApproved = request.getParameter("approved");
        if (stringApproved != null && (stringApproved.equals("true") || stringApproved.equals("false"))) {
            approved = new Boolean(stringApproved);
        }
        //
        //
        themeID = request.getParameter("themeID");

        modalityID = request.getParameter("modalityID");

        seminaryID = request.getParameter("seminaryID");

        case1Id = request.getParameter("case1ID");

        case2Id = request.getParameter("case2ID");

        case3Id = request.getParameter("case3ID");

        case4Id = request.getParameter("case4ID");

        case5Id = request.getParameter("case5ID");

        curricularCourseID = request.getParameter("courseID");

        degreeID = request.getParameter("degreeID");

        return ReadCandidacies.runReadCandidacies(modalityID, seminaryID, themeID, case1Id, case2Id, case3Id, case4Id, case5Id,
                curricularCourseID, degreeID, approved);
    }

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws FenixActionException {
        String document = DownloadCandidaciesTable.COLUMNS_HEADERS + "\n";
        User userView = getUserView(request);

        //
        List candidacies = new LinkedList();
        try {
            candidacies = doReadCandidacies(request);
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

                document += "\"" + candidacy.getInfoClassification().getAritmeticClassification() + "\"" + "\t";
                document += "\"" + candidacy.getInfoClassification().getCompletedCourses() + "\"" + "\t";
                //
                //
                //
                String friendlyBoolean;
                if ((candidacy.getApproved() != null) && (candidacy.getApproved().booleanValue())) {
                    friendlyBoolean = "Sim";
                } else {
                    friendlyBoolean = "Não";
                }
                document += "\"" + friendlyBoolean + "\"" + "\t";
                document += "\"" + candidacy.getStudent().getInfoPerson().getEmail() + "\"" + "\t";
                document += "\"" + candidacy.getSeminary().getName() + "\"" + "\t";
                document +=
                        "\"" + candidacy.getCurricularCourse().getInfoDegreeCurricularPlan().getInfoDegree().getSigla() + "\""
                                + "\t";
                document += "\"" + candidacy.getCurricularCourse().getName() + "\"" + "\t";
                document += "\"" + candidacy.getModality().getName() + "\"" + "\t";
                if (candidacy.getTheme() != null) {
                    document += "\"" + candidacy.getTheme().getName() + "\"" + "\t";
                } else {
                    document += "\"" + "N/A\"\t";
                }
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