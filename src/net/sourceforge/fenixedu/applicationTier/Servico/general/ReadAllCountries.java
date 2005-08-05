/*
 * ReadAllCountries.java
 * 
 * O Servico ReadAllCountries devolve a lista de paises existentes
 * 
 * Created on 16 de Dezembro de 2002, 12:54
 */

/**
 * 
 * Autores : - Nuno Nunes (nmsn@rnl.ist.utl.pt) - Joana Mota
 * (jccm@rnl.ist.utl.pt)
 *  
 */

package net.sourceforge.fenixedu.applicationTier.Servico.general;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.ExcepcaoInexistente;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoCountry;
import net.sourceforge.fenixedu.domain.ICountry;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import pt.utl.ist.berserk.logic.serviceManager.IService;

public class ReadAllCountries implements IService {

    public Object run() throws ExcepcaoInexistente, FenixServiceException, ExcepcaoPersistencia {

        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        List countries = sp.getIPersistentCountry().readAllCountrys();

        if (countries.size() == 0) {
            throw new ExcepcaoInexistente("Non existing Countries !!");
        }

        return CollectionUtils.collect(countries, new Transformer() {

            public Object transform(Object input) {
                ICountry country = (ICountry) input;
                return InfoCountry.newInfoFromDomain(country);
            }
        });

    }
}