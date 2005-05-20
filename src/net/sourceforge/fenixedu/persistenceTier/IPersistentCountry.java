package net.sourceforge.fenixedu.persistenceTier;

import java.util.List;

import net.sourceforge.fenixedu.domain.ICountry;

/**
 * 
 * @author Nuno Nunes & Joana Mota
 */

public interface IPersistentCountry extends IPersistentObject {

    public ICountry readCountryByName(String name) throws ExcepcaoPersistencia;

    public ICountry readCountryByNationality(String nationality) throws ExcepcaoPersistencia;

    public ICountry readCountryByCode(String code) throws ExcepcaoPersistencia;

    public List readAllCountrys() throws ExcepcaoPersistencia;

}