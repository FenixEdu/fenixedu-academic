package net.sourceforge.fenixedu.applicationTier.Servico.person.qualification;

import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoPerson;
import net.sourceforge.fenixedu.dataTransferObject.InfoPersonWithInfoCountry;
import net.sourceforge.fenixedu.dataTransferObject.person.InfoQualificationWithPersonAndCountry;
import net.sourceforge.fenixedu.dataTransferObject.person.InfoSiteQualifications;
import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.domain.IQualification;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPessoaPersistente;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import pt.utl.ist.berserk.logic.serviceManager.IService;

public class ReadQualifications implements IService {

    public InfoSiteQualifications run(String user) throws ExcepcaoPersistencia {
        final ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        final IPessoaPersistente persistentPerson = sp.getIPessoaPersistente();
        final IPerson person = persistentPerson.lerPessoaPorUsername(user);

        List<IQualification> qualifications = person.getAssociatedQualifications();

        List infoQualifications = (List) CollectionUtils.collect(qualifications, new Transformer() {
            public Object transform(Object o) {
                IQualification qualification = (IQualification) o;
                return InfoQualificationWithPersonAndCountry.newInfoFromDomain(qualification);
            }
        });
        Collections.sort(infoQualifications, new BeanComparator("date"));

        InfoSiteQualifications infoSiteQualifications = new InfoSiteQualifications();
        infoSiteQualifications.setInfoQualifications(infoQualifications);

        final InfoPerson infoPerson = InfoPersonWithInfoCountry.newInfoFromDomain(person);
        infoSiteQualifications.setInfoPerson(infoPerson);

        return infoSiteQualifications;
    }

}
