/*
 * ChangeMasterDegreeCandidate.java
 *
 * O Servico ChangeMasterDegreeCandidate altera a informacao de um candidato de Mestrado
 * Nota : E suposto os campos (numeroCandidato, anoCandidatura, chaveCursoMestrado, username)
 *        nao se puderem alterar
 *
 * Created on 02 de Dezembro de 2002, 16:25
 */

/**
 *
 * Autores :
 *   - Nuno Nunes (nmsn@rnl.ist.utl.pt)
 *   - Joana Mota (jccm@rnl.ist.utl.pt)
 *
 */

package ServidorAplicacao.Servico.person;

import DataBeans.InfoPerson;
import Dominio.IPessoa;
import ServidorAplicacao.FenixServiceException;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.ExcepcaoInexistente;
import ServidorAplicacao.Servico.UserView;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

public class ChangePersonalInfo implements IServico {
    
    private static ChangePersonalInfo servico = new ChangePersonalInfo();
    
    /**
     * The singleton access method of this class.
     **/
    public static ChangePersonalInfo getService() {
        return servico;
    }
    
    /**
     * The actor of this class.
     **/
    private ChangePersonalInfo() { 
    }
    
    /**
     * Returns the service name
     **/
    
    public final String getNome() {
        return "ChangePersonalInfo";
    }
    
    
    public UserView run(InfoPerson newInfoPerson, UserView userView) 
	    throws ExcepcaoInexistente, FenixServiceException {

        ISuportePersistente sp = null;
        IPessoa person = null;

        try {
	        sp = SuportePersistenteOJB.getInstance();
			person = sp.getIPessoaPersistente().lerPessoaPorUsername(userView.getUtilizador());
        } catch (ExcepcaoPersistencia ex) {
            FenixServiceException newEx = new FenixServiceException("Persistence layer error");
            newEx.fillInStackTrace();
            throw newEx; 
        } 
		if (person == null)
			throw new ExcepcaoInexistente("Unknown Person !!");	


		// FIXME: At this time we only can change the Email, Mobile Phone and the Web Site


		// Get new Country
//		ICountry nationality = null;
//		if (person.getPais() == null)
//			person.setPais(null);
//		else
//			try {
//				if (!newInfoPerson.getInfoPais().getNationality().equals(person.getPais().getNationality())) {
//					nationality = sp.getIPersistentCountry().readCountryByNationality(newInfoPerson.getInfoPais().getNationality());
//					person.setPais(nationality);
//				}
//			} catch (ExcepcaoPersistencia ex) {
//			  FenixServiceException newEx = new FenixServiceException("Persistence layer error");
//			  newEx.fillInStackTrace();
//			  throw newEx;
//			}
//
//		if (!newInfoPerson.getUsername().equals(userView.getUtilizador())){
//			userView.setUtilizador(newInfoPerson.getUsername());
//			person.setUsername(newInfoPerson.getUsername());
//		}
		
		// Change personal Information
//		person.setNascimento(newInfoPerson.getNascimento());
//		person.setDataEmissaoDocumentoIdentificacao(newInfoPerson.getDataEmissaoDocumentoIdentificacao());		
//		person.setDataValidadeDocumentoIdentificacao(newInfoPerson.getDataValidadeDocumentoIdentificacao());
//		person.setTipoDocumentoIdentificacao(newInfoPerson.getTipoDocumentoIdentificacao());
//		person.setNumeroDocumentoIdentificacao(newInfoPerson.getNumeroDocumentoIdentificacao());			
//		person.setLocalEmissaoDocumentoIdentificacao(newInfoPerson.getLocalEmissaoDocumentoIdentificacao());
//		person.setNome(newInfoPerson.getNome());
//		person.setSexo(newInfoPerson.getSexo());
//		person.setEstadoCivil(newInfoPerson.getEstadoCivil());
//		person.setNomePai(newInfoPerson.getNomePai());
//		person.setNomeMae(newInfoPerson.getNomeMae());
//		person.setFreguesiaNaturalidade(newInfoPerson.getFreguesiaNaturalidade());
//		person.setConcelhoNaturalidade(newInfoPerson.getConcelhoNaturalidade());
//		person.setDistritoNaturalidade(newInfoPerson.getDistritoNaturalidade());
//		person.setLocalidadeCodigoPostal(newInfoPerson.getLocalidadeCodigoPostal());
//		person.setMorada(newInfoPerson.getMorada());						
//		person.setLocalidade(newInfoPerson.getLocalidade());
//		person.setCodigoPostal(newInfoPerson.getCodigoPostal());
//		person.setFreguesiaMorada(newInfoPerson.getFreguesiaMorada());
//		person.setConcelhoMorada(newInfoPerson.getConcelhoMorada());
//		person.setDistritoMorada(newInfoPerson.getDistritoMorada());
//		person.setTelefone(newInfoPerson.getTelefone());
		person.setTelemovel(newInfoPerson.getTelemovel());
		person.setEmail(newInfoPerson.getEmail());
		person.setEnderecoWeb(newInfoPerson.getEnderecoWeb());
//		person.setNumContribuinte(newInfoPerson.getNumContribuinte());
//		person.setProfissao(newInfoPerson.getProfissao());
//		person.setNacionalidade(newInfoPerson.getNacionalidade());
		try {
            sp.getIPessoaPersistente().escreverPessoa(person);
	    } catch (ExcepcaoPersistencia ex) {
	      FenixServiceException newEx = new FenixServiceException("Persistence layer error");
	      newEx.fillInStackTrace();
	      throw newEx;
	    }
	    
	    return userView;
    }
}