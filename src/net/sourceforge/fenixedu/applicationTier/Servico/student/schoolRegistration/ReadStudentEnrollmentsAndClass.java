/*
 * Created on Jul 28, 2004
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.student.schoolRegistration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.InfoClass;
import net.sourceforge.fenixedu.dataTransferObject.InfoEnrolment;
import net.sourceforge.fenixedu.dataTransferObject.InfoEnrolmentWithInfoCurricularCourse;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.SchoolClass;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.ShiftType;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

/**
 * @author Nuno Correia
 * @author Ricardo Rodrigues
 */
public class ReadStudentEnrollmentsAndClass extends Service {

    public List run(IUserView userView) throws ExcepcaoPersistencia {
        Registration registration = userView.getPerson().getStudentByType(DegreeType.DEGREE);
        StudentCurricularPlan scp = null;
        if(registration != null) {
        	scp = registration.getActiveStudentCurricularPlan();
        }

        List studentEnrollments = scp.getEnrolments();
        final ExecutionPeriod executionPeriod = ExecutionPeriod.readActualExecutionPeriod();
        
        List studentShifts = new ArrayList();
        List<Shift> shifts = scp.getStudent().getShifts();
        for (Shift shift : shifts) {
            if (shift.getDisciplinaExecucao().getExecutionPeriod().equals(executionPeriod)) {
                studentShifts.add(shift);
            }
        }
        
        List filteredStudentShifts = filterStudentShifts(studentShifts);

        InfoClass infoClass = getClass(filteredStudentShifts, scp.getDegreeCurricularPlan().getDegree()
                .getNome());
        List infoEnrollments = new ArrayList();

        for (int iterator = 0; iterator < studentEnrollments.size(); iterator++) {

            Enrolment enrollment = (Enrolment) studentEnrollments.get(iterator);

            InfoEnrolment infoEnrollment = InfoEnrolmentWithInfoCurricularCourse
                    .newInfoFromDomain(enrollment);
            infoEnrollments.add(infoEnrollment);
        }

        List result = new ArrayList();
        result.add(infoEnrollments);
        result.add(infoClass);
        result.add(scp.getDegreeCurricularPlan().getDegree().getNome());

        return result;
    }

    /**
     * @param studentShifts
     * @return
     */
    private InfoClass getClass(List studentShifts, String degreeName) {

        List<SchoolClass> classesName = new ArrayList<SchoolClass>();
        for (int iter = 0; iter < studentShifts.size(); iter++) {
            Shift shift = (Shift) studentShifts.get(0);
            List classes = shift.getAssociatedClasses();
            if (classes.size() == 1) {
                SchoolClass klass = (SchoolClass) classes.get(0);
                return InfoClass.newInfoFromDomain(klass);
            }

            for (int j = 0; j < classes.size(); j++) {
                SchoolClass klass = (SchoolClass) classes.get(j);
                if (degreeName.equals(klass.getExecutionDegree().getDegreeCurricularPlan().getDegree()
                        .getNome())) {
                    classesName.add(klass);
                }
            }

        }
        return InfoClass.newInfoFromDomain(getMaxOcurrenceElement(classesName));
    }

    /**
     * @param studentShifts
     * @return
     */
    private List filterStudentShifts(List studentShifts) {
        List filteredStudentShifts = (List) CollectionUtils.select(studentShifts, new Predicate() {
            List validTypes = Arrays.asList(new ShiftType[] { ShiftType.PRATICA,
                    ShiftType.TEORICO_PRATICA });

            public boolean evaluate(Object input) {
                Shift shift = (Shift) input;
                return validTypes.contains(shift.getTipo());
            }
        });

        return filteredStudentShifts;
    }

    private SchoolClass getMaxOcurrenceElement(List<SchoolClass> classes) {

        int maxNumberOfOcurrencies = 0;
        SchoolClass resultElement = null;
        for (int iter = 0; iter < classes.size(); iter++) {
            SchoolClass element = (SchoolClass) classes.get(iter);
            int numberOfOcurrencis = CollectionUtils.cardinality(element, classes);
            if (numberOfOcurrencis > maxNumberOfOcurrencies)
                resultElement = element;
        }
        return resultElement;
    }
}

