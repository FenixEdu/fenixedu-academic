package net.sourceforge.fenixedu.domain.phd;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.phd.permissions.PhdPermission;

import org.joda.time.DateTime;

public class PhdProcessesManager extends PhdProcessesManager_Base {

    private PhdProcessesManager() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
	setWhenCreated(new DateTime());
    }

    public PhdProcessesManager(final AdministrativeOffice office) {
	this();
	checkOffice(office);

	super.setAdministrativeOffice(office);
    }

    private void checkOffice(AdministrativeOffice office) {
	check(office, "error.PhdProcessesManager.office.cannot.be.null");
	if (office.hasPhdProcessesManager()) {
	    throw new DomainException("error.PhdProcessesManager.office.already.has.phd.processes.manager");
	}
    }

    public void delete() {
	for (; hasAnyPermissions(); getPermissions().get(0).delete())
	    ;
	removeAdministrativeOffice();
	removeRootDomainObject();
	super.deleteDomainObject();
    }

    public Object getPermissionsSortedByType() {
	final List<PhdPermission> result = new ArrayList<PhdPermission>(getPermissions());
	Collections.sort(result, PhdPermission.COMPARATOR_BY_TYPE);
	return result;
    }
}
