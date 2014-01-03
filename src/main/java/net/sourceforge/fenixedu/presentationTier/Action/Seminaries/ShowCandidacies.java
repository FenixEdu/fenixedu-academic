/*
 * Created on 1/Set/2003, 15:06:55
 *
 *By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
package net.sourceforge.fenixedu.presentationTier.Action.Seminaries;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.Seminaries.GetAllCasesStudy;
import net.sourceforge.fenixedu.applicationTier.Servico.Seminaries.GetAllEquivalencies;
import net.sourceforge.fenixedu.applicationTier.Servico.Seminaries.GetAllModalities;
import net.sourceforge.fenixedu.applicationTier.Servico.Seminaries.GetAllSeminaries;
import net.sourceforge.fenixedu.applicationTier.Servico.Seminaries.GetAllThemes;
import net.sourceforge.fenixedu.applicationTier.Servico.Seminaries.ReadCandidacies;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourse;
import net.sourceforge.fenixedu.dataTransferObject.Seminaries.InfoCandidacyDetails;
import net.sourceforge.fenixedu.dataTransferObject.Seminaries.InfoCaseStudy;
import net.sourceforge.fenixedu.dataTransferObject.Seminaries.InfoCaseStudyChoice;
import net.sourceforge.fenixedu.dataTransferObject.Seminaries.InfoEquivalency;
import net.sourceforge.fenixedu.presentationTier.Action.Seminaries.Exceptions.BDException;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.core.domain.User;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

/**
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 * 
 * 
 *         Created at 1/Set/2003, 15:06:55
 * 
 */
@Mapping(module = "teacher", path = "/showCandidacies", attribute = "showCandidacies", formBean = "showCandidacies",
        scope = "request")
@Forwards(value = { @Forward(name = "allCandidaciesGrid", path = "/teacher/candidaciesGrid.jsp", tileProperties = @Tile(
        navLocal = "/teacher/showSeminariesIndex_bd.jsp", title = "private.seminars.viewapplications")) })
public class ShowCandidacies extends FenixAction {

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
        User userView = getUserView(request);
        //
        List candidacies = new LinkedList();
        ActionForward destiny = null;
        List candidaciesExtendedInfo = new LinkedList();
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
                candidaciesExtendedInfo.add(candidacy);
                Collections.sort(candidaciesExtendedInfo, new BeanComparator("student.number"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new FenixActionException(e);
        }
        this.setAvaliableOptionsForInputQueries(request, userView);
        request.setAttribute("candidacies", candidaciesExtendedInfo);
        destiny = mapping.findForward("allCandidaciesGrid");
        return destiny;
    }

    private void setAvaliableOptionsForInputQueries(HttpServletRequest request, User userView) throws FenixActionException {
        List seminaries = null;
        List cases = null;
        List modalities = null;
        List equivalencies = null;
        List themes = null;
        List curricularCoursesWithEquivalency = new LinkedList();
        List avaliableCurricularPlans = new LinkedList();
        try {

            seminaries = GetAllSeminaries.runGetAllSeminaries(false);
            cases = GetAllCasesStudy.runGetAllCasesStudy();
            modalities = GetAllModalities.runGetAllModalities();
            themes = GetAllThemes.runGetAllThemes();
            equivalencies = GetAllEquivalencies.runGetAllEquivalencies();
            //
            //
            final Set<String> addedNames = new HashSet<String>();
            for (Iterator iterator = equivalencies.iterator(); iterator.hasNext();) {
                InfoEquivalency equivalency = (InfoEquivalency) iterator.next();

                InfoCurricularCourse curricularCourse = equivalency.getCurricularCourse();
                if (curricularCourse != null) {
                    curricularCoursesWithEquivalency.add(curricularCourse);
                    if (!addedNames.contains(curricularCourse.getInfoDegreeCurricularPlan().getName())) {
                        avaliableCurricularPlans.add(curricularCourse.getInfoDegreeCurricularPlan());
                        addedNames.add(curricularCourse.getInfoDegreeCurricularPlan().getName());
                    }
                } else {
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new FenixActionException(e);
        }
        Collections.sort(avaliableCurricularPlans, new BeanComparator("name")); // TODO
        // gedl
        // remover
        // dupicados

        request.setAttribute("seminaries", seminaries);
        request.setAttribute("cases", cases);
        request.setAttribute("degrees", avaliableCurricularPlans);
        request.setAttribute("modalities", modalities);
        request.setAttribute("courses", curricularCoursesWithEquivalency);
        request.setAttribute("themes", themes);
    }
}