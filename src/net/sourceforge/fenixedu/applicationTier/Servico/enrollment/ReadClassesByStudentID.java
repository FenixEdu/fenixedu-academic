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

import net.sourceforge.fenixedu.dataTransferObject.InfoClass;
import net.sourceforge.fenixedu.domain.IAttends;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.domain.ISchoolClass;
import net.sourceforge.fenixedu.domain.IShift;
import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.domain.IStudentCurricularPlan;
import net.sourceforge.fenixedu.domain.Student;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionPeriod;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudent;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Ricardo Rodrigues
 * 
 */

public class ReadClassesByStudentID implements IService {

    public List run(Integer studentID) throws ExcepcaoPersistencia {
        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

        IPersistentStudent persistentStudent = sp.getIPersistentStudent();
        IPersistentExecutionPeriod persistentExecutionPeriod = sp.getIPersistentExecutionPeriod();

        final IExecutionPeriod currentExecutionPeriod = persistentExecutionPeriod.readActualExecutionPeriod();

        final IStudent student = (IStudent) persistentStudent.readByOID(Student.class, studentID);
        
        List<IExecutionCourse> executionCourses = new ArrayList();
        for (Iterator iter = student.getAssociatedAttends().iterator(); iter.hasNext();) {
            IAttends attends = (IAttends) iter.next();
            if(attends.getDisciplinaExecucao().getExecutionPeriod().equals(currentExecutionPeriod)){
                executionCourses.add(attends.getDisciplinaExecucao());
            }
        }

        List remainingExecutionCourses = new ArrayList(executionCourses);
        IStudentCurricularPlan scp = student.getActiveStudentCurricularPlan();
        
        Set<ISchoolClass> allSchooClasses = new HashSet();
        Set<ISchoolClass> selectedSchoolClasses = new HashSet();
        for (IExecutionCourse executionCourse : executionCourses) {
            List<IShift> shifts = executionCourse.getAssociatedShifts();
            for (IShift shift : shifts) {
                List<ISchoolClass> schoolClasses = shift.getAssociatedClasses();
                for (ISchoolClass schoolClass : schoolClasses) {
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
        
        Collections.sort(classList, new BeanComparator("nome"));
        
        List infoClassList = (List) CollectionUtils.collect(classList, new Transformer() {

            public Object transform(Object arg0) {
                ISchoolClass schoolClass = (ISchoolClass) arg0;
                return InfoClass.newInfoFromDomain(schoolClass);
            }
        });

        return infoClassList;
    }

}
