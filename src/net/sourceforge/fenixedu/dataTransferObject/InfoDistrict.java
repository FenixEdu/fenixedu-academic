/**
 * Aug 31, 2005
 */
package net.sourceforge.fenixedu.dataTransferObject;

import net.sourceforge.fenixedu.domain.District;

/**
 * @author Ricardo Rodrigues
 * 
 */

public class InfoDistrict extends InfoObject {

    private String name;
    private String code;

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public String getCode() {
	return code;
    }

    public void setCode(String code) {
	this.code = code;
    }

    public void copyFromDomain(District district) {
	super.copyFromDomain(district);
	if (district != null) {
	    setName(district.getName());
	    setCode(district.getCode());
	}
    }

    public static InfoDistrict newInfoFromDomain(District district) {
	InfoDistrict infoDistrict = null;
	if (district != null) {
	    infoDistrict = new InfoDistrict();
	    infoDistrict.copyFromDomain(district);
	}
	return infoDistrict;
    }
}
