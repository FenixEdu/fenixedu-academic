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
import net.sourceforge.fenixedu.domain.Curriculum;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.NonExistingActionException;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.ServiceUtils;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import pt.ist.fenixWebFramework.security.UserView;

/**
 * @author lmac1
 */

public class EditCurriculumDA extends FenixDispatchAction {

    public ActionForward prepareEdit(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, FenixFilterException {

        IUserView userView = UserView.getUser();

        DynaActionForm curriculumForm = (DynaActionForm) form;

        Integer curricularCourseId = new Integer(request.getParameter("curricularCourseId"));
        String language = request.getParameter("language");

        InfoCurriculum infoCurriculum = null;

        Object args[] = { curricularCourseId };

        try {
            infoCurriculum = (InfoCurriculum) ServiceUtils.executeService("ReadCurriculum",
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

        request.setAttribute("executionYears", ExecutionYear.readNotClosedExecutionYears());
        
        if (language == null)
            return mapping.findForward("editCurriculum");

        return mapping.findForward("editCurriculumEnglish");
    }

    public ActionForward edit(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, FenixFilterException {

        IUserView userView = UserView.getUser();

        DynaActionForm editForm = (DynaActionForm) form;

        InfoCurriculum infoCurriculum = new InfoCurriculum();
        final CurricularCourse curricularCourse = (CurricularCourse) rootDomainObject.
        		readDegreeModuleByOID(Integer.valueOf(request.getParameter("curricularCourseId")));
        InfoCurricularCourse infoCurricularCourse = new InfoCurricularCourse(curricularCourse);
        Curriculum curriculum = curricularCourse.findLatestCurriculum();
        
        infoCurriculum.setInfoCurricularCourse(infoCurricularCourse);
        
        String objectives = (String) editForm.get("generalObjectives");
        infoCurriculum.setGeneralObjectives((StringUtils.isEmpty(objectives) && curriculum != null) ? curriculum.getGeneralObjectives() : objectives );
        
        String operationalObjectives = (String) editForm.get("operacionalObjectives");
        infoCurriculum.setOperacionalObjectives(StringUtils.isEmpty(operationalObjectives) && curriculum != null? curriculum.getOperacionalObjectives() : operationalObjectives);
                
	String program = (String) editForm.get("program");
	infoCurriculum.setProgram(StringUtils.isEmpty(program) && curriculum != null ? curriculum.getProgram() : program);
	
        String objectivesEn = (String) editForm.get("generalObjectivesEn");
	infoCurriculum.setGeneralObjectivesEn(StringUtils.isEmpty(objectivesEn) && curriculum != null ? curriculum.getGeneralObjectivesEn() : objectivesEn);
        
	String operationalObjectivesEn = (String) editForm.get("operacionalObjectivesEn");
	infoCurriculum.setOperacionalObjectivesEn(StringUtils.isEmpty(operationalObjectivesEn) && curriculum != null ? curriculum.getOperacionalObjectivesEn() : operationalObjectivesEn);
        
	String programEn = (String) editForm.get("programEn");
	infoCurriculum.setProgramEn(StringUtils.isEmpty(programEn) && curriculum != null ? curriculum.getProgramEn() : programEn);

	Integer executionYearId = (Integer)editForm.get("executionYearId");
	infoCurriculum.setExecutionYearId(executionYearId);
	
        Object args[] = { infoCurriculum, request.getParameter("language"), userView.getUtilizador() };

        try {
            ServiceUtils.executeService("EditCurriculumByManager", args);

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