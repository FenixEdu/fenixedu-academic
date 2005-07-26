package net.sourceforge.fenixedu.presentationTier.backBeans.degreeAdministrativeOffice;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoEnrolment;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.backBeans.base.FenixBackingBean;

public class ChangeDegree extends FenixBackingBean {

    private String studentNumber;

    private InfoStudentCurricularPlan activeInfoStudentCurricularPlan;
    private InfoStudent infoStudent;
    private List<InfoEnrolment> enrolments;

    public String getStudentNumber() {
        return studentNumber;
    }

    public void setStudentNumber(String studentNumber) throws FenixFilterException, FenixServiceException {
        this.studentNumber = studentNumber;

        final Object[] args = { Integer.valueOf(studentNumber), DegreeType.DEGREE };
        activeInfoStudentCurricularPlan = (InfoStudentCurricularPlan) ServiceUtils.executeService(userView, "student.ReadActiveStudentCurricularPlanByNumberAndDegreeType", args);
        infoStudent = (InfoStudent) ServiceUtils.executeService(userView, "ReadStudentByNumberAndDegreeType", args);

        final Object[] argsReadEnrolments = { activeInfoStudentCurricularPlan.getIdInternal() };
        enrolments = (List<InfoEnrolment>) ServiceUtils.executeService(userView, "ReadEnrolmentsByStudentCurricularPlan", argsReadEnrolments);
    }

    public InfoStudentCurricularPlan getActiveInfoStudentCurricularPlan() throws FenixFilterException, FenixServiceException {
        return activeInfoStudentCurricularPlan;
    }

    public InfoStudent getInfoStudent() {
        return infoStudent;
    }

    public List<InfoEnrolment> getEnrolments() {
        return enrolments;
    }

}