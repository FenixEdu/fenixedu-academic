/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.fenixedu.dataTransferObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sourceforge.fenixedu.domain.GuiderType;
import net.sourceforge.fenixedu.domain.MasterDegreeThesisDataVersion;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.organizationalStructure.ExternalContract;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Fernanda Quit�rio Created on 6/Set/2004
 * 
 */
public class InfoMasterDegreeThesisDataVersionWithGuiders extends InfoMasterDegreeThesisDataVersion {

    private static final Logger logger = LoggerFactory.getLogger(InfoMasterDegreeThesisDataVersionWithGuiders.class);

    private List<GuiderDTO> allGuiders;

    public List getAllGuiders() {
        String institution = Unit.getInstitutionAcronym();

        this.allGuiders = new ArrayList<GuiderDTO>();

        for (InfoTeacher guider : this.getInfoGuiders()) {
            this.allGuiders.add(new GuiderDTO(GuiderType.GUIDER, guider.getInfoPerson().getNome(), guider.getTeacherId(),
                    institution));
        }

        for (InfoExternalPerson guider : this.getInfoExternalGuiders()) {
            this.allGuiders.add(new GuiderDTO(GuiderType.EXTERNAL_GUIDER, guider.getInfoPerson().getNome(), null, guider
                    .getInfoInstitution().getName()));
        }

        for (InfoTeacher assistentGuider : this.getInfoAssistentGuiders()) {
            this.allGuiders.add(new GuiderDTO(GuiderType.ASSISTENT, assistentGuider.getInfoPerson().getNome(), assistentGuider
                    .getTeacherId(), institution));
        }

        for (InfoExternalPerson assistentGuider : this.getInfoExternalAssistentGuiders()) {
            this.allGuiders.add(new GuiderDTO(GuiderType.EXTERNAL_ASSISTENT, assistentGuider.getInfoPerson().getNome(), null,
                    assistentGuider.getInfoInstitution().getName()));
        }

        return this.allGuiders;
    }

    @Override
    public void copyFromDomain(MasterDegreeThesisDataVersion masterDegreeThesisDataVersion) {
        super.copyFromDomain(masterDegreeThesisDataVersion);
        if (masterDegreeThesisDataVersion != null) {
            setInfoAssistentGuiders(copyTeachers(masterDegreeThesisDataVersion.getAssistentGuiders()));

            setInfoExternalAssistentGuiders(copyExternalPersons(masterDegreeThesisDataVersion.getExternalAssistentGuiders()));

            setInfoExternalGuiders(copyExternalPersons(masterDegreeThesisDataVersion.getExternalGuiders()));

            setInfoGuiders(copyTeachers(masterDegreeThesisDataVersion.getGuiders()));
        }
    }

    /**
     * @param externalPersons
     * @return
     */
    private List<InfoExternalPerson> copyExternalPersons(Collection<ExternalContract> externalPersons) {

        List<InfoExternalPerson> infoExternalPersons = new ArrayList<InfoExternalPerson>(externalPersons.size());
        for (ExternalContract externalPerson : externalPersons) {
            infoExternalPersons.add(InfoExternalPerson.newInfoFromDomain(externalPerson));
        }
        return infoExternalPersons;

    }

    /**
     * @param masterDegreeThesisDataVersion
     * @return
     */
    private List<InfoTeacher> copyTeachers(Collection<Teacher> teachers) {
        return (List<InfoTeacher>) CollectionUtils.collect(teachers, new Transformer() {
            @Override
            public Object transform(Object arg0) {
                Teacher teacher = (Teacher) arg0;
                return InfoTeacher.newInfoFromDomain(teacher);
            }
        });
    }

    public static InfoMasterDegreeThesisDataVersion newInfoFromDomain(MasterDegreeThesisDataVersion masterDegreeThesisDataVersion) {
        InfoMasterDegreeThesisDataVersionWithGuiders infoMasterDegreeThesisDataVersionWithGuiders = null;
        if (masterDegreeThesisDataVersion != null) {
            infoMasterDegreeThesisDataVersionWithGuiders = new InfoMasterDegreeThesisDataVersionWithGuiders();
            infoMasterDegreeThesisDataVersionWithGuiders.copyFromDomain(masterDegreeThesisDataVersion);
        }
        return infoMasterDegreeThesisDataVersionWithGuiders;
    }
}
