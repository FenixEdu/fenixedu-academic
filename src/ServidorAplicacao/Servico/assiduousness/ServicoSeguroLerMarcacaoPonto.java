/*
 *
 * Por enquanto não é utilizada!!!
 *
 **/
package ServidorAplicacao.Servico.assiduousness;

import java.util.List;

import Dominio.MarcacaoPonto;
import ServidorAplicacao.ServicoAutorizacao;
import ServidorAplicacao.ServicoSeguro;
import ServidorAplicacao.Servico.exceptions.NotExecuteException;
import ServidorPersistenteJDBC.IMarcacaoPontoPersistente;
import ServidorPersistenteJDBC.SuportePersistente;

/**
 * 
 * @author Fernanda Quitério & Tania Pousão
 */
public class ServicoSeguroLerMarcacaoPonto extends ServicoSeguro {
    private MarcacaoPonto _marcacaoPonto = null;

    private List _listaMarcacoes = null;

    private int _chaveMarcacao;

    public ServicoSeguroLerMarcacaoPonto(ServicoAutorizacao servicoAutorizacaoLerHorarioTipo,
            int chaveMarcacao) {

        super(servicoAutorizacaoLerHorarioTipo);
        _chaveMarcacao = chaveMarcacao;
    }

    public void execute() throws NotExecuteException {
        IMarcacaoPontoPersistente iMarcacaoPontoPersistente = SuportePersistente.getInstance()
                .iMarcacaoPontoPersistente();
        if (_chaveMarcacao == 0) {
            if ((_listaMarcacoes = iMarcacaoPontoPersistente.lerMarcacoesPonto()) == null) {
                throw new NotExecuteException("error.noMarcacoes");
            }
        } else {
            if ((_marcacaoPonto = iMarcacaoPontoPersistente.lerMarcacaoPonto(_chaveMarcacao)) == null) {
                throw new NotExecuteException("error.noMarcacoes");
            }
        }
    }

    public MarcacaoPonto getMarcacaoPonto() {
        return _marcacaoPonto;
    }

    public List getListaMarcacoes() {
        return _listaMarcacoes;
    }
}