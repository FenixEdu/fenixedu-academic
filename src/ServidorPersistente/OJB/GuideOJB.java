package ServidorPersistente.OJB;

import java.util.List;

import org.apache.ojb.broker.query.Criteria;
import org.apache.ojb.broker.query.QueryByCriteria;

import Dominio.Contributor;
import Dominio.Guide;
import Dominio.IContributor;
import Dominio.IGuide;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentGuide;
import ServidorPersistente.exceptions.ExistingPersistentException;
import Util.SituationOfGuide;
import Util.State;
import Util.TipoDocumentoIdentificacao;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class GuideOJB extends ObjectFenixOJB implements IPersistentGuide
{

    public GuideOJB()
    {
    }

    public void write(IGuide guideToWrite) throws ExcepcaoPersistencia, ExistingPersistentException
    {
        IGuide guideBD = new Guide();
        IContributor contributor = new Contributor();
        contributor = guideToWrite.getContributor();
        if (guideToWrite == null)
            // Should we throw an exception saying nothing to write or
            // something of the sort?
            // By default, if OJB received a null object it would complain.
            return;

        // read Guide

        guideBD =
            this.readByNumberAndYearAndVersion(
                guideToWrite.getNumber(),
                guideToWrite.getYear(),
                guideToWrite.getVersion());

        // if (guide not in database) then write it
        if (guideBD == null)
            super.lockWrite(guideToWrite);
        // else if (guide is mapped to the database then write any existing
        // changes)
        else if (
            (guideToWrite != null)
                && ((Guide) guideBD).getIdInternal().equals(((Guide) guideToWrite).getIdInternal()))
        {

            guideBD.setTotal(guideToWrite.getTotal());
            guideBD.setRemarks(guideToWrite.getRemarks());
            guideBD.setContributor(contributor);

            //			guideBD.setCreationDate(guideToWrite.getCreationDate());
            //			guideBD.setVersion(guideToWrite.getVersion());

            super.lockWrite(guideBD);
            // else throw an AlreadyExists exception.
        }
        else
            throw new ExistingPersistentException();
    }

    public Integer generateGuideNumber(Integer year) throws ExcepcaoPersistencia
    {

        Integer guideNumber = new Integer(0);
        Criteria crit = new Criteria();
        crit.addEqualTo("year", year);
        crit.addOrderBy("number", false);

        List result = queryList(Guide.class, crit);

        if (result != null && result.size() != 0)
        {

            guideNumber = ((IGuide) result.get(0)).getNumber();
        }
        return new Integer(guideNumber.intValue() + 1);

    }

    public IGuide readByNumberAndYearAndVersion(Integer number, Integer year, Integer version)
        throws ExcepcaoPersistencia
    {
        Criteria crit = new Criteria();
        crit.addEqualTo("number",number);
        crit.addEqualTo("year",year);
        crit.addEqualTo("version",version);
        return (IGuide) queryObject(Guide.class,crit);
       
    }

    /**
	 * IMPORTANT: If the ordering is changed here you must change the private
	 * method getLatestVersions in the ChooseGuide Service
	 */
    public List readByYear(Integer year) throws ExcepcaoPersistencia
    {
        Criteria criteria = new Criteria();
        //QueryByCriteria query = new QueryByCriteria(Guide.class, criteria);

        //	criteria.addOrderByAscending("number");
        //	criteria.addOrderByDescending("version");

        criteria.addEqualTo("year", year);

        List result = queryList(Guide.class, criteria);
        //(List)
        // PersistenceBrokerFactory.defaultPersistenceBroker().getCollectionByQuery(query);

        if ((result == null) || (result.size() == 0))
        {
            return null;
        }

        lockRead(result);
        return result;
    }

    public IGuide readLatestVersion(Integer year, Integer number) throws ExcepcaoPersistencia
    {
        Criteria criteria = new Criteria();
        QueryByCriteria query = new QueryByCriteria(Guide.class, criteria);
        query.addOrderByDescending("version");

        criteria.addEqualTo("year", year);
        criteria.addEqualTo("number", number);

        List result = queryList(Guide.class, criteria);

        if (result.size() != 0)
        {
            return (IGuide) result.get(0);
        }

        return null;
    }

    public List readByPerson(
        String identificationDocumentNumber,
        TipoDocumentoIdentificacao identificationDocumentType)
        throws ExcepcaoPersistencia
    {

        Criteria criteria = new Criteria();
        //QueryByCriteria query = new QueryByCriteria(Guide.class, criteria);

        //		query.addGroupBy("number");

        criteria.addEqualTo("person.numeroDocumentoIdentificacao", identificationDocumentNumber);
        criteria.addEqualTo("person.tipoDocumentoIdentificacao", identificationDocumentType.getTipo());

        List result = queryList(Guide.class, criteria);

        return result;
    }

    public List readByYearAndState(Integer guideYear, SituationOfGuide situationOfGuide)
        throws ExcepcaoPersistencia
    {
        Criteria criteria = new Criteria();

        if (guideYear != null)
        {
            criteria.addEqualTo("year", guideYear);
        }

        if (situationOfGuide != null)
        {
            criteria.addEqualTo("guideSituations.state", new State(State.ACTIVE));
            criteria.addEqualTo("guideSituations.situation", situationOfGuide);
        }

        return queryList(Guide.class, criteria);
    }

    //	public static void main(String args[]) throws ExcepcaoPersistencia{
    //		
    //		SuportePersistenteOJB sp = SuportePersistenteOJB.getInstance();
    //		sp.iniciarTransaccao();
    //		System.out.println(((IGuide)
    // sp.getIPersistentGuide().readLatestVersion(new Integer(2003), new
    // Integer(199))).getVersion());
    //		sp.getIPersistentGuide().readByYear(new Integer(2003));
    //		
    //		sp.confirmarTransaccao();
    //		
    //	}

    public List readByNumberAndYear(Integer number, Integer year) throws ExcepcaoPersistencia
    {
        Criteria criteria = new Criteria();

        criteria.addEqualTo("number", number);
        criteria.addEqualTo("year", year);
        criteria.addOrderBy("version", true);

        List result = queryList(Guide.class, criteria);

        return result;
    }
}