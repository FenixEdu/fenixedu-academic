/*
 * Created on 12/Nov/2003
 */

package net.sourceforge.fenixedu.applicationTier.Servico.person.qualification;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.IServico;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoPerson;
import net.sourceforge.fenixedu.dataTransferObject.InfoPersonWithInfoCountry;
import net.sourceforge.fenixedu.dataTransferObject.person.InfoQualificationWithPersonAndCountry;
import net.sourceforge.fenixedu.dataTransferObject.person.InfoSiteQualifications;
import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.domain.IQualification;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentQualification;
import net.sourceforge.fenixedu.persistenceTier.IPessoaPersistente;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.PersistenceSupportFactory;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

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
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
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