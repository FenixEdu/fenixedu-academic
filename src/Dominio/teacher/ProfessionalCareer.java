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
public class ProfessionalCareer extends Career implements IProfessionalCareer {

    private String entity;

    private String function;

    /**
     *  
     */
    public ProfessionalCareer() {
        super();
    }

    /**
     * @param idInternal
     */
    public ProfessionalCareer(Integer idInternal) {
        super(idInternal);
    }

    /**
     * @return Returns the entity.
     */
    public String getEntity() {
        return entity;
    }

    /**
     * @param entity
     *            The entity to set.
     */
    public void setEntity(String entity) {
        this.entity = entity;
    }

    /**
     * @return Returns the function.
     */
    public String getFunction() {
        return function;
    }

    /**
     * @param function
     *            The function to set.
     */
    public void setFunction(String function) {
        this.function = function;
    }

    public String toString() {
        String result = "[Dominio.teacher.ProfessionalCareer ";
        result += ", beginYear=" + getBeginYear();
        result += ", endYear=" + getEndYear();
        result += ", entity=" + getEntity();
        result += ", function=" + getFunction();
        result += ", teacher=" + getTeacher();
        result += "]";
        return result;
    }

}