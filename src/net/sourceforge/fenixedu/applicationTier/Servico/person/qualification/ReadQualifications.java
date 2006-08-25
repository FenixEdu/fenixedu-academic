package net.sourceforge.fenixedu.applicationTier.Servico.person.qualification;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.InfoPerson;
import net.sourceforge.fenixedu.dataTransferObject.person.InfoQualification;
import net.sourceforge.fenixedu.dataTransferObject.person.InfoQualificationWithPersonAndCountry;
import net.sourceforge.fenixedu.dataTransferObject.person.InfoSiteQualifications;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Qualification;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

public class ReadQualifications extends Service {

    public InfoSiteQualifications run(String user) throws ExcepcaoPersistencia {

	final Person person = Person.readPersonByUsername(user);

	final List<InfoQualification> infoQualifications = (List) CollectionUtils.collect(person
		.getAssociatedQualifications(), new Transformer() {
	    public Object transform(Object o) {
		Qualification qualification = (Qualification) o;
		return InfoQualificationWithPersonAndCountry.newInfoFromDomain(qualification);
	    }
	});
	Collections.sort(infoQualifications, new Comparator() {

	    public int compare(Object o1, Object o2) {
		InfoQualification infoQualification1 = (InfoQualification) o1;
		InfoQualification infoQualification2 = (InfoQualification) o2;
		if (infoQualification1.getDate() == null && infoQualification2.getDate() == null) {
		    return 0;
		} else if (infoQualification1.getDate() == null) {
		    return -1;
		} else if (infoQualification2.getDate() == null) {
		    return 1;
		} else {
		    if (infoQualification1.getDate().before(infoQualification2.getDate())) {
			return -1;
		    } else if (infoQualification1.getDate().after(infoQualification2.getDate())) {
			return 1;
		    } else if (infoQualification1.getDate().equals(infoQualification2.getDate())) {
			return 0;
		    }
		}
		return 0;
	    }
	});

	InfoSiteQualifications infoSiteQualifications = new InfoSiteQualifications();
	infoSiteQualifications.setInfoQualifications(infoQualifications);
	infoSiteQualifications.setInfoPerson(InfoPerson.newInfoFromDomain(person));

	return infoSiteQualifications;
    }

}
