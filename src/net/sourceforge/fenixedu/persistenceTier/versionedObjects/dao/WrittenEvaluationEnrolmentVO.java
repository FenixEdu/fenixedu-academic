/*
 * Created on 16/Mai/2005
 *
 * 
 */
package net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao;

import java.util.List;

import net.sourceforge.fenixedu.domain.Exam;
import net.sourceforge.fenixedu.domain.Student;
import net.sourceforge.fenixedu.domain.WrittenEvaluationEnrolment;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentWrittenEvaluationEnrolment;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.VersionedObjectsBase;

/**
 * @author Pedro Santos e Rita Carvalho
 *  
 */
public class WrittenEvaluationEnrolmentVO extends VersionedObjectsBase implements IPersistentWrittenEvaluationEnrolment {

    public List readByExamOID(Integer examOID) throws ExcepcaoPersistencia {
        Exam exam = (Exam) readByOID(Exam.class, examOID);
        
        return exam.getWrittenEvaluationEnrolments();
    }

    public List readByStudentOID(Integer studentOID) throws ExcepcaoPersistencia {
        Student student = (Student) readByOID(Student.class, studentOID);
        
        return student.getWrittenEvaluationEnrolments();        
    }

    public WrittenEvaluationEnrolment readBy(Integer examOID, Integer studentOID) throws ExcepcaoPersistencia {
        Exam exam = (Exam) readByOID(Exam.class, examOID);
        List<WrittenEvaluationEnrolment> writtenEvaluationEnrolments = exam.getWrittenEvaluationEnrolments();
        for(WrittenEvaluationEnrolment writtenEvaluationEnrolment : writtenEvaluationEnrolments){
            if(writtenEvaluationEnrolment.getStudent().getIdInternal().equals(studentOID)){
                return writtenEvaluationEnrolment;
            }
        }
        return null;
    }

}
