/*
 * Created on 16/Set/2003
 */
package net.sourceforge.fenixedu.presentationTier.Action.manager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurriculum;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.NonExistingActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

/**
 * @author lmac1
 */

public class EditCurriculumDA extends FenixDispatchAction {

    public ActionForward prepareEdit(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, FenixFilterException {

        IUserView userView = SessionUtils.getUserView(request);

        DynaActionForm curriculumForm = (DynaActionForm) form;

        Integer curricularCourseId = new Integer(request.getParameter("curricularCourseId"));
        String language = request.getParameter("language");

        InfoCurriculum infoCurriculum = null;

        Object args[] = { curricularCourseId };

        try {
            infoCurriculum = (InfoCurriculum) ServiceUtils.executeService(userView, "ReadCurriculum",
                    args);

        } catch (NonExistingServiceException e) {
            throw new NonExistingActionException("message.nonExistingCurricularCourse", mapping
                    .findForward("readDegreeCurricularPlan"));
        } catch (FenixServiceException fenixServiceException) {
            throw new FenixActionException(fenixServiceException.getMessage());
        }

        if (infoCurriculum != null) {
            if (language == null) {
                curriculumForm.set("generalObjectives", infoCurriculum.getGeneralObjectives());
                curriculumForm.set("operacionalObjectives", infoCurriculum.getOperacionalObjectives());
                curriculumForm.set("program", infoCurriculum.getProgram());
            } else {
                curriculumForm.set("generalObjectivesEn", infoCurriculum.getGeneralObjectivesEn());
                curriculumForm.set("operacionalObjectivesEn", infoCurriculum
                        .getOperacionalObjectivesEn());
                curriculumForm.set("programEn", infoCurriculum.getProgramEn());
            }
        }

        if (language == null)
            return mapping.findForward("editCurriculum");

        return mapping.findForward("editCurriculumEnglish");
    }

    public ActionForward edit(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, FenixFilterException {

        IUserView userView = SessionUtils.getUserView(request);

        DynaActionForm editForm = (DynaActionForm) form;

        InfoCurriculum infoCurriculum = new InfoCurriculum();
        final CurricularCourse curricularCourse = (CurricularCourse) rootDomainObject.
        		readDegreeModuleByOID(Integer.valueOf(request.getParameter("curricularCourseId")));
        InfoCurricularCourse infoCurricularCourse = new InfoCurricularCourse(curricularCourse);
        infoCurriculum.setInfoCurricularCourse(infoCurricularCourse);
        infoCurriculum.setGeneralObjectives((String) editForm.get("generalObjectives"));
        infoCurriculum.setOperacionalObjectives((String) editForm.get("operacionalObjectives"));
        infoCurriculum.setProgram((String) editForm.get("program"));
        infoCurriculum.setGeneralObjectivesEn((String) editForm.get("generalObjectivesEn"));
        infoCurriculum.setOperacionalObjectivesEn((String) editForm.get("operacionalObjectivesEn"));
        infoCurriculum.setProgramEn((String) editForm.get("programEn"));

        Object args[] = { infoCurriculum, request.getParameter("language"), userView.getUtilizador() };

        try {
            ServiceUtils.executeService(userView, "EditCurriculumByManager", args);

        } catch (NonExistingServiceException nonExistingServiceException) {
            nonExistingServiceException.printStackTrace();
            throw new NonExistingActionException("message.nonExistingCurricularCourse", mapping
                    .findForward("readDegreeCurricularPlan"));
        } catch (FenixServiceException fenixServiceException) {
            fenixServiceException.printStackTrace();
            throw new FenixActionException(fenixServiceException.getMessage());
        }
        return mapping.findForward("readCurricularCourse");
    }
}