/*
 * Created on Nov 21, 2003 by jpvl
 *  
 */
package ServidorApresentacao.Action.department;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.struts.action.DynaActionForm;

import DataBeans.InfoDepartment;
import DataBeans.InfoExecutionYear;
import DataBeans.InfoTeacher;
import DataBeans.teacher.professorship.DetailedProfessorship;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import Util.PeriodState;
import Util.RoleType;

/**
 * @author jpvl
 */
public class ReadTeacherProfessorshipsByExecutionYearAction extends AbstractReadProfessorshipsAction {

    /*
     * (non-Javadoc)
     * 
     * @see ServidorApresentacao.Action.department.AbstractReadProfessorshipsAction#getDetailedProfessorships(ServidorAplicacao.IUserView,
     *      java.lang.Integer, org.apache.struts.action.DynaActionForm,
     *      javax.servlet.http.HttpServletRequest)
     */
    List getDetailedProfessorships(IUserView userView, Integer teacherId, DynaActionForm actionForm,
            HttpServletRequest request) throws FenixServiceException {

        List detailedInfoProfessorshipList = (List) ServiceUtils.executeService(userView,
                "ReadDetailedTeacherProfessorshipsByExecutionYear", new Object[] { teacherId,
                        actionForm.get("executionYearId") });
        return detailedInfoProfessorshipList;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorApresentacao.Action.department.AbstractReadProfessorshipsAction#readExtraInformation(javax.servlet.http.HttpServletRequest,
     *      org.apache.struts.action.DynaActionForm)
     */
    protected void extraPreparation(IUserView userView, InfoTeacher infoTeacher,
            HttpServletRequest request, DynaActionForm dynaForm) throws FenixServiceException {

        prepareConstants(userView, infoTeacher, request);

        prepareForm(dynaForm, request);

    }

    /**
     * @param dynaForm
     */
    private void prepareForm(DynaActionForm dynaForm, HttpServletRequest request) {
        InfoExecutionYear infoExecutionYear = (InfoExecutionYear) request.getAttribute("executionYear");
        InfoTeacher infoTeacher = (InfoTeacher) request.getAttribute("infoTeacher");
        dynaForm.set("idInternal", infoTeacher.getIdInternal());
        dynaForm.set("teacherId", infoTeacher.getIdInternal());
        if (dynaForm.get("executionYearId") == null) {
            dynaForm.set("executionYearId", infoExecutionYear.getIdInternal());
        }

        List detailedProfessorshipList = (List) request.getAttribute("detailedProfessorshipList");

        List executionCourseIds = new ArrayList();
        Map hours = new HashMap();
        for (int i = 0; i < detailedProfessorshipList.size(); i++) {
            DetailedProfessorship dps = (DetailedProfessorship) detailedProfessorshipList.get(i);

            Integer executionCourseId = dps.getInfoProfessorship().getInfoExecutionCourse()
                    .getIdInternal();
            if (dps.getResponsibleFor().booleanValue()) {
                executionCourseIds.add(executionCourseId);
            }
            if (dps.getMasterDegreeOnly().booleanValue()) {
                hours
                        .put(executionCourseId.toString(), dps.getInfoProfessorship().getHours()
                                .toString());
            }
        }

        dynaForm.set("executionCourseResponsability", executionCourseIds.toArray(new Integer[] {}));
        dynaForm.set("hours", hours);

    }

    private void prepareConstants(IUserView userView, InfoTeacher infoTeacher, HttpServletRequest request)
            throws FenixServiceException {

        List executionYears = (List) ServiceUtils.executeService(userView,
                "ReadNotClosedExecutionYears", null);

        InfoExecutionYear infoExecutionYear = (InfoExecutionYear) CollectionUtils.find(executionYears,
                new Predicate() {
                    public boolean evaluate(Object arg0) {
                        InfoExecutionYear infoExecutionYearElem = (InfoExecutionYear) arg0;
                        if (infoExecutionYearElem.getState().equals(PeriodState.CURRENT)) {
                            return true;
                        }
                        return false;
                    }
                });

        Object args2[] = { infoTeacher };
        InfoDepartment teacherDepartment = (InfoDepartment) ServiceUtils.executeService(userView,
                "ReadDepartmentByTeacher", args2);

        if (!request.isUserInRole(RoleType.CREDITS_MANAGER.getName())) {
            Object args[] = { userView.getUtilizador() };

            InfoDepartment userDepartment = (InfoDepartment) ServiceUtils.executeService(userView,
                    "ReadDepartmentByUser", args);
            if (userDepartment != null) {
                request.setAttribute("isDepartmentManager", Boolean.valueOf(userDepartment
                        .equals(teacherDepartment)));
            } else {
                request.setAttribute("isDepartmentManager", Boolean.FALSE);
            }
        } else {
            request.setAttribute("isDepartmentManager", Boolean.FALSE);
        }

        request.setAttribute("teacherDepartment", teacherDepartment);

        request.setAttribute("executionYear", infoExecutionYear);
        request.setAttribute("executionYears", executionYears);
    }
}