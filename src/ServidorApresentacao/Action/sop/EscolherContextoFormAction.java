package ServidorApresentacao.Action.sop;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import DataBeans.CurricularYearAndSemesterAndInfoExecutionDegree;
import DataBeans.InfoCurricularYear;
import DataBeans.InfoExecutionDegree;
import DataBeans.InfoExecutionPeriod;
import ServidorAplicacao.IUserView;
import ServidorApresentacao.Action.base.FenixContextAction;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import ServidorApresentacao.Action.sop.utils.SessionUtils;

/**
 * @author tfc130
 */
public class EscolherContextoFormAction extends FenixContextAction {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        super.execute(mapping, form, request, response);

        DynaActionForm escolherContextoForm = (DynaActionForm) form;

        HttpSession session = request.getSession(false);
        IUserView userView = SessionUtils.getUserView(request);

        if (session != null) {
            InfoExecutionPeriod infoExecutionPeriod = (InfoExecutionPeriod) request
                    .getAttribute(SessionConstants.EXECUTION_PERIOD);

            Integer semestre = infoExecutionPeriod.getSemester();
            Integer anoCurricular = (Integer) escolherContextoForm.get("anoCurricular");

            Object argsReadCurricularYearByOID[] = { anoCurricular };
            InfoCurricularYear infoCurricularYear = (InfoCurricularYear) ServiceUtils.executeService(
                    userView, "ReadCurricularYearByOID", argsReadCurricularYearByOID);

            int index = Integer.parseInt((String) escolherContextoForm.get("index"));

            request.setAttribute(SessionConstants.CURRICULAR_YEAR, infoCurricularYear);

            Object argsLerLicenciaturas[] = { infoExecutionPeriod.getInfoExecutionYear() };

            List infoExecutionDegreeList = (List) ServiceUtils.executeService(userView,
                    "ReadExecutionDegreesByExecutionYear", argsLerLicenciaturas);

            InfoExecutionDegree infoExecutionDegree = (InfoExecutionDegree) infoExecutionDegreeList
                    .get(index);

            if (infoExecutionDegree != null) {
                CurricularYearAndSemesterAndInfoExecutionDegree cYSiED = new CurricularYearAndSemesterAndInfoExecutionDegree(
                        anoCurricular, semestre, infoExecutionDegree);
                request.setAttribute(SessionConstants.CONTEXT_KEY, cYSiED);

                request.setAttribute(SessionConstants.EXECUTION_DEGREE, infoExecutionDegree);
            } else {

                return mapping.findForward("Licenciatura execucao inexistente");
            }
            return mapping.findForward("Sucesso");
        }
        throw new Exception();

    }
}