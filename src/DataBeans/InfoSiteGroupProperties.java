/*
 * Created on 17/Ago/2003
 *
 */
package DataBeans;

/**
 * @author asnr and scpo
 *
 */
public class InfoSiteGroupProperties extends DataTranferObject implements ISiteComponent{

	private InfoGroupProperties infoGroupProperties;

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		String result = "[INFO_SITE_GROUP_PROPERTIES";
		result += ", infoGroupProperties=" + getInfoGroupProperties();
		result += "]";
		return result;
	}

	public boolean equals(Object arg0) {
			boolean result = false;
			if (arg0 instanceof InfoGroupProperties) {
				result = (getInfoGroupProperties().equals(((InfoSiteGroupProperties) arg0).getInfoGroupProperties()));
			} 
			return result;		
	}

	

	/**
	 * @return
	 */
	public InfoGroupProperties getInfoGroupProperties() {
		return infoGroupProperties;
	}

	/**
	 * @param infoShift
	 */
	public void setInfoGroupProperties(InfoGroupProperties infoGroupProperties) {
		this.infoGroupProperties = infoGroupProperties;
	}
	
}