package ServidorPersistenteJDBC;

import Dominio.FuncNaoDocente;

/**
 * 
 * @author Fernanda Quitério e Tania Pousão
 */
public interface IFuncNaoDocentePersistente {
    public boolean alterarFuncNaoDocente(FuncNaoDocente funcionario);

    public boolean apagarFuncNaoDocente(int chaveFuncionario);

    public boolean escreverFuncNaoDocente(FuncNaoDocente funcionario);

    public FuncNaoDocente lerFuncNaoDocente(int codigoInterno);

    public FuncNaoDocente lerFuncNaoDocentePorFuncionario(int chaveFuncionario);

    public FuncNaoDocente lerFuncNaoDocentePorNumMecanografico(int numMecanografico);

    public FuncNaoDocente lerFuncNaoDocentePorPessoa(int chavePessoa);

    public int ultimoCodigoInterno();
}