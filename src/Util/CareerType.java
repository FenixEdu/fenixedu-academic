/*
 * Created on Nov 13, 2003
 *  
 */
package Util;

import java.util.ArrayList;

import org.apache.struts.util.LabelValueBean;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 *  
 */
public class CareerType
{

	public static final int PROFESSIONAL = 1;
	public static final int TEACHING = 2;

	public static final String PROFESSIONAL_STRING = "Professional";
	public static final String TEACHING_STRING = "Teaching";

	private Integer careerType;

	/** Creates a new instance of CareerType */
	public CareerType()
	{
	}

	public CareerType(int careerType)
	{
		this.careerType = new Integer(careerType);
	}

	public CareerType(Integer careerType)
	{
		this.careerType = careerType;
	}

	public CareerType(String careerType)
	{
		if (careerType.equals(CareerType.PROFESSIONAL_STRING))
			this.careerType = new Integer(CareerType.PROFESSIONAL);
		if (careerType.equals(CareerType.TEACHING_STRING))
			this.careerType = new Integer(CareerType.TEACHING);
	}

	/**
	 * @return Returns the careerType.
	 */
	public Integer getCareerType()
	{
		return careerType;
	}

	/**
	 * @param careerType
	 *                    The careerType to set.
	 */
	public void setCareerType(Integer careerType)
	{
		this.careerType = careerType;
	}

	public boolean equals(Object obj)
	{
		if (obj instanceof CareerType)
		{
			CareerType career = (CareerType) obj;
			return this.careerType.equals(career.getCareerType());
		} else
		{
			return false;
		}
	}

	public ArrayList toArrayList()
	{
		ArrayList result = new ArrayList();
		result.add(new LabelValueBean(CareerType.PROFESSIONAL_STRING, CareerType.PROFESSIONAL_STRING));
		result.add(new LabelValueBean(CareerType.TEACHING_STRING, CareerType.TEACHING_STRING));
		return result;
	}

	public String toString()
	{
		if (careerType.intValue() == CareerType.PROFESSIONAL)
			return CareerType.PROFESSIONAL_STRING;
		if (careerType.intValue() == CareerType.TEACHING)
			return CareerType.TEACHING_STRING;
		return ""; // Nunca e atingido
	}

}
