/*
 * Created on 23/Jul/2003
 *
 * 
 */
package net.sourceforge.fenixedu.domain;

import java.util.Calendar;

/**
 * @author João Mota
 * 
 * 23/Jul/2003 fenix-head Dominio
 *  
 */
public class DegreeObjectives extends DomainObject implements IDegreeObjectives {

    private String generalObjectives;

    private String operacionalObjectives;

    private IDegree degree;

    private Integer keyDegree;

    private Calendar startingDate;

    private Calendar endDate;

    /**
     * @return
     */
    public Calendar getEndDate() {
        return endDate;
    }

    /**
     * @param endDate
     */
    public void setEndDate(Calendar endDate) {
        this.endDate = endDate;
    }

    /**
     * @return
     */
    public Calendar getStartingDate() {
        return startingDate;
    }

    /**
     * @param startingDate
     */
    public void setStartingDate(Calendar startingDate) {
        this.startingDate = startingDate;
    }

    /**
     *  
     */
    public DegreeObjectives() {
    }

    /**
     * @return
     */
    public IDegree getDegree() {
        return degree;
    }

    /**
     * @param degree
     */
    public void setDegree(IDegree degree) {
        this.degree = degree;
    }

    /**
     * @return
     */
    public Integer getKeyDegree() {
        return keyDegree;
    }

    /**
     * @param degreeKey
     */
    public void setKeyDegree(Integer degreeKey) {
        this.keyDegree = degreeKey;
    }

    /**
     * @return
     */
    public String getGeneralObjectives() {
        return generalObjectives;
    }

    /**
     * @param generalObjectives
     */
    public void setGeneralObjectives(String generalObjectives) {
        this.generalObjectives = generalObjectives;
    }

    /**
     * @return
     */
    public String getOperacionalObjectives() {
        return operacionalObjectives;
    }

    /**
     * @param operacionalObjectives
     */
    public void setOperacionalObjectives(String operacionalObjectives) {
        this.operacionalObjectives = operacionalObjectives;
    }

}