/*
 * Created on 2/Fev/2004
 *  
 */
package Dominio;

/**
 * @author Tânia Pousão
 *  
 */
public interface ITutor extends IDomainObject {
    public ITeacher getTeacher();

    public IStudent getStudent();

    public void setTeacher(ITeacher teacher);

    public void setStudent(IStudent student);
}