package ServidorPersistenteJDBC;

import java.util.List;

import Dominio.StatusAssiduidade;

/**
 * 
 * @author Fernanda Quitério & Tânia Pousão
 */
public interface IStatusAssiduidadePersistente {
    public boolean alterarStatus(StatusAssiduidade status);

    public boolean escreverStatus(StatusAssiduidade status);

    public StatusAssiduidade lerStatus(int codigoInterno);

    public StatusAssiduidade lerStatus(String designacao);

    public StatusAssiduidade lerStatus(String sigla, String designacao);

    public List lerTodosStatus();

    public List lerTodosStatusActivos();

    public List lerTodosStatusInactivos();

    public List lerTodosStatusPendentes();
}