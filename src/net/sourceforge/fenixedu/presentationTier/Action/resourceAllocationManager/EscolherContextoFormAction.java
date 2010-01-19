package net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.commons.ReadCurricularYearByOID;
import net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager.ReadExecutionDegreesByExecutionYear;
import net.sourceforge.fenixedu.dataTransferObject.CurricularYearAndSemesterAndInfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularYear;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixContextAction;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import pt.ist.fenixWebFramework.security.UserView;

/**
 * @author tfc130
 */
public class EscolherContextoFormAction extends FenixContextAction {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	    throws Exception {

	super.execute(mapping, form, request, response);

	DynaActionForm escolherContextoForm = (DynaActionForm) form;

	IUserView userView = UserView.getUser();

	InfoExecutionPeriod infoExecutionPeriod = (InfoExecutionPeriod) request
		.getAttribute(PresentationConstants.EXECUTION_PERIOD);

	Integer semestre = infoExecutionPeriod.getSemester();
	Integer anoCurricular = (Integer) escolherContextoForm.get("anoCurricular");

	InfoCurricularYear infoCurricularYear = (InfoCurricularYear) ReadCurricularYearByOID.run(anoCurricular);

	int index = Integer.parseInt((String) escolherContextoForm.get("index"));

	request.setAttribute(PresentationConstants.CURRICULAR_YEAR, infoCurricularYear);

	List infoExecutionDegreeList = (List) ReadExecutionDegreesByExecutionYear.run(infoExecutionPeriod.getInfoExecutionYear());

	InfoExecutionDegree infoExecutionDegree = (InfoExecutionDegree) infoExecutionDegreeList.get(index);

	if (infoExecutionDegree != null) {
	    CurricularYearAndSemesterAndInfoExecutionDegree cYSiED = new CurricularYearAndSemesterAndInfoExecutionDegree(
		    anoCurricular, semestre, infoExecutionDegree);
	    request.setAttribute(PresentationConstants.CONTEXT_KEY, cYSiED);

	    request.setAttribute(PresentationConstants.EXECUTION_DEGREE, infoExecutionDegree);
	} else {

	    return mapping.findForward("Licenciatura execucao inexistente");
	}
	return mapping.findForward("Sucesso");
    }
}