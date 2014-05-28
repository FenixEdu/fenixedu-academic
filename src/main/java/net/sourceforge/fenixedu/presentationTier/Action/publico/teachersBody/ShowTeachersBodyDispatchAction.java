/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * Created on 19/Dez/2003
 *  
 */
package net.sourceforge.fenixedu.presentationTier.Action.publico.teachersBody;

import java.text.Collator;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.commons.ReadExecutionDegreeByOID;
import net.sourceforge.fenixedu.applicationTier.Servico.commons.ReadNotClosedExecutionYears;
import net.sourceforge.fenixedu.applicationTier.Servico.department.ReadAllDepartments;
import net.sourceforge.fenixedu.applicationTier.Servico.department.ReadDepartmentByOID;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.publico.teachersBody.ReadProfessorshipsAndResponsibilitiesByDepartmentAndExecutionPeriod;
import net.sourceforge.fenixedu.applicationTier.Servico.publico.teachersBody.ReadProfessorshipsAndResponsibilitiesByExecutionDegreeAndExecutionPeriod;
import net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager.ReadExecutionDegreesByExecutionYearId;
import net.sourceforge.fenixedu.dataTransferObject.InfoDepartment;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionYear;
import net.sourceforge.fenixedu.dataTransferObject.comparators.ComparatorByNameForInfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.teacher.professorship.DetailedProfessorship;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

/**
 * @author <a href="mailto:joao.mota@ist.utl.pt">Jo�o Mota </a> 19/Dez/2003
 * 
 */
@Mapping(module = "publico", path = "/searchProfessorships", attribute = "teachersBodyForm", formBean = "teachersBodyForm",
        scope = "session", parameter = "method")
@Forwards(value = { @Forward(name = "showForm", path = "searchProfessorships"),
        @Forward(name = "showProfessorships", path = "showProfessorships") })
public class ShowTeachersBodyDispatchAction extends FenixDispatchAction {

    private String makeBodyHeader(String executionYear, Integer semester, Integer teacherType) {

        String sem = semester.intValue() == 0 ? "Ambos Semestres" : (semester.intValue() + "&ordm; Semestre");
        String teacher = teacherType.intValue() == 0 ? "Todos os Docentes" : "Apenas Respons&aacute;veis";
        String header = "Ano Lectivo " + executionYear + " - " + sem + " - " + teacher;

        return header;

    }

    public ActionForward prepareForm(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {

        DynaActionForm executionYearForm = (DynaActionForm) actionForm;
        String executionYearId = (String) executionYearForm.get("executionYearId");
        Integer semester = (Integer) executionYearForm.get("semester");
        Integer teacherType = (Integer) executionYearForm.get("teacherType");

        try {

            List executionDegrees = ReadExecutionDegreesByExecutionYearId.run(executionYearId);
            List executionYears = ReadNotClosedExecutionYears.run();
            List departments = ReadAllDepartments.run();

            if (executionDegrees != null && executionDegrees.size() > 0) {
                // put execution year in the form
                if (StringUtils.isEmpty(executionYearId)) {
                    executionYearId =
                            ((InfoExecutionDegree) executionDegrees.iterator().next()).getInfoExecutionYear().getExternalId();

                    executionYearForm.set("executionYearId", executionYearId);
                }
                // initialize semester
                if (semester == null) {
                    semester = new Integer(0);
                }
                // initialize teacherType
                if (teacherType == null) {
                    teacherType = new Integer(0);
                }

                Collections.sort(executionDegrees, new ComparatorByNameForInfoExecutionDegree());
            }

            Iterator iter = executionYears.iterator();
            while (iter.hasNext()) {
                InfoExecutionYear year = (InfoExecutionYear) iter.next();
                if (year.getExternalId().equals(executionYearId)) {
                    request.setAttribute("searchDetails", makeBodyHeader(year.getYear(), semester, teacherType));
                    break;
                }
            }

            if (semester != null) {
                request.setAttribute("semester", semester);
            }
            if (teacherType != null) {
                request.setAttribute("teacherType", teacherType);
            }

            request.setAttribute("executionYearId", executionYearId);
            request.setAttribute("executionDegrees", executionDegrees);
            request.setAttribute("executionYears", executionYears);
            request.setAttribute("departments", departments);

        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        return mapping.findForward("showForm");
    }

    public ActionForward showProfessorshipsByExecutionDegree(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException {

        DynaActionForm executionDegreeForm = (DynaActionForm) actionForm;
        String executionDegreeId = (String) executionDegreeForm.get("executionDegreeId");

        Integer semester = (Integer) executionDegreeForm.get("semester");
        Integer teacherType = (Integer) executionDegreeForm.get("teacherType");
        String searchDetails = (String) executionDegreeForm.get("searchDetails");
        try {

            List detailedProfessorShipsListofLists =
                    ReadProfessorshipsAndResponsibilitiesByExecutionDegreeAndExecutionPeriod.run(executionDegreeId, semester,
                            teacherType);

            if ((detailedProfessorShipsListofLists != null) && (!detailedProfessorShipsListofLists.isEmpty())) {

                Collections.sort(detailedProfessorShipsListofLists, new Comparator() {

                    @Override
                    public int compare(Object o1, Object o2) {

                        List list1 = (List) o1;
                        List list2 = (List) o2;
                        DetailedProfessorship dt1 = (DetailedProfessorship) list1.iterator().next();
                        DetailedProfessorship dt2 = (DetailedProfessorship) list2.iterator().next();

                        return Collator.getInstance().compare(dt1.getInfoProfessorship().getInfoExecutionCourse().getNome(),
                                dt2.getInfoProfessorship().getInfoExecutionCourse().getNome());
                    }

                });

                request.setAttribute("detailedProfessorShipsListofLists", detailedProfessorShipsListofLists);
            }

            InfoExecutionDegree degree = ReadExecutionDegreeByOID.run(executionDegreeId);

            request.setAttribute("searchType", "Consulta Por Curso");
            request.setAttribute("searchTarget", degree.getInfoDegreeCurricularPlan().getInfoDegree().getDegreeType() + " em "
                    + degree.getInfoDegreeCurricularPlan().getInfoDegree().getNome());
            request.setAttribute("searchDetails", searchDetails);
            request.setAttribute("semester", semester);
            request.setAttribute("teacherType", teacherType);
            request.setAttribute("executionDegree", degree);

        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        return mapping.findForward("showProfessorships");
    }

    public ActionForward showTeachersBodyByDepartment(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {

        DynaActionForm departmentForm = (DynaActionForm) actionForm;
        String departmentId = (String) departmentForm.get("departmentId");
        String executionYearId = (String) departmentForm.get("executionYearId");

        Integer semester = (Integer) departmentForm.get("semester");
        Integer teacherType = (Integer) departmentForm.get("teacherType");
        String searchDetails = (String) departmentForm.get("searchDetails");

        try {

            List detailedProfessorShipsListofLists =
                    ReadProfessorshipsAndResponsibilitiesByDepartmentAndExecutionPeriod.run(departmentId, executionYearId,
                            semester, teacherType);

            if ((detailedProfessorShipsListofLists != null) && (!detailedProfessorShipsListofLists.isEmpty())) {

                Collections.sort(detailedProfessorShipsListofLists, new Comparator() {

                    @Override
                    public int compare(Object o1, Object o2) {

                        List list1 = (List) o1;
                        List list2 = (List) o2;
                        DetailedProfessorship dt1 = (DetailedProfessorship) list1.iterator().next();
                        DetailedProfessorship dt2 = (DetailedProfessorship) list2.iterator().next();

                        return Collator.getInstance().compare(dt1.getInfoProfessorship().getInfoExecutionCourse().getNome(),
                                dt2.getInfoProfessorship().getInfoExecutionCourse().getNome());
                    }

                });
                request.setAttribute("detailedProfessorShipsListofLists", detailedProfessorShipsListofLists);
            }

            InfoDepartment department = ReadDepartmentByOID.run(departmentId);

            request.setAttribute("searchType", "Consulta Por Departmento");
            request.setAttribute("searchTarget", department.getName());
            request.setAttribute("searchDetails", searchDetails);
            request.setAttribute("semester", semester);
            request.setAttribute("teacherType", teacherType);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        return mapping.findForward("showProfessorships");
    }

}