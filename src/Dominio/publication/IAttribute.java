/*
 * Created on 20/Abr/2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package Dominio.publication;
import java.util.List;
/**
 * @author _Sairf_
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public interface IAttribute {
	/**
	 * @return Returns the attributeType.
	 */
	public abstract String getAttributeType();
	/**
	 * @param attributeType The attributeType to set.
	 */
	public abstract void setAttributeType(String attributeType);
	/**
	 * @return Returns the publications.
	 */
	public abstract List getPublications();
	/**
	 * @param publications The publications to set.
	 */
	public abstract void setPublications(List publications);
}