package net.sourceforge.fenixedu.presentationTier.Action.person;

import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.util.MultiLanguageString;

public class PersonFileSourceBean implements PersonFileSource {

	/**
	 * Default serial id. 
	 */
	private static final long serialVersionUID = 1L;

	private DomainReference<Unit> unit;
	private int count;
	
	public PersonFileSourceBean(Unit unit) {
		this.unit = new DomainReference<Unit>(unit);
		this.count = -1;
	}

	public MultiLanguageString getName() {
		return getUnit().getNameI18n();
	}

	public Unit getUnit() {
		return this.unit.getObject();
	}

	public int getCount() {
		if (this.count < 0) {
			this.count = getUnit().getAccessibileFiles(AccessControl.getPerson()).size();
		}
		
		return this.count;
	}
	
	public List<PersonFileSource> getChildren() {
		return Collections.emptyList();
	}
	
}
