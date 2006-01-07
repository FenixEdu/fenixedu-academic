package net.sourceforge.fenixedu.applicationTier.Servico.publication;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoTeacher;
import net.sourceforge.fenixedu.dataTransferObject.SiteView;
import net.sourceforge.fenixedu.dataTransferObject.publication.InfoPublication;
import net.sourceforge.fenixedu.dataTransferObject.publication.InfoSitePublications;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.publication.Authorship;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentTeacher;
import net.sourceforge.fenixedu.persistenceTier.IPessoaPersistente;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class ReadAuthorshipsByUsername implements IService {

	public SiteView run(String user) throws FenixServiceException, ExcepcaoPersistencia {
		ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
		IPessoaPersistente persistentPerson = sp.getIPessoaPersistente();
		IPersistentTeacher persistentTeacher = sp.getIPersistentTeacher();

		Person person = persistentPerson.lerPessoaPorUsername(user);
		Teacher teacher = persistentTeacher.readTeacherByUsername(user);

		InfoTeacher infoTeacher = InfoTeacher.newInfoFromDomain(teacher);
		List<InfoPublication> infoPublications = new ArrayList<InfoPublication>(person
				.getPersonAuthorshipsCount());

		for (Authorship authorship : person.getPersonAuthorships()) {
			infoPublications.add(InfoPublication.newInfoFromDomain(authorship.getPublication()));
		}

		InfoSitePublications infoSitePublications = new InfoSitePublications();
		infoSitePublications.setInfoTeacher(infoTeacher);
		infoSitePublications.setInfoPublications(infoPublications);

		return new SiteView(infoSitePublications);
	}

}