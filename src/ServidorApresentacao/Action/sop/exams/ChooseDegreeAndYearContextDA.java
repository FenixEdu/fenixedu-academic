/**
 * 
 * Created on 14/Oct/2003
 *
 */
package ServidorApresentacao.Action.sop.exams;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.LabelValueBean;
import org.apache.struts.validator.DynaValidatorForm;

import DataBeans.InfoDegree;
import DataBeans.InfoExecutionDegree;
import DataBeans.InfoExecutionPeriod;
import DataBeans.comparators.ComparatorByNameForInfoExecutionDegree;
import ServidorAplicacao.IUserView;
import ServidorApresentacao.Action.base.FenixContextDispatchAction;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import ServidorApresentacao.Action.sop.utils.SessionUtils;
import ServidorApresentacao.Action.utils.ContextUtils;

/**
 * @author Ana e Ricardo
 */
public class ChooseDegreeAndYearContextDA extends FenixContextDispatchAction
{

    /* (non-Javadoc)
     * @see org.apache.struts.actions.DispatchAction#dispatchMethod(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.String)
     */
    public ActionForward prepare(
        ActionMapping mapping,
        ActionForm form,
        HttpServletRequest request,
        HttpServletResponse response)
        throws Exception
    {

        IUserView userView = SessionUtils.getUserView(request);

        InfoExecutionPeriod infoExecutionPeriod =
            (InfoExecutionPeriod) request.getAttribute(SessionConstants.EXECUTION_PERIOD);

        /* Criar o bean de anos curriculares */
        ArrayList anosCurriculares = createCurricularYearList();
        request.setAttribute(SessionConstants.LABELLIST_CURRICULAR_YEARS, anosCurriculares);

        /* Cria o form bean com as licenciaturas em execucao.*/
        Object argsLerLicenciaturas[] = { infoExecutionPeriod.getInfoExecutionYear()};

        List executionDegreeList =
            (List) ServiceUtils.executeService(
                userView,
                "ReadExecutionDegreesByExecutionYear",
                argsLerLicenciaturas);

        ArrayList licenciaturas = new ArrayList();

        licenciaturas.add(new LabelValueBean("", ""));

        Collections.sort(executionDegreeList, new ComparatorByNameForInfoExecutionDegree());

        Iterator iterator = executionDegreeList.iterator();

        while (iterator.hasNext())
        {
            InfoExecutionDegree infoExecutionDegree = (InfoExecutionDegree) iterator.next();
            String name = infoExecutionDegree.getInfoDegreeCurricularPlan().getInfoDegree().getNome();

            name =
                infoExecutionDegree
                    .getInfoDegreeCurricularPlan()
                    .getInfoDegree()
                    .getTipoCurso()
                    .toString()
                    + " em "
                    + name;

            name += duplicateInfoDegree(executionDegreeList, infoExecutionDegree)
                ? "-" + infoExecutionDegree.getInfoDegreeCurricularPlan().getName()
                : "";

            licenciaturas.add(new LabelValueBean(name, infoExecutionDegree.getIdInternal().toString()));
        }

        request.setAttribute(SessionConstants.DEGREES, licenciaturas);

        return mapping.findForward("chooseDegreeAndYearContext");
    }

    public ActionForward choose(
        ActionMapping mapping,
        ActionForm form,
        HttpServletRequest request,
        HttpServletResponse response)
        throws Exception
    {

        DynaValidatorForm chooseDegreeAndYearForm = (DynaValidatorForm) form;

        String executionDegree = (String) chooseDegreeAndYearForm.get("executionDegree");
        String curricularYear = (String) chooseDegreeAndYearForm.get("curricularYear");

        request.setAttribute(SessionConstants.EXECUTION_DEGREE_OID, executionDegree);
        request.setAttribute(SessionConstants.CURRICULAR_YEAR_OID, curricularYear);

        ContextUtils.setExecutionDegreeContext(request);
        ContextUtils.setCurricularYearContext(request);

        return mapping.findForward("viewExamsMap");

    }

    /**
     * Method createCurricularYearList.
     * @param mapping
     * @param form
     * @param request
     * @param response
     */
    private ArrayList createCurricularYearList()
    {

        ArrayList anosCurriculares = new ArrayList();

        anosCurriculares.add(new LabelValueBean("", ""));
        anosCurriculares.add(new LabelValueBean("1 º", "1"));
        anosCurriculares.add(new LabelValueBean("2 º", "2"));
        anosCurriculares.add(new LabelValueBean("3 º", "3"));
        anosCurriculares.add(new LabelValueBean("4 º", "4"));
        anosCurriculares.add(new LabelValueBean("5 º", "5"));

        return anosCurriculares;
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
}
