package ServidorAplicacao;

interface IServicoAssiduidade {
    void execute() throws NotExecuteException, NotAuthorizeException;
}