package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.student.certificate;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudentCurricularPlanWithInfoStudentWithPersonAndDegree;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.studentCurricularPlan.Specialization;
import net.sourceforge.fenixedu.domain.studentCurricularPlan.StudentCurricularPlanState;
import net.sourceforge.fenixedu.persistenceTier.exceptions.ExistingPersistentException;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */

public class CreateDeclaration extends Service {

    public List run(InfoStudent infoStudent, Specialization persistentSupportecialization) throws Exception {

        List studentCurricularPlanList = null;
        List infoStudentCurricularPlanList = new ArrayList();

        try {
            studentCurricularPlanList = persistentSupport.getIStudentCurricularPlanPersistente()
                    .readAllByStudentNumberAndSpecialization(infoStudent.getNumber(),
                            infoStudent.getDegreeType(), persistentSupportecialization);

        } catch (ExistingPersistentException ex) {
            throw new ExistingServiceException(ex);
        }

        if (studentCurricularPlanList == null || studentCurricularPlanList.isEmpty()) {
            return null;
        }

        for (Iterator iter = studentCurricularPlanList.iterator(); iter.hasNext();) {
            StudentCurricularPlan studentCurricularPlan = (StudentCurricularPlan) iter.next();

            if (studentCurricularPlan != null || studentCurricularPlan.getIdInternal() != null) {
                infoStudentCurricularPlanList
                        .add(InfoStudentCurricularPlanWithInfoStudentWithPersonAndDegree
                                .newInfoFromDomain(studentCurricularPlan));
            }

        }

        return infoStudentCurricularPlanList;

    }

    public List run(InfoStudent infoStudent, Specialization persistentSupportecialization,
            StudentCurricularPlanState state) throws Exception {

        List studentCurricularPlanList = null;
        List infoStudentCurricularPlanList = new ArrayList();

        try {
            studentCurricularPlanList = persistentSupport.getIStudentCurricularPlanPersistente()
                    .readAllByStudentNumberAndSpecializationAndState(infoStudent.getNumber(),
                            infoStudent.getDegreeType(), persistentSupportecialization, state);

        } catch (ExistingPersistentException ex) {
            throw new ExistingServiceException(ex);
        }

        if (studentCurricularPlanList == null || studentCurricularPlanList.isEmpty()) {
            return null;
        }

        for (Iterator iter = studentCurricularPlanList.iterator(); iter.hasNext();) {
            StudentCurricularPlan studentCurricularPlan = (StudentCurricularPlan) iter.next();

            if (studentCurricularPlan != null || studentCurricularPlan.getIdInternal() != null) {
                infoStudentCurricularPlanList
                        .add(InfoStudentCurricularPlanWithInfoStudentWithPersonAndDegree
                                .newInfoFromDomain(studentCurricularPlan));
            }

        }

        return infoStudentCurricularPlanList;

    }

    // FIXME change paraemter states to List type, when berserk's reflection bug
    // is fixed
    public List run(InfoStudent infoStudent, Specialization persistentSupportecialization, ArrayList states)
            throws Exception {

        List studentCurricularPlanList = new ArrayList();
        List studentCurricularPlanListTmp = null;
        List infoStudentCurricularPlanList = new ArrayList();

        for (Iterator iter = states.iterator(); iter.hasNext();) {
            StudentCurricularPlanState state = (StudentCurricularPlanState) iter.next();

            try {
                studentCurricularPlanListTmp = persistentSupport.getIStudentCurricularPlanPersistente()
                        .readAllByStudentNumberAndSpecializationAndState(infoStudent.getNumber(),
                                infoStudent.getDegreeType(), persistentSupportecialization, state);

            } catch (ExistingPersistentException ex) {
                throw new ExistingServiceException(ex);
            }

            for (Iterator iterator = studentCurricularPlanListTmp.iterator(); iterator.hasNext();) {
                StudentCurricularPlan studentCurricularPlan = (StudentCurricularPlan) iterator.next();

                studentCurricularPlanList.add(studentCurricularPlan);
            }

        }

        if (studentCurricularPlanList.isEmpty()) {
            return null;
        }

        for (Iterator iter = studentCurricularPlanList.iterator(); iter.hasNext();) {
            StudentCurricularPlan studentCurricularPlan = (StudentCurricularPlan) iter.next();

            if (studentCurricularPlan != null || studentCurricularPlan.getIdInternal() != null) {
                infoStudentCurricularPlanList
                        .add(InfoStudentCurricularPlanWithInfoStudentWithPersonAndDegree
                                .newInfoFromDomain(studentCurricularPlan));
            }

        }

        return infoStudentCurricularPlanList;

    }
}