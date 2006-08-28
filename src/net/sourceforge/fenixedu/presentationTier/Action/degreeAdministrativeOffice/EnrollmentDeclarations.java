/*
 * Created on 20/Set/2004
 *
 */
package net.sourceforge.fenixedu.presentationTier.Action.degreeAdministrativeOffice;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.dataTransferObject.InfoPerson;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;
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

        IUserView userView = getUserView(request);

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
        List studentData = new ArrayList();
        InfoPerson infoPerson = infoStudent.getInfoPerson();

        Calendar calendar = Calendar.getInstance();

        Integer studentNumber = infoStudent.getNumber();
        String idNumber = infoPerson.getNumeroDocumentoIdentificacao();
        String parishOfBirth = infoPerson.getFreguesiaNaturalidade();
        String districtOfBirth = infoPerson.getDistritoNaturalidade();

        String nameOfFather = infoPerson.getNomePai();
        String nameOfMother = infoPerson.getNomeMae();

        String partOne = "DECLARA, a pedido do interessado, que o aluno Número " + studentNumber + ", ";
        String partTwo = infoPerson.getNome().toUpperCase() + ", ";
        String partThree = "portador do Bilhete de Identidade " + idNumber + ", ";
        String partFour = "natural de " + parishOfBirth.toUpperCase() + ", "
                + districtOfBirth.toUpperCase() + " ";
        String partFive = "filho de " + nameOfFather.toUpperCase() + " ";
        String partSix = "e de " + nameOfMother.toUpperCase() + ", ";
        String partSeven = "no ano lectivo " + calendar.get(Calendar.YEAR) + "/"
                + (calendar.get(Calendar.YEAR) + 1) + " ESTÁ INSCRITO no curso de "
                + degreeName.toUpperCase() + " deste instituto.";
        List allMonths = Data.getMonths();
        LabelValueBean label = (LabelValueBean) allMonths.get(calendar.get(Calendar.MONTH) + 1);
        String month = label.getLabel();
        String partEight = "Secretaria dos Serviços Académicos do Instituto Superior Técnico,<br/>em Lisboa, "
                + calendar.get(Calendar.DAY_OF_MONTH)
                + " de "
                + month
                + " de "
                + calendar.get(Calendar.YEAR);

        studentData.add(partOne);
        studentData.add(partTwo);
        studentData.add(partThree);
        studentData.add(partFour);
        studentData.add(partFive);
        studentData.add(partSix);
        studentData.add(partSeven);
        studentData.add(partEight);

        allStudentsData.add(studentData);

    }

}