/*
 * Created on 1/Set/2003, 15:06:55
 *
 *By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
package net.sourceforge.fenixedu.presentationTier.Action.Seminaries;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegreeCurricularPlan;
import net.sourceforge.fenixedu.dataTransferObject.Seminaries.InfoCandidacyDetails;
import net.sourceforge.fenixedu.dataTransferObject.Seminaries.InfoCaseStudy;
import net.sourceforge.fenixedu.dataTransferObject.Seminaries.InfoCaseStudyChoice;
import net.sourceforge.fenixedu.dataTransferObject.Seminaries.InfoEquivalency;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants;
import net.sourceforge.fenixedu.util.TipoCurso;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 * 
 * 
 * Created at 1/Set/2003, 15:06:55
 *  
 */
public class ShowCandidacies extends FenixAction {

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
        HttpSession session = this.getSession(request);
        IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
        //
        List candidacies = new LinkedList();
        ActionForward destiny = null;
        List candidaciesExtendedInfo = new LinkedList();
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

    private void setAvaliableOptionsForInputQueries(HttpServletRequest request, IUserView userView)
            throws FenixActionException {
        List seminaries = null;
        List cases = null;
        List degrees = null;
        List modalities = null;
        List equivalencies = null;
        List themes = null;
        List curricularCoursesWithEquivalency = new LinkedList();
        try {
            Object[] argsReadSeminaries = { new Boolean(false) };
            Object[] argsReadCasesStudy = {};
            Object[] argsReadDegrees = {};
            Object[] argsReadModalities = {};
            Object[] argsReadThemes = {};
            Object[] argsReadEquivalencies = {};
            Object[] argsReadCurrentExecutionPeriod = {};
            seminaries = (List) ServiceManagerServiceFactory.executeService(userView,
                    "Seminaries.GetAllSeminaries", argsReadSeminaries);
            cases = (List) ServiceManagerServiceFactory.executeService(userView,
                    "Seminaries.GetAllCasesStudy", argsReadCasesStudy);
            degrees = (List) ServiceManagerServiceFactory.executeService(userView,
                    "manager.ReadDegreeCurricularPlans", argsReadDegrees);
            modalities = (List) ServiceManagerServiceFactory.executeService(userView,
                    "Seminaries.GetAllModalities", argsReadModalities);
            themes = (List) ServiceManagerServiceFactory.executeService(userView,
                    "Seminaries.GetAllThemes", argsReadThemes);
            equivalencies = (List) ServiceManagerServiceFactory.executeService(userView,
                    "Seminaries.GetAllEquivalencies", argsReadEquivalencies);
            ServiceManagerServiceFactory.executeService(userView, "ReadCurrentExecutionPeriod",
                    argsReadCurrentExecutionPeriod);
            //
            //
            for (Iterator iterator = equivalencies.iterator(); iterator.hasNext();) {
                InfoEquivalency equivalency = (InfoEquivalency) iterator.next();

                InfoCurricularCourse curricularCourse = equivalency.getCurricularCourse();
                curricularCoursesWithEquivalency.add(curricularCourse);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new FenixActionException(e);
        }
        //this is very ugly, but the boss asked for it :)
        //we will display only the Master Curricular Plans and the actual
        // (current execution period) curricular plans for other degree types
        List avaliableCurricularPlans = new LinkedList();
        for (Iterator iter = degrees.iterator(); iter.hasNext();) {
            InfoDegreeCurricularPlan infoDegreeCurricularPlan = (InfoDegreeCurricularPlan) iter.next();
            if (infoDegreeCurricularPlan.getInfoDegree().getTipoCurso().equals(TipoCurso.MESTRADO_OBJ))
                avaliableCurricularPlans.add(infoDegreeCurricularPlan);
            else if (infoDegreeCurricularPlan.getName().endsWith("2003/2004")) {
                String newName = new String();
                newName = infoDegreeCurricularPlan.getName().replaceAll("2003/2004", "");
                infoDegreeCurricularPlan.setName(newName);
                avaliableCurricularPlans.add(infoDegreeCurricularPlan);
            } else if (infoDegreeCurricularPlan.getName().indexOf("2") != -1) {
                String newName = new String();
                newName = infoDegreeCurricularPlan.getName().replaceAll("2003", "");
                infoDegreeCurricularPlan.setName(newName);
                avaliableCurricularPlans.add(infoDegreeCurricularPlan);
            }
        }
        Collections.sort(avaliableCurricularPlans, new BeanComparator("name"));

        request.setAttribute("seminaries", seminaries);
        request.setAttribute("cases", cases);
        request.setAttribute("degrees", avaliableCurricularPlans);
        request.setAttribute("modalities", modalities);
        request.setAttribute("courses", curricularCoursesWithEquivalency);
        request.setAttribute("themes", themes);
    }
}