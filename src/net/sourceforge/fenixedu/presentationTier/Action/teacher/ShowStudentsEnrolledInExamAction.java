/*
 * Created on 15/May/2003
 *
 * 
 */
package net.sourceforge.fenixedu.presentationTier.Action.teacher;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteTeacherStudentsEnrolledList;
import net.sourceforge.fenixedu.dataTransferObject.InfoWrittenEvaluationEnrolment;
import net.sourceforge.fenixedu.dataTransferObject.SiteView;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.commons.collections.comparators.ReverseComparator;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class ShowStudentsEnrolledInExamAction extends FenixAction {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, FenixFilterException {

        final IUserView userView = getUserView(request);
        
        final Integer executionCourseCode = Integer.valueOf(request.getParameter("objectCode"));
        final Integer writtenEvaluationCode = Integer.valueOf(request.getParameter("evaluationCode"));

        final Object[] args = { executionCourseCode, writtenEvaluationCode };
        SiteView siteView = null;
        try {
            siteView = (SiteView) ServiceUtils.executeService(userView, "ReadStudentsEnrolledInWrittenEvaluation",
                    args);
        } catch (FenixServiceException e) {
            ActionErrors actionErrors = new ActionErrors();
            actionErrors.add(e.getMessage(), new ActionError(e.getMessage()));
            saveErrors(request, actionErrors);
            return mapping.getInputForward();
        }

        InfoSiteTeacherStudentsEnrolledList infoSiteTeacherStudentsEnrolledList = (InfoSiteTeacherStudentsEnrolledList) siteView
                .getComponent();

        ComparatorChain comparatorChain = new ComparatorChain();

        comparatorChain.addComparator(new ReverseComparator(new BeanComparator(
                "infoRoom.capacidadeExame")));
        comparatorChain.addComparator(new BeanComparator("infoRoom.nome"));
        comparatorChain.addComparator(new BeanComparator("infoStudent.number"));

        //Select all the objects with room to later sort them
        List infoWrittenEvaluationEnrolmentList = (List) CollectionUtils.select(infoSiteTeacherStudentsEnrolledList
                .getInfoWrittenEvaluationEnrolmentList(), new Predicate() {
            public boolean evaluate(Object input) {
                InfoWrittenEvaluationEnrolment infoWrittenEvaluationEnrolment = (InfoWrittenEvaluationEnrolment) input;
                return infoWrittenEvaluationEnrolment.getInfoRoom() != null;
            }
        });

        Collections.sort(infoWrittenEvaluationEnrolmentList, comparatorChain);
        Collection collection = CollectionUtils.subtract(infoSiteTeacherStudentsEnrolledList
                .getInfoWrittenEvaluationEnrolmentList(), infoWrittenEvaluationEnrolmentList);
        infoSiteTeacherStudentsEnrolledList.setInfoWrittenEvaluationEnrolmentList((List) collection);
        infoSiteTeacherStudentsEnrolledList.getInfoWrittenEvaluationEnrolmentList().addAll(infoWrittenEvaluationEnrolmentList);

        request.setAttribute("siteView", siteView);
        request.setAttribute("objectCode", executionCourseCode);
        request.setAttribute("evaluationCode", writtenEvaluationCode);
        
        return mapping.findForward("showStudents");

    }
}