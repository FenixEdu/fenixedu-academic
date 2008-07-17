package net.sourceforge.fenixedu.domain;

public class EducationArea extends EducationArea_Base {

    public EducationArea(String code, String description) {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
	setCode(code);
	setDescription(description);
    }

    public EducationArea(String code, String description, EducationArea parentArea) {
	this(code, description);
	setParentArea(parentArea);
    }

}
