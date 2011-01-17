package net.sourceforge.fenixedu.domain.phd.migration;

import java.util.Map;

import net.sourceforge.fenixedu.domain.RootDomainObject;

public class PhdMigrationGuiding extends PhdMigrationGuiding_Base {
    public static final String IST_INSTITUTION_CODE = "XPTO";
    
    private transient Integer phdStudentNumber;
    private transient String institutionCode;
    private transient String name;
    private transient String teacherCode;

    protected PhdMigrationGuiding() {
        super();
	setRootDomainObject(RootDomainObject.getInstance());
    }
    
    protected PhdMigrationGuiding(String data) {
	super();
	setData(data);
    }

    private void parse() {
	String[] compounds = getData().split("\\t");
	
	this.phdStudentNumber = Integer.parseInt(compounds[0].trim());
	this.institutionCode = compounds[2].trim();
	this.teacherCode = compounds[3].trim();
	this.name = compounds[4].trim();
    }

    public void parseAndSetNumber(Map<String, String> INSTITUTION_MAP) {
	parse();

	setNumber(this.phdStudentNumber);
	setInstitutionName(INSTITUTION_MAP.get(institutionCode));
    }

    public boolean isExternal() {
	return !institutionCode.equals(IST_INSTITUTION_CODE);
    }

}
