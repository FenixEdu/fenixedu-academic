/*
 * Created on Nov 7, 2003 by jpvl
 *  
 */
package DataBeans.util;

/**
 * @author jpvl
 */
final public class ClassDescriptor
{
	private Class[] fieldClass;
	private String[] fieldsNames;

	protected ClassDescriptor(Class[] fieldClasses, String[] fieldsNames)
	{
		this.fieldClass = fieldClasses;
		this.fieldsNames = fieldsNames;
	}
	/**
	 * @return Returns the fieldClass.
	 */
	public Class[] getFieldClass()
	{
		return this.fieldClass;
	}

	/**
	 * @return Returns the fieldsNames.
	 */
	public String[] getFieldsNames()
	{
		return this.fieldsNames;
	}
}
