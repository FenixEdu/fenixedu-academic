package pt.utl.ist.scripts.process.exportData.academic;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import net.sourceforge.fenixedu.dataTransferObject.GenericPair;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degree.DegreeType;

import org.fenixedu.bennu.scheduler.annotation.Task;
import org.fenixedu.bennu.scheduler.custom.CustomTask;

import com.google.common.base.Strings;

@Task(englishTitle = "EnrolmentStatistics", readOnly = true)
public class EnrolmentStatistics extends CustomTask {

    final Map<StudentCurricularPlan, Set<Enrolment>> studentsEnrolments = new HashMap<StudentCurricularPlan, Set<Enrolment>>();
    final Map<DegreeCurricularPlan, GenericPair<Integer, Integer>> degreesMap =
            new TreeMap<DegreeCurricularPlan, GenericPair<Integer, Integer>>(DegreeCurricularPlan.COMPARATOR_BY_NAME);
    final Map<Integer, Integer> enrolmentsNumber = new TreeMap<Integer, Integer>();
    int totalEnrolments = 0;

    @Override
    public void runTask() {
        final ExecutionSemester executionPeriod = ExecutionSemester.readActualExecutionSemester();
        for (final Enrolment enrolment : executionPeriod.getEnrolmentsSet()) {
            addToStudentsEnrolmentsMap(enrolment);
        }

        for (final Entry<StudentCurricularPlan, Set<Enrolment>> studentEnrolmentEntry : studentsEnrolments.entrySet()) {
            final Set<Enrolment> enrolments = studentEnrolmentEntry.getValue();
            totalEnrolments += enrolments.size();
            final StudentCurricularPlan studentCurricularPlan = studentEnrolmentEntry.getKey();
            addToEnrolmentsNumberMap(studentCurricularPlan, enrolments);
            addToDegreesMap(studentCurricularPlan.getDegreeCurricularPlan(), enrolments);
        }
        writeResults();
    }

    private void writeResults() {
        taskLog("Total -> " + totalEnrolments);
        taskLog("********************************");
        int total = 0;
        for (Entry<Integer, Integer> entry : enrolmentsNumber.entrySet()) {
            Integer value = entry.getValue();
            taskLog(entry.getKey() + " Inscricao -> " + value);
            total += value;
        }

        taskLog("Total de alunos inscritos -> " + total);
        taskLog("********************************");
        int totalEnrolments = 0;
        for (Entry<DegreeCurricularPlan, GenericPair<Integer, Integer>> entry : degreesMap.entrySet()) {
            String degreeName = Strings.padEnd(entry.getKey().getName(), 12, ' ');
            //degree name -> students enrolled - number of enrolments
            taskLog(degreeName + " -> " + Strings.padStart(entry.getValue().getLeft().toString(), 3, ' ') + "  "
                    + Strings.padStart(entry.getValue().getRight().toString(), 3, ' '));
            totalEnrolments += entry.getValue().getRight();
        }
        taskLog("********************************");
        taskLog("Total inscrições -> " + totalEnrolments);
    }

    private void addToDegreesMap(DegreeCurricularPlan degreeCurricularPlan, Set<Enrolment> enrolments) {
        GenericPair<Integer, Integer> genericPair = degreesMap.get(degreeCurricularPlan);
        if (genericPair == null) {
            genericPair = new GenericPair<Integer, Integer>(Integer.valueOf(0), Integer.valueOf(0));
            degreesMap.put(degreeCurricularPlan, genericPair);
        }
        genericPair.setLeft(genericPair.getLeft() + 1);
        genericPair.setRight(genericPair.getRight() + enrolments.size());
    }

    private void addToEnrolmentsNumberMap(StudentCurricularPlan studentCurricularPlan, final Set<Enrolment> enrolments) {
        int size = enrolments.size();
        Integer value = enrolmentsNumber.get(Integer.valueOf(size));
        if (value == null) {
            value = Integer.valueOf(0);
        }

        enrolmentsNumber.put(Integer.valueOf(size), value + 1);
    }

    private void addToStudentsEnrolmentsMap(final Enrolment enrolment) {
        final StudentCurricularPlan studentCurricularPlan = enrolment.getStudentCurricularPlan();
        if (isDegree(studentCurricularPlan.getDegreeCurricularPlan())) {
            Set<Enrolment> enrolmentsSet = studentsEnrolments.get(studentCurricularPlan);
            if (enrolmentsSet == null) {
                enrolmentsSet = new HashSet<Enrolment>();
                studentsEnrolments.put(studentCurricularPlan, enrolmentsSet);
            }
            enrolmentsSet.add(enrolment);
        }
    }

    private boolean isDegree(final DegreeCurricularPlan degreeCurricularPlan) {
        final DegreeType degreeType = degreeCurricularPlan.getDegreeType();
        return degreeType == DegreeType.BOLONHA_DEGREE || degreeType == DegreeType.BOLONHA_MASTER_DEGREE
                || degreeType == DegreeType.BOLONHA_INTEGRATED_MASTER_DEGREE;
    }
}
