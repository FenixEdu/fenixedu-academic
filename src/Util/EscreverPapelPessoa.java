package Util;

import java.util.ListIterator;

import Dominio.Pessoa;
import ServidorAplicacao.Executor;
import ServidorAplicacao.NotAuthorizeException;
import ServidorAplicacao.NotExecuteException;
import ServidorAplicacao.PersistenceException;
import ServidorAplicacao.Servico.assiduousness.ServicoAutorizacaoLer;
import ServidorAplicacao.Servico.person.ServicoSeguroEscreverPapelPessoa;
import ServidorAplicacao.Servico.person.ServicoSeguroLerTodasPessoas;

/**
 *
 * @author  Fernanda Quitério & Tania Pousão
 */
public class EscreverPapelPessoa {

	public EscreverPapelPessoa() {
	}

	public static void main(String[] args) {

		// ler todas as pessoas existentes como funcionarios
		ServicoAutorizacaoLer servicoAutorizacaoLer = new ServicoAutorizacaoLer();
		ServicoSeguroLerTodasPessoas servicoSeguroLerTodasPessoas = new ServicoSeguroLerTodasPessoas(servicoAutorizacaoLer);		
		try {
			Executor.getInstance().doIt(servicoSeguroLerTodasPessoas);
		} catch (NotAuthorizeException nae) {
			System.out.println(nae.getMessage());
		} catch (NotExecuteException nee) {
			System.out.println(nee.getMessage());
		} catch (PersistenceException pe) {
			System.out.println(pe.getMessage());
		}
		System.out.println("PESSOAS: " + servicoSeguroLerTodasPessoas.getListaPessoas().size());
		
		
		ListIterator iteradorFuncionario = servicoSeguroLerTodasPessoas.getListaPessoas().listIterator();
		Pessoa pessoa = null;
		while (iteradorFuncionario.hasNext()) {
			pessoa = (Pessoa) iteradorFuncionario.next();
			
			System.out.println("ROLE da PERSON: " + pessoa.getCodigoInterno().intValue());
			
			// actualizar os papeis desta pessoa
			ServicoSeguroEscreverPapelPessoa servicoSeguroEscreverPapelPessoa =
				new ServicoSeguroEscreverPapelPessoa(servicoAutorizacaoLer, pessoa);
			try {
				Executor.getInstance().doIt(servicoSeguroEscreverPapelPessoa);
			} catch (NotAuthorizeException nae) {
				System.out.println(nae.getMessage());
			} catch (NotExecuteException nee) {
				System.out.println(nee.getMessage());
			} catch (PersistenceException pe) {
				System.out.println(pe.getMessage());
			}
		}
	}
}