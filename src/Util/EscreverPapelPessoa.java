package Util;

import java.util.ListIterator;

import Dominio.Pessoa;
import ServidorAplicacao.Executor;
import ServidorAplicacao.PersistenceException;
import ServidorAplicacao.Servico.assiduousness.ServicoAutorizacaoLer;
import ServidorAplicacao.Servico.exceptions.NotExecuteException;
import ServidorAplicacao.Servico.person.ServicoSeguroEscreverPapelPessoa;
import ServidorAplicacao.Servico.person.ServicoSeguroLerTodasPessoas;

/**
 * 
 * @author Fernanda Quitério & Tania Pousão
 */
public class EscreverPapelPessoa extends FenixUtil {

    public EscreverPapelPessoa() {
    }

    public static void main(String[] args) {

        // ler todas as pessoas existentes como funcionarios
        ServicoAutorizacaoLer servicoAutorizacaoLer = new ServicoAutorizacaoLer();
        ServicoSeguroLerTodasPessoas servicoSeguroLerTodasPessoas = new ServicoSeguroLerTodasPessoas(
                servicoAutorizacaoLer);
        try {
            Executor.getInstance().doIt(servicoSeguroLerTodasPessoas);
        } catch (NotExecuteException nee) {
            System.out.println(nee.getMessage());
        } catch (PersistenceException pe) {
            System.out.println(pe.getMessage());
        }

        ListIterator iteradorFuncionario = servicoSeguroLerTodasPessoas.getListaPessoas().listIterator();
        Pessoa pessoa = null;
        while (iteradorFuncionario.hasNext()) {
            pessoa = (Pessoa) iteradorFuncionario.next();

            // actualizar os papeis desta pessoa
            ServicoSeguroEscreverPapelPessoa servicoSeguroEscreverPapelPessoa = new ServicoSeguroEscreverPapelPessoa(
                    servicoAutorizacaoLer, pessoa);
            try {
                Executor.getInstance().doIt(servicoSeguroEscreverPapelPessoa);
            } catch (NotExecuteException nee) {
                System.out.println(nee.getMessage());
            } catch (PersistenceException pe) {
                System.out.println(pe.getMessage());
            }
        }
    }
}