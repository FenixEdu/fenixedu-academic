/*
 * PessoaOJB.java
 *
 * Created on 15 de Outubro de 2002, 15:16
 */

package ServidorPersistente.OJB;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import org.odmg.QueryException;

import Dominio.IPessoa;
import Dominio.Pessoa;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPessoaPersistente;
import Util.TipoDocumentoIdentificacao;

public class PessoaOJB extends ObjectFenixOJB implements IPessoaPersistente {
    
    public PessoaOJB() {}
    
    public void escreverPessoa(IPessoa pessoa) throws ExcepcaoPersistencia {
        super.lockWrite(pessoa);
    }
    
    public void apagarPessoaPorNumDocIdETipoDocId(String numeroDocumentoIdentificacao, TipoDocumentoIdentificacao tipoDocumentoIdentificacao) throws ExcepcaoPersistencia {
        try {
            String oqlQuery = "select all from " + Pessoa.class.getName();
            oqlQuery += " where numeroDocumentoIdentificacao = $1 and tipoDocumentoIdentificacao = $2";
            query.create(oqlQuery);
            query.bind(numeroDocumentoIdentificacao);
            query.bind(tipoDocumentoIdentificacao.getTipo());
            List result = (List) query.execute();
            ListIterator iterator = result.listIterator();
            while(iterator.hasNext())
                super.delete(iterator.next());
        } catch (QueryException ex) {
            throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
        }
    }

    public void apagarPessoa(IPessoa pessoa) throws ExcepcaoPersistencia {
        super.delete(pessoa);
    }
    
    public void apagarTodasAsPessoas() throws ExcepcaoPersistencia {
        String oqlQuery = "select all from " + Pessoa.class.getName();
        super.deleteAll(oqlQuery);
    }

    public IPessoa lerPessoaPorUsername(String username) throws ExcepcaoPersistencia {
        try {
            IPessoa pessoa = null;
            String oqlQuery = "select all from " + Pessoa.class.getName();
            oqlQuery += " where username = $1";
            query.create(oqlQuery);
            query.bind(username);
            List result = (List) query.execute();
            super.lockRead(result);
            if(result.size() != 0)
                pessoa = (IPessoa) result.get(0);
            return pessoa;
        } catch(QueryException ex) {
            throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
        }
    }

    public IPessoa lerPessoaPorNumDocIdETipoDocId(String numeroDocumentoIdentificacao, TipoDocumentoIdentificacao tipoDocumentoIdentificacao) throws ExcepcaoPersistencia {
        try {
            IPessoa p = null;
            String oqlQuery = "select all from " + Pessoa.class.getName();
            oqlQuery += " where numeroDocumentoIdentificacao = $1 and tipoDocumentoIdentificacao = $2";
            query.create(oqlQuery);
            query.bind(numeroDocumentoIdentificacao);
            query.bind(tipoDocumentoIdentificacao.getTipo());
            List result = (List) query.execute();
            super.lockRead(result);
            if(result.size() != 0)
                p = (IPessoa) result.get(0);
            return p;
        } catch(QueryException ex) {
            throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
        }
    }

    public ArrayList lerTodasAsPessoas() throws ExcepcaoPersistencia {
        try {
            ArrayList listap = new ArrayList();
            String oqlQuery = "select all from " + Pessoa.class.getName();
            query.create(oqlQuery);
            List result = (List) query.execute();
            super.lockRead(result);
            if(result.size() != 0) {
                ListIterator iterator = result.listIterator();
                while(iterator.hasNext())
                    listap.add((IPessoa)iterator.next());
            }
            return listap;
        } catch(QueryException ex) {
            throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
        }
    }       
    
    public void alterarPessoa(String numDocId, TipoDocumentoIdentificacao tipoDocId, IPessoa pessoa) throws ExcepcaoPersistencia {
    }
    
}
