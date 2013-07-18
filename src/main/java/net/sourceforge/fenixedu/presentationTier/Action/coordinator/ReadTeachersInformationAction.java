/*
 * Created on Dec 17, 2003
 *  
 */
package net.sourceforge.fenixedu.presentationTier.Action.coordinator;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.commons.ReadExecutionYearsByDegreeCurricularPlanID;
import net.sourceforge.fenixedu.applicationTier.Servico.coordinator.ReadTeachersInformation;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionYear;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixAction;
import net.sourceforge.fenixedu.presentationTier.Action.masterDegree.coordinator.CoordinatedDegreeInfo;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.util.LabelValueBean;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 * 
 */
@Mapping(module = "coordinator", path = "/teachersInformation", attribute = "teacherInformationForm",
        formBean = "teacherInformationForm", scope = "request")
@Forwards(value = { @Forward(name = "show", path = "/coordinator/teachers/viewTeachersInformation.jsp") })
public class ReadTeachersInformationAction extends FenixAction {

    /*
     * (non-Javadoc)
     * 
     * @seeorg.apache.struts.action.Action#execute(org.apache.struts.action.
     * ActionMapping, org.apache.struts.action.ActionForm,
     * javax.servlet.http.HttpServletRequest,
     * javax.servlet.http.HttpServletResponse)
     */
    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        CoordinatedDegreeInfo.setCoordinatorContext(request);
        Integer degreeCurricularPlanID = (Integer) request.getAttribute("degreeCurricularPlanID");

        Integer executionDegreeID = new Integer(request.getParameter("executionDegreeId"));
        request.setAttribute("executionDegreeId", executionDegreeID);

        // Lists all years attatched to the degree curricular plan
        List executionYearList = ReadExecutionYearsByDegreeCurricularPlanID.run(degreeCurricularPlanID);

        List executionYearsLabelValueList = new ArrayList();
        for (int i = 0; i < executionYearList.size(); i++) {
            InfoExecutionYear infoExecutionYear = (InfoExecutionYear) executionYearList.get(i);
            executionYearsLabelValueList.add(new LabelValueBean(infoExecutionYear.getYear(), infoExecutionYear.getYear()));
        }

        request.setAttribute("executionYearList", executionYearsLabelValueList);

        DynaActionForm teacherInformationForm = (DynaActionForm) actionForm;
        String yearString = (String) teacherInformationForm.get("yearString");
        List infoSiteTeachersInformation = null;

        if (yearString.equalsIgnoreCase("") || yearString == null) {
            // show user option
            teacherInformationForm.set("yearString",
                    ((InfoExecutionYear) executionYearList.get(executionYearList.size() - 1)).getYear());
        }

        infoSiteTeachersInformation =
                ReadTeachersInformation.runReadTeachersInformation(executionDegreeID, Boolean.FALSE, yearString);

        request.setAttribute("infoSiteTeachersInformation", infoSiteTeachersInformation);

        return mapping.findForward("show");
    }

}