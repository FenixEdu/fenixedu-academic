/*
 * IPessoaPersistente.java
 *
 * Created on 15 de Outubro de 2002, 15:03
 */

package ServidorPersistente;

import java.util.ArrayList;

import Dominio.IPessoa;
import Util.TipoDocumentoIdentificacao;

public interface IPessoaPersistente extends IPersistentObject {
    public void escreverPessoa(IPessoa pessoa) throws ExcepcaoPersistencia;
    public void apagarPessoaPorNumDocIdETipoDocId(String numeroDocumentoIdentificacao, TipoDocumentoIdentificacao tipoDocumentoIdentificacao) throws ExcepcaoPersistencia;
    public void apagarPessoa(IPessoa pessoa) throws ExcepcaoPersistencia;
    public void apagarTodasAsPessoas() throws ExcepcaoPersistencia;
    public IPessoa lerPessoaPorUsername(String username) throws ExcepcaoPersistencia;
    public IPessoa lerPessoaPorNumDocIdETipoDocId(String numeroDocumentoIdentificacao, TipoDocumentoIdentificacao tipoDocumentoIdentificacao) throws ExcepcaoPersistencia;
    public ArrayList lerTodasAsPessoas() throws ExcepcaoPersistencia;
}
