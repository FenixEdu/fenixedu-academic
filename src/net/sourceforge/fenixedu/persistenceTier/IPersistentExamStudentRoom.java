/*
 * Created on 30/Mai/2003
 *
 * 
 */
package net.sourceforge.fenixedu.persistenceTier;

import java.util.List;

import net.sourceforge.fenixedu.domain.IExam;
import net.sourceforge.fenixedu.domain.IExamStudentRoom;
import net.sourceforge.fenixedu.domain.IRoom;
import net.sourceforge.fenixedu.domain.IStudent;

/**
 * @author João Mota
 *  
 */
public interface IPersistentExamStudentRoom extends IPersistentObject {

    public List readBy(IExam exam) throws ExcepcaoPersistencia;

    public List readBy(IStudent student) throws ExcepcaoPersistencia;

    public List readBy(IRoom room) throws ExcepcaoPersistencia;

    public List readBy(IExam exam, IRoom room) throws ExcepcaoPersistencia;

    public IExamStudentRoom readBy(IExam exam, IStudent student) throws ExcepcaoPersistencia;

    public List readBy(IStudent student, IRoom room) throws ExcepcaoPersistencia;

    public IExamStudentRoom readBy(IExam exam, IStudent student, IRoom room) throws ExcepcaoPersistencia;

    public void delete(IExamStudentRoom examStudentRoom) throws ExcepcaoPersistencia;

    public void deleteByCriteria(IExamStudentRoom examStudentRoom) throws ExcepcaoPersistencia;

}