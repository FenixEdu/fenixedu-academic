/*
 * Created on 28/Mai/2003
 * 
 * To change the template for this generated file go to Window>Preferences>Java>Code Generation>Code and
 * Comments
 */
package middleware.posgrad;

import java.util.Iterator;
import java.util.List;

import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.PersistenceBrokerFactory;
import org.apache.ojb.broker.query.Criteria;
import org.apache.ojb.broker.query.Query;
import org.apache.ojb.broker.query.QueryByCriteria;

import Dominio.IPessoa;
import Dominio.IQualification;
import Dominio.Pessoa;
import Dominio.Qualification;
import Util.TipoDocumentoIdentificacao;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class MigrateHabilitcoes2Qualifications
{

    PersistenceBroker broker = null;

    public MigrateHabilitcoes2Qualifications()
    {
        broker = PersistenceBrokerFactory.defaultPersistenceBroker();
    }

    public static void main(String args[]) throws Exception
    {
        MigrateHabilitcoes2Qualifications migrateHabilitcoes2Qualifications =
            new MigrateHabilitcoes2Qualifications();
        migrateHabilitcoes2Qualifications.migrateHabilitacoes2Qualifications();
    }

    private void migrateHabilitacoes2Qualifications() throws Exception
    {
        Posgrad_candidato_mestrado candidato = null;

        int qualificationsWritten = 0;

        List result = null;
        Query query = null;
        Criteria criteria = null;
        try
        {
            System.out.print("Reading PosGrad Candidatos ...");

            broker.beginTransaction();
            broker.clearCache();

            List areasCientificasPG = getCandidatosMestrado();
            System.out.println("  Done !");

            broker.commitTransaction();

            System.out.println(
                "Migrating "
                    + areasCientificasPG.size()
                    + " PosGrad Qualificacoes to Fenix Qualifications ...");
            Iterator iterator = areasCientificasPG.iterator();
            while (iterator.hasNext())
            {
                candidato = (Posgrad_candidato_mestrado) iterator.next();

                broker.beginTransaction();
                broker.clearCache();
                TipoDocumentoIdentificacao identificationDocumentType = null;
                if (candidato.getTipodocumentoidentificacao().equalsIgnoreCase("BILHETE DE IDENTIDADE"))
                {
                    identificationDocumentType =
                        new TipoDocumentoIdentificacao(TipoDocumentoIdentificacao.BILHETE_DE_IDENTIDADE);
                } else if (candidato.getTipodocumentoidentificacao().equalsIgnoreCase("PASSAPORTE"))
                {
                    identificationDocumentType =
                        new TipoDocumentoIdentificacao(TipoDocumentoIdentificacao.PASSAPORTE);
                } else
                    identificationDocumentType =
                        new TipoDocumentoIdentificacao(TipoDocumentoIdentificacao.OUTRO);

                // Read Fenix Person
                criteria = new Criteria();
                criteria.addEqualTo(
                    "numeroDocumentoIdentificacao",
                    candidato.getNumerodocumentoidentificacao());
                criteria.addEqualTo("tipoDocumentoIdentificacao", identificationDocumentType);
                query = new QueryByCriteria(Pessoa.class, criteria);
                result = (List) broker.getCollectionByQuery(query);

                if (result.size() != 1)
                {
                    System.out.println(
                        "Candidato " + candidato.getCodigointerno() + " nao foi aprovado");
                    broker.commitTransaction();
                    continue;
                }

                if (checkNull(candidato))
                {
                    broker.commitTransaction();
                    continue;
                }

                if (candidato.getLicenciatura().equals("APRECIAÇÃO CURRICULAR"))
                {
                    broker.commitTransaction();
                    continue;
                }

                IPessoa person = (IPessoa) result.get(0);

                IQualification qualification = new Qualification();

                qualification.setMark(String.valueOf(candidato.getMedia()));
                qualification.setPerson(person);
                qualification.setSchool(candidato.getEscolalicenciatura());
                qualification.setTitle(candidato.getLicenciatura());
//                qualification.setYear(new Integer(String.valueOf(candidato.getAnolicenciatura())));

                try
                {
                    broker.store(qualification);
                } catch (Exception e)
                {
                    throw new Exception(e);
                }
                qualificationsWritten++;
                broker.commitTransaction();
            }
            System.out.println("  Done !");
            System.out.println("  Qualifications Written " + qualificationsWritten);

        } catch (Exception e)
        {
            System.out.println();
            throw new Exception(
                "Error Migrating Qualificacao do Candidato " + candidato.getCodigointerno() + e);
        }
    }

    /**
	 * @param candidato
	 */
    private boolean checkNull(Posgrad_candidato_mestrado candidato)
    {
        if (((candidato.getEscolalicenciatura() == null)
            || (candidato.getEscolalicenciatura().length() == 0))
            && ((candidato.getLicenciatura() == null) || (candidato.getLicenciatura().length() == 0))
            && (candidato.getAnolicenciatura() == 0)
            && ((candidato.getMedia() == null) || (candidato.getMedia().equals(new Double(0)))))
        {
            return true;
        }
        return false;
    }

    private List getCandidatosMestrado() throws Exception
    {
        Criteria criteria = new Criteria();
        QueryByCriteria query = new QueryByCriteria(Posgrad_candidato_mestrado.class, criteria);
        return (List) broker.getCollectionByQuery(query);
    }
}
