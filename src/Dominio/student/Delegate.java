/*
 * Created on Feb 18, 2004
 *  
 */
package Dominio.student;

import Dominio.DomainObject;
import Dominio.ICurso;
import Dominio.IExecutionYear;
import Dominio.IStudent;
import Util.DelegateYearType;

/**
 * @author <a href="mailto:lesa@mega.ist.utl.pt">Leonor Almeida </a>
 * @author <a href="mailto:shmc@mega.ist.utl.pt">Sergio Montelobo </a>
 *  
 */
public class Delegate extends DomainObject implements IDelegate {
    private IStudent student;

    private ICurso degree;

    private IExecutionYear executionYear;

    private DelegateYearType yearType;

    private Integer keyStudent;

    private Integer keyDegree;

    private Integer keyExecutionYear;

    private Boolean type;

    /**
     *  
     */
    public Delegate() {
        super();
    }

    /**
     * @return Returns the type.
     */
    public Boolean getType() {
        return type;
    }

    /**
     * @param type
     *            The type to set.
     */
    public void setType(Boolean type) {
        this.type = type;
    }

    /**
     * @param idInternal
     */
    public Delegate(Integer idInternal) {
        super(idInternal);
    }

    /**
     * @return Returns the degree.
     */
    public ICurso getDegree() {
        return degree;
    }

    /**
     * @param degree
     *            The degree to set.
     */
    public void setDegree(ICurso degree) {
        this.degree = degree;
    }

    /**
     * @return Returns the executionYear.
     */
    public IExecutionYear getExecutionYear() {
        return executionYear;
    }

    /**
     * @param executionYear
     *            The executionYear to set.
     */
    public void setExecutionYear(IExecutionYear executionYear) {
        this.executionYear = executionYear;
    }

    /**
     * @return Returns the keyDegree.
     */
    public Integer getKeyDegree() {
        return keyDegree;
    }

    /**
     * @param keyDegree
     *            The keyDegree to set.
     */
    public void setKeyDegree(Integer keyDegree) {
        this.keyDegree = keyDegree;
    }

    /**
     * @return Returns the keyExecutionYear.
     */
    public Integer getKeyExecutionYear() {
        return keyExecutionYear;
    }

    /**
     * @param keyExecutionYear
     *            The keyExecutionYear to set.
     */
    public void setKeyExecutionYear(Integer keyExecutionYear) {
        this.keyExecutionYear = keyExecutionYear;
    }

    /**
     * @return Returns the keyStudent.
     */
    public Integer getKeyStudent() {
        return keyStudent;
    }

    /**
     * @param keyStudent
     *            The keyStudent to set.
     */
    public void setKeyStudent(Integer keyStudent) {
        this.keyStudent = keyStudent;
    }

    /**
     * @return Returns the student.
     */
    public IStudent getStudent() {
        return student;
    }

    /**
     * @param student
     *            The student to set.
     */
    public void setStudent(IStudent student) {
        this.student = student;
    }

    /**
     * @return Returns the type.
     */
    public DelegateYearType getYearType() {
        return yearType;
    }

    /**
     * @param type
     *            The type to set.
     */
    public void setYearType(DelegateYearType yearType) {
        this.yearType = yearType;
    }

}