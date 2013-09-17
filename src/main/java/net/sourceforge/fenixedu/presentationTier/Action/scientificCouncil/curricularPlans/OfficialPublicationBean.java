package net.sourceforge.fenixedu.presentationTier.Action.scientificCouncil.curricularPlans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.scientificCouncil.curricularPlans.ChangeDegreeOfficialPublicationReference;
import net.sourceforge.fenixedu.applicationTier.Servico.scientificCouncil.curricularPlans.CreateDegreeSpecializationArea;
import net.sourceforge.fenixedu.applicationTier.Servico.scientificCouncil.curricularPlans.DeleteDegreeSpecializationArea;
import net.sourceforge.fenixedu.domain.DegreeOfficialPublication;
import net.sourceforge.fenixedu.domain.DegreeSpecializationArea;

import org.joda.time.LocalDate;

import pt.ist.fenixframework.Atomic;

public class OfficialPublicationBean implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String newReference;
    private String newNamePt;
    private String newNameEn;
    private final DegreeOfficialPublication degreeOfficialPublication;
    private final List<SpecializationName> specializationNames = new ArrayList<OfficialPublicationBean.SpecializationName>();
    private LocalDate publication;

    public OfficialPublicationBean(DegreeOfficialPublication degreeOfficialPublication) {
        super();
        this.degreeOfficialPublication = degreeOfficialPublication;
        this.newReference = degreeOfficialPublication.getOfficialReference();
        this.newNamePt = "";
        this.newNameEn = "";
        initSpecializationNames();
        this.publication = degreeOfficialPublication.getPublication();
    }

    private void initSpecializationNames() {
        for (DegreeSpecializationArea area : this.degreeOfficialPublication.getSpecializationArea()) {
            SpecializationName name = new SpecializationName(area);
            this.specializationNames.add(name);
        }
    }

    public String getNewReference() {
        return newReference;
    }

    public void setNewReference(String newReference) {
        this.newReference = newReference;
    }

    public List<SpecializationName> getSpecializationNames() {
        return specializationNames;
    }

    public DegreeOfficialPublication getDegreeOfficialPublication() {
        return degreeOfficialPublication;
    }

    public String getNewNamePt() {
        return newNamePt;
    }

    public void setNewNamePt(String newNamePt) {
        this.newNamePt = newNamePt;
    }

    public String getNewNameEn() {
        return newNameEn;
    }

    public void setNewNameEn(String newNameEn) {
        this.newNameEn = newNameEn;
    }

    public LocalDate getPublication() {
        return publication;
    }

    public void setPublication(LocalDate publication) {
        this.publication = publication;
    }

    public class ReferenceName implements Serializable {

        /**
	 * 
	 */
        private static final long serialVersionUID = 3L;
        private String newReference;
        private final DegreeOfficialPublication degreeOfficialPublication;

        public ReferenceName(DegreeOfficialPublication degreeOfficialPublication) {
            this.degreeOfficialPublication = degreeOfficialPublication;
            this.newReference = this.degreeOfficialPublication.getOfficialReference();
        }

        public String getNewReference() {
            return newReference;
        }

        public void setNewReference(String newReference) {
            this.newReference = newReference;
        }

        public DegreeOfficialPublication getDegreeOfficialPublication() {
            return degreeOfficialPublication;
        }

        public void update() throws FenixServiceException {
            ChangeDegreeOfficialPublicationReference.run(degreeOfficialPublication, newReference);
        }
    }

    public class SpecializationName implements Serializable {
        /**
	 * 
	 */
        private static final long serialVersionUID = 2L;

        private String enName;
        private String ptName;
        private final DegreeSpecializationArea specializationArea;

        public SpecializationName(DegreeSpecializationArea specializationArea) {
            this.specializationArea = specializationArea;
            this.enName = (specializationArea.getNameEn() == null ? "" : specializationArea.getNameEn());
            this.ptName = (specializationArea.getNamePt() == null ? "" : specializationArea.getNamePt());
        }

        public String getEnName() {
            return enName;
        }

        public void setEnName(String enName) {
            this.enName = enName;
        }

        public String getPtName() {
            return ptName;
        }

        public void setPtName(String ptName) {
            this.ptName = ptName;
        }

        public DegreeSpecializationArea getSpecializationArea() {
            return specializationArea;
        }

        public void update() {
            specializationArea.setNameEn(this.enName);
            specializationArea.setNamePt(this.ptName);
        }
    }

    public void removeSpecializationArea(DegreeSpecializationArea area) {

        try {
            DeleteDegreeSpecializationArea.run(area.getOfficialPublication(), area);
        } catch (FenixServiceException e) {
            // TODO ADICIONAR A MENSAGENS
            e.printStackTrace();
        }
    }

    public void addSpecializationArea(DegreeOfficialPublication degreeOfficialPublication, String namePt, String nameEn)
            throws InvalidArgumentsServiceException {
        try {
            CreateDegreeSpecializationArea.run(degreeOfficialPublication, nameEn, namePt);
        } catch (FenixServiceException e) {
            // TODO ADICIONAR A MENSAGENS
            e.printStackTrace();
        }

    }

    @Atomic
    public void update(List<SpecializationName> names) {
        DegreeOfficialPublication degreeOfficialPublication = this.getDegreeOfficialPublication();

        // check officialReference
        if (this.getNewReference().compareTo(degreeOfficialPublication.getOfficialReference()) != 0) {
            degreeOfficialPublication.setOfficialReference(this.getNewReference());
        }

        // check specializationNames
        for (SpecializationName name : names) {

            String enNameInEspArea =
                    (name.getSpecializationArea().getNameEn() == null ? "" : name.getSpecializationArea().getNameEn());

            String ptNameInEspArea =
                    (name.getSpecializationArea().getNamePt() == null ? "" : name.getSpecializationArea().getNamePt());

            // check english
            if (name.getEnName().compareTo(enNameInEspArea) != 0) {
                name.getSpecializationArea().setNameEn(name.getEnName());
            }

            // check portuguese
            if (name.getPtName().compareTo(ptNameInEspArea) != 0) {
                name.getSpecializationArea().setNamePt(name.getPtName());
            }

        }

    }

    public void updateReference() {

        // TODO:Remove..
        if (this.getNewReference() == null) {
            throw new RuntimeException("this should NOTTTTT HAPPEN!!!!");
        }

        this.getDegreeOfficialPublication().setOfficialReference(this.newReference);
        try {
            ChangeDegreeOfficialPublicationReference.run(getDegreeOfficialPublication(), getNewReference());
        } catch (FenixServiceException e) {

            e.printStackTrace();
        }
    }

}
