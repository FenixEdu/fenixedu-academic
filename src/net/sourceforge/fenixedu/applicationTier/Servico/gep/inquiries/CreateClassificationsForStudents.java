/**
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.gep.inquiries;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Formatter;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.GenericPair;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.studentCurricularPlan.StudentCurricularPlanState;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.Closure;
import org.apache.commons.collections.Transformer;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class CreateClassificationsForStudents extends Service {

    private static Transformer getEntryGradeTransformer = new Transformer() {
        public Object transform(Object input) {
            return ((Registration) input).getEntryGrade();
        }
    };

    private static Transformer getApprovationRatioTransformer = new Transformer() {
        public Object transform(Object input) {
            return ((Registration) input).getApprovationRatio();
        }
    };

    private static Transformer getArithmeticMeanTransformer = new Transformer() {
        public Object transform(Object input) {
            return ((Registration) input).getArithmeticMean();
        }
    };

    private static Closure setEntryGradeClosure = new Closure() {
        public void execute(Object input) {
            if (input instanceof GenericPair<?, ?>) {
                GenericPair<Registration, Character> pairStudentClassification = (GenericPair<Registration, Character>) input;
                pairStudentClassification.getLeft().setEntryGradeClassification(
                        pairStudentClassification.getRight());
            }
        }
    };

    private static Closure setApprovationRatioClosure = new Closure() {
        public void execute(Object input) {
            if (input instanceof GenericPair<?, ?>) {
                GenericPair<Registration, Character> pairStudentClassification = (GenericPair<Registration, Character>) input;
                pairStudentClassification.getLeft().setApprovationRatioClassification(
                        pairStudentClassification.getRight());
            }
        }
    };

    private static Closure setArithmeticMeanClosure = new Closure() {
        public void execute(Object input) {
            if (input instanceof GenericPair<?, ?>) {
                GenericPair<Registration, Character> pairStudentClassification = (GenericPair<Registration, Character>) input;
                pairStudentClassification.getLeft().setArithmeticMeanClassification(
                        pairStudentClassification.getRight());
            }
        }
    };

    public ByteArrayOutputStream run(Integer[] entryGradeLimits, Integer[] approvationRatioLimits,
            Integer[] arithmeticMeanLimits, Integer degreeCurricularPlanID) throws ExcepcaoPersistencia,
            FileNotFoundException {
        
        ExecutionYear currentExecutionYear = ExecutionYear.readCurrentExecutionYear();

        List<Registration> otherYearsStudents = new ArrayList<Registration>();
        List<Registration> firstYearStudents = new ArrayList<Registration>();

        DegreeCurricularPlan degreeCurricularPlan = rootDomainObject.readDegreeCurricularPlanByOID(degreeCurricularPlanID);
        List<StudentCurricularPlan> studentCurricularPlans = degreeCurricularPlan
                .getStudentCurricularPlans();

        for (StudentCurricularPlan studentCurricularPlan : studentCurricularPlans) {
            if (studentCurricularPlan.getCurrentState() == StudentCurricularPlanState.ACTIVE) {
                Registration registration = studentCurricularPlan.getStudent();
                if (registration.getRegistrationYear() == currentExecutionYear) {
                    firstYearStudents.add(registration);
                } else {
                    registration.calculateApprovationRatioAndArithmeticMeanIfActive(true);
                    otherYearsStudents.add(registration);
                }
            }
        }

        Arrays.sort(entryGradeLimits);
        Arrays.sort(approvationRatioLimits);
        Arrays.sort(arithmeticMeanLimits);

        HashMap<Character, List<Registration>> splitedStudentsByEntryGrade = splitStudentsByClassification(
                entryGradeLimits, firstYearStudents, "entryGrade", getEntryGradeTransformer,
                setEntryGradeClosure);
        ByteArrayOutputStream entryGradeStream = studentsRenderer(splitedStudentsByEntryGrade,
                getEntryGradeTransformer);

        HashMap<Character, List<Registration>> splitedStudentsByApprovationRatio = splitStudentsByClassification(
                approvationRatioLimits, otherYearsStudents, "approvationRatio",
                getApprovationRatioTransformer, setApprovationRatioClosure);
        ByteArrayOutputStream approvationRatioStream = studentsRenderer(
                splitedStudentsByApprovationRatio, getApprovationRatioTransformer);

        HashMap<Character, List<Registration>> splitedStudentsByArithmeticMean = splitStudentsByClassification(
                arithmeticMeanLimits, otherYearsStudents, "arithmeticMean",
                getArithmeticMeanTransformer, setArithmeticMeanClosure);
        ByteArrayOutputStream arithmeticMeanStream = studentsRenderer(splitedStudentsByArithmeticMean,
                getArithmeticMeanTransformer);

        return zipResults(entryGradeStream, approvationRatioStream, arithmeticMeanStream);

    }

    private HashMap<Character, List<Registration>> splitStudentsByClassification(Integer[] limits,
            List<Registration> students, String field, Transformer fieldGetter, Closure fieldSetter) {
        HashMap<Character, List<Registration>> studentsClassifications = new HashMap<Character, List<Registration>>(
                limits.length);

        Collections.sort(students, new BeanComparator(field));
        
        List<Registration> weakStudents = new ArrayList<Registration>();
        for (Registration registration : students) {
            if(((Double) fieldGetter.transform(registration)) == 0.0){
                weakStudents.add(registration);
                fieldSetter.execute(new GenericPair<Registration, Character>(registration, 'F'));
            }
        }
        studentsClassifications.put('F', weakStudents);
        List<Registration> otherStudents = new ArrayList<Registration>(students); 
        otherStudents.removeAll(weakStudents);

        char classification = 'A';
        ListIterator<Registration> studentsIter = otherStudents.listIterator(otherStudents.size());
        for (int i = limits.length - 1; i > 0; i--) {

            int groundLimitIndex = (int) Math.ceil(otherStudents.size() * (limits[i - 1] / 100.0));
            Double groundLimitValue = (Double) fieldGetter.transform(otherStudents.get(groundLimitIndex));            
            Double  maxValue = (Double) fieldGetter.transform(otherStudents.get(otherStudents.size() - 1));
            if(maxValue.equals(groundLimitValue)){
                groundLimitValue -= 0.0000000001;
            }
            
            int limitIndex = studentsIter.previousIndex() + 1;
            for (Registration registration = studentsIter.previous(); (Double) fieldGetter.transform(registration) > groundLimitValue; registration = studentsIter
                    .previous()) {
                fieldSetter.execute(new GenericPair<Registration, Character>(registration, classification));
            }
            studentsIter.next();
            groundLimitIndex = studentsIter.nextIndex();

            studentsClassifications.put(classification, otherStudents.subList(groundLimitIndex, limitIndex));

            classification++;
        }

        
        List<Registration> lastStudents = otherStudents.subList((int) Math.ceil(otherStudents.size()
                * (limits[0] / 100.0)), studentsIter.nextIndex());
        for (Registration lastStudent : lastStudents) {
            fieldSetter.execute(new GenericPair<Registration, Character>(lastStudent, 'E'));
        }
        List<Registration> allLastStudents = new ArrayList<Registration>(lastStudents);
        allLastStudents.addAll(studentsClassifications.get('E'));
        studentsClassifications.put('E', allLastStudents); 
        return studentsClassifications;
    }

    private ByteArrayOutputStream studentsRenderer(
            HashMap<Character, List<Registration>> studentsClassifications, Transformer transformer) {

        List<Character> keys = new ArrayList<Character>(studentsClassifications.keySet());
        Collections.sort(keys);

        OutputStream outputStream = new ByteArrayOutputStream();
        Formatter fmt = new Formatter(outputStream);

        for (Character classification : keys) {
            for (ListIterator<Registration> studentIter = studentsClassifications.get(classification)
                    .listIterator(studentsClassifications.get(classification).size()); studentIter.hasPrevious();) {
                Registration registration = (Registration) studentIter.previous();
                fmt.format("%d\t%s\t%f\t%c\n", registration.getNumber(), registration.getPerson().getNome(),
                        transformer.transform(registration), classification);
            }
        }

        fmt.flush();

        return (ByteArrayOutputStream) outputStream;

    }

    private ByteArrayOutputStream zipResults(ByteArrayOutputStream entryGradeStream,
            ByteArrayOutputStream approvationRatioStream, ByteArrayOutputStream arithmeticMeanStream) {

        String[] filenames = new String[] { "entryGrade", "approvationRatio", "arithmeticMean" };
        ByteArrayOutputStream[] outputStreams = new ByteArrayOutputStream[] { entryGradeStream,
                approvationRatioStream, arithmeticMeanStream };

        try {

            ByteArrayOutputStream resultStream = new ByteArrayOutputStream();
            ZipOutputStream out = new ZipOutputStream(resultStream);

            // Compress the files
            for (int i = 0; i < filenames.length; i++) {

                out.putNextEntry(new ZipEntry(filenames[i]));
                out.write(outputStreams[i].toByteArray());
                out.closeEntry();
            }

            // Complete the ZIP file
            out.close();

            return resultStream;

        } catch (IOException e) {
        }

        return null;

    }

}
