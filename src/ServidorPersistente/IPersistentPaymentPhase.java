/*
 * Created on 6/Jan/2004
 *
 */
package ServidorPersistente;

import java.util.List;

import Dominio.IGratuityValues;

/**
 * @author Tânia Pousão
 *  
 */
public interface IPersistentPaymentPhase extends IPersistentObject {
    public void deletePaymentPhasesOfThisGratuity(Integer gratuityValuesID) throws ExcepcaoPersistencia;

    public List readByGratuityValues(IGratuityValues gratuityValues) throws ExcepcaoPersistencia;
}