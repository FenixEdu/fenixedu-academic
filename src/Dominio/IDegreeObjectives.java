/*
 * Created on 23/Jul/2003
 *
 * 
 */
package Dominio;

import java.util.Calendar;

/**
 * @author João Mota
 * 
 * 23/Jul/2003 fenix-head Dominio
 *  
 */
public interface IDegreeObjectives extends IDomainObject {
    /**
     * @return
     */
    public abstract ICurso getDegree();

    /**
     * @param degree
     */
    public abstract void setDegree(ICurso degree);

    /**
     * @return
     */
    public abstract Integer getKeyDegree();

    /**
     * @param degreeKey
     */
    public abstract void setKeyDegree(Integer keyDegree);

    /**
     * @return
     */
    public abstract String getGeneralObjectives();

    /**
     * @param generalObjectives
     */
    public abstract void setGeneralObjectives(String generalObjectives);

    /**
     * @return
     */
    public abstract String getOperacionalObjectives();

    /**
     * @param operacionalObjectives
     */
    public abstract void setOperacionalObjectives(String operacionalObjectives);

    public Calendar getEndDate();

    /**
     * @param endDate
     */
    public void setEndDate(Calendar endDate);

    /**
     * @return
     */
    public Calendar getStartingDate();

    /**
     * @param startingDate
     */
    public void setStartingDate(Calendar startingDate);
}