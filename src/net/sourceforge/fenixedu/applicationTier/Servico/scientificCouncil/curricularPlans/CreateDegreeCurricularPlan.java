package net.sourceforge.fenixedu.applicationTier.Servico.scientificCouncil.curricularPlans;

import net.sourceforge.fenixedu.accessControl.AccessControl;
import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.GradeScale;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Role;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class CreateDegreeCurricularPlan extends Service {

    public void run(Integer degreeId, String name, GradeScale gradeScale) throws FenixServiceException, ExcepcaoPersistencia {

        if (degreeId == null || name == null) {
            throw new InvalidArgumentsServiceException();
        }

        final Person creator = AccessControl.getUserView().getPerson();
        if (creator == null) {
            throw new FenixServiceException("error.degreeCurricularPlan.non.existing.creator");
        }
        
        final Degree degree = rootDomainObject.readDegreeByOID(degreeId);
        if (degree == null) {
            throw new FenixServiceException("error.degreeCurricularPlan.non.existing.degree");
        }

        // this is needed in the domain to give this role to the creator
        final Role bolonhaRole = Role.getRoleByRoleType(RoleType.BOLONHA_MANAGER);
        degree.createDegreeCurricularPlan(name, gradeScale, creator, bolonhaRole);
    }

}
