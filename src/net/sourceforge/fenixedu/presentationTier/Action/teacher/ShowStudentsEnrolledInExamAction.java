/*
 * Created on 15/May/2003
 *
 * 
 */
package net.sourceforge.fenixedu.presentationTier.Action.teacher;

/**
 * @author lmac1
 *  
 */
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExamStudentRoom;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteTeacherStudentsEnrolledList;
import net.sourceforge.fenixedu.dataTransferObject.SiteView;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.commons.collections.comparators.ReverseComparator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class ShowStudentsEnrolledInExamAction extends FenixAction {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, FenixFilterException {

        HttpSession session = request.getSession(false);

        IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

        String executionCourseCodeString = request.getParameter("objectCode");
        Integer executionCourseCode = new Integer(executionCourseCodeString);

        String examCodeString = request.getParameter("evaluationCode");
        Integer examCode = new Integer(examCodeString);

        Object[] args = { executionCourseCode, examCode };
        SiteView siteView = null;
        try {
            siteView = (SiteView) ServiceUtils.executeService(userView, "ReadStudentsEnrolledInExam",
                    args);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        InfoSiteTeacherStudentsEnrolledList infoSiteTeacherStudentsEnrolledList = (InfoSiteTeacherStudentsEnrolledList) siteView
                .getComponent();

        ComparatorChain comparatorChain = new ComparatorChain();

        comparatorChain.addComparator(new ReverseComparator(new BeanComparator(
                "infoRoom.capacidadeExame")));
        comparatorChain.addComparator(new BeanComparator("infoRoom.nome"));
        comparatorChain.addComparator(new BeanComparator("infoStudent.number"));

        //Select all the objects with room to later sort them
        List infoExamStudentRoomList = (List) CollectionUtils.select(infoSiteTeacherStudentsEnrolledList
                .getInfoExamStudentRoomList(), new Predicate() {
            public boolean evaluate(Object input) {
                InfoExamStudentRoom infoExamStudentRoom = (InfoExamStudentRoom) input;
                return infoExamStudentRoom.getInfoRoom() != null;
            }
        });

        Collections.sort(infoExamStudentRoomList, comparatorChain);
        Collection collection = CollectionUtils.subtract(infoSiteTeacherStudentsEnrolledList
                .getInfoExamStudentRoomList(), infoExamStudentRoomList);
        infoSiteTeacherStudentsEnrolledList.setInfoExamStudentRoomList((List) collection);
        infoSiteTeacherStudentsEnrolledList.getInfoExamStudentRoomList().addAll(infoExamStudentRoomList);

        request.setAttribute("siteView", siteView);
        request.setAttribute("objectCode", executionCourseCode);
        request.setAttribute("evaluationCode", examCode);
        return mapping.findForward("showStudents");

    }
}