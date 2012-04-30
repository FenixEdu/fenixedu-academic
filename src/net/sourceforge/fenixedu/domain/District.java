package net.sourceforge.fenixedu.domain;

import java.util.Comparator;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;

//TODO: Refactor remaining object to use district instead of strings
public class District extends District_Base {

    public static Comparator<District> COMPARATOR_BY_NAME = new Comparator<District>() {
	public int compare(District leftDistrict, District rightDistrict) {
	    int comparationResult = leftDistrict.getName().compareTo(rightDistrict.getName());
	    return (comparationResult == 0) ? leftDistrict.getIdInternal().compareTo(rightDistrict.getIdInternal())
		    : comparationResult;
	}
    };

    private District() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
    }
    
    public District(final String code, final String name) {
	this();
	init(code, name);
    }

    private void init(final String code, final String name) {
	checkParameters(code, name);

	super.setCode(code);
	super.setName(name);
    }

    private void checkParameters(final String code, final String name) {
	if (code == null) {
	    throw new DomainException("error.net.sourceforge.fenixedu.domain.District.code.cannot.be.null");
	}

	if (name == null) {
	    throw new DomainException("error.net.sourceforge.fenixedu.domain.District.name.cannot.be.null");
	}
    }

    public DistrictSubdivision getDistrictSubdivisionByName(final String name) {

	for (final DistrictSubdivision districtSubdivision : getDistrictSubdivisionsSet()) {
	    if (districtSubdivision.getName().equals(name)) {
		return districtSubdivision;
	    }
	}

	return null;

    }

    static public District readByCode(final String code) {
	for (final District district : RootDomainObject.getInstance().getDistricts()) {
	    if (district.getCode().equals(code)) {
		return district;
	    }
	}

	return null;
    }

    static public District readByName(final String name) {
	for (final District district : RootDomainObject.getInstance().getDistricts()) {
	    if (district.getName().equals(name)) {
		return district;
	    }
	}

	return null;
    }

}
