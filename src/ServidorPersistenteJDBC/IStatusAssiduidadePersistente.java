package ServidorPersistenteJDBC;

import java.util.ArrayList;

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
	public ArrayList lerTodosStatus();
	public ArrayList lerTodosStatusActivos();	
	public ArrayList lerTodosStatusInactivos();	
	public ArrayList lerTodosStatusPendentes();
}
