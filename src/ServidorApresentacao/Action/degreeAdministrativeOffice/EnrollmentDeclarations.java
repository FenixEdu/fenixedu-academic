/*
 * Created on 20/Set/2004
 *
 */
package ServidorApresentacao.Action.degreeAdministrativeOffice;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.util.LabelValueBean;

import DataBeans.InfoPerson;
import DataBeans.InfoStudent;
import ServidorAplicacao.IUserView;
import ServidorApresentacao.Action.base.FenixDispatchAction;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import Util.Data;
import framework.factory.ServiceManagerServiceFactory;

/**
 * @author Nuno Correia
 * @author Ricardo Rodrigues
 *  
 */
public class EnrollmentDeclarations extends FenixDispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        return mapping.findForward("prepare");
    }

    public ActionForward generate(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        HttpSession session = request.getSession(false);

        IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

        DynaActionForm studentsNumberForm = (DynaActionForm) form;
        Integer firstStudentNumber = new Integer((String) studentsNumberForm.get("firstStudentNumber"));
        Integer lastStudentNumber = new Integer((String) studentsNumberForm.get("lastStudentNumber"));
        Object args[] = { firstStudentNumber, lastStudentNumber };

        List result = (List) ServiceManagerServiceFactory.executeService(userView,
                "ReadStudentsWithEnrollmentInCurrentSemester", args);

        List infoStudents = (List) result.get(0);
        List degreeNames = (List) result.get(1);
        List allStudentsData = new ArrayList();

        for (int iter = 0; iter < infoStudents.size(); iter++) {
            InfoStudent infoStudent = (InfoStudent) infoStudents.get(iter);
            String degreeName = (String) degreeNames.get(iter);
            fillStudentData(infoStudent, degreeName, allStudentsData);
        }

        request.setAttribute("allStudentsData", allStudentsData);

        return mapping.findForward("sucess");
    }

    /**
     * 
     * @param infoPerson
     * @param degreeName
     * @param allStudentsData
     */
    private void fillStudentData(InfoStudent infoStudent, String degreeName, List allStudentsData) {

        final int columnNumber = 73;
        List studentData = new ArrayList();
        InfoPerson infoPerson = infoStudent.getInfoPerson();

        Calendar calendar = Calendar.getInstance();

        Integer studentNumber = infoStudent.getNumber();
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
        String partSeven = "no ano lectivo " + calendar.get(Calendar.YEAR) + "/"
                + (calendar.get(Calendar.YEAR) + 1) + " ESTÁ INSCRITO no curso de "
                + degreeName.toUpperCase() + " deste instituto.";
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

        studentData.add(partOne1);
        studentData.add(partTwo1);
        studentData.add(partThree1);
        studentData.add(partFour1);
        studentData.add(partFive1);
        studentData.add(partSix1);
        studentData.add(partSeven1);
        studentData.add(partEight);

        allStudentsData.add(studentData);

    }

    /**
     * 
     * @param line
     * @param columnNumber
     * @return
     */
    private String completeLine(String line, int columnNumber) {
        StringBuffer completedLineBuffer = new StringBuffer(line);

        int columnCouter = columnNumber;
        if (line.length() > columnCouter)
            columnCouter += columnCouter;

        for (int iter = line.length(); iter < columnCouter; iter++)
            completedLineBuffer.append('-');

        return completedLineBuffer.toString();
    }

    /**
     * 
     * @param line
     * @param columnNumber
     * @param bool
     * @return
     */
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