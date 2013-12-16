package net.sourceforge.fenixedu.presentationTier.Action.masterDegree.administrativeOffice.gratuity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pt.ist.bennu.core.domain.User;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.gratuity.ReadInsuranceValueByExecutionYearID;
import net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.gratuity.UpdateAndReadGratuitySituationsByStudentNumber;
import net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.gratuity.transactions.ReadInsuranceTransactionByStudentIDAndExecutionYearID;
import net.sourceforge.fenixedu.applicationTier.Servico.student.ReadStudentByNumberAndDegreeType;
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
import net.sourceforge.fenixedu.presentationTier.Action.masterDegree.utils.PresentationConstants;
import net.sourceforge.fenixedu.presentationTier.config.FenixErrorExceptionHandler;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import pt.ist.bennu.core.security.Authenticate;
import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Input;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

/**
 * @author <a href="mailto:sana@ist.utl.pt">Shezad Anavarali </a>
 * @author <a href="mailto:naat@ist.utl.pt">Nadir Tarmahomed </a>
 * 
 */

@Mapping(path = "/studentSituation", formBean = "studentSituationForm", module = "masterDegreeAdministrativeOffice")
@Forwards({
        @Forward(name = "chooseStudent", path = "chooseStudentForStudentSituation", tileProperties = @Tile(title = "teste61")),
        @Forward(name = "success", path = "studentSituation", tileProperties = @Tile(title = "teste62")) })
@Exceptions({ @ExceptionHandling(type = NonExistingActionException.class, handler = FenixErrorExceptionHandler.class) })
public class StudentSituationDispatchAction extends FenixDispatchAction {

    @Input
    public ActionForward chooseStudent(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        return mapping.findForward("chooseStudent");

    }

    public ActionForward readStudent(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        ActionErrors errors = new ActionErrors();
        DynaActionForm studentSituationForm = (DynaActionForm) form;
        User userView = Authenticate.getUser();

        Integer studentNumber = getIntegerFromRequestOrForm(request, studentSituationForm, "studentNumber");
        String degreeType = (String) getFromRequestOrForm(request, studentSituationForm, "degreeType");

        InfoStudent infoStudent =
                (InfoStudent) ReadStudentByNumberAndDegreeType.run(studentNumber, DegreeType.valueOf(degreeType));

        if (infoStudent == null) {
            throw new NonExistingActionException("error.exception.masterDegree.nonExistentStudent", mapping.findForward("choose"));
        }

        request.setAttribute(PresentationConstants.STUDENT, infoStudent);

        List gratuitySituations = UpdateAndReadGratuitySituationsByStudentNumber.run(studentNumber);

        InfoGratuitySituation infoGratuitySituation = null;
        InfoInsuranceTransaction infoInsuranceTransaction = null;
        InfoInsuranceValue infoInsuranceValue = null;
        InsuranceSituationDTO insuranceSituationDTO = null;
        List insuranceSituationsDTOList = new ArrayList();
        InfoExecutionYear infoExecutionYear = null;
        Iterator iterator = gratuitySituations.iterator();

        while (iterator.hasNext()) {

            infoGratuitySituation = (InfoGratuitySituation) iterator.next();

            try {
                infoInsuranceTransaction =
                        ReadInsuranceTransactionByStudentIDAndExecutionYearID.run(infoStudent.getExternalId(),
                                infoGratuitySituation.getInfoGratuityValues().getInfoExecutionDegree().getInfoExecutionYear()
                                        .getExternalId());
            } catch (FenixServiceException e) {
                errors.add("insurance", new ActionError("error.duplicate.insurance", studentNumber));
                saveErrors(request, errors);
                return mapping.getInputForward();
            }

            insuranceSituationDTO = new InsuranceSituationDTO();

            infoExecutionYear = infoGratuitySituation.getInfoGratuityValues().getInfoExecutionDegree().getInfoExecutionYear();

            try {
                infoInsuranceValue = ReadInsuranceValueByExecutionYearID.run(infoExecutionYear.getExternalId());
            } catch (FenixServiceException e) {
                throw new FenixActionException(e);
            }
            if (infoInsuranceValue != null) {
                insuranceSituationDTO.setAnualValue(infoInsuranceValue.getAnnualValue());
            }

            if (infoInsuranceTransaction != null) {

                insuranceSituationDTO.setInfoExecutionYear(infoInsuranceTransaction.getInfoExecutionYear());
                insuranceSituationDTO.setExecutionYearID(infoInsuranceTransaction.getInfoExecutionYear().getExternalId());
                insuranceSituationDTO.setPayedValue(infoInsuranceTransaction.getValue());
                insuranceSituationDTO.setInsuranceTransactionID(infoInsuranceTransaction.getExternalId());
            } else {

                insuranceSituationDTO.setInfoExecutionYear(infoExecutionYear);
                insuranceSituationDTO.setExecutionYearID(infoExecutionYear.getExternalId());
                insuranceSituationDTO.setPayedValue(new Double(0));

            }

            if (!insuranceSituationsDTOList.contains(insuranceSituationDTO)) {
                insuranceSituationsDTOList.add(insuranceSituationDTO);
            }

        }

        request.setAttribute(PresentationConstants.GRATUITY_SITUATIONS_LIST, gratuitySituations);
        request.setAttribute(PresentationConstants.INSURANCE_SITUATIONS_LIST, insuranceSituationsDTOList);

        return mapping.findForward("success");

    }
}