/*
 * Created on 13/Nov/2003
 *
 */
package DataBeans.teacher;

import Util.CareerType;


/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 *
 */
public class InfoProfessionalCareer extends InfoCareer
{
	private String entity;
	private String function;
	
	/**
	 * 
	 */
	public InfoProfessionalCareer()
	{
	    setCareerType(new CareerType(CareerType.PROFESSIONAL));
	}
	
    /**
     * @return Returns the entity.
     */
    public String getEntity()
    {
        return entity;
    }

    /**
     * @param entity The entity to set.
     */
    public void setEntity(String entity)
    {
        this.entity = entity;
    }

    /**
     * @return Returns the function.
     */
    public String getFunction()
    {
        return function;
    }

    /**
     * @param function The function to set.
     */
    public void setFunction(String function)
    {
        this.function = function;
    }
}
