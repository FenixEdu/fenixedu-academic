package net.sourceforge.fenixedu.applicationTier.Servico.assiduousness;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.ServicoAutorizacao;
import net.sourceforge.fenixedu.applicationTier.ServicoSeguro;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotExecuteException;
import net.sourceforge.fenixedu.persistenceTierJDBC.IParamJustificacaoPersistente;
import net.sourceforge.fenixedu.persistenceTierJDBC.SuportePersistente;

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