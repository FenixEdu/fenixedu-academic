package org.fenixedu.academic.task;

import org.fenixedu.academic.domain.Locality;
import org.fenixedu.academic.domain.organizationalStructure.Unit;
import org.fenixedu.bennu.core.util.CoreConfiguration;
import org.fenixedu.bennu.scheduler.custom.CustomTask;
import org.fenixedu.commons.i18n.LocalizedString;
import org.fenixedu.spaces.domain.Information;
import org.fenixedu.spaces.domain.Space;
import org.fenixedu.spaces.domain.SpaceClassification;

import pt.ist.fenixframework.FenixFramework;

public class CreateCampusLocality extends CustomTask {

	@Override
	public void runTask() throws Exception {
		Unit administrativeOfficeUnit = FenixFramework.getDomainObject("285241663029249");
		
		if (administrativeOfficeUnit.getCampus() == null) {
			LocalizedString.Builder campusNameBuilder = new LocalizedString.Builder();
	        CoreConfiguration.supportedLocales().stream().forEach(l -> campusNameBuilder.with(l, "Campus"));
	        SpaceClassification classification = new SpaceClassification("1", campusNameBuilder.build());
	        
	        Information spaceInformation = new Information.Builder().name("Academy").build();
			Space campus = new Space(spaceInformation);
			Locality locality = new Locality();
			locality.setName("Vulcan");
			campus.setLocality(locality);
	        
			administrativeOfficeUnit.setCampus(campus);
		}		
	}

}
