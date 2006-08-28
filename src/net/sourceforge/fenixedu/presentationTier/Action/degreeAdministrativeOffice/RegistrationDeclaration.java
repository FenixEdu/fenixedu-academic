/*
 * Created on 14/Set/2004
 *
 */
package net.sourceforge.fenixedu.presentationTier.Action.degreeAdministrativeOffice;

import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.dataTransferObject.InfoPerson;
import net.sourceforge.fenixedu.dataTransferObject.student.InfoRegistrationDeclaration;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants;
import net.sourceforge.fenixedu.util.Data;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.util.LabelValueBean;

/**
 * @author Nuno Correia
 * @author Ricardo Rodrigues
 */
public class RegistrationDeclaration extends FenixDispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        return mapping.findForward("prepare");
    }

    public ActionForward generate(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        HttpSession session = request.getSession(false);

        IUserView userView = getUserView(request);

        InfoRegistrationDeclaration infoRegistrationDeclaration = null;

        DynaActionForm studentForm = (DynaActionForm) form;
        String studentNumber = (String) studentForm.get("studentNumber");

        Object args[] = { new Integer(studentNumber), DegreeType.DEGREE };

        infoRegistrationDeclaration = (InfoRegistrationDeclaration) ServiceManagerServiceFactory
                .executeService(userView, "ReadInfoRegistrationDeclaration", args);        

        String degreeName = infoRegistrationDeclaration.getDegreeName();

        InfoPerson infoPerson = infoRegistrationDeclaration.getInfoPerson();
        Calendar calendar = Calendar.getInstance();

        String idNumber = infoPerson.getNumeroDocumentoIdentificacao();
        String parishOfBirth = infoPerson.getFreguesiaNaturalidade();
        String districtOfBirth = infoPerson.getDistritoNaturalidade();

        String nameOfFather = infoPerson.getNomePai();
        String nameOfMother = infoPerson.getNomeMae();

        String partOne = "DECLARA, a pedido do interessado, que o aluno Número " + studentNumber + ", ";
        String partTwo = infoPerson.getNome().toUpperCase() + " ";
        String partThree = "portador do Bilhete de Identidade " + idNumber + ", ";
        String partFour = "natural de " + parishOfBirth.toUpperCase() + ", "
                + districtOfBirth.toUpperCase() + " ";
        String partFive = "filho de " + nameOfFather.toUpperCase() + " ";
        String partSix = "e de " + nameOfMother.toUpperCase() + ", ";
        String partSeven = "no ano lectivo "
                + infoRegistrationDeclaration.getInfoExecutionYear().getYear()
                + " ESTÁ INSCRITO no curso de " + degreeName.toUpperCase() + " deste instituto.";
        List allMonths = Data.getMonths();
        LabelValueBean label = (LabelValueBean) allMonths.get(calendar.get(Calendar.MONTH) + 1);
        String month = label.getLabel();
        String partEight = "Secretaria dos Serviços Académicos do Instituto Superior Técnico,<br/>em Lisboa, "
                + calendar.get(Calendar.DAY_OF_MONTH)
                + " de "
                + month
                + " de "
                + calendar.get(Calendar.YEAR);

        request.setAttribute("partOne1", partOne);
        request.setAttribute("partTwo1", partTwo);
        request.setAttribute("partThree1", partThree);
        request.setAttribute("partFour1", partFour);
        request.setAttribute("partFive1", partFive);
        request.setAttribute("partSix1", partSix);
        request.setAttribute("partSeven1", partSeven);
        request.setAttribute("partEight1", partEight);

        return mapping.findForward("sucess");
    }

}
