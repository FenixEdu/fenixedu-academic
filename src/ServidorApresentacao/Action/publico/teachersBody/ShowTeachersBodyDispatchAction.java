/*
 * Created on 19/Dez/2003
 *  
 */
package ServidorApresentacao.Action.publico.teachersBody;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import DataBeans.InfoDegree;
import DataBeans.InfoExecutionDegree;
import DataBeans.comparators.ComparatorByNameForInfoExecutionDegree;
import DataBeans.teacher.professorship.DetailedProfessorship;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.base.FenixDispatchAction;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;

/**
 * @author <a href="mailto:joao.mota@ist.utl.pt">João Mota</a> 19/Dez/2003
 *  
 */
public class ShowTeachersBodyDispatchAction extends FenixDispatchAction
{

    public ActionForward prepareForm(
        ActionMapping mapping,
        ActionForm actionForm,
        HttpServletRequest request,
        HttpServletResponse response)
        throws FenixActionException
    {
        DynaActionForm executionYearForm = (DynaActionForm) actionForm;
        Integer executionYearId = (Integer) executionYearForm.get("executionYearId");
        try
        {

            Object[] args = { executionYearId };

            List executionDegrees =
                (List) ServiceUtils.executeService(null, "ReadExecutionDegreesByExecutionYearId", args);

            List executionYears =
                (List) ServiceUtils.executeService(null, "ReadAllExecutionYears", null);

            List departments = (List) ServiceUtils.executeService(null, "ReadAllDepartments", null);

            Collections.sort(executionDegrees, new ComparatorByNameForInfoExecutionDegree());

            Iterator iter = executionDegrees.iterator();
            while (iter.hasNext())
            {
                InfoExecutionDegree infoExecutionDegree = (InfoExecutionDegree) iter.next();
                if (duplicateInfoDegree(executionDegrees, infoExecutionDegree))
                {
                    infoExecutionDegree.getInfoDegreeCurricularPlan().getInfoDegree().setNome(
                        infoExecutionDegree.getInfoDegreeCurricularPlan().getInfoDegree().getNome()
                            + "-"
                            + infoExecutionDegree.getInfoDegreeCurricularPlan().getName());
                }
            }

            request.setAttribute("executionDegrees", executionDegrees);
            request.setAttribute("executionYears", executionYears);
            request.setAttribute("departments", departments);
        }
        catch (FenixServiceException e)
        {
            throw new FenixActionException(e);
        }

        return mapping.findForward("showForm");
    }

    private boolean duplicateInfoDegree(
        List executionDegreeList,
        InfoExecutionDegree infoExecutionDegree)
    {
        InfoDegree infoDegree = infoExecutionDegree.getInfoDegreeCurricularPlan().getInfoDegree();
        Iterator iterator = executionDegreeList.iterator();

        while (iterator.hasNext())
        {
            InfoExecutionDegree infoExecutionDegree2 = (InfoExecutionDegree) iterator.next();
            if (infoDegree.equals(infoExecutionDegree2.getInfoDegreeCurricularPlan().getInfoDegree())
                && !(infoExecutionDegree.equals(infoExecutionDegree2)))
                return true;

        }
        return false;
    }

    public ActionForward showProfessorshipsByExecutionDegree(
        ActionMapping mapping,
        ActionForm actionForm,
        HttpServletRequest request,
        HttpServletResponse response)
        throws FenixActionException
    {
        DynaActionForm executionDegreeForm = (DynaActionForm) actionForm;
        Integer executionDegreeId = (Integer) executionDegreeForm.get("executionDegreeId");
        try
        {

            Object[] args = { executionDegreeId };

            List detailedProfessorShipsListofLists =
                (List) ServiceUtils.executeService(
                    null,
                    "ReadProfessorshipsAndResponsibilitiesByExecutionDegree",
                    args);
            Collections.sort(detailedProfessorShipsListofLists, new Comparator()
            {

                public int compare(Object o1, Object o2)
                {
                    List list1 = (List) o1;
                    List list2 = (List) o2;
                    DetailedProfessorship dt1 = (DetailedProfessorship) list1.get(0);
                    DetailedProfessorship dt2 = (DetailedProfessorship) list2.get(0);

                    return dt1
                        .getInfoProfessorship()
                        .getInfoExecutionCourse()
                        .getNome()
                        .compareToIgnoreCase(
                        dt2.getInfoProfessorship().getInfoExecutionCourse().getNome());
                }

            });

            request.setAttribute("detailedProfessorShipsListofLists", detailedProfessorShipsListofLists);
        }
        catch (FenixServiceException e)
        {
            throw new FenixActionException(e);
        }

        return mapping.findForward("showProfessorships");
    }

    public ActionForward showTeachersBodyByDepartment(
        ActionMapping mapping,
        ActionForm actionForm,
        HttpServletRequest request,
        HttpServletResponse response)
        throws FenixActionException
    {

        DynaActionForm departmentForm = (DynaActionForm) actionForm;
        Integer departmentId = (Integer) departmentForm.get("departmentId");
        try
        {

            Object[] args = { departmentId };

            List detailedProfessorShipsListofLists =
                (List) ServiceUtils.executeService(
                    null,
                    "ReadProfessorshipsAndResponsibilitiesByDepartment",
                    args);
            Collections.sort(detailedProfessorShipsListofLists, new Comparator()
            {

                public int compare(Object o1, Object o2)
                {
                    List list1 = (List) o1;
                    List list2 = (List) o2;
                    DetailedProfessorship dt1 = (DetailedProfessorship) list1.get(0);
                    DetailedProfessorship dt2 = (DetailedProfessorship) list2.get(0);

                    return dt1
                        .getInfoProfessorship()
                        .getInfoExecutionCourse()
                        .getNome()
                        .compareToIgnoreCase(
                        dt2.getInfoProfessorship().getInfoExecutionCourse().getNome());
                }

            });

            request.setAttribute("detailedProfessorShipsListofLists", detailedProfessorShipsListofLists);
        }
        catch (FenixServiceException e)
        {
            throw new FenixActionException(e);
        }

        return mapping.findForward("showProfessorships");
    }

}
