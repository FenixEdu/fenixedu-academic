/*
 * Created on 12/Nov/2003
 */

package ServidorAplicacao.Servico.person.qualification;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import DataBeans.InfoPerson;
import DataBeans.InfoPersonWithInfoCountry;
import DataBeans.person.InfoQualificationWithPersonAndCountry;
import DataBeans.person.InfoSiteQualifications;
import Dominio.IPerson;
import Dominio.IQualification;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentQualification;
import ServidorPersistente.IPessoaPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Barbosa
 * @author Pica
 */

public class ReadQualifications implements IServico {

    private static ReadQualifications service = new ReadQualifications();

    /**
     * The singleton access method of this class.
     */
    public static ReadQualifications getService() {
        return service;
    }

    /**
     * The constructor of this class.
     */
    private ReadQualifications() {
    }

    /**
     * The name of the service
     */
    public final String getNome() {
        return "ReadQualifications";
    }

    /**
     * Executes the service
     */
    public InfoSiteQualifications run(String user) throws FenixServiceException {
        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            IPersistentQualification persistentQualification = sp.getIPersistentQualification();
            IPessoaPersistente persistentPerson = sp.getIPessoaPersistente();

            IPerson person = persistentPerson.lerPessoaPorUsername(user);
            InfoPerson infoPerson = InfoPersonWithInfoCountry.newInfoFromDomain(person);
            List qualifications = persistentQualification.readQualificationsByPerson(person);

            List infoQualifications = (List) CollectionUtils.collect(qualifications, new Transformer() {
                public Object transform(Object o) {
                    IQualification qualification = (IQualification) o;
                    return InfoQualificationWithPersonAndCountry.newInfoFromDomain(qualification);
                }
            });

            InfoSiteQualifications infoSiteQualifications = new InfoSiteQualifications();
            infoSiteQualifications.setInfoQualifications(infoQualifications);
            infoSiteQualifications.setInfoPerson(infoPerson);

            return infoSiteQualifications;
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e.getMessage());
        } catch (Exception e) {
            throw new FenixServiceException(e.getMessage());
        }
    }
}