package ServidorAplicacao.Servico.person;

import java.util.Collection;
import java.util.Iterator;

import Dominio.Pessoa;
import ServidorAplicacao.Servico.exceptions.NotExecuteException;
import ServidorPersistenteJDBC.IPessoaPersistente;
import ServidorPersistenteJDBC.SuportePersistente;
import Util.LeituraFicheiroPessoa;

/**
 * @author  Ivo Brandão
 */
public class ServicoSeguroActualizarPessoas {

	private String _ficheiro = null;
	private Collection _listaPessoas = null;
	private Pessoa _pessoa = null;
	private String _delimitador = new String(";");

	/** Construtor */
	public ServicoSeguroActualizarPessoas(String[] args) {
		_ficheiro = args[0];
	}

	/** executa a actualizacao da tabela Pessoa na Base de Dados */
	public static void main(String[] args) throws NotExecuteException {
		ServicoSeguroActualizarPessoas servico = new ServicoSeguroActualizarPessoas(args);

		try {
			servico._listaPessoas =
				LeituraFicheiroPessoa.lerFicheiro(servico._ficheiro, servico._delimitador);
		} catch (NotExecuteException nee) {
			throw new NotExecuteException(nee.getMessage());
		}

		if (servico._listaPessoas != null) {
			Iterator iterador = servico._listaPessoas.iterator();
			IPessoaPersistente iPessoaPersistente = SuportePersistente.getInstance().iPessoaPersistente();
			Pessoa pessoaBD = null;

			System.out.println("-->ServicoSeguroActualizarPessoas.main");

			/* ciclo que percorre a Collection de Pessoas */
			while (iterador.hasNext()) {
				servico._pessoa = (Pessoa)iterador.next();

				System.out.println(
					"PESSOA: "
						+ servico._pessoa.getNome()
						+ " com BI "
						+ servico._pessoa.getNumeroDocumentoIdentificacao());

				pessoaBD =
					iPessoaPersistente.lerPessoa(
						servico._pessoa.getNumeroDocumentoIdentificacao(),
						servico._pessoa.getTipoDocumentoIdentificacao().getTipo().intValue());

				/* aqui testar se a pessoa ja existe na Base de Dados, se existir verificar se houve alteracoes */
				if (pessoaBD != null) {

					System.out.println(
						"!!!!!!!!!! A pessoa " + pessoaBD.getCodigoInterno() + " JÁ existe na BASE DE DADOS.");
					System.out.println();
					servico._pessoa.setCodigoInterno(pessoaBD.getCodigoInterno());
					/* verificar se sao diferentes, se forem escrever novos valores na Base de Dados */
					if (!(pessoaBD.equals(servico._pessoa))) {
						System.out.println("Os dados da pessoa " + pessoaBD.getCodigoInterno() + " mudaram.");
						System.out.println();
						iPessoaPersistente.alterarPessoa(servico._pessoa);
					} //if (!pessoaBD.equals(servico._pessoa))
				} //if(pessoaBD != null)
				else {
					//nova pessoa
					int codigoInterno = iPessoaPersistente.ultimoCodigoInterno() + 1;
					servico._pessoa.setCodigoInterno(new Integer(codigoInterno));
					System.out.println(
						"A pessoa "
							+ servico._pessoa.getCodigoInterno()
							+ " vai ser inserida na base da dados.");
					System.out.println();
					iPessoaPersistente.escreverPessoa(servico._pessoa);
				} //else
			} //while(iterador.hasNext())
		} //if(servico._listaPessoas != null)

	}

	/** retorna a lista de pessoas */
	public Collection getListaPessoas() {
		return _listaPessoas;
	}

}