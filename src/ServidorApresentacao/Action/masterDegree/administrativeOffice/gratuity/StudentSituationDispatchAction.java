package ServidorApresentacao.Action.masterDegree.administrativeOffice.gratuity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import DataBeans.InfoExecutionYear;
import DataBeans.InfoGratuitySituation;
import DataBeans.InfoInsuranceValue;
import DataBeans.InfoStudent;
import DataBeans.transactions.InfoInsuranceTransaction;
import DataBeans.transactions.InsuranceSituationDTO;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.base.FenixDispatchAction;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.exceptions.NonExistingActionException;
import ServidorApresentacao.Action.masterDegree.utils.SessionConstants;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionUtils;
import Util.TipoCurso;

/**
 * @author <a href="mailto:sana@ist.utl.pt">Shezad Anavarali </a>
 * @author <a href="mailto:naat@ist.utl.pt">Nadir Tarmahomed </a>
 *  
 */
public class StudentSituationDispatchAction extends FenixDispatchAction {

    public ActionForward chooseStudent(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        return mapping.findForward("chooseStudent");

    }

    public ActionForward readStudent(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        ActionErrors errors = new ActionErrors();
        DynaActionForm studentSituationForm = (DynaActionForm) form;
        IUserView userView = SessionUtils.getUserView(request);

        Integer studentNumber = (Integer) studentSituationForm.get("studentNumber");
        Integer degreeType = (Integer) studentSituationForm.get("degreeType");

        InfoStudent infoStudent = null;

        Object argsStudent[] = { studentNumber, new TipoCurso(degreeType) };

        try {
            infoStudent = (InfoStudent) ServiceUtils.executeService(userView,
                    "ReadStudentByNumberAndDegreeType", argsStudent);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        if (infoStudent == null) {
            throw new NonExistingActionException("error.exception.masterDegree.nonExistentStudent",
                    mapping.findForward("choose"));
        }

        request.setAttribute(SessionConstants.STUDENT, infoStudent);

        List gratuitySituations = null;
        Object argsGratuitySituations[] = { studentNumber };

        try {
            gratuitySituations = (List) ServiceUtils.executeService(userView,
                    "ReadGratuitySituationsByStudentNumber", argsGratuitySituations);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        InfoGratuitySituation infoGratuitySituation = null;
        InfoInsuranceTransaction infoInsuranceTransaction = null;
        InfoInsuranceValue infoInsuranceValue = null;
        InsuranceSituationDTO insuranceSituationDTO = null;
        List insuranceSituationsDTOList = new ArrayList();
        InfoExecutionYear infoExecutionYear = null;
        Iterator iterator = gratuitySituations.iterator();

        while (iterator.hasNext()) {

            infoGratuitySituation = (InfoGratuitySituation) iterator.next();
            Object argsInsurance[] = {
                    infoStudent.getIdInternal(),
                    infoGratuitySituation.getInfoGratuityValues().getInfoExecutionDegree()
                            .getInfoExecutionYear().getIdInternal() };

            try {
                infoInsuranceTransaction = (InfoInsuranceTransaction) ServiceUtils
                        .executeService(userView,
                                "ReadInsuranceTransactionByStudentIDAndExecutionYearID", argsInsurance);
            } catch (FenixServiceException e) {
                errors.add("insurance", new ActionError("error.duplicate.insurance", studentNumber));
                saveErrors(request, errors);
                return mapping.getInputForward();
            }

            insuranceSituationDTO = new InsuranceSituationDTO();

            infoExecutionYear = infoGratuitySituation.getInfoGratuityValues().getInfoExecutionDegree()
                    .getInfoExecutionYear();

            Object argsInsuranceValue[] = { infoExecutionYear.getIdInternal() };

            try {
                infoInsuranceValue = (InfoInsuranceValue) ServiceUtils.executeService(userView,
                        "ReadInsuranceValueByExecutionYearID", argsInsuranceValue);
            } catch (FenixServiceException e) {
                throw new FenixActionException(e);
            }
            if (infoInsuranceValue != null) {
                insuranceSituationDTO.setAnualValue(infoInsuranceValue.getAnnualValue());
            }

            if (infoInsuranceTransaction != null) {

                insuranceSituationDTO.setInfoExecutionYear(infoInsuranceTransaction
                        .getInfoExecutionYear());
                insuranceSituationDTO.setExecutionYearID(infoInsuranceTransaction.getInfoExecutionYear()
                        .getIdInternal());
                insuranceSituationDTO.setPayedValue(infoInsuranceTransaction.getValue());
                insuranceSituationDTO
                        .setInsuranceTransactionID(infoInsuranceTransaction.getIdInternal());
            } else {

                insuranceSituationDTO.setInfoExecutionYear(infoExecutionYear);
                insuranceSituationDTO.setExecutionYearID(infoExecutionYear.getIdInternal());
                insuranceSituationDTO.setPayedValue(new Double(0));

            }

            if (!insuranceSituationsDTOList.contains(insuranceSituationDTO)) {
                insuranceSituationsDTOList.add(insuranceSituationDTO);
            }

        }

        request.setAttribute(SessionConstants.GRATUITY_SITUATIONS_LIST, gratuitySituations);
        request.setAttribute(SessionConstants.INSURANCE_SITUATIONS_LIST, insuranceSituationsDTOList);

        return mapping.findForward("success");

    }
}