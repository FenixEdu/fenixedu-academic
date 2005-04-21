/*
 * Created on Oct 13, 2003
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package net.sourceforge.fenixedu.persistenceTier.OJB;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.ExternalPerson;
import net.sourceforge.fenixedu.domain.IExternalPerson;
import net.sourceforge.fenixedu.domain.person.IDDocumentType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExternalPerson;

import org.apache.ojb.broker.query.Criteria;

/**
 * @author - Shezad Anavarali (sana@mega.ist.utl.pt) - Nadir Tarmahomed
 *         (naat@mega.ist.utl.pt)
 *  
 */
public class ExternalPersonOJB extends PersistentObjectOJB implements IPersistentExternalPerson {

    /** Creates a new instance of ExternalPersonOJB */
    public ExternalPersonOJB() {
    }

    public IExternalPerson readByUsername(String username) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        IExternalPerson externalPerson = null;

        criteria.addEqualTo("person.username", username);
        externalPerson = (IExternalPerson) queryObject(ExternalPerson.class, criteria);

        return externalPerson;

    }

    public List readByName(String name) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        List externalPersons = new ArrayList();

        criteria.addLike("person.name", "%" + name + "%");
        externalPersons = queryList(ExternalPerson.class, criteria);

        return externalPersons;

    }

    public IExternalPerson readByNameAndAddressAndWorkLocationID(String name, String address,
            Integer workLocationID) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        IExternalPerson externalPerson = null;

        criteria.addEqualTo("person.nome", name);
        criteria.addEqualTo("person.address", address);
        criteria.addEqualTo("workLocation.idInternal", workLocationID);
        externalPerson = (IExternalPerson) queryObject(ExternalPerson.class, criteria);

        return externalPerson;
    }

    public List readByWorkLocation(Integer workLocationID) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        List externalPersons = new ArrayList();

        if (workLocationID != null)
            criteria.addEqualTo("workLocation.idInternal", workLocationID);

        externalPersons = queryList(ExternalPerson.class, criteria);

        return externalPersons;

    }

    public String readLastDocumentIdNumber() throws ExcepcaoPersistencia {
        IExternalPerson externalPerson = null;

        Criteria criteria = new Criteria();
        criteria.addEqualTo("person.idDocumentType", IDDocumentType.EXTERNAL);

        externalPerson = (IExternalPerson) queryList(ExternalPerson.class, criteria,
                "person.numeroDocumentoIdentificacao", false).get(0);
        String lastIdStr = null;
        if (externalPerson == null) {
            lastIdStr = "0";
        } else {
            lastIdStr = externalPerson.getPerson().getNumeroDocumentoIdentificacao();
        }

        return lastIdStr;
    }

}