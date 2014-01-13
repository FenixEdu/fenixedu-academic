/*
 * Created on 16/Set/2003
 */
package net.sourceforge.fenixedu.presentationTier.Action.manager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.manager.EditCurriculum;
import net.sourceforge.fenixedu.applicationTier.Servico.manager.ReadCurriculum;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurriculum;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Curriculum;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.NonExistingActionException;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.security.Authenticate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;
import pt.ist.fenixframework.FenixFramework;

/**
 * @author lmac1
 */

@Mapping(module = "manager", path = "/editCurriculum", input = "/editCurriculum.do?method=prepareEdit&page=0",
        attribute = "curriculumForm", formBean = "curriculumForm", scope = "request", parameter = "method")
@Forwards(value = {
        @Forward(name = "readDegreeCurricularPlan", path = "/readDegreeCurricularPlan.do"),
        @Forward(name = "readCurricularCourse", path = "/readCurricularCourse.do"),
        @Forward(name = "editCurriculum", path = "/manager/editCurriculum_bd.jsp", tileProperties = @Tile(
                navLocal = "/manager/curricularCourseNavLocalManager.jsp")),
        @Forward(name = "editCurriculumEnglish", path = "/manager/editCurriculumEnglish_bd.jsp", tileProperties = @Tile(
                navLocal = "/manager/curricularCourseNavLocalManager.jsp")) })
@Exceptions(value = { @ExceptionHandling(
        type = net.sourceforge.fenixedu.presentationTier.Action.exceptions.NonExistingActionException.class,
        key = "resources.Action.exceptions.NonExistingActionException",
        handler = net.sourceforge.fenixedu.presentationTier.config.FenixErrorExceptionHandler.class, scope = "request") })
public class EditCurriculumDA extends FenixDispatchAction {

    private static final Logger logger = LoggerFactory.getLogger(EditCurriculumDA.class);

    public ActionForward prepareEdit(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {

        User userView = Authenticate.getUser();

        DynaActionForm curriculumForm = (DynaActionForm) form;

        String language = request.getParameter("language");

        InfoCurriculum infoCurriculum = null;

        try {
            infoCurriculum = ReadCurriculum.run(request.getParameter("curricularCourseId"));

        } catch (NonExistingServiceException e) {
            throw new NonExistingActionException("message.nonExistingCurricularCourse",
                    mapping.findForward("readDegreeCurricularPlan"));
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
                curriculumForm.set("operacionalObjectivesEn", infoCurriculum.getOperacionalObjectivesEn());
                curriculumForm.set("programEn", infoCurriculum.getProgramEn());
            }
        }

        request.setAttribute("executionYears", ExecutionYear.readNotClosedExecutionYears());

        if (language == null) {
            return mapping.findForward("editCurriculum");
        }

        return mapping.findForward("editCurriculumEnglish");
    }

    public ActionForward edit(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws FenixActionException {

        User userView = Authenticate.getUser();

        DynaActionForm editForm = (DynaActionForm) form;

        InfoCurriculum infoCurriculum = new InfoCurriculum();
        final CurricularCourse curricularCourse =
                (CurricularCourse) FenixFramework.getDomainObject(request.getParameter("curricularCourseId"));
        InfoCurricularCourse infoCurricularCourse = new InfoCurricularCourse(curricularCourse);
        Curriculum curriculum = curricularCourse.findLatestCurriculum();

        infoCurriculum.setInfoCurricularCourse(infoCurricularCourse);

        String objectives = (String) editForm.get("generalObjectives");
        infoCurriculum.setGeneralObjectives((StringUtils.isEmpty(objectives) && curriculum != null) ? curriculum
                .getGeneralObjectives() : objectives);

        String operationalObjectives = (String) editForm.get("operacionalObjectives");
        infoCurriculum.setOperacionalObjectives(StringUtils.isEmpty(operationalObjectives) && curriculum != null ? curriculum
                .getOperacionalObjectives() : operationalObjectives);

        String program = (String) editForm.get("program");
        infoCurriculum.setProgram(StringUtils.isEmpty(program) && curriculum != null ? curriculum.getProgram() : program);

        String objectivesEn = (String) editForm.get("generalObjectivesEn");
        infoCurriculum.setGeneralObjectivesEn(StringUtils.isEmpty(objectivesEn) && curriculum != null ? curriculum
                .getGeneralObjectivesEn() : objectivesEn);

        String operationalObjectivesEn = (String) editForm.get("operacionalObjectivesEn");
        infoCurriculum.setOperacionalObjectivesEn(StringUtils.isEmpty(operationalObjectivesEn) && curriculum != null ? curriculum
                .getOperacionalObjectivesEn() : operationalObjectivesEn);

        String programEn = (String) editForm.get("programEn");
        infoCurriculum.setProgramEn(StringUtils.isEmpty(programEn) && curriculum != null ? curriculum.getProgramEn() : programEn);

        String executionYearId = (String) editForm.get("executionYearId");
        infoCurriculum.setExecutionYearId(executionYearId);

        try {
            EditCurriculum.run(infoCurriculum, request.getParameter("language"), userView.getUsername());

        } catch (NonExistingServiceException nonExistingServiceException) {
            logger.error(nonExistingServiceException.getMessage(), nonExistingServiceException);
            throw new NonExistingActionException("message.nonExistingCurricularCourse",
                    mapping.findForward("readDegreeCurricularPlan"));
        } catch (FenixServiceException fenixServiceException) {
            logger.error(fenixServiceException.getMessage(), fenixServiceException);
            throw new FenixActionException(fenixServiceException.getMessage());
        }
        return mapping.findForward("readCurricularCourse");
    }
}