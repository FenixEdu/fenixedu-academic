package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.student.certificate;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudentCurricularPlan;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.studentCurricularPlan.Specialization;
import net.sourceforge.fenixedu.domain.studentCurricularPlan.StudentCurricularPlanState;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */

public class CreateDeclaration {

    @Checked("RolePredicates.MASTER_DEGREE_ADMINISTRATIVE_OFFICE_PREDICATE")
    @Service
    public static List run(InfoStudent infoStudent, Specialization specialization) throws Exception {

        List infoStudentCurricularPlanList = new ArrayList();

        Registration registration =
                Registration.readStudentByNumberAndDegreeType(infoStudent.getNumber(), infoStudent.getDegreeType());
        if (registration == null) {
            return null;
        }

        List<StudentCurricularPlan> studentCurricularPlanList =
                registration.getStudentCurricularPlansBySpecialization(specialization);

        if (studentCurricularPlanList == null || studentCurricularPlanList.isEmpty()) {
            return null;
        }

        for (Object element : studentCurricularPlanList) {
            StudentCurricularPlan studentCurricularPlan = (StudentCurricularPlan) element;

            if (studentCurricularPlan != null && studentCurricularPlan.getIdInternal() != null) {
                infoStudentCurricularPlanList.add(InfoStudentCurricularPlan.newInfoFromDomain(studentCurricularPlan));
            }

        }

        return infoStudentCurricularPlanList;

    }

    @Checked("RolePredicates.MASTER_DEGREE_ADMINISTRATIVE_OFFICE_PREDICATE")
    @Service
    public static List run(InfoStudent infoStudent, Specialization specialization, StudentCurricularPlanState state)
            throws Exception {

        List infoStudentCurricularPlanList = new ArrayList();

        Registration registration =
                Registration.readStudentByNumberAndDegreeType(infoStudent.getNumber(), infoStudent.getDegreeType());
        if (registration == null) {
            return null;
        }
        List studentCurricularPlanList = registration.getStudentCurricularPlansBySpecialization(specialization);

        if (studentCurricularPlanList == null || studentCurricularPlanList.isEmpty()) {
            return null;
        }

        for (Iterator iter = studentCurricularPlanList.iterator(); iter.hasNext();) {
            StudentCurricularPlan studentCurricularPlan = (StudentCurricularPlan) iter.next();

            if (studentCurricularPlan != null && studentCurricularPlan.getIdInternal() != null) {
                infoStudentCurricularPlanList.add(InfoStudentCurricularPlan.newInfoFromDomain(studentCurricularPlan));
            }

        }

        return infoStudentCurricularPlanList;
    }

    // FIXME change paraemter states to List type, when berserk's reflection bug
    // is fixed
    @Checked("RolePredicates.MASTER_DEGREE_ADMINISTRATIVE_OFFICE_PREDICATE")
    @Service
    public static List run(InfoStudent infoStudent, Specialization specialization, ArrayList states) throws Exception {

        List studentCurricularPlanList = new ArrayList();
        List infoStudentCurricularPlanList = new ArrayList();

        Registration registration =
                Registration.readStudentByNumberAndDegreeType(infoStudent.getNumber(), infoStudent.getDegreeType());
        if (registration == null) {
            return null;
        }
        for (Iterator iter = states.iterator(); iter.hasNext();) {
            StudentCurricularPlanState state = (StudentCurricularPlanState) iter.next();
            List<StudentCurricularPlan> studentCurricularPlanListTmp =
                    registration.getStudentCurricularPlansBySpecialization(specialization);
            for (Object element : studentCurricularPlanListTmp) {
                StudentCurricularPlan studentCurricularPlan = (StudentCurricularPlan) element;

                studentCurricularPlanList.add(studentCurricularPlan);
            }

        }

        if (studentCurricularPlanList.isEmpty()) {
            return null;
        }

        for (Iterator iter = studentCurricularPlanList.iterator(); iter.hasNext();) {
            StudentCurricularPlan studentCurricularPlan = (StudentCurricularPlan) iter.next();

            if (studentCurricularPlan != null && studentCurricularPlan.getIdInternal() != null) {
                infoStudentCurricularPlanList.add(InfoStudentCurricularPlan.newInfoFromDomain(studentCurricularPlan));
            }

        }

        return infoStudentCurricularPlanList;
    }
}