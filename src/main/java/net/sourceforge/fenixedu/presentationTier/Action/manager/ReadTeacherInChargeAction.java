/*
 * Created on 16/Set/2003
 */
package net.sourceforge.fenixedu.presentationTier.Action.manager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.manager.ReadExecutionCourseByID;
import net.sourceforge.fenixedu.applicationTier.Servico.manager.ReadExecutionCourseResponsiblesIds;
import net.sourceforge.fenixedu.applicationTier.Servico.manager.ReadExecutionCourseTeachers;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoTeacher;
import net.sourceforge.fenixedu.dataTransferObject.teacher.InfoNonAffiliatedTeacher;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.security.Authenticate;

/**
 * @author lmac1
 */
public class ReadTeacherInChargeAction extends FenixAction {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws FenixActionException {

        User userView = Authenticate.getUser();
        String executionCourseId = request.getParameter("executionCourseId");

        InfoExecutionCourse infoExecutionCourse = null;
        try {
            infoExecutionCourse = ReadExecutionCourseByID.runReadExecutionCourseManagerByID(executionCourseId);

        } catch (FenixServiceException fenixServiceException) {
            throw new FenixActionException(fenixServiceException.getMessage());
        }

        List<InfoTeacher> infoTeachersList = null;

        try {
            infoTeachersList = ReadExecutionCourseTeachers.runReadExecutionCourseTeachers(executionCourseId);

        } catch (FenixServiceException fenixServiceException) {
            throw new FenixActionException(fenixServiceException.getMessage());
        }

        List infoNonAffiliatedTeachers = infoExecutionCourse.getNonAffiliatedTeachers();
        if (infoTeachersList != null) {
            List<String> teachersIds = new ArrayList<String>();
            Integer teacherId;
            Iterator<InfoTeacher> iter = infoTeachersList.iterator();
            while (iter.hasNext()) {
                teachersIds.add(iter.next().getExternalId());
            }

            String[] professorShipTeachersIds = teachersIds.toArray(new String[] {});
            DynaActionForm newForm = (DynaActionForm) form;
            newForm.set("professorShipTeachersIds", professorShipTeachersIds);

            List<String> nonAffiliatedTeacherIDs = new ArrayList<String>();

            if (infoNonAffiliatedTeachers != null) {
                for (Iterator iterator = infoNonAffiliatedTeachers.iterator(); iterator.hasNext();) {
                    InfoNonAffiliatedTeacher infoNonAffiliatedTeacher = (InfoNonAffiliatedTeacher) iterator.next();
                    nonAffiliatedTeacherIDs.add(infoNonAffiliatedTeacher.getExternalId());
                }

                String[] nonAffiliatedTeacherArrayIDs = nonAffiliatedTeacherIDs.toArray(new String[] {});
                newForm.set("nonAffiliatedTeachersIds", nonAffiliatedTeacherArrayIDs);
            }

            List<String> responsiblesIds = null;
            try {
                responsiblesIds = ReadExecutionCourseResponsiblesIds.runReadExecutionCourseResponsiblesIds(executionCourseId);

            } catch (FenixServiceException fenixServiceException) {
                throw new FenixActionException(fenixServiceException.getMessage());
            }

            String[] responsibleTeachersIds = responsiblesIds.toArray(new String[] {});
            newForm.set("responsibleTeachersIds", responsibleTeachersIds);
            request.setAttribute("infoTeachersList", infoTeachersList);
            request.setAttribute("infoNonAffiliatedTeachers", infoNonAffiliatedTeachers);

        }
        request.setAttribute("executionCourseName", infoExecutionCourse.getNome());
        return mapping.findForward("readExecutionCourseTeachers");
    }
}