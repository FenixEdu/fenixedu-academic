/*
 * Created on 13/Nov/2003
 *
 */
package Dominio.teacher;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 *  
 */
public interface IProfessionalCareer extends ICareer {

    public String getEntity();

    public String getFunction();

    public void setEntity(String entity);

    public void setFunction(String function);
}