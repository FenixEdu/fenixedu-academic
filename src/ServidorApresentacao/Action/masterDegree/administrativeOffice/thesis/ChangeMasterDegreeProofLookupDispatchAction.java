package ServidorApresentacao.Action.masterDegree.administrativeOffice.thesis;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.actions.LookupDispatchAction;

import DataBeans.InfoStudentCurricularPlan;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorAplicacao.Servico.exceptions.ScholarshipNotFinishedServiceException;
import ServidorApresentacao.Action.exceptions.ExistingActionException;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.exceptions.NonExistingActionException;
import ServidorApresentacao.Action.exceptions.RequiredJuriesActionException;
import ServidorApresentacao.Action.exceptions.ScholarshipNotFinishedActionException;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import ServidorApresentacao.Action.sop.utils.SessionUtils;
import Util.Data;
import Util.MasterDegreeClassification;
import Util.TipoCurso;

/**
 * 
 * @author :
 *   - Shezad Anavarali (sana@mega.ist.utl.pt)
 *   - Nadir Tarmahomed (naat@mega.ist.utl.pt)
 *
 */

public class ChangeMasterDegreeProofLookupDispatchAction extends LookupDispatchAction
{

    public ActionForward addJury(
        ActionMapping mapping,
        ActionForm form,
        HttpServletRequest request,
        HttpServletResponse response)
        throws FenixActionException
    {

        MasterDegreeThesisOperations operations = new MasterDegreeThesisOperations();
        ActionErrors actionErrors = new ActionErrors();

        transportData(form, request);

        try
        {
            operations.getTeachersByNumbers(
                form,
                request,
                "juriesNumbers",
                SessionConstants.JURIES_LIST,
                actionErrors);
            operations.getStudentByNumberAndDegreeType(form, request, actionErrors);

        }
        catch (Exception e1)
        {
            throw new FenixActionException(e1);
        }
        finally
        {
            saveErrors(request, actionErrors);
        }

        return mapping.findForward("start");

    }

    public ActionForward removeJuries(
        ActionMapping mapping,
        ActionForm form,
        HttpServletRequest request,
        HttpServletResponse response)
        throws FenixActionException
    {

        DynaActionForm changeMasterDegreeProofForm = (DynaActionForm) form;

        Integer[] teachersNumbersList = (Integer[]) changeMasterDegreeProofForm.get("juriesNumbers");
        Integer[] removedJuries = (Integer[]) changeMasterDegreeProofForm.get("removedJuriesNumbers");

        changeMasterDegreeProofForm.set(
            "juriesNumbers",
            subtractArray(teachersNumbersList, removedJuries));

        MasterDegreeThesisOperations operations = new MasterDegreeThesisOperations();
        ActionErrors actionErrors = new ActionErrors();

        transportData(form, request);

        try
        {
            operations.getTeachersByNumbers(
                form,
                request,
                "juriesNumbers",
                SessionConstants.JURIES_LIST,
                actionErrors);
            operations.getStudentByNumberAndDegreeType(form, request, actionErrors);

        }
        catch (Exception e1)
        {
            throw new FenixActionException(e1);
        }
        finally
        {
            saveErrors(request, actionErrors);
        }

        return mapping.findForward("start");

    }

    public ActionForward changeMasterDegreeProof(
        ActionMapping mapping,
        ActionForm form,
        HttpServletRequest request,
        HttpServletResponse response)
        throws FenixActionException
    {

        IUserView userView = SessionUtils.getUserView(request);

        DynaActionForm changeMasterDegreeProofForm = (DynaActionForm) form;

        Integer degreeType = (Integer) changeMasterDegreeProofForm.get("degreeType");
        Integer studentNumber = (Integer) changeMasterDegreeProofForm.get("studentNumber");
        MasterDegreeClassification finalResult =
            MasterDegreeClassification.getEnum(
                ((Integer) changeMasterDegreeProofForm.get("finalResult")).intValue());
        Integer attachedCopiesNumber = (Integer) changeMasterDegreeProofForm.get("attachedCopiesNumber");

        String proofDateDay = (String) changeMasterDegreeProofForm.get("proofDateDay");
        String proofDateMonth = (String) changeMasterDegreeProofForm.get("proofDateMonth");
        String proofDateYear = (String) changeMasterDegreeProofForm.get("proofDateYear");

        String thesisDeliveryDateDay = (String) changeMasterDegreeProofForm.get("thesisDeliveryDateDay");
        String thesisDeliveryDateMonth =
            (String) changeMasterDegreeProofForm.get("thesisDeliveryDateMonth");
        String thesisDeliveryDateYear =
            (String) changeMasterDegreeProofForm.get("thesisDeliveryDateYear");

        Date proofDate = buildProofDate(proofDateDay, proofDateMonth, proofDateYear);

        Date thesisDeliveryDate =
            buildThesisDeliveryDate(
                thesisDeliveryDateDay,
                thesisDeliveryDateMonth,
                thesisDeliveryDateYear);

        InfoStudentCurricularPlan infoStudentCurricularPlan =
            readStudentCurricularPlan(userView, degreeType, studentNumber);

        MasterDegreeThesisOperations operations = new MasterDegreeThesisOperations();
        ActionErrors actionErrors = new ActionErrors();
        ArrayList infoTeacherJuries = null;
        try
        {
            operations.getStudentByNumberAndDegreeType(form, request, actionErrors);
            infoTeacherJuries =
                operations.getTeachersByNumbers(
                    form,
                    request,
                    "juriesNumbers",
                    SessionConstants.JURIES_LIST,
                    actionErrors);

        }
        catch (Exception e1)
        {
            throw new FenixActionException(e1);
        }
        finally
        {

            saveErrors(request, actionErrors);

            if (actionErrors.isEmpty() == false)
            {
                transportData(form, request);
                return mapping.findForward("start");
            }

        }

        executeChangeMasterDegreeProofService(
            mapping,
            userView,
            finalResult,
            attachedCopiesNumber,
            proofDate,
            thesisDeliveryDate,
            infoStudentCurricularPlan,
            infoTeacherJuries);

        return mapping.findForward("success");

    }

    private void executeChangeMasterDegreeProofService(
        ActionMapping mapping,
        IUserView userView,
        MasterDegreeClassification finalResult,
        Integer attachedCopiesNumber,
        Date proofDate,
        Date thesisDeliveryDate,
        InfoStudentCurricularPlan infoStudentCurricularPlan,
        ArrayList infoTeacherJuries)
        throws
            RequiredJuriesActionException,
            NonExistingActionException,
            ScholarshipNotFinishedActionException,
            ExistingActionException
    {
        Object args2[] =
            {
                userView,
                infoStudentCurricularPlan,
                proofDate,
                thesisDeliveryDate,
                finalResult,
                attachedCopiesNumber,
                infoTeacherJuries };

        try
        {
            ServiceUtils.executeService(userView, "ChangeMasterDegreeProof", args2);
        }
        catch (NonExistingServiceException e)
        {
            throw new NonExistingActionException(e.getMessage(), mapping.findForward("start"));
        }
        catch (ScholarshipNotFinishedServiceException e)
        {
            throw new ScholarshipNotFinishedActionException(
                e.getMessage(),
                mapping.findForward("start"));
        }
        catch (FenixServiceException e)
        {
            throw new ExistingActionException(e.getMessage(), mapping.findForward("start"));
        }
    }

    private InfoStudentCurricularPlan readStudentCurricularPlan(
        IUserView userView,
        Integer degreeType,
        Integer studentNumber)
        throws FenixActionException
    {
        InfoStudentCurricularPlan infoStudentCurricularPlan = null;

        Object args[] = { studentNumber, new TipoCurso(degreeType)};
        try
        {
            infoStudentCurricularPlan =
                (InfoStudentCurricularPlan) ServiceUtils.executeService(
                    userView,
                    "student.ReadActiveStudentCurricularPlanByNumberAndDegreeType",
                    args);
        }
        catch (FenixServiceException e)
        {
            throw new FenixActionException(e);
        }
        return infoStudentCurricularPlan;
    }

    private Date buildThesisDeliveryDate(
        String thesisDeliveryDateDay,
        String thesisDeliveryDateMonth,
        String thesisDeliveryDateYear)
        throws NumberFormatException
    {
        Date thesisDeliveryDate = null;

        if ((thesisDeliveryDateDay.length() > 0)
            && (thesisDeliveryDateMonth.length() > 0)
            && (thesisDeliveryDateYear.length() > 0))
        {
            Calendar thesisDeliveryDateCalendar =
                new GregorianCalendar(
                    Integer.parseInt(thesisDeliveryDateYear),
                    Integer.parseInt(thesisDeliveryDateMonth),
                    Integer.parseInt(thesisDeliveryDateDay));

            thesisDeliveryDate = thesisDeliveryDateCalendar.getTime();
        }
        return thesisDeliveryDate;
    }

    private Date buildProofDate(String proofDateDay, String proofDateMonth, String proofDateYear)
        throws NumberFormatException
    {
        Date proofDate = null;

        if ((proofDateDay.length() > 0)
            && (proofDateMonth.length() > 0)
            && (proofDateYear.length() > 0))
        {
            Calendar proofDateCalendar =
                new GregorianCalendar(
                    Integer.parseInt(proofDateYear),
                    Integer.parseInt(proofDateMonth),
                    Integer.parseInt(proofDateDay));

            proofDate = proofDateCalendar.getTime();
        }
        return proofDate;
    }

    public ActionForward cancelChangeMasterDegreeProof(
        ActionMapping mapping,
        ActionForm form,
        HttpServletRequest request,
        HttpServletResponse response)
        throws Exception
    {

        PrepareStudentDataForThesisOperationsDispatchAction prepareStudentDataForThesisOperations =
            new PrepareStudentDataForThesisOperationsDispatchAction();
        return prepareStudentDataForThesisOperations.getStudentAndDegreeTypeForThesisOperations(
            mapping,
            form,
            request,
            response);

    }

    public void transportData(ActionForm form, HttpServletRequest request) throws FenixActionException
    {

        // dissertation title
        DynaActionForm masterDegreeProofForm = (DynaActionForm) form;
        String dissertationTitle = (String) masterDegreeProofForm.get("dissertationTitle");
        request.setAttribute(SessionConstants.DISSERTATION_TITLE, dissertationTitle);

        // final result options
        List finalResult = MasterDegreeClassification.toArrayList();
        request.setAttribute(SessionConstants.CLASSIFICATION, finalResult);

        // dates combo boxes options
        request.setAttribute(SessionConstants.DAYS_LIST, Data.getMonthDays());
        request.setAttribute(SessionConstants.MONTHS_LIST, Data.getMonths());
        request.setAttribute(SessionConstants.YEARS_LIST, Data.getExpirationYears());

    }

    private Integer[] subtractArray(Integer[] originalArray, Integer[] arrayToSubtract)
    {
        List tmp = new ArrayList();

        for (int i = 0; i < originalArray.length; i++)
            tmp.add(originalArray[i]);

        for (int i = 0; i < arrayToSubtract.length; i++)
            tmp.remove(arrayToSubtract[i]);

        originalArray = (Integer[]) tmp.toArray(new Integer[] {
        });
        return originalArray;
    }

    private String getFromRequest(String parameter, HttpServletRequest request)
    {
        String parameterString = request.getParameter(parameter);
        if (parameterString == null)
        {
            parameterString = (String) request.getAttribute(parameter);
        }
        return parameterString;
    }

    /* (non-Javadoc)
     * @see org.apache.struts.actions.LookupDispatchAction#getKeyMethodMap()
     */
    protected Map getKeyMethodMap()
    {

        Map map = new HashMap();
        map.put("button.submit.masterDegree.thesis.addJury", "addJury");
        map.put("button.submit.masterDegree.thesis.removeJuries", "removeJuries");
        map.put("button.submit.masterDegree.thesis.changeProof", "changeMasterDegreeProof");
        map.put("button.cancel", "cancelChangeMasterDegreeProof");
        return map;
    }

}