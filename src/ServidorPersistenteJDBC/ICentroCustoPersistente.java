package ServidorPersistenteJDBC;

import java.util.List;

import Dominio.CostCenter;

/**
 * 
 * @author Fernanda Quitério e Tania Pousão
 */
public interface ICentroCustoPersistente {
    public boolean alterarCentroCusto(CostCenter centroCusto);

    public boolean escreverCentroCusto(CostCenter centroCusto);

    public CostCenter lerCentroCusto(String sigla);

    public CostCenter lerCentroCusto(int codigoInterno);

    public List lerTodosCentrosCusto();
}