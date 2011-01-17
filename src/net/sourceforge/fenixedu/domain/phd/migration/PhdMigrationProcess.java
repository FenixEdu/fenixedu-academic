package net.sourceforge.fenixedu.domain.phd.migration;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.phd.migration.common.exceptions.ParseException;

public class PhdMigrationProcess extends PhdMigrationProcess_Base {
    
    protected PhdMigrationProcess() {
        super();
	setRootDomainObject(RootDomainObject.getInstance());
    }
    
    protected PhdMigrationProcess(String[] processDataEntries, String[] personalDataEntries, String[] guidingEntries) {
	super();
	createProcessDataEntries(processDataEntries);
	createPersonalDataEntries(personalDataEntries);
	createGuidingEntries(guidingEntries);
    }

    static public PhdMigrationProcess createMigrationProcess(String[] processDataEntries, String[] personalDataEntries,
	    String[] guidingEntries) {
	return new PhdMigrationProcess(processDataEntries, personalDataEntries, guidingEntries);
    }

    private void createGuidingEntries(String[] guidingEntries) {


    }

    private void createPersonalDataEntries(String[] personalDataEntries) {
	for(String entry : personalDataEntries) {
	    PhdMigrationIndividualPersonalData personalData = new PhdMigrationIndividualPersonalData(entry);
	    try {
		personalData.parseAndSetNumber();
	    } catch (ParseException e) {
		// Save the stack
	    }
	}
    }

    private void createProcessDataEntries(String[] processDataEntries) {
	for (String entry : processDataEntries) {
	    PhdMigrationIndividualProcessData processData = new PhdMigrationIndividualProcessData(entry);
	    try {
		processData.parseAndSetNumber();
	    } catch (ParseException e) {

	    }
	}
    }

}
