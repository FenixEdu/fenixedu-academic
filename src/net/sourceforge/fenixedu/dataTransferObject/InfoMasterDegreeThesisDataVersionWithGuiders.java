package net.sourceforge.fenixedu.dataTransferObject;

import java.util.List;

import net.sourceforge.fenixedu.domain.IExternalPerson;
import net.sourceforge.fenixedu.domain.IMasterDegreeThesisDataVersion;
import net.sourceforge.fenixedu.domain.ITeacher;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

/**
 * @author Fernanda Quitério Created on 6/Set/2004
 *  
 */
public class InfoMasterDegreeThesisDataVersionWithGuiders extends InfoMasterDegreeThesisDataVersion {

    public void copyFromDomain(IMasterDegreeThesisDataVersion masterDegreeThesisDataVersion) {
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
        return (List) CollectionUtils.collect(externalPersons, new Transformer() {
            public Object transform(Object arg0) {
                IExternalPerson externalPerson = (IExternalPerson) arg0;
                return InfoExternalPersonWithPersonAndWLocation.newInfoFromDomain(externalPerson);
            }
        });
    }

    /**
     * @param masterDegreeThesisDataVersion
     * @return
     */
    private List copyTeachers(List teachers) {
        return (List) CollectionUtils.collect(teachers, new Transformer() {
            public Object transform(Object arg0) {
                ITeacher teacher = (ITeacher) arg0;
                return InfoTeacherWithPerson.newInfoFromDomain(teacher);
            }
        });
    }

    public static InfoMasterDegreeThesisDataVersion newInfoFromDomain(
            IMasterDegreeThesisDataVersion masterDegreeThesisDataVersion) {
        InfoMasterDegreeThesisDataVersionWithGuiders infoMasterDegreeThesisDataVersionWithGuiders = null;
        if (masterDegreeThesisDataVersion != null) {
            infoMasterDegreeThesisDataVersionWithGuiders = new InfoMasterDegreeThesisDataVersionWithGuiders();
            infoMasterDegreeThesisDataVersionWithGuiders.copyFromDomain(masterDegreeThesisDataVersion);
        }
        return infoMasterDegreeThesisDataVersionWithGuiders;
    }
}