package net.sourceforge.fenixedu.domain;

import java.util.Comparator;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;

//TODO: Refactor remaining object to use district subdivision instead of strings
public class DistrictSubdivision extends DistrictSubdivision_Base {

    public static Comparator<DistrictSubdivision> COMPARATOR_BY_NAME = new Comparator<DistrictSubdivision>() {
	public int compare(DistrictSubdivision leftDistrictSubdivision, DistrictSubdivision rightDistrictSubdivision) {
	    int comparationResult = leftDistrictSubdivision.getName().compareTo(rightDistrictSubdivision.getName());
	    return (comparationResult == 0) ? leftDistrictSubdivision.getIdInternal().compareTo(
		    rightDistrictSubdivision.getIdInternal()) : comparationResult;
	}
    };

    protected DistrictSubdivision() {
	super();
	super.setRootDomainObject(RootDomainObject.getInstance());
    }

    public DistrictSubdivision(final String code, final String name, final District district) {
	this();
	init(code, name, district);
    }

    private void init(final String code, final String name, final District district) {
	checkParameters(code, name, district);

	super.setCode(code);
	super.setName(name);
	super.setDistrict(district);
    }

    private void checkParameters(String code, String name, District district) {

	if (code == null) {
	    throw new DomainException("error.net.sourceforge.fenixedu.domain.DistrictSubdivision.code.cannot.be.null");
	}

	if (name == null) {
	    throw new DomainException("error.net.sourceforge.fenixedu.domain.DistrictSubdivision.name.cannot.be.null");
	}

	if (district == null) {
	    throw new DomainException("error.net.sourceforge.fenixedu.domain.DistrictSubdivision.district.cannot.be.null");
	}

    }

}
