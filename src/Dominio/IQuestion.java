/*
 * Created on 24/Jul/2003
 *
 */
package Dominio;

/**
 * @author Susana Fernandes
 */
public interface IQuestion extends IDomainObject {
	public abstract Integer getKeyMetadata();
	public abstract IMetadata getMetadata();
	public abstract String getXmlFile();
	public abstract String getXmlFileName();
	public abstract Boolean getVisibility();
	public abstract void setKeyMetadata(Integer integer);
	public abstract void setMetadata(IMetadata metadata);
	public abstract void setXmlFile(String string);
	public abstract void setXmlFileName(String string);
	public abstract void setVisibility(Boolean boolean1);
	public abstract boolean compareTo(Object obj);
}