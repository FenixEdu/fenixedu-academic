/*
 * Created on 30/Ago/2004
 */
package net.sourceforge.fenixedu.dataTransferObject;

/**
 * @author joaosa & rmalo
 */

public class InfoSiteAttendsSet extends DataTranferObject implements ISiteComponent{


	private InfoAttendsSet infoAttendsSet;

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		String result = "[INFO_SITE_ATTENDS_SET";
		result += ", infoAttendsSet=" + getInfoAttendsSet();
		result += "]";
		return result;
	}

	public boolean equals(Object arg0) {
			boolean result = false;
			if (arg0 instanceof InfoAttendsSet) {
				result = (getInfoAttendsSet().equals(((InfoSiteAttendsSet) arg0).getInfoAttendsSet()));
			} 
			return result;		
	}

	

	/**
	 * @return InfoAttendsSet 
	 */
	public InfoAttendsSet getInfoAttendsSet() {
		return infoAttendsSet;
	}

	/**
	 * @param infoAttendsSet
	 */
	public void setInfoAttendsSet(InfoAttendsSet infoAttendsSet) {
		this.infoAttendsSet = infoAttendsSet;
	}
	
}