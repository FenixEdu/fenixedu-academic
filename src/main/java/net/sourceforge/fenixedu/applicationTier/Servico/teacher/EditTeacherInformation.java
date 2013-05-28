package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Filtro.teacher.EditTeacherInformationAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.dataTransferObject.teacher.InfoOrientation;
import net.sourceforge.fenixedu.dataTransferObject.teacher.InfoPublicationsNumber;
import net.sourceforge.fenixedu.dataTransferObject.teacher.InfoServiceProviderRegime;
import net.sourceforge.fenixedu.dataTransferObject.teacher.InfoWeeklyOcupation;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.teacher.Orientation;
import net.sourceforge.fenixedu.domain.teacher.PublicationsNumber;
import net.sourceforge.fenixedu.domain.teacher.ServiceProviderRegime;
import net.sourceforge.fenixedu.domain.teacher.WeeklyOcupation;
import pt.ist.fenixWebFramework.services.Service;

public class EditTeacherInformation {

    protected Boolean run(InfoServiceProviderRegime infoServiceProviderRegime, InfoWeeklyOcupation infoWeeklyOcupation,
            List<InfoOrientation> infoOrientations, List<InfoPublicationsNumber> infoPublicationsNumbers) {

        Teacher teacher = RootDomainObject.getInstance().readTeacherByOID(infoServiceProviderRegime.getInfoTeacher().getExternalId());

        editServiceProviderRegime(infoServiceProviderRegime, teacher);
        editWeeklyOcupation(infoWeeklyOcupation, teacher);
        editOrientations(infoOrientations, teacher);
        editPublicationNumbers(infoPublicationsNumbers, teacher);

        // TODO <cargos de gestão>

        return Boolean.TRUE;
    }

    private void editServiceProviderRegime(InfoServiceProviderRegime infoServiceProviderRegime, Teacher teacher) {

        ServiceProviderRegime serviceProviderRegime =
                RootDomainObject.getInstance().readServiceProviderRegimeByOID(infoServiceProviderRegime.getExternalId());

        if (serviceProviderRegime == null) {
            serviceProviderRegime = new ServiceProviderRegime(teacher, infoServiceProviderRegime);

        } else {
            serviceProviderRegime.edit(infoServiceProviderRegime);

        }

    }

    private void editWeeklyOcupation(InfoWeeklyOcupation infoWeeklyOcupation, Teacher teacher) {
        // Weekly Ocupation
        WeeklyOcupation weeklyOcupation = teacher.getWeeklyOcupation();
        if (weeklyOcupation == null) {
            weeklyOcupation = new WeeklyOcupation(teacher, infoWeeklyOcupation);
        } else {
            weeklyOcupation.edit(infoWeeklyOcupation);
        }
    }

    private void editOrientations(List<InfoOrientation> infoOrientations, Teacher teacher) {
        // Orientations
        for (InfoOrientation infoOrientation : infoOrientations) {
            Orientation orientation = RootDomainObject.getInstance().readOrientationByOID(infoOrientation.getExternalId());

            if (orientation == null) {
                orientation = new Orientation(teacher, infoOrientation);

            } else {
                orientation.edit(infoOrientation);
            }

        }

    }

    private void editPublicationNumbers(List<InfoPublicationsNumber> infoPublicationsNumbers, Teacher teacher) {
        // Publications Number
        for (InfoPublicationsNumber infoPublicationsNumber : infoPublicationsNumbers) {
            PublicationsNumber publicationsNumber =
                    RootDomainObject.getInstance().readPublicationsNumberByOID(infoPublicationsNumber.getExternalId());

            if (publicationsNumber == null) {
                publicationsNumber = new PublicationsNumber(teacher, infoPublicationsNumber);

            } else {
                publicationsNumber.edit(infoPublicationsNumber);

            }

        }

    }

    // Service Invokers migrated from Berserk

    private static final EditTeacherInformation serviceInstance = new EditTeacherInformation();

    @Service
    public static Boolean runEditTeacherInformation(InfoServiceProviderRegime infoServiceProviderRegime,
            InfoWeeklyOcupation infoWeeklyOcupation, List<InfoOrientation> infoOrientations,
            List<InfoPublicationsNumber> infoPublicationsNumbers) throws NotAuthorizedException {
        EditTeacherInformationAuthorizationFilter.instance.execute(infoServiceProviderRegime, infoWeeklyOcupation,
                infoOrientations, infoPublicationsNumbers);
        return serviceInstance.run(infoServiceProviderRegime, infoWeeklyOcupation, infoOrientations, infoPublicationsNumbers);
    }

}