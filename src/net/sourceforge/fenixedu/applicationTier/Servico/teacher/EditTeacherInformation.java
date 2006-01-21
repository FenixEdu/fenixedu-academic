/*
 * Created on 17/Nov/2003
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.teacher.InfoOrientation;
import net.sourceforge.fenixedu.dataTransferObject.teacher.InfoPublicationsNumber;
import net.sourceforge.fenixedu.dataTransferObject.teacher.InfoServiceProviderRegime;
import net.sourceforge.fenixedu.dataTransferObject.teacher.InfoWeeklyOcupation;
import net.sourceforge.fenixedu.domain.DomainFactory;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.teacher.Orientation;
import net.sourceforge.fenixedu.domain.teacher.PublicationsNumber;
import net.sourceforge.fenixedu.domain.teacher.ServiceProviderRegime;
import net.sourceforge.fenixedu.domain.teacher.WeeklyOcupation;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;
import net.sourceforge.fenixedu.persistenceTier.IPersistentTeacher;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 * 
 */
public class EditTeacherInformation extends Service {
	
	private IPersistentObject persistentObject;

    /**
     * Executes the service.
     * 
     * @throws ExcepcaoPersistencia
     */
    public Boolean run(InfoServiceProviderRegime infoServiceProviderRegime,
            InfoWeeklyOcupation infoWeeklyOcupation, List<InfoOrientation> infoOrientations,
            List<InfoPublicationsNumber> infoPublicationsNumbers) throws ExcepcaoPersistencia {

		persistentObject = persistentSupport.getIPersistentObject();

        final IPersistentTeacher pteacher = persistentSupport.getIPersistentTeacher();
        Teacher teacher = (Teacher) pteacher.readByOID(Teacher.class, infoServiceProviderRegime
                .getInfoTeacher().getIdInternal());

		editServiceProviderRegime(infoServiceProviderRegime, teacher);	
		editWeeklyOcupation(infoWeeklyOcupation, teacher);
		editOrientations(infoOrientations, teacher);
		editPublicationNumbers(infoPublicationsNumbers, teacher);

        // TODO <cargos de gestão>

        return Boolean.TRUE;
    }
	
	private void editServiceProviderRegime(InfoServiceProviderRegime infoServiceProviderRegime,
			Teacher teacher) throws ExcepcaoPersistencia {

        ServiceProviderRegime serviceProviderRegime =
			(ServiceProviderRegime) persistentObject.readByOID(ServiceProviderRegime.class, infoServiceProviderRegime.getIdInternal());

        if (serviceProviderRegime == null) {
			serviceProviderRegime = DomainFactory.makeServiceProviderRegime(teacher, infoServiceProviderRegime);

		} else {
			serviceProviderRegime.edit(infoServiceProviderRegime);
			
		}
		
		
	}

	private void editWeeklyOcupation(InfoWeeklyOcupation infoWeeklyOcupation, Teacher teacher) throws ExcepcaoPersistencia {
        // Weekly Ocupation
        WeeklyOcupation weeklyOcupation = (WeeklyOcupation) persistentObject.readByOID(
                WeeklyOcupation.class, infoWeeklyOcupation.getIdInternal());

        if (weeklyOcupation == null) {
			weeklyOcupation = DomainFactory.makeWeeklyOcupation(teacher, infoWeeklyOcupation);

		} else {
			weeklyOcupation.edit(infoWeeklyOcupation);
        }

		
	}

	private void editOrientations(List<InfoOrientation> infoOrientations, Teacher teacher) throws ExcepcaoPersistencia {
        // Orientations
        for (InfoOrientation infoOrientation : infoOrientations) {
            Orientation orientation = (Orientation) persistentObject.readByOID(
                    Orientation.class, infoOrientation.getIdInternal());

            if (orientation == null) {
				orientation = DomainFactory.makeOrientation(teacher, infoOrientation);

			} else {
				orientation.edit(infoOrientation);				
			}

        }

	}

	private void editPublicationNumbers(List<InfoPublicationsNumber> infoPublicationsNumbers, Teacher teacher) throws ExcepcaoPersistencia {
        // Publications Number
        for (InfoPublicationsNumber infoPublicationsNumber : infoPublicationsNumbers) {
            PublicationsNumber publicationsNumber = (PublicationsNumber)
            	persistentObject.readByOID(PublicationsNumber.class, infoPublicationsNumber.getIdInternal());

            if (publicationsNumber == null) {
				publicationsNumber = DomainFactory.makePublicationsNumber(teacher, infoPublicationsNumber);

			} else {
				publicationsNumber.edit(infoPublicationsNumber);
				
            }

			
        }

		
	}

}
