package net.sourceforge.fenixedu.persistenceTierJDBC;

import net.sourceforge.fenixedu.domain.NonTeacherEmployee;

/**
 * 
 * @author Fernanda Quitério e Tania Pousão
 */
public interface IFuncNaoDocentePersistente {
    public boolean alterarFuncNaoDocente(NonTeacherEmployee funcionario);

    public boolean apagarFuncNaoDocente(int chaveFuncionario);

    public boolean escreverFuncNaoDocente(NonTeacherEmployee funcionario);

    public NonTeacherEmployee lerFuncNaoDocente(int codigoInterno);

    public NonTeacherEmployee lerFuncNaoDocentePorFuncionario(int chaveFuncionario);

    public NonTeacherEmployee lerFuncNaoDocentePorNumMecanografico(int numMecanografico);

    public NonTeacherEmployee lerFuncNaoDocentePorPessoa(int chavePessoa);

    public int ultimoCodigoInterno();
}