package net.sourceforge.fenixedu.persistenceTierJDBC;

import net.sourceforge.fenixedu.domain.CostCenter;

/**
 * 
 * @author Fernanda Quitério e Tania Pousão
 */
public interface ICentroCustoPersistente {
    public CostCenter lerCentroCusto(int codigoInterno);
}
