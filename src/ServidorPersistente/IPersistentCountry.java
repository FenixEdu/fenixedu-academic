/*
 * IPersistentCountry.java
 *
 * Created on 28 of December 2002, 10:11
 */
 
/**
 *
 * @author  Nuno Nunes & Joana Mota
 */

package ServidorPersistente;

import java.util.List;

import Dominio.ICountry;

public interface IPersistentCountry extends IPersistentObject {
    
    public ICountry readCountryByName(String name) throws ExcepcaoPersistencia;
	public ICountry readCountryByNationality(String nationality) throws ExcepcaoPersistencia;
	public ICountry readCountryByCode(String code) throws ExcepcaoPersistencia;
	
    public List readAllCountrys() throws ExcepcaoPersistencia;
    public void writeCountry(ICountry country) throws ExcepcaoPersistencia;
    public void deleteCountryByName(String name) throws ExcepcaoPersistencia;
    public void deleteCountry(ICountry country) throws ExcepcaoPersistencia;
    
}
