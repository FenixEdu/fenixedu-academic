/*
 * Created on 7/Mar/2004
 */
package ServidorAplicacao.Servico.credits.serviceExemption;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.credits.InfoServiceExemptionCreditLine;
import DataBeans.util.Cloner;
import Dominio.Teacher;
import Dominio.credits.IServiceExemptionCreditLine;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.credits.IPersistentServiceExemptionCreditLine;

/**
 * @author jpvl
 */
public class ReadTeacherServiceExemptionsService implements IService {
    public List run(Integer teacherId) throws FenixServiceException {
        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();

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