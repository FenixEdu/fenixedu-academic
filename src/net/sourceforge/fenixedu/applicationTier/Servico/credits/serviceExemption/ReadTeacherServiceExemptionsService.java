/*
 * Created on 7/Mar/2004
 */
package net.sourceforge.fenixedu.applicationTier.Servico.credits.serviceExemption;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.credits.InfoServiceExemptionCreditLine;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.credits.IServiceExemptionCreditLine;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.credits.IPersistentServiceExemptionCreditLine;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author jpvl
 */
public class ReadTeacherServiceExemptionsService implements IService {
    public List run(Integer teacherId) throws FenixServiceException {
        try {
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

            IPersistentServiceExemptionCreditLine serviceExemptionCreditLineDAO = sp
                    .getIPersistentServiceExemptionCreditLine();

            List serviceExemptions = serviceExemptionCreditLineDAO.readByTeacher(new Teacher(teacherId));

            List infoServiceExemptions = (List) CollectionUtils.collect(serviceExemptions,
                    new Transformer() {

                        public Object transform(Object input) {
                            IServiceExemptionCreditLine serviceExemptionCreditLine = (IServiceExemptionCreditLine) input;
                            InfoServiceExemptionCreditLine infoServiceExemptionCreditLine = Cloner
                                    .copyIServiceExemptionCreditLine2InfoServiceExemptionCreditLine(serviceExemptionCreditLine);
                            return infoServiceExemptionCreditLine;
                        }
                    });
            return infoServiceExemptions;
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException("Problems on database", e);

        }

    }
}