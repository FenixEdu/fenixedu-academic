/*
 * PessoaOJB.java
 * 
 * Created on 15 de Outubro de 2002, 15:16
 */

package ServidorPersistente.OJB;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.ListIterator;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.ojb.broker.query.Criteria;

import Dominio.IPessoa;
import Dominio.Pessoa;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPessoaPersistente;
import ServidorPersistente.exceptions.ExistingPersistentException;
import Util.TipoDocumentoIdentificacao;

public class PessoaOJB extends ObjectFenixOJB implements IPessoaPersistente
{

    public PessoaOJB()
    {
    }

    public void escreverPessoa(IPessoa personToWrite)
        throws
            ExcepcaoPersistencia,
            ExistingPersistentException,
            IllegalAccessException,
            InvocationTargetException
    {

        IPessoa personFromDB1 = null;
        IPessoa personFromDB2 = null;

        // If there is nothing to write, simply return.
        if (personToWrite == null)
            return;

        // Read person from database.
        personFromDB1 = this.lerPessoaPorUsername(personToWrite.getUsername());
        personFromDB2 =
            this.lerPessoaPorNumDocIdETipoDocId(
                personToWrite.getNumeroDocumentoIdentificacao(),
                personToWrite.getTipoDocumentoIdentificacao());

        // If person is not in database, then write it.
        if ((personFromDB1 == null) && (personFromDB2 == null))
        {

            super.lockWrite(personToWrite);
            return;

        }
        else if (
            (personFromDB1 != null)
                && (personFromDB2 != null)
                && (((Pessoa) personFromDB1)
                    .getIdInternal()
                    .equals(((Pessoa) personToWrite).getIdInternal()))
                && (((Pessoa) personFromDB2)
                    .getIdInternal()
                    .equals(((Pessoa) personToWrite).getIdInternal())))
        {

            super.lockWrite(personFromDB1);
            BeanUtils.copyProperties(personFromDB1, personToWrite);
            return;

            // else If the person is mapped to the database, then write any
            // existing changes.
        }
        else if (
            (personFromDB1 != null)
                && (personFromDB2 == null)
                && (personToWrite instanceof Pessoa)
                && (((Pessoa) personFromDB1)
                    .getIdInternal()
                    .equals(((Pessoa) personToWrite).getIdInternal())))
        {

            super.lockWrite(personFromDB1);
            BeanUtils.copyProperties(personFromDB1, personToWrite);

            return;

        }
        else if (
            (personFromDB2 != null)
                && (personFromDB1 == null)
                && (personToWrite instanceof Pessoa)
                && (((Pessoa) personFromDB2)
                    .getIdInternal()
                    .equals(((Pessoa) personToWrite).getIdInternal())))
        {
            super.lockWrite(personFromDB1);
            BeanUtils.copyProperties(personFromDB2, personToWrite);

            return;
        }
        // else Throw an already existing exception
        throw new ExistingPersistentException();
    }

    public void apagarPessoaPorNumDocIdETipoDocId(
        String numeroDocumentoIdentificacao,
        TipoDocumentoIdentificacao tipoDocumentoIdentificacao)
        throws ExcepcaoPersistencia
    {

        Criteria crit = new Criteria();
        crit.addEqualTo("numeroDocumentoIdentificacao", numeroDocumentoIdentificacao);
        crit.addEqualTo("tipoDocumentoIdentificacao", tipoDocumentoIdentificacao.getTipo());

        List result = queryList(Pessoa.class, crit);
        if (result != null)
        {

            ListIterator iterator = result.listIterator();
            while (iterator.hasNext())
            {

                super.delete(iterator.next());
            }
        }

    }

    public void apagarPessoa(IPessoa pessoa) throws ExcepcaoPersistencia
    {
        super.delete(pessoa);
    }

   

    public IPessoa lerPessoaPorUsername(String username) throws ExcepcaoPersistencia
    {
        IPessoa person = null;

        Criteria criteria = new Criteria();
        criteria.addEqualTo("username", username);
        person = (IPessoa) queryObject(Pessoa.class, criteria);
        return person;
    }

    public List findPersonByName(String name) throws ExcepcaoPersistencia
    {
        List personList = null;

        Criteria criteria = new Criteria();
        criteria.addLike("name", name);
        personList = queryList(Pessoa.class, criteria);
        return personList;
    }

    public List findPersonByNameAndEmailAndUsername(String name, String email, String username)
        throws ExcepcaoPersistencia
    {
        List personList = null;

        Criteria criteria = new Criteria();

        if (name != null && name.length() > 0)
        {
            criteria.addLike("nome", name);
        }

        if (email != null && email.length() > 0)
        {
            criteria.addLike("email", email);
        }

        if (username != null && username.length() > 0)
        {
            criteria.addLike("username", username);
        }

        personList = queryList(Pessoa.class, criteria);
        return personList;
    }

    public IPessoa lerPessoaPorNumDocIdETipoDocId(
        String numeroDocumentoIdentificacao,
        TipoDocumentoIdentificacao tipoDocumentoIdentificacao)
        throws ExcepcaoPersistencia
    {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("numeroDocumentoIdentificacao", numeroDocumentoIdentificacao);
        criteria.addEqualTo("tipoDocumentoIdentificacao", tipoDocumentoIdentificacao.getTipo());
        return (IPessoa) queryObject(Pessoa.class, criteria);
    }

    public List lerTodasAsPessoas() throws ExcepcaoPersistencia
    {
        return queryList(Pessoa.class,new Criteria());
    }

    public void alterarPessoa(String numDocId, TipoDocumentoIdentificacao tipoDocId, IPessoa pessoa)
        throws ExcepcaoPersistencia
    {
    }
}
