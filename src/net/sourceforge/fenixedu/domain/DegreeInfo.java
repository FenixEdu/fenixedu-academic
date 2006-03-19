package net.sourceforge.fenixedu.domain;

import java.sql.Timestamp;
import java.util.Calendar;

/**
 * @author Tania Pousao Created on 30/Out/2003
 */
public class DegreeInfo extends DegreeInfo_Base {

	public DegreeInfo() {
		super();
		setRootDomainObject(RootDomainObject.getInstance());
		setLastModificationDate(new Timestamp(Calendar.getInstance().getTimeInMillis()));
	}
	
	public DegreeInfo(Degree degree) {
		this();
		setDegree(degree);
	}

	public void delete() {
		removeDegree();
		deleteDomainObject();
	}
}
