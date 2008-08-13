/*
 * Created on 04/Sep/2004
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.Grouping;
import net.sourceforge.fenixedu.domain.StudentGroup;

/**
 * @author joaosa & rmalo
 * 
 */

public class DeleteAllGroupingMembers extends Service {

    public boolean run(Integer objectCode, Integer groupingCode) throws FenixServiceException {
	Grouping grouping = rootDomainObject.readGroupingByOID(groupingCode);

	if (grouping == null) {
	    throw new ExistingServiceException();
	}

	List attendsElements = new ArrayList();
	attendsElements.addAll(grouping.getAttends());
	Iterator iterator = attendsElements.iterator();
	while (iterator.hasNext()) {
	    Attends attend = (Attends) iterator.next();

	    boolean found = false;
	    Iterator iterStudentsGroups = grouping.getStudentGroups().iterator();
	    while (iterStudentsGroups.hasNext() && !found) {

		StudentGroup studentGroup = (StudentGroup) iterStudentsGroups.next();

		if (studentGroup != null) {
		    studentGroup.removeAttends(attend);
		    found = true;
		}
	    }
	    grouping.removeAttends(attend);
	}

	return true;
    }
}