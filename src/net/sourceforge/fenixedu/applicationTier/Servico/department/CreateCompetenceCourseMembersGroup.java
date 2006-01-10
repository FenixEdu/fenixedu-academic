/**
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.department;

import net.sourceforge.fenixedu.accessControl.AccessControl;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.DomainFactory;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.department.CompetenceCourseMembersGroup;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class CreateCompetenceCourseMembersGroup implements IService {

    public CompetenceCourseMembersGroup run(Department department) {

        Person creator = AccessControl.getUserView().getPerson();

        return (department.getCompetenceCourseMembersGroup() == null) ? DomainFactory
                .makeCompetenceCourseMembersGroup(creator, department) : department
                .getCompetenceCourseMembersGroup();
    }

}
