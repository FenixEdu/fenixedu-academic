package net.sourceforge.fenixedu.presentationTier.Action.masterDegree.administrativeOffice.gratuity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionYear;
import net.sourceforge.fenixedu.dataTransferObject.InfoGratuitySituation;
import net.sourceforge.fenixedu.dataTransferObject.InfoInsuranceValue;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;
import net.sourceforge.fenixedu.dataTransferObject.transactions.InfoInsuranceTransaction;
import net.sourceforge.fenixedu.dataTransferObject.transactions.InsuranceSituationDTO;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.NonExistingActionException;
import net.sourceforge.fenixedu.presentationTier.Action.masterDegree.utils.SessionConstants;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

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
        String degreeType = (String) studentSituationForm.get("degreeType");

        InfoStudent infoStudent = null;

        Object argsStudent[] = { studentNumber, DegreeType.valueOf(degreeType) };

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