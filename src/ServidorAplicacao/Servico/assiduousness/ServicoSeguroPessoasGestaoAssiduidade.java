package ServidorAplicacao.Servico.assiduousness;

import java.util.List;

import ServidorAplicacao.ServicoAutorizacao;
import ServidorAplicacao.ServicoSeguro;
import ServidorAplicacao.Servico.exceptions.NotExecuteException;
import ServidorPersistenteJDBC.IPessoaPersistente;
import ServidorPersistenteJDBC.SuportePersistente;
import constants.assiduousness.Constants;

/**
 * 
 * @author Fernanda Quitério & Tânia Pousão
 */
public class ServicoSeguroPessoasGestaoAssiduidade extends ServicoSeguro {

    List _listaPessoas = null;

    public ServicoSeguroPessoasGestaoAssiduidade(ServicoAutorizacao servicoAutorizacaoLer) {
        super(servicoAutorizacaoLer);
    }

    public void execute() throws NotExecuteException {
        IPessoaPersistente iPessoaPersistente = SuportePersistente.getInstance().iPessoaPersistente();

        _listaPessoas = iPessoaPersistente.lerPessoasPorCargo(Constants.GESTAO_ASSIDUIDADE);

        if ((_listaPessoas == null) || (_listaPessoas != null && _listaPessoas.size() == 0)) {
            throw new NotExecuteException("error.gestaoAssiduidade.naoExiste");
        }
    }

    public List getListaPessoas() {
        return _listaPessoas;
    }
}