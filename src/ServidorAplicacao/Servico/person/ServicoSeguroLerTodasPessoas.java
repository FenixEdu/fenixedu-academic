package ServidorAplicacao.Servico.person;

import java.util.ArrayList;

import ServidorAplicacao.NotExecuteException;
import ServidorAplicacao.ServicoAutorizacao;
import ServidorAplicacao.ServicoSeguro;
import ServidorPersistenteJDBC.IPessoaPersistente;
import ServidorPersistenteJDBC.SuportePersistente;

/**
 * @author Fernanda & Tânia
 *
 */
public class ServicoSeguroLerTodasPessoas extends ServicoSeguro {
	private ArrayList listaPessoas = null;
	
	public ServicoSeguroLerTodasPessoas(ServicoAutorizacao servicoAutorizacaoLer){
		super(servicoAutorizacaoLer);
	}

	public void execute() throws NotExecuteException{
		IPessoaPersistente iPessoaPersistente = SuportePersistente.getInstance().iPessoaPersistente();
		
		if((listaPessoas = iPessoaPersistente.lerTodasPessoas()) == null){
			throw new NotExecuteException("error.pessoa.naoExiste");
		}
	}
	
	public ArrayList getListaPessoas() {
		return listaPessoas;
	}
}
