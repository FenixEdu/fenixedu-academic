/*
 * Created on 10/11/2003
 *  
 */

package ServidorAplicacao.Servico.grant.owner;

import java.util.ArrayList;
import java.util.List;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoPersonWithInfoCountry;
import DataBeans.grant.owner.InfoGrantOwner;
import DataBeans.grant.owner.InfoGrantOwnerWithPerson;
import Dominio.IPessoa;
import Dominio.grant.owner.IGrantOwner;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.grant.utils.SessionConstants;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPessoaPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.grant.IPersistentGrantOwner;
import Util.TipoDocumentoIdentificacao;

/**
 * @author Barbosa
 * @author Pica
 *  
 */
public class SearchGrantOwner implements IService {

    public SearchGrantOwner() {
    }

    public List run(String name, String IdNumber, Integer IdType, Integer grantOwnerNumber,
            Boolean onlyGrantOwner, Integer startIndex) throws FenixServiceException {
        ISuportePersistente persistentSupport = null;
        IPessoaPersistente persistentPerson = null;
        IPersistentGrantOwner persistentGrantOwner = null;
        TipoDocumentoIdentificacao type = null;
        List grantOwnerList = new ArrayList();
        List personList = new ArrayList();
        List infoGrantOwnerList = new ArrayList();
        IGrantOwner grantOwner = null;
        IPessoa person = null;
        try {
            persistentSupport = SuportePersistenteOJB.getInstance();
            persistentPerson = persistentSupport.getIPessoaPersistente();
            persistentGrantOwner = persistentSupport.getIPersistentGrantOwner();

            // Search by Grant Owner Number
            if (grantOwnerNumber != null) {
                grantOwner = persistentGrantOwner.readGrantOwnerByNumber(grantOwnerNumber);
                if (grantOwner != null) {
                    InfoGrantOwner newInfoGrantOwner = InfoGrantOwnerWithPerson
                            .newInfoFromDomain(grantOwner);
                    infoGrantOwnerList.add(newInfoGrantOwner);
                    return infoGrantOwnerList;
                }
            }
            // Search by ID number and ID type
            if ((IdNumber != null) && (IdType != null)) {
                type = new TipoDocumentoIdentificacao(IdType);
                if (onlyGrantOwner.booleanValue())
                    grantOwner = persistentGrantOwner.readGrantOwnerByPersonID(IdNumber, type);
                else
                    person = persistentPerson.lerPessoaPorNumDocIdETipoDocId(IdNumber, type);
            }
            Integer numberOfResultsByName = null;
            //Search by name IF search by ID has failed
            if (person == null && grantOwner == null) {
                if (name != null) {
                    Integer tempNumberOfElementInSpan = new Integer(SessionConstants.NUMBER_OF_ELEMENTS_IN_SPAN.intValue() - 1); 
                    if (onlyGrantOwner.booleanValue()) {
                        grantOwnerList = persistentGrantOwner.readGrantOwnerByPersonName(name,startIndex, tempNumberOfElementInSpan);
                        numberOfResultsByName = persistentGrantOwner.countAllGrantOwnerByName(name);
                    } else {
                        personList = persistentPerson.findPersonByName(name,startIndex,tempNumberOfElementInSpan);
                        numberOfResultsByName = persistentPerson.countAllPersonByName(name);
                    }
                }
            } else if (grantOwner != null)
                grantOwnerList.add(grantOwner);
            else
                personList.add(person);
            if ((personList.size() > 0) && !onlyGrantOwner.booleanValue()) {
                //Get all the grantOwners associated with each person in list
                for (int i = 0; i < personList.size(); i++) {
                    InfoGrantOwner infoGrantOwner = new InfoGrantOwner();
                    IPessoa newPerson = (IPessoa) personList.get(i);
                    grantOwner = persistentGrantOwner.readGrantOwnerByPerson(newPerson.getIdInternal());
                    if (grantOwner != null)
                        //The person is a GrantOwner
                        infoGrantOwner = InfoGrantOwnerWithPerson.newInfoFromDomain(grantOwner);
                    else {
                        //The person is NOT a GrantOwner
                        infoGrantOwner.setPersonInfo(InfoPersonWithInfoCountry.newInfoFromDomain(person));
                    }
                    infoGrantOwnerList.add(infoGrantOwner);
                }
            } else if ((grantOwnerList.size() > 0) && onlyGrantOwner.booleanValue()) {
                for (int i = 0; i < grantOwnerList.size(); i++) {
                    InfoGrantOwner infoGrantOwner = InfoGrantOwnerWithPerson
                            .newInfoFromDomain((IGrantOwner) grantOwnerList.get(i));
                    infoGrantOwnerList.add(infoGrantOwner);
                }
            }

            if (numberOfResultsByName != null) {
                /**
                 * Set an arraylist with:
                 * 	position 0: result size
                 *  position 1: start index used
                 *  position 2: list with results
                 */
                List result = new ArrayList();
                result.add(0, numberOfResultsByName);
                result.add(1, startIndex);
                result.add(2, infoGrantOwnerList);
                return result;
            }
            return infoGrantOwnerList;

        } catch (ExcepcaoPersistencia excepcaoPersistencia) {
            throw new FenixServiceException(excepcaoPersistencia.getMessage());
        }
    }
}