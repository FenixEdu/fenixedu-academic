/*
 * IPessoaPersistente.java
 *
 * Created on 15 de Outubro de 2002, 15:03
 */

package ServidorPersistente;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import Dominio.IPessoa;
import ServidorPersistente.exceptions.ExistingPersistentException;
import Util.TipoDocumentoIdentificacao;

public interface IPessoaPersistente extends IPersistentObject {
    /**
     * 
     * @param pessoa
     * @throws ExcepcaoPersistencia
     * @throws ExistingPersistentException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @deprecated
     */
    public void escreverPessoa(IPessoa pessoa) throws ExcepcaoPersistencia, ExistingPersistentException, IllegalAccessException, InvocationTargetException;
    public void apagarPessoaPorNumDocIdETipoDocId(String numeroDocumentoIdentificacao, TipoDocumentoIdentificacao tipoDocumentoIdentificacao) throws ExcepcaoPersistencia;
    public void apagarPessoa(IPessoa pessoa) throws ExcepcaoPersistencia;
   
    public IPessoa lerPessoaPorUsername(String username) throws ExcepcaoPersistencia;
	public List findPersonByName(String name) throws ExcepcaoPersistencia;
    public List findPersonByName(String name, Integer startIndex, Integer numberOfElementsInSpan) throws ExcepcaoPersistencia;
    public Integer countAllPersonByName(String name);
	public List findPersonByNameAndEmailAndUsernameAndDocumentId(String name, String email, String username, String documentIdNumber) throws ExcepcaoPersistencia;
    public IPessoa lerPessoaPorNumDocIdETipoDocId(String numeroDocumentoIdentificacao, TipoDocumentoIdentificacao tipoDocumentoIdentificacao) throws ExcepcaoPersistencia;
    public List lerTodasAsPessoas() throws ExcepcaoPersistencia;
}
