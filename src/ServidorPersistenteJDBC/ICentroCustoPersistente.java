package ServidorPersistenteJDBC;

import java.util.List;

import Dominio.CentroCusto;

/**
 * 
 * @author Fernanda Quitério e Tania Pousão
 */
public interface ICentroCustoPersistente {
    public boolean alterarCentroCusto(CentroCusto centroCusto);

    public boolean escreverCentroCusto(CentroCusto centroCusto);

    public CentroCusto lerCentroCusto(String sigla);

    public CentroCusto lerCentroCusto(int codigoInterno);

    public List lerTodosCentrosCusto();
}