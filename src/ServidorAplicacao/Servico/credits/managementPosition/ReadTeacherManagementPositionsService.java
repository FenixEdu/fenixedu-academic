/*
 * Created on 7/Mar/2004
 */
package ServidorAplicacao.Servico.credits.managementPosition;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.credits.InfoManagementPositionCreditLine;
import DataBeans.util.Cloner;
import Dominio.Teacher;
import Dominio.credits.IManagementPositionCreditLine;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.credits.IPersistentManagementPositionCreditLine;

/**
 * @author jpvl
 */
public class ReadTeacherManagementPositionsService implements IService {
    public List run(Integer teacherId) throws FenixServiceException {
        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();

            IPersistentManagementPositionCreditLine managementPositionCreditLineDAO = sp
                    .getIPersistentManagementPositionCreditLine();

            List managementPositions = managementPositionCreditLineDAO.readByTeacher(new Teacher(
                    teacherId));

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