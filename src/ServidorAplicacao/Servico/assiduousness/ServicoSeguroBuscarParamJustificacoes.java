package ServidorAplicacao.Servico.assiduousness;

import java.util.List;

import ServidorAplicacao.ServicoAutorizacao;
import ServidorAplicacao.ServicoSeguro;
import ServidorAplicacao.Servico.exceptions.NotExecuteException;
import ServidorPersistenteJDBC.IParamJustificacaoPersistente;
import ServidorPersistenteJDBC.SuportePersistente;

/**
 * 
 * @author Fernanda Quitério & Tania Pousão
 */
public class ServicoSeguroBuscarParamJustificacoes extends ServicoSeguro {

    private List _listaJustificacoes = null;

    private List _listaParamJustificacoes = null;

    public ServicoSeguroBuscarParamJustificacoes(ServicoAutorizacao servicoAutorizacaoLer,
            List listaJustificacoes) {
        super(servicoAutorizacaoLer);
        _listaJustificacoes = listaJustificacoes;
    }

    public void execute() throws NotExecuteException {

        IParamJustificacaoPersistente iParamJustificacaoPersistente = SuportePersistente.getInstance()
                .iParamJustificacaoPersistente();
        if ((_listaParamJustificacoes = iParamJustificacaoPersistente
                .lerParamJustificacoes(_listaJustificacoes)) == null) {
            throw new NotExecuteException("error.tiposJustificacao.naoExistem");
        }
    }

    public List getListaJustificacoes() {
        return _listaParamJustificacoes;
    }
}