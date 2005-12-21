/**
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.department;

import net.sourceforge.fenixedu.accessControl.AccessControl;
import net.sourceforge.fenixedu.domain.DomainFactory;
import net.sourceforge.fenixedu.domain.IDepartment;
import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.domain.department.ICompetenceCourseMembersGroup;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class CreateCompetenceCourseMembersGroup implements IService {

    public ICompetenceCourseMembersGroup run(IDepartment department) {

        IPerson creator = AccessControl.getUserView().getPerson();

        return (department.getCompetenceCourseMembersGroup() == null) ? DomainFactory
                .makeCompetenceCourseMembersGroup(creator, department) : department
                .getCompetenceCourseMembersGroup();
    }

}
