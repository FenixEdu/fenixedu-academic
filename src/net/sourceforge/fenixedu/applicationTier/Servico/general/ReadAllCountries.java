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

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoCountry;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.ICountry;
import net.sourceforge.fenixedu.applicationTier.Servico.ExcepcaoInexistente;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import pt.utl.ist.berserk.logic.serviceManager.IService;

public class ReadAllCountries implements IService {

    private static ReadAllCountries servico = new ReadAllCountries();

    /**
     * The singleton access method of this class.
     */
    public static ReadAllCountries getService() {
        return servico;
    }

    /**
     * The actor of this class.
     */
    private ReadAllCountries() {
    }

    /**
     * Returns the service name
     */

    public final String getNome() {
        return "ReadAllCountries";
    }

    public Object run() throws ExcepcaoInexistente, FenixServiceException {

        ISuportePersistente sp = null;
        List paises = new ArrayList();

        try {
            sp = SuportePersistenteOJB.getInstance();
            paises = sp.getIPersistentCountry().readAllCountrys();
        } catch (ExcepcaoPersistencia ex) {
            FenixServiceException newEx = new FenixServiceException("Persistence layer error");
            newEx.fillInStackTrace();
            throw newEx;
        }

        if (paises.size() == 0)
            throw new ExcepcaoInexistente("Non existing Countries !!");

        List countryList = (ArrayList) CollectionUtils.collect(paises, new Transformer() {

            public Object transform(Object input) {
                ICountry country = (ICountry) input;
                InfoCountry infoCountry = Cloner.copyICountry2InfoCountry(country);
                return infoCountry;
            }
        });

        return countryList;
    }
}