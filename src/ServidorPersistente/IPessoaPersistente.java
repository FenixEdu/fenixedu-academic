/*
 * IPessoaPersistente.java
 *
 * Created on 15 de Outubro de 2002, 15:03
 */

package ServidorPersistente;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import Dominio.IPessoa;
import ServidorPersistente.exceptions.ExistingPersistentException;
import Util.TipoDocumentoIdentificacao;

public interface IPessoaPersistente extends IPersistentObject {
    public void escreverPessoa(IPessoa pessoa) throws ExcepcaoPersistencia, ExistingPersistentException, IllegalAccessException, InvocationTargetException;
    public void apagarPessoaPorNumDocIdETipoDocId(String numeroDocumentoIdentificacao, TipoDocumentoIdentificacao tipoDocumentoIdentificacao) throws ExcepcaoPersistencia;
    public void apagarPessoa(IPessoa pessoa) throws ExcepcaoPersistencia;
    public void apagarTodasAsPessoas() throws ExcepcaoPersistencia;
    public IPessoa lerPessoaPorUsername(String username) throws ExcepcaoPersistencia;
	public List findPersonByName(String name) throws ExcepcaoPersistencia;
    public IPessoa lerPessoaPorNumDocIdETipoDocId(String numeroDocumentoIdentificacao, TipoDocumentoIdentificacao tipoDocumentoIdentificacao) throws ExcepcaoPersistencia;
    public ArrayList lerTodasAsPessoas() throws ExcepcaoPersistencia;
}
