package net.sourceforge.fenixedu.persistenceTierJDBC;

import net.sourceforge.fenixedu.domain.Administrative;

/**
 * 
 * @author Fernanda Quitério e Tania Pousão
 */
public interface IFuncNaoDocentePersistente {

    public Administrative lerFuncNaoDocentePorNumMecanografico(int numMecanografico);

}