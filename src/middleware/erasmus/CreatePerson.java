/*
 * Created on Apr 7, 2004
 */
package middleware.erasmus;

import java.util.Iterator;
import java.util.List;

import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.query.QueryByCriteria;

import pt.utl.ist.berserk.storage.exceptions.StorageException;
import Dominio.IPessoa;
import Dominio.Pessoa;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPessoaPersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author João Mota
 */
public class CreatePerson
{

    public static void main(String[] args)
    {
        try
        {
            SuportePersistenteOJB persistentSuport = SuportePersistenteOJB.getInstance();
            IPessoaPersistente persistentPerson = persistentSuport.getIPessoaPersistente();
            persistentSuport.iniciarTransaccao();
            PersistenceBroker broker = persistentSuport.currentBroker();
            List test = (List) broker.getCollectionByQuery(new QueryByCriteria(Dbo_aluno.class, null));
            Iterator iter = test.iterator();
            while (iter.hasNext())
            {
                Dbo_aluno erasmusStudent = (Dbo_aluno) iter.next();

                IPessoa newPerson = new Pessoa();
                // persistentPerson.simpleLockWrite(newPerson);

                Integer newNumber = generateNewStudentNumber(erasmusStudent);
                System.out.println(newNumber);
                //   newPerson.setUsername("L"+newNumber);
                //            newPerson.setTipoDocumentoIdentificacao()
                //            newPerson.setTelemovel()
                //            newPerson.setTelefone()
                //            newPerson.setSexo()
                //            newPerson.setPassword()
                //            newPerson.setProfissao()
                //            newPerson.setPais()
                //            newPerson.setNumeroDocumentoIdentificacao()
                //            newPerson.setNumContribuinte()
                //            newPerson.setNomePai()
                //            newPerson.setNomeMae()
                //            newPerson.setNome()
                //            newPerson.setNascimento()
                //            newPerson.setNacionalidade()
                //            newPerson.setMorada()
                //            newPerson.setLocalidade()
                //            newPerson.setLocalEmissaoDocumentoIdentificacao()
                //            newPerson.setFreguesiaNaturalidade()
                //            newPerson.setFreguesiaMorada()
                //            newPerson.setEstadoCivil()
                //            newPerson.setEnderecoWeb()
                //            newPerson.setEmail()
                //            newPerson.setDistritoNaturalidade()
                //            newPerson.setDistritoMorada()
                //            newPerson.setDataValidadeDocumentoIdentificacao()
                //            newPerson.setDataEmissaoDocumentoIdentificacao()
                //            newPerson.setConcelhoNaturalidade()
                //            newPerson.setConcelhoMorada()
                //            newPerson.setCodigoPostal()
                // newPerson.setCodigoFiscal()
            }

            persistentSuport.confirmarTransaccao();
            System.out.println("The End");

        }
        catch (ExcepcaoPersistencia e)
        {

            e.printStackTrace();
        }
        catch (StorageException e)
        {

            e.printStackTrace();
        }
    }

    /**
     * @param erasmusStudent
     * @return
     */
    private static Integer generateNewStudentNumber(Dbo_aluno erasmusStudent)
    {
        Integer temp = null;
        String number = erasmusStudent.getNumero();
        try
        {
            temp = new Integer(number);
        }
        catch (NumberFormatException e)
        {
            number = number.replaceAll("A", "");
            temp = new Integer(number);
        }
        return new Integer(100000 + temp.intValue());
    }
}