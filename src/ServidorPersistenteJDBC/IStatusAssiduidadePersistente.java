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
	public ArrayList lerTodosStatusActivos();	
	public ArrayList lerTodosStatusInactivos();	
}
