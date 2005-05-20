/*
 * Created on 7/Mar/2004
 */
package net.sourceforge.fenixedu.applicationTier.Servico.credits.managementPosition;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.credits.InfoManagementPositionCreditLine;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.credits.IManagementPositionCreditLine;
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
    public List run(Integer teacherId) throws FenixServiceException {
        try {
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

            IPersistentManagementPositionCreditLine managementPositionCreditLineDAO = sp
                    .getIPersistentManagementPositionCreditLine();

            List managementPositions = managementPositionCreditLineDAO.readByTeacher(teacherId);

            List infoManagementPositions = (List) CollectionUtils.collect(managementPositions,
                    new Transformer() {

                        public Object transform(Object input) {
                            IManagementPositionCreditLine managementPositionCreditLine = (IManagementPositionCreditLine) input;
                            InfoManagementPositionCreditLine infoManagementPositionCreditLine = Cloner
                                    .copyIManagementPositionCreditLine2InfoManagementPositionCreditLine(managementPositionCreditLine);
                            return infoManagementPositionCreditLine;
                        }
                    });
            return infoManagementPositions;
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException("Problems on database", e);

        }

    }
}