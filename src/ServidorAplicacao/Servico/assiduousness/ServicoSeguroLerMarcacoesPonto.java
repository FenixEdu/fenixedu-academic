package ServidorAplicacao.Servico.assiduousness;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import ServidorAplicacao.ServicoAutorizacao;
import ServidorAplicacao.ServicoSeguro;
import ServidorAplicacao.Servico.exceptions.NotExecuteException;
import ServidorPersistenteJDBC.IMarcacaoPontoPersistente;
import ServidorPersistenteJDBC.SuportePersistente;

/**
 * 
 * @author Fernanda Quitério & Tânia Pousão
 */
public class ServicoSeguroLerMarcacoesPonto extends ServicoSeguro {
    private List _listaFuncionarios = null;

    private List _listaCartoes = null;

    private List _listaEstados = null;

    private Timestamp _dataInicio;

    private Timestamp _dataFim;

    private List _listaMarcacoesPonto = new ArrayList();

    public ServicoSeguroLerMarcacoesPonto(ServicoAutorizacao servicoAutorizacaoLer,
            List listaFuncionarios, List listaCartoes, List listaEstados, Timestamp dataInicio,
            Timestamp dataFim) {
        super(servicoAutorizacaoLer);
        _listaFuncionarios = listaFuncionarios;
        _listaCartoes = listaCartoes;
        _listaEstados = listaEstados;
        _dataInicio = dataInicio;
        _dataFim = dataFim;
    }

    public void execute() throws NotExecuteException {

        IMarcacaoPontoPersistente iMarcacaoPontoPersistente = SuportePersistente.getInstance()
                .iMarcacaoPontoPersistente();
        if ((_listaMarcacoesPonto = iMarcacaoPontoPersistente.consultarMarcacoesPonto(
                _listaFuncionarios, _listaCartoes, _listaEstados, _dataInicio, _dataFim)) == null) {
            throw new NotExecuteException("error.marcacaoPonto.naoExistem");
        }

    }

    public List getListaMarcacoesPonto() {
        return _listaMarcacoesPonto;
    }
}