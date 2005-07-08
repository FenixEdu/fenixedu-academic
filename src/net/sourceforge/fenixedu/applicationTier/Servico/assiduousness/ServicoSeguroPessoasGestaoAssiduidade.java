package net.sourceforge.fenixedu.applicationTier.Servico.assiduousness;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.ServicoAutorizacao;
import net.sourceforge.fenixedu.applicationTier.ServicoSeguro;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotExecuteException;
import net.sourceforge.fenixedu.constants.assiduousness.Constants;
import net.sourceforge.fenixedu.persistenceTierJDBC.IPessoaPersistente;
import net.sourceforge.fenixedu.persistenceTierJDBC.SuportePersistente;

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