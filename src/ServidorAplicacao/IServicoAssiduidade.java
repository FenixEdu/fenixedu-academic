package ServidorAplicacao;

import ServidorAplicacao.Servico.exceptions.NotAuthorizeException;
import ServidorAplicacao.Servico.exceptions.NotExecuteException;

interface IServicoAssiduidade {
    void execute() throws NotExecuteException, NotAuthorizeException;
}