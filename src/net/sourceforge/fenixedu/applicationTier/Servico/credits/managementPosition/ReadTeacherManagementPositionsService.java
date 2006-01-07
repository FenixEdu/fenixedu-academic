/*
 * Created on 7/Mar/2004
 */
package net.sourceforge.fenixedu.applicationTier.Servico.credits.managementPosition;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.credits.InfoManagementPositionCreditLine;
import net.sourceforge.fenixedu.domain.credits.ManagementPositionCreditLine;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.credits.IPersistentManagementPositionCreditLine;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author jpvl
 */
public class ReadTeacherManagementPositionsService implements IService {
    public List run(Integer teacherId) throws FenixServiceException, ExcepcaoPersistencia {
        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

        IPersistentManagementPositionCreditLine managementPositionCreditLineDAO = sp
                .getIPersistentManagementPositionCreditLine();

        List managementPositions = managementPositionCreditLineDAO.readByTeacher(teacherId);

        List infoManagementPositions = (List) CollectionUtils.collect(managementPositions,
                new Transformer() {

                    public Object transform(Object input) {
                        ManagementPositionCreditLine managementPositionCreditLine = (ManagementPositionCreditLine) input;
                        InfoManagementPositionCreditLine infoManagementPositionCreditLine = InfoManagementPositionCreditLine
                                .newInfoFromDomain(managementPositionCreditLine);
                        return infoManagementPositionCreditLine;
                    }
                });
        return infoManagementPositions;
    }
}