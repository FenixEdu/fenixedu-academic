package net.sourceforge.fenixedu.applicationTier.Servico.general;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.ExcepcaoInexistente;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoCountry;
import net.sourceforge.fenixedu.domain.Country;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import pt.ist.fenixframework.Atomic;

public class ReadAllCountries {

    @Atomic
    public static Object run() throws ExcepcaoInexistente, FenixServiceException {
        List<InfoCountry> result = new ArrayList<InfoCountry>();

        Collection<Country> countries = RootDomainObject.getInstance().getCountrys();
        if (countries.isEmpty()) {
            throw new ExcepcaoInexistente("Non existing Countries !!");
        }

        for (Country country : countries) {
            result.add(InfoCountry.newInfoFromDomain(country));
        }

        return result;
    }

}