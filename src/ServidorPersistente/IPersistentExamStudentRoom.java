/*
 * Created on 30/Mai/2003
 *
 * 
 */
package ServidorPersistente;

import java.util.List;

import Dominio.IExam;
import Dominio.IExamStudentRoom;
import Dominio.ISala;
import Dominio.IStudent;

/**
 * @author João Mota
 *  
 */
public interface IPersistentExamStudentRoom extends IPersistentObject {

    public List readBy(IExam exam) throws ExcepcaoPersistencia;

    public List readBy(IStudent student) throws ExcepcaoPersistencia;

    public List readBy(ISala room) throws ExcepcaoPersistencia;

    public List readBy(IExam exam, ISala room) throws ExcepcaoPersistencia;

    public IExamStudentRoom readBy(IExam exam, IStudent student) throws ExcepcaoPersistencia;

    public List readBy(IStudent student, ISala room) throws ExcepcaoPersistencia;

    public IExamStudentRoom readBy(IExam exam, IStudent student, ISala room) throws ExcepcaoPersistencia;

    public void delete(IExamStudentRoom examStudentRoom) throws ExcepcaoPersistencia;

    public void deleteByCriteria(IExamStudentRoom examStudentRoom) throws ExcepcaoPersistencia;

}