/*
 * Created on Dec 17, 2003
 *  
 */
package ServidorApresentacao.Action.coordinator;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.util.LabelValueBean;

import DataBeans.InfoExecutionYear;
import ServidorAplicacao.IUserView;
import ServidorApresentacao.Action.base.FenixAction;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionUtils;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 *  
 */
public class ReadTeachersInformationAction extends FenixAction {

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.struts.action.Action#execute(org.apache.struts.action.ActionMapping,
     *      org.apache.struts.action.ActionForm,
     *      javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        IUserView userView = SessionUtils.getUserView(request);

        DynaActionForm teacherInformationForm = (DynaActionForm) actionForm;
        Integer degreeCurricularPlanID = new Integer(request.getParameter("degreeCurricularPlanID"));
        request.setAttribute("degreeCurricularPlanID", degreeCurricularPlanID);

        Integer executionDegreeID = new Integer(request.getParameter("executionDegreeId"));
        request.setAttribute("executionDegreeId", executionDegreeID);

        //Lists all years attatched to the degree curricular plan
        List executionYearList = null;
        try {
            Object[] args = { degreeCurricularPlanID };
            executionYearList = (List) ServiceUtils.executeService(userView,
                    "ReadExecutionYearsByDegreeCurricularPlanID", args);
        } catch (Exception e) {
            e.printStackTrace();
        }

        List executionYearsLabelValueList = new ArrayList();
        for (int i = 0; i < executionYearList.size(); i++) {
            InfoExecutionYear infoExecutionYear = (InfoExecutionYear) executionYearList.get(i);
            executionYearsLabelValueList.add(new LabelValueBean(infoExecutionYear.getYear(),
                    infoExecutionYear.getYear()));
        }

        request.setAttribute("executionYearList", executionYearsLabelValueList);

        String yearString = (String) teacherInformationForm.get("yearString");
        List infoSiteTeachersInformation = null;

        if (yearString.equalsIgnoreCase("") || yearString == null) {
            //show user option
            teacherInformationForm.set("yearString", ((InfoExecutionYear) executionYearList
                    .get(executionYearList.size() - 1)).getYear());
        }

        Object[] args = { executionDegreeID, Boolean.FALSE, yearString };
        infoSiteTeachersInformation = (List) ServiceUtils.executeService(userView,
                "ReadTeachersInformation", args);

        request.setAttribute("infoSiteTeachersInformation", infoSiteTeachersInformation);

        return mapping.findForward("show");
    }

}