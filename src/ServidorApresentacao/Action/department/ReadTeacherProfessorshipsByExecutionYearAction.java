/*
 * Created on Nov 21, 2003 by jpvl
 *  
 */
package ServidorApresentacao.Action.department;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.Transformer;
import org.apache.struts.action.DynaActionForm;

import DataBeans.InfoDepartment;
import DataBeans.InfoExecutionYear;
import DataBeans.InfoTeacher;
import DataBeans.teacher.professorship.DetailedProfessorship;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import Util.PeriodState;

/**
 * @author jpvl
 */
public class ReadTeacherProfessorshipsByExecutionYearAction extends AbstractReadProfessorshipsAction
{

    /*
	 * (non-Javadoc)
	 * 
	 * @see ServidorApresentacao.Action.department.AbstractReadProfessorshipsAction#getDetailedProfessorships(ServidorAplicacao.IUserView,
	 *          java.lang.Integer, org.apache.struts.action.DynaActionForm,
	 *          javax.servlet.http.HttpServletRequest)
	 */
    List getDetailedProfessorships(IUserView userView, Integer teacherId, DynaActionForm actionForm,
            HttpServletRequest request) throws FenixServiceException
    {
        
        List detailedInfoProfessorshipList = (List) ServiceUtils.executeService(userView,
                "ReadDetailedTeacherProfessorshipsByExecutionYear", new Object[]{teacherId, actionForm.get("executionYearId")});
        return detailedInfoProfessorshipList;
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see ServidorApresentacao.Action.department.AbstractReadProfessorshipsAction#readExtraInformation(javax.servlet.http.HttpServletRequest,
	 *          org.apache.struts.action.DynaActionForm)
	 */
    protected void extraPreparation(IUserView userView, InfoTeacher infoTeacher,
            HttpServletRequest request, DynaActionForm dynaForm) throws FenixServiceException
    {

        prepareConstants(userView, infoTeacher, request);

        prepareForm(dynaForm, request);

    }

    /**
	 * @param dynaForm
	 */
    private void prepareForm(DynaActionForm dynaForm, HttpServletRequest request)
    {
        InfoExecutionYear infoExecutionYear = (InfoExecutionYear) request.getAttribute("executionYear");
        InfoTeacher infoTeacher = (InfoTeacher) request.getAttribute("infoTeacher");
        dynaForm.set("idInternal", infoTeacher.getIdInternal());
        dynaForm.set("teacherId", infoTeacher.getIdInternal());
        if(dynaForm.get("executionYearId") == null) {
            dynaForm.set("executionYearId", infoExecutionYear.getIdInternal());
        }

        List detailedProfessorshipList = (List) request.getAttribute("detailedProfessorshipList");

        List predicatedList = (List) CollectionUtils.select(detailedProfessorshipList, new Predicate()
        {

            public boolean evaluate(Object input)
            {
                DetailedProfessorship detailedProfessorship = (DetailedProfessorship) input;
                return detailedProfessorship.getResponsibleFor().booleanValue();
            }
        });

        List executionCourseIds = (List) CollectionUtils.collect(predicatedList, new Transformer()
        {

            public Object transform(Object input)
            {
                DetailedProfessorship detailedProfessorship = (DetailedProfessorship) input;
                Integer executionCourseId = detailedProfessorship.getInfoProfessorship()
                        .getInfoExecutionCourse().getIdInternal();
                return executionCourseId;
            }
        });
        
        dynaForm.set("executionCourseResponsability", executionCourseIds.toArray(new Integer [] {}));
    }

    private void prepareConstants(IUserView userView, InfoTeacher infoTeacher, HttpServletRequest request)
            throws FenixServiceException
    {

        List executionYears = (List) ServiceUtils.executeService(userView, "ReadNotClosedExecutionYears", null);
        
        InfoExecutionYear infoExecutionYear = (InfoExecutionYear) CollectionUtils.find(executionYears, new Predicate() {
            public boolean evaluate(Object arg0) {
                InfoExecutionYear infoExecutionYearElem = (InfoExecutionYear)arg0;
                if(infoExecutionYearElem.getState().equals(PeriodState.CURRENT)) {
                    return true;
                }
                return false;
            }
        });

//        InfoExecutionYear infoExecutionYear = (InfoExecutionYear) ServiceUtils.executeService(userView,
//                "ReadCurrentExecutionYear", null);

        Object args[] = {userView.getUtilizador()};
        InfoDepartment userDepartment = (InfoDepartment) ServiceUtils.executeService(userView,
                "ReadDepartmentByUser", args);

        args[0] = infoTeacher;
        InfoDepartment teacherDepartment = (InfoDepartment) ServiceUtils.executeService(userView,
                "ReadDepartmentByTeacher", args);

        request.setAttribute("teacherDepartment", teacherDepartment);

        request.setAttribute("isDepartmentManager", Boolean.valueOf(userDepartment
                .equals(teacherDepartment)));

        request.setAttribute("executionYear", infoExecutionYear);
        request.setAttribute("executionYears", executionYears);
    }
}