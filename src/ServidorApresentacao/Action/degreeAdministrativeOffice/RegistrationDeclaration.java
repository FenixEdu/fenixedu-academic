/*
 * Created on 14/Set/2004
 *
 */
package ServidorApresentacao.Action.degreeAdministrativeOffice;

import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.util.LabelValueBean;

import DataBeans.InfoPerson;
import DataBeans.student.InfoRegistrationDeclaration;
import ServidorAplicacao.IUserView;
import ServidorApresentacao.Action.base.FenixDispatchAction;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import Util.Data;
import Util.TipoCurso;
import framework.factory.ServiceManagerServiceFactory;

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

        IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
        ActionErrors actionErrors = new ActionErrors();

        InfoRegistrationDeclaration infoRegistrationDeclaration = null;

        DynaActionForm studentForm = (DynaActionForm) form;
        String studentNumber = (String) studentForm.get("studentNumber");

        Object args[] = { new Integer(studentNumber), TipoCurso.LICENCIATURA_OBJ };

        infoRegistrationDeclaration = (InfoRegistrationDeclaration) ServiceManagerServiceFactory
                .executeService(userView, "ReadInfoRegistrationDeclaration", args);        

        String degreeName = infoRegistrationDeclaration.getDegreeName();

        final int columnNumber = 73;

        InfoPerson infoPerson = infoRegistrationDeclaration.getInfoPerson();
        Calendar calendar = Calendar.getInstance();

        String idNumber = infoPerson.getNumeroDocumentoIdentificacao();
        String parishOfBirth = infoPerson.getFreguesiaNaturalidade();
        String districtOfBirth = infoPerson.getDistritoNaturalidade();

        String nameOfFather = infoPerson.getNomePai();
        String nameOfMother = infoPerson.getNomeMae();

        String partOne = "DECLARA, a pedido do interessado, que o aluno Número " + studentNumber + " ";
        String partTwo = infoPerson.getNome().toUpperCase() + " ";
        String partThree = "portador do Bilhete de Identidade " + idNumber + " ";
        String partFour = "natural de " + parishOfBirth.toUpperCase() + ", "
                + districtOfBirth.toUpperCase() + " ";
        String partFive = "filho de " + nameOfFather.toUpperCase() + " ";
        String partSix = "e de " + nameOfMother.toUpperCase() + " ";
        String partSeven = "no ano lectivo "
                + infoRegistrationDeclaration.getInfoExecutionYear().getYear()
                + " ESTÁ INSCRITO no curso de " + degreeName.toUpperCase() + " deste instituto.";
        List allMonths = Data.getMonths();
        LabelValueBean label = (LabelValueBean) allMonths.get(calendar.get(Calendar.MONTH) + 1);
        String month = label.getLabel();
        String partEight = "Secretaria dos Serviços Académicos do Instituto Superior Técnico,<br><br>em Lisboa, "
                + calendar.get(Calendar.DAY_OF_MONTH)
                + " de "
                + month
                + " de "
                + calendar.get(Calendar.YEAR);

        String partOne1 = completeLine(partOne, columnNumber);
        String partTwo1 = adjustLine(partTwo, columnNumber, true);
        String partThree1 = completeLine(partThree, columnNumber);
        String partFour1 = adjustLine(partFour, columnNumber, true);
        String partFive1 = completeLine(partFive, columnNumber);
        String partSix1 = completeLine(partSix, columnNumber);
        String partSeven1 = adjustLine(partSeven, columnNumber, false);

        request.setAttribute("partOne1", partOne1);
        request.setAttribute("partTwo1", partTwo1);
        request.setAttribute("partThree1", partThree1);
        request.setAttribute("partFour1", partFour1);
        request.setAttribute("partFive1", partFive1);
        request.setAttribute("partSix1", partSix1);
        request.setAttribute("partSeven1", partSeven1);
        request.setAttribute("partEight1", partEight);

        return mapping.findForward("sucess");
    }

    private String completeLine(String line, int columnNumber) {
        StringBuffer completedLineBuffer = new StringBuffer(line);

        int columnCouter = columnNumber;
        if (line.length() > columnCouter)
            columnCouter += columnCouter;

        for (int iter = line.length(); iter < columnCouter; iter++)
            completedLineBuffer.append('-');

        return completedLineBuffer.toString();
    }

    private String adjustLine(String line, int columnNumber, boolean bool) {

        String adjustedLine = new String();
        if (line.length() < columnNumber)
            return completeLine(line, columnNumber);
        String partLine = line.substring(0, columnNumber);
        if (line.charAt(columnNumber + 1) != ' ') {
            int lastSpace = partLine.lastIndexOf(" ", columnNumber);
            adjustedLine = line.substring(0, lastSpace);
            adjustedLine = completeLine(adjustedLine + " ", columnNumber);
            adjustedLine += "<tr><td>";
            if (bool)
                adjustedLine += completeLine(line.substring(lastSpace + 1, line.length()), columnNumber);
            else
                adjustedLine += line.substring(lastSpace + 1, line.length());

            adjustedLine += "</td></tr>";
        } else {
            adjustedLine += line.substring(0, columnNumber + 1);
            adjustedLine += "</td></tr><tr><td>";
            adjustedLine += line.substring(columnNumber + 1, line.length());
        }

        return adjustedLine;
    }
}
