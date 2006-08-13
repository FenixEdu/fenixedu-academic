/*
 * Created on 19/Dez/2003
 *  
 */
package net.sourceforge.fenixedu.presentationTier.Action.publico.teachersBody;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegreeCurricularPlan;
import net.sourceforge.fenixedu.dataTransferObject.InfoDepartment;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionYear;
import net.sourceforge.fenixedu.dataTransferObject.comparators.ComparatorByNameForInfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.teacher.professorship.DetailedProfessorship;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

/**
 * @author <a href="mailto:joao.mota@ist.utl.pt">Jo�o Mota </a> 19/Dez/2003
 *  
 */
public class ShowTeachersBodyDispatchAction extends FenixDispatchAction {
	
	private String makeBodyHeader(String executionYear, Integer semester, Integer teacherType) {
		
		String sem = semester.intValue() == 0 ? "Ambos Semestres" : (semester.intValue() + "&ordm; Semestre");
		String teacher = teacherType.intValue() == 0 ? "Todos os Docentes" : "Apenas Respons&aacute;veis";		
		String header = "Ano Lectivo " + executionYear + " - " + sem + " - " + teacher;
		
		return header;
		
	}

    public ActionForward prepareForm(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException, FenixFilterException {

        DynaActionForm executionYearForm = (DynaActionForm) actionForm;
        Integer executionYearId = (Integer) executionYearForm.get("executionYearId");
        Integer semester = (Integer)executionYearForm.get("semester");
        Integer teacherType = (Integer)executionYearForm.get("teacherType");
        
        try {
        	
            Object[] args = { executionYearId };

            List executionDegrees = (List) ServiceUtils.executeService(null,
                    "ReadExecutionDegreesByExecutionYearId", args);

            List executionYears = (List) ServiceUtils
                    .executeService(null, "ReadNotClosedExecutionYears", null);
            
            List departments = (List) ServiceUtils.executeService(null, "ReadAllDepartments", null);
            
            if (executionDegrees != null && executionDegrees.size() > 0) {
                //put execution year in the form
                if (executionYearId == null) {
                    executionYearId = ((InfoExecutionDegree) executionDegrees.get(0))
                            .getInfoExecutionYear().getIdInternal();

                    executionYearForm.set("executionYearId", executionYearId);
                }
                //initialize semester
                if(semester == null) {
                	semester = new Integer(0);
                }
                //initialize teacherType
                if(teacherType == null) {
                	teacherType = new Integer(0);
                }
                
                Collections.sort(executionDegrees, new ComparatorByNameForInfoExecutionDegree());
            }

            Iterator iter = executionYears.iterator();
            while(iter.hasNext()) {
            	InfoExecutionYear year = (InfoExecutionYear)iter.next();
            	if(year.getIdInternal().intValue() == executionYearId.intValue()) {
                	request.setAttribute("searchDetails", makeBodyHeader(year.getYear(), semester, teacherType));
                	break;
            	}
            }
            
            if(semester != null)
            	request.setAttribute("semester", semester);
            if(teacherType != null)
            	request.setAttribute("teacherType", teacherType);

            request.setAttribute("executionYearId", executionYearId);
            request.setAttribute("executionDegrees", executionDegrees);
            request.setAttribute("executionYears", executionYears);
            request.setAttribute("departments", departments);

        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        return mapping.findForward("showForm");
    }

    private boolean duplicateInfoDegree(List executionDegreeList, InfoExecutionDegree infoExecutionDegree) {

        InfoDegree infoDegree = infoExecutionDegree.getInfoDegreeCurricularPlan().getInfoDegree();
        Iterator iterator = executionDegreeList.iterator();

        while (iterator.hasNext()) {
            InfoExecutionDegree infoExecutionDegree2 = (InfoExecutionDegree) iterator.next();
            if (infoDegree.equals(infoExecutionDegree2.getInfoDegreeCurricularPlan().getInfoDegree())
                    && !(infoExecutionDegree.equals(infoExecutionDegree2)))
                return true;

        }
        return false;
    }

    public ActionForward showProfessorshipsByExecutionDegree(ActionMapping mapping,
            ActionForm actionForm, HttpServletRequest request, HttpServletResponse response)
            throws FenixActionException, FenixFilterException {

        DynaActionForm executionDegreeForm = (DynaActionForm) actionForm;
        Integer executionDegreeId = (Integer) executionDegreeForm.get("executionDegreeId");
        
        Integer semester = (Integer)executionDegreeForm.get("semester");
        Integer teacherType = (Integer)executionDegreeForm.get("teacherType");
        String searchDetails = (String)executionDegreeForm.get("searchDetails");
        try {

            Object[] args = { executionDegreeId, semester, teacherType };

            List detailedProfessorShipsListofLists = (List) ServiceUtils.executeService(null,
                    "ReadProfessorshipsAndResponsibilitiesByExecutionDegreeAndExecutionPeriod", args);
            
            if((detailedProfessorShipsListofLists != null) && (!detailedProfessorShipsListofLists.isEmpty())) {

                Collections.sort(detailedProfessorShipsListofLists, new Comparator() {

                    public int compare(Object o1, Object o2) {

                        List list1 = (List) o1;
                        List list2 = (List) o2;
                        DetailedProfessorship dt1 = (DetailedProfessorship) list1.get(0);
                        DetailedProfessorship dt2 = (DetailedProfessorship) list2.get(0);

                        return dt1.getInfoProfessorship().getInfoExecutionCourse().getNome()
                                .compareToIgnoreCase(
                                        dt2.getInfoProfessorship().getInfoExecutionCourse().getNome());
                    }

                });

                request.setAttribute("detailedProfessorShipsListofLists", detailedProfessorShipsListofLists);
            }
            
            Object[] oid = { executionDegreeId };
            InfoExecutionDegree degree = (InfoExecutionDegree) ServiceUtils.executeService(null, "ReadExecutionDegreeByOID", oid);
            
            InfoDegreeCurricularPlan infoDegreeCurricularPlan = degree.getInfoDegreeCurricularPlan();
            infoDegreeCurricularPlan.prepareEnglishPresentation(getLocale(request));
            degree.setInfoDegreeCurricularPlan(infoDegreeCurricularPlan);
            
            request.setAttribute("searchType", "Consulta Por Curso");
            request.setAttribute("searchTarget", degree.getInfoDegreeCurricularPlan().getInfoDegree().getTipoCurso() + " em " + 
            		degree.getInfoDegreeCurricularPlan().getInfoDegree().getNome());
            request.setAttribute("searchDetails", searchDetails);
            request.setAttribute("semester", semester);
           	request.setAttribute("teacherType", teacherType);
            request.setAttribute("executionDegree",degree);
            
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        
        return mapping.findForward("showProfessorships");
    }

    public ActionForward showTeachersBodyByDepartment(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException, FenixFilterException {

        DynaActionForm departmentForm = (DynaActionForm) actionForm;
        Integer departmentId = (Integer) departmentForm.get("departmentId");
        Integer executionYearId = (Integer) departmentForm.get("executionYearId");

        Integer semester = (Integer)departmentForm.get("semester");
        Integer teacherType = (Integer)departmentForm.get("teacherType");
        String searchDetails = (String)departmentForm.get("searchDetails");
        
        try {

            Object[] args = { departmentId, executionYearId, semester, teacherType };

            List detailedProfessorShipsListofLists = (List) ServiceUtils.executeService(null,
                    "ReadProfessorshipsAndResponsibilitiesByDepartmentAndExecutionPeriod", args);
            
            if((detailedProfessorShipsListofLists != null) && (!detailedProfessorShipsListofLists.isEmpty())) {
	
	
	            Collections.sort(detailedProfessorShipsListofLists, new Comparator() {
	
	                public int compare(Object o1, Object o2) {
	
	                    List list1 = (List) o1;
	                    List list2 = (List) o2;
	                    DetailedProfessorship dt1 = (DetailedProfessorship) list1.get(0);
	                    DetailedProfessorship dt2 = (DetailedProfessorship) list2.get(0);
	
	                    return dt1.getInfoProfessorship().getInfoExecutionCourse().getNome()
	                            .compareToIgnoreCase(
	                                    dt2.getInfoProfessorship().getInfoExecutionCourse().getNome());
	                }
	
	            });
	            request.setAttribute("detailedProfessorShipsListofLists", detailedProfessorShipsListofLists);
                }
            
            Object[] oid = { departmentId };
            InfoDepartment department = (InfoDepartment) ServiceUtils.executeService(null, "ReadDepartmentByOID", oid);
            
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
