package net.sourceforge.fenixedu.applicationTier.Servico.credits.managementPosition;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.credits.InfoManagementPositionCreditLine;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.credits.ManagementPositionCreditLine;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class ReadTeacherManagementPositionsService extends Service {

    public List run(Integer teacherId) throws FenixServiceException, ExcepcaoPersistencia {
        final Teacher teacher = rootDomainObject.readTeacherByOID(teacherId);
        if (teacher == null) {
            throw new FenixServiceException("error.noTeacher");
        }

        final List<InfoManagementPositionCreditLine> result = new ArrayList<InfoManagementPositionCreditLine>();
        for (final ManagementPositionCreditLine managementPositionCreditLine : teacher
                .getManagementPositionsSet()) {
            result.add(InfoManagementPositionCreditLine.newInfoFromDomain(managementPositionCreditLine));
        }
        return result;
    }
}