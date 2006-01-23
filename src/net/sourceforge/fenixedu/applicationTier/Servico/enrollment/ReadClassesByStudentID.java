/**
 * Aug 7, 2005
 */
package net.sourceforge.fenixedu.applicationTier.Servico.enrollment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.InfoClass;
import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.SchoolClass;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.Student;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionPeriod;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.Transformer;

/**
 * @author Ricardo Rodrigues
 * 
 */

public class ReadClassesByStudentID extends Service {

    public List run(final Integer studentID, final Integer executionCourseID) throws ExcepcaoPersistencia {
        IPersistentExecutionPeriod persistentExecutionPeriod = persistentSupport.getIPersistentExecutionPeriod();

        final ExecutionPeriod currentExecutionPeriod = persistentExecutionPeriod.readActualExecutionPeriod();

        final Student student = (Student) persistentObject.readByOID(Student.class, studentID);
        
        List<ExecutionCourse> executionCourses = new ArrayList();
        for (Iterator iter = student.getAssociatedAttends().iterator(); iter.hasNext();) {
            Attends attends = (Attends) iter.next();
            if(attends.getDisciplinaExecucao().getExecutionPeriod().equals(currentExecutionPeriod)){
                executionCourses.add(attends.getDisciplinaExecucao());
            }
        }

        List remainingExecutionCourses = new ArrayList(executionCourses);
        StudentCurricularPlan scp = student.getActiveStudentCurricularPlan();
        
        Set<SchoolClass> allSchooClasses = new HashSet();
        Set<SchoolClass> selectedSchoolClasses = new HashSet();
        for (ExecutionCourse executionCourse : executionCourses) {
            List<Shift> shifts = executionCourse.getAssociatedShifts();
            for (Shift shift : shifts) {
                List<SchoolClass> schoolClasses = shift.getAssociatedClasses();
                for (SchoolClass schoolClass : schoolClasses) {
                    if(schoolClass.getExecutionDegree().getDegreeCurricularPlan().equals(scp.getDegreeCurricularPlan())){
                        selectedSchoolClasses.add(schoolClass);
                        remainingExecutionCourses.remove(executionCourse);
                    }
                    allSchooClasses.add(schoolClass);
                }
            }
        }        
        
        List classList = null;
        if(remainingExecutionCourses.isEmpty()){
            classList = new ArrayList(selectedSchoolClasses);
        }else{
            classList = new ArrayList(allSchooClasses);
        }        
        
        if(executionCourseID != null){
            CollectionUtils.filter(classList, new Predicate(){

                public boolean evaluate(Object arg0) {
                    SchoolClass schoolClass = (SchoolClass) arg0;
                    for (Shift shift : schoolClass.getAssociatedShifts()) {
                        if(shift.getDisciplinaExecucao().getIdInternal().equals(executionCourseID)){
                            return true;
                        }
                    }
                    return false;
                }});
        }
        
        Collections.sort(classList, new BeanComparator("nome"));
        
        List infoClassList = (List) CollectionUtils.collect(classList, new Transformer() {

            public Object transform(Object arg0) {
                SchoolClass schoolClass = (SchoolClass) arg0;
                return InfoClass.newInfoFromDomain(schoolClass);
            }
        });

        return infoClassList;
    }

}
