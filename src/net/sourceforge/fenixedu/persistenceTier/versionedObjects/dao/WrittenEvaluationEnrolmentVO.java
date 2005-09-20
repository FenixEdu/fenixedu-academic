/*
 * Created on 16/Mai/2005
 *
 * 
 */
package net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao;

import java.util.List;

import net.sourceforge.fenixedu.domain.Exam;
import net.sourceforge.fenixedu.domain.IExam;
import net.sourceforge.fenixedu.domain.IWrittenEvaluationEnrolment;
import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.domain.Student;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentWrittenEvaluationEnrolment;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.VersionedObjectsBase;

/**
 * @author Pedro Santos e Rita Carvalho
 *  
 */
public class WrittenEvaluationEnrolmentVO extends VersionedObjectsBase implements IPersistentWrittenEvaluationEnrolment {

    public List readByExamOID(Integer examOID) throws ExcepcaoPersistencia {
        IExam exam = (IExam) readByOID(Exam.class, examOID);
        
        return exam.getWrittenEvaluationEnrolments();
    }

    public List readByStudentOID(Integer studentOID) throws ExcepcaoPersistencia {
        IStudent student = (IStudent) readByOID(Student.class, studentOID);
        
        return student.getWrittenEvaluationEnrolments();        
    }

    public IWrittenEvaluationEnrolment readBy(Integer examOID, Integer studentOID) throws ExcepcaoPersistencia {
        IExam exam = (IExam) readByOID(Exam.class, examOID);
        List<IWrittenEvaluationEnrolment> writtenEvaluationEnrolments = exam.getWrittenEvaluationEnrolments();
        for(IWrittenEvaluationEnrolment writtenEvaluationEnrolment : writtenEvaluationEnrolments){
            if(writtenEvaluationEnrolment.getStudent().getIdInternal().equals(studentOID)){
                return writtenEvaluationEnrolment;
            }
        }
        return null;
    }

}
