/**
 * Project Sop 
 * 
 * Package ServidorApresentacao.Action.sop
 * 
 * Created on 12/Dez/2002
 *
 */
package ServidorApresentacao.Action.sop;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import DataBeans.InfoClass;
import DataBeans.InfoExecutionCourse;
import DataBeans.InfoExecutionDegree;
import DataBeans.InfoExecutionPeriod;
import DataBeans.InfoShift;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.exceptions.ExistingActionException;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.base.FenixClassAndExecutionCourseAndExecutionDegreeAndCurricularYearContextDispatchAction;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import ServidorApresentacao.Action.sop.utils.SessionUtils;

/**
 * @author jpvl
 */
public class ClassShiftManagerDispatchAction extends
        FenixClassAndExecutionCourseAndExecutionDegreeAndCurricularYearContextDispatchAction {

    public static final String SHIFT_LIST_ATT = "shiftList";

    public static final String AVAILABLE_LIST = "showAvailableList";

    public ActionForward addClassShift(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            //HttpSession session = request.getSession(false);
            IUserView userView = SessionUtils.getUserView(request);

            setShiftAvailableListToRequest(request, userView);

            InfoShift infoShift = getInfoShift(request, SHIFT_LIST_ATT);
            InfoClass classView = getInfoTurma(request);

            Object[] argsAdicionarTurno = { classView, infoShift };

            try {
                ServiceUtils.executeService(userView, "AdicionarTurno", argsAdicionarTurno);
            } catch (Exception e) {
                throw new ExistingActionException("A aula", e);
            }

            setClassShiftListToRequest(request, userView, classView.getNome());

            request.setAttribute(SessionConstants.CLASS_INFO_SHIFT_LIST_KEY, request
                    .getAttribute(SHIFT_LIST_ATT));
            request.removeAttribute(SessionConstants.AVAILABLE_INFO_SHIFT_LIST_KEY);
        } catch (FenixServiceException e) {
            throw new FenixActionException("Aconteceu um erro", e);

        }
        return mapping.findForward("viewClassShiftList");

    }

    public ActionForward removeClassShift(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        //HttpSession session = request.getSession(false);
        IUserView userView = SessionUtils.getUserView(request);

        InfoClass classView = getInfoTurma(request);

        setClassShiftListToRequest(request, userView, classView.getNome());

        request.setAttribute(SessionConstants.CLASS_INFO_SHIFT_LIST_KEY, request
                .getAttribute(SHIFT_LIST_ATT));

        InfoShift infoShift = getInfoShift(request, SessionConstants.CLASS_INFO_SHIFT_LIST_KEY);

        Object[] argsRemoverTurno = { infoShift, classView };
        ServiceUtils.executeService(userView, "RemoverTurno", argsRemoverTurno);

        setClassShiftListToRequest(request, userView, classView.getNome());

        request.setAttribute(SessionConstants.CLASS_INFO_SHIFT_LIST_KEY, request
                .getAttribute(SHIFT_LIST_ATT));
        request.removeAttribute(SessionConstants.AVAILABLE_INFO_SHIFT_LIST_KEY);

        request.setAttribute(SessionConstants.CLASS_VIEW, classView);

        return mapping.findForward("viewClassShiftList");

    }

    /**
     * @param request
     * @return InfoShift
     */
    private InfoShift getInfoShift(HttpServletRequest request, String listAttributeKey) {
        try {
            Integer infoShiftIndex = new Integer(request.getParameter("shiftIndex"));
            //HttpSession session = request.getSession(false);

            List infoShiftList = (List) request.getAttribute(listAttributeKey);

            return (InfoShift) infoShiftList.get(infoShiftIndex.intValue());
        } catch (RuntimeException e) {
            e.printStackTrace(System.out);

        }
        return null;

    }

    public ActionForward viewClassShiftList(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        IUserView userView = SessionUtils.getUserView(request);

        InfoClass classView = getInfoTurma(request);

        //HttpSession session = request.getSession(false);

        setClassShiftListToRequest(request, userView, classView.getNome());

        request.removeAttribute(SessionConstants.AVAILABLE_INFO_SHIFT_LIST_KEY);
        request.setAttribute(SessionConstants.CLASS_INFO_SHIFT_LIST_KEY, request
                .getAttribute(SHIFT_LIST_ATT));

        request.setAttribute(SessionConstants.CLASS_VIEW, classView);

        return mapping.findForward("viewClassShiftList");
    }

    public ActionForward listAvailableShifts(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        //HttpSession session = request.getSession(false);

        IUserView userView = SessionUtils.getUserView(request);

        setShiftAvailableListToRequest(request, userView);

        request.setAttribute(AVAILABLE_LIST, " ");

        request.setAttribute(SessionConstants.AVAILABLE_INFO_SHIFT_LIST_KEY, request
                .getAttribute(SHIFT_LIST_ATT));
        request.removeAttribute(SessionConstants.CLASS_INFO_SHIFT_LIST_KEY);

        return mapping.findForward("viewAvailableShiftList");

    }

    private List returnShiftList(HttpServletRequest request, IUserView userView, String serviceName,
            Object[] args) throws Exception {

        /** InfoShift List */
        List shiftList = (ArrayList) ServiceUtils.executeService(userView, serviceName, args);

        return shiftList;
    }

    private void setShiftListToRequest(HttpServletRequest request, IUserView userView,
            String serviceName, Object[] args) throws Exception {

        /** InfoShift List */
        List shiftList = returnShiftList(request, userView, serviceName, args);
        //if (shiftList != null && !shiftList.isEmpty())
        request.setAttribute(SHIFT_LIST_ATT, shiftList);
    }

    private void setClassShiftListToRequest(HttpServletRequest request, IUserView userView,
            String className) throws Exception {

        //HttpSession session = request.getSession(false);

        InfoExecutionPeriod infoExecutionPeriod = (InfoExecutionPeriod) request
                .getAttribute(SessionConstants.EXECUTION_PERIOD);
        InfoExecutionDegree infoExecutionDegree = (InfoExecutionDegree) request
                .getAttribute(SessionConstants.EXECUTION_DEGREE);

        Object argsLerTurnosTurma[] = { className, infoExecutionDegree, infoExecutionPeriod };

        setShiftListToRequest(request, userView, "LerTurnosDeTurma", argsLerTurnosTurma);

    }

    private List returnClassShiftList(HttpServletRequest request, IUserView userView, String className)
            throws Exception {

        //HttpSession session = request.getSession(false);

        InfoExecutionPeriod infoExecutionPeriod = (InfoExecutionPeriod) request
                .getAttribute(SessionConstants.EXECUTION_PERIOD);
        InfoExecutionDegree infoExecutionDegree = (InfoExecutionDegree) request
                .getAttribute(SessionConstants.EXECUTION_DEGREE);

        Object argsLerTurnosTurma[] = { className, infoExecutionDegree, infoExecutionPeriod };

        List list = returnShiftList(request, userView, "LerTurnosDeTurma", argsLerTurnosTurma);

        return list;

    }

    private void setShiftAvailableListToRequest(HttpServletRequest request, IUserView userView)
            throws Exception {
        //HttpSession session = request.getSession(false);

        InfoClass classView = getInfoTurma(request);

        InfoExecutionCourse courseView = (InfoExecutionCourse) request
                .getAttribute(SessionConstants.EXECUTION_COURSE);

        Object[] argsLerTurnosDeDisciplinaExecucao = { courseView };

        List listClassShift = returnClassShiftList(request, userView, classView.getNome());

        List listAvailable = returnShiftList(request, userView, "LerTurnosDeDisciplinaExecucao",
                argsLerTurnosDeDisciplinaExecucao);
        if (listAvailable != null && listClassShift != null) {
            listAvailable.removeAll(listClassShift);
        }

        if (listAvailable != null && !listAvailable.isEmpty()) {
            request.setAttribute(SHIFT_LIST_ATT, listAvailable);
        }
    }

    /**
     * Method getInfoTurma.
     * 
     * @param request
     * @return InfoClass
     */
    private InfoClass getInfoTurma(HttpServletRequest request) throws Exception {
        //HttpSession session = request.getSession(false);

        InfoClass classView = (InfoClass) request.getAttribute(SessionConstants.CLASS_VIEW);
        if (classView == null)
            throw new Exception("Class is not in session!");
        return classView;
    }

}