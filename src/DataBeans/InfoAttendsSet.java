/*
 * Created on 12/Ago/2004
 */
package DataBeans;

import java.util.List;

import Dominio.IAttendsSet;

/**
 * @author joasa & rmalo
 *
 */
public class InfoAttendsSet extends InfoObject{

	private String name;
	
	private InfoGroupProperties infoGroupProperties;
	
	private List infoAttends;
	
	private List infoStudentGroups;	
	

	/**
	 * Construtor
	 */
	public InfoAttendsSet()
	{

	}

	/**
	 * Construtor
	 */
	public InfoAttendsSet(String name)
	{
		this.name=name;
	}

	
	public String getName() {
        return name;
    }
	
	
	public void setName(String name) {
        this.name=name;
    }
	
	
	public InfoGroupProperties getInfoGroupProperties() {
        return infoGroupProperties;
    }
	
	
	public void setInfoGroupProperties(InfoGroupProperties infoGroupProperties) {
        this.infoGroupProperties=infoGroupProperties;
    }
	
	
	public List getInfoAttends() {
        return infoAttends;
    }
	
	
	public List getInfoStudentGroups() {
        return infoStudentGroups;
    }
	
	
	public void setInfoAttends(List infoAttends) {
        this.infoAttends=infoAttends;
    }
	
	
	public void setInfoStudentGroups(List infoStudentGroups) {
        this.infoStudentGroups=infoStudentGroups;
    }
	
	
	public boolean equals(Object arg0)
	{

		boolean result = false;
		if (arg0 instanceof InfoAttendsSet)
		{
			result = (getInfoAttends().equals(((InfoAttendsSet) arg0)
							.getInfoAttends())) 
					&& (getInfoStudentGroups().equals(((InfoAttendsSet) arg0)
							.getInfoStudentGroups()))
					&& (getName().equals(((InfoAttendsSet) arg0).getName()));
		}
		return result;
	}
	
	
	 public String toString() {
        String result = "[INFOATTENDSSET";
        result += ", nome=" + name;
        result += "]";
        return result;
    }

	 
	 
	 public void copyFromDomain(IAttendsSet attendsSet) {
        super.copyFromDomain(attendsSet);
        if (attendsSet != null) {
            setName(attendsSet.getName());
        }
    }

    public static InfoAttendsSet newInfoFromDomain(IAttendsSet attendsSet) {
        InfoAttendsSet infoAttendsSet = null;
        if (attendsSet != null) {
            infoAttendsSet = new InfoAttendsSet();
            infoAttendsSet.copyFromDomain(attendsSet);
        }
        return infoAttendsSet;
    }
	 
}
