/*
 * Created on 1/Mar/2004
 *  
 */
package net.sourceforge.fenixedu.domain;

/**
 * @author T�nia Pous�o
 * 
 */
public class CostCenter extends CostCenter_Base {

    public CostCenter() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
    }

    public CostCenter(String code, String departament, String section1, String section2) {
	this();
	setCode(code);
	setDepartament(departament);
	setSection1(section1);
	setSection2(section2);
    }

    // -------------------------------------------------------------
    // read static methods
    // -------------------------------------------------------------

    public static CostCenter readByCode(final String code) {
	for (final CostCenter costCenter : RootDomainObject.getInstance().getCostCenters()) {
	    if (costCenter.getCode().equals(code)) {
		return costCenter;
	    }
	}
	return null;
    }

}