/*
 * Created on 11/Ago/2005 - 19:15:37
 * 
 */

package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Filtro.teacher.CareerTeacherAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.teacher.Career;
import net.sourceforge.fenixedu.domain.teacher.ProfessionalCareer;
import net.sourceforge.fenixedu.domain.teacher.TeachingCareer;
import pt.ist.fenixWebFramework.services.Service;

/**
 * @author João Fialho & Rita Ferreira
 * 
 */
public class DeleteCareer extends FenixService {

    protected void run(Integer careerId) {
        Career career = rootDomainObject.readCareerByOID(careerId);

        if (career instanceof TeachingCareer) {
            TeachingCareer teachingCareer = (TeachingCareer) career;
            teachingCareer.delete();

        } else if (career instanceof ProfessionalCareer) {
            ProfessionalCareer professionalCareer = (ProfessionalCareer) career;
            professionalCareer.delete();
        }

    }

    // Service Invokers migrated from Berserk

    private static final DeleteCareer serviceInstance = new DeleteCareer();

    @Service
    public static void runDeleteCareer(Integer careerId) throws NotAuthorizedException {
        CareerTeacherAuthorizationFilter.instance.execute(careerId);
        serviceInstance.run(careerId);
    }

}