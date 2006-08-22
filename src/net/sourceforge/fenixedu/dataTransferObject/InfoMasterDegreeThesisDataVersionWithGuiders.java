package net.sourceforge.fenixedu.dataTransferObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import net.sourceforge.fenixedu.domain.ExternalPerson;
import net.sourceforge.fenixedu.domain.GuiderType;
import net.sourceforge.fenixedu.domain.MasterDegreeThesisDataVersion;
import net.sourceforge.fenixedu.domain.Teacher;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

/**
 * @author Fernanda Quitério Created on 6/Set/2004
 * 
 */
public class InfoMasterDegreeThesisDataVersionWithGuiders extends InfoMasterDegreeThesisDataVersion {

    private List<GuiderDTO> allGuiders;

    public List getAllGuiders() {

        Properties properties = new Properties();

        InputStream inputStream = getClass().getResourceAsStream(
                "/resources/GlobalResources.properties");
        try {
            properties.load(inputStream);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        String institution = properties.getProperty("institution.name.abbreviation");

        this.allGuiders = new ArrayList<GuiderDTO>();

        for (InfoTeacher guider : (List<InfoTeacher>) this.getInfoGuiders()) {
            this.allGuiders.add(new GuiderDTO(GuiderType.GUIDER, guider.getInfoPerson().getNome(),
                    guider.getTeacherNumber(), institution));
        }

        for (InfoExternalPerson guider : (List<InfoExternalPerson>) this.getInfoExternalGuiders()) {
            this.allGuiders.add(new GuiderDTO(GuiderType.EXTERNAL_GUIDER, guider.getInfoPerson()
                    .getNome(), null, guider.getInfoInstitution().getName()));
        }

        for (InfoTeacher assistentGuider : (List<InfoTeacher>) this.getInfoAssistentGuiders()) {
            this.allGuiders.add(new GuiderDTO(GuiderType.ASSISTENT, assistentGuider.getInfoPerson()
                    .getNome(), assistentGuider.getTeacherNumber(), institution));
        }

        for (InfoExternalPerson assistentGuider : (List<InfoExternalPerson>) this
                .getInfoExternalAssistentGuiders()) {
            this.allGuiders.add(new GuiderDTO(GuiderType.EXTERNAL_ASSISTENT, assistentGuider
                    .getInfoPerson().getNome(), null, assistentGuider.getInfoInstitution().getName()));
        }

        return this.allGuiders;
    }

    public void copyFromDomain(MasterDegreeThesisDataVersion masterDegreeThesisDataVersion) {
        super.copyFromDomain(masterDegreeThesisDataVersion);
        if (masterDegreeThesisDataVersion != null) {
            setInfoAssistentGuiders(copyTeachers(masterDegreeThesisDataVersion.getAssistentGuiders()));

            setInfoExternalAssistentGuiders(copyExternalPersons(masterDegreeThesisDataVersion
                    .getExternalAssistentGuiders()));

            setInfoExternalGuiders(copyExternalPersons(masterDegreeThesisDataVersion
                    .getExternalGuiders()));

            setInfoGuiders(copyTeachers(masterDegreeThesisDataVersion.getGuiders()));
        }
    }

    /**
     * @param externalPersons
     * @return
     */
    private List copyExternalPersons(List externalPersons) {
        
        List<InfoExternalPerson> infoExternalPersons = new ArrayList<InfoExternalPerson>(externalPersons.size());
        for (ExternalPerson externalPerson : (List<ExternalPerson>)externalPersons) {
            infoExternalPersons.add(InfoExternalPersonWithPersonAndWLocation.newInfoFromDomain(externalPerson));            
        }
        return infoExternalPersons;
        
    }

    /**
     * @param masterDegreeThesisDataVersion
     * @return
     */
    private List copyTeachers(List teachers) {
        return (List) CollectionUtils.collect(teachers, new Transformer() {
            public Object transform(Object arg0) {
                Teacher teacher = (Teacher) arg0;
                return InfoTeacher.newInfoFromDomain(teacher);
            }
        });
    }

    public static InfoMasterDegreeThesisDataVersion newInfoFromDomain(
            MasterDegreeThesisDataVersion masterDegreeThesisDataVersion) {
        InfoMasterDegreeThesisDataVersionWithGuiders infoMasterDegreeThesisDataVersionWithGuiders = null;
        if (masterDegreeThesisDataVersion != null) {
            infoMasterDegreeThesisDataVersionWithGuiders = new InfoMasterDegreeThesisDataVersionWithGuiders();
            infoMasterDegreeThesisDataVersionWithGuiders.copyFromDomain(masterDegreeThesisDataVersion);
        }
        return infoMasterDegreeThesisDataVersionWithGuiders;
    }
}