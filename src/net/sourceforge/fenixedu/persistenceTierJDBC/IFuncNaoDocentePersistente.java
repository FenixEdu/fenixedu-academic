package net.sourceforge.fenixedu.persistenceTierJDBC;

import net.sourceforge.fenixedu.domain.NonTeacherEmployee;

/**
 * 
 * @author Fernanda Quitério e Tania Pousão
 */
public interface IFuncNaoDocentePersistente {

    public NonTeacherEmployee lerFuncNaoDocentePorNumMecanografico(int numMecanografico);

}