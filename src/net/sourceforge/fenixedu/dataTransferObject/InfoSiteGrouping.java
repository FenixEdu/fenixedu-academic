/*
 * Created on 30/Ago/2004
 */
package net.sourceforge.fenixedu.dataTransferObject;

/**
 * @author joaosa & rmalo
 */

public class InfoSiteGrouping extends DataTranferObject implements ISiteComponent {

    private InfoGrouping infoGrouping;

    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {
	String result = "[INFO_SITE_ATTENDS_SET";
	result += ", infoAttendsSet=" + getInfoGrouping();
	result += "]";
	return result;
    }

    public boolean equals(Object arg0) {
	boolean result = false;
	if (arg0 instanceof InfoGrouping) {
	    result = (getInfoGrouping().equals(((InfoSiteGrouping) arg0).getInfoGrouping()));
	}
	return result;
    }

    /**
     * @return InfoAttendsSet
     */
    public InfoGrouping getInfoGrouping() {
	return infoGrouping;
    }

    /**
     * @param infoAttendsSet
     */
    public void setInfoGrouping(InfoGrouping infoGrouping) {
	this.infoGrouping = infoGrouping;
    }

}