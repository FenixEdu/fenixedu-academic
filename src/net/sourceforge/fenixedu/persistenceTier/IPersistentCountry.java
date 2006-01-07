package net.sourceforge.fenixedu.persistenceTier;

import java.util.List;

import net.sourceforge.fenixedu.domain.Country;

/**
 * 
 * @author Nuno Nunes & Joana Mota
 */

public interface IPersistentCountry extends IPersistentObject {

    public Country readCountryByName(String name) throws ExcepcaoPersistencia;

    public Country readCountryByNationality(String nationality) throws ExcepcaoPersistencia;

    public Country readCountryByCode(String code) throws ExcepcaoPersistencia;

    public List readAllCountrys() throws ExcepcaoPersistencia;

}