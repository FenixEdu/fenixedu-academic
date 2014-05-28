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
/*
 * Created on 21/Mar/2003
 *  
 */
package net.sourceforge.fenixedu.dataTransferObject;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.contacts.PhysicalAddress;
import net.sourceforge.fenixedu.domain.contacts.PhysicalAddressData;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.person.IDDocumentType;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class InfoContributor extends InfoObject {

    public enum ContributorType {
        EXTERNAL_PERSON, EXTERNAL_INSTITUTION_UNIT,
    }

    private ContributorType contributorType;
    private String contributorName;
    private String contributorNumber;
    private String contributorAddress;
    private String areaCode;
    private String areaOfAreaCode;
    private String area;
    private String parishOfResidence;
    private String districtSubdivisionOfResidence;
    private String districtOfResidence;
    private IDDocumentType documentType;
    private String documentIdNumber;

    public InfoContributor() {
    }

    public InfoContributor(String contributorNumber, String contributorName, String contributorAddress) {
        this.contributorNumber = contributorNumber;
        this.contributorName = contributorName;
        this.contributorAddress = contributorAddress;
    }

    @Override
    public boolean equals(Object o) {
        return ((o instanceof InfoContributor) && (contributorNumber.equals(((InfoContributor) o).getContributorNumber()))
                && (contributorName.equals(((InfoContributor) o).getContributorName())) && (contributorAddress
                    .equals(((InfoContributor) o).getContributorAddress())));

    }

    public void copyFromDomain(Party contributor) {
        super.copyFromDomain(contributor);
        if (contributor != null) {
            setContributorName(contributor.getName());
            setContributorNumber(contributor.getSocialSecurityNumber());

            if (contributor.hasDefaultPhysicalAddress()) {
                final PhysicalAddress physicalAddress = contributor.getDefaultPhysicalAddress();
                setContributorAddress(physicalAddress.getAddress());
                setAreaCode(physicalAddress.getAreaCode());
                setAreaOfAreaCode(physicalAddress.getAreaOfAreaCode());
                setArea(physicalAddress.getArea());
                setParishOfResidence(physicalAddress.getParishOfResidence());
                setDistrictSubdivisionOfResidence(physicalAddress.getDistrictSubdivisionOfResidence());
                setDistrictOfResidence(physicalAddress.getDistrictOfResidence());
            }

            if (contributor.getSocialSecurityNumber() == null && contributor instanceof Person) {
                Person contributorPerson = (Person) contributor;
                setDocumentIdNumber(contributorPerson.getDocumentIdNumber());
                setDocumentType(contributorPerson.getIdDocumentType());
            }
        }
    }

    public static InfoContributor newInfoFromDomain(Party contributor) {
        InfoContributor infoContributor = null;
        if (contributor != null) {
            infoContributor = new InfoContributor();
            infoContributor.copyFromDomain(contributor);
        }
        return infoContributor;
    }

    @Atomic
    public void createContributor() throws InvalidArgumentsServiceException, DomainException {
        if (getContributorType() == ContributorType.EXTERNAL_PERSON) {
            Person.createContributor(getContributorName(), getContributorNumber().toString(), new PhysicalAddressData(
                    getContributorAddress(), getAreaCode(), getAreaOfAreaCode(), getArea(), getParishOfResidence(),
                    getDistrictSubdivisionOfResidence(), getDistrictOfResidence(), null));
        } else if (getContributorType() == ContributorType.EXTERNAL_INSTITUTION_UNIT) {
            Unit.createContributor(getContributorName(), getContributorNumber().toString(), new PhysicalAddressData(
                    getContributorAddress(), getAreaCode(), getAreaOfAreaCode(), getArea(), getParishOfResidence(),
                    getDistrictSubdivisionOfResidence(), getDistrictOfResidence(), null));
        } else {
            throw new InvalidArgumentsServiceException();
        }
    }

    @Atomic
    public InfoContributor editContributor(Integer contributorNumber, String contributorName, String contributorAddress,
            String areaCode, String areaOfAreaCode, String area, String parishOfResidence, String districtSubdivisionOfResidence,
            String districtOfResidence) throws FenixServiceException {

        final Party storedContributor = FenixFramework.getDomainObject(getExternalId());
        if (storedContributor == null) {
            throw new NonExistingServiceException();
        }

        try {
            storedContributor.editContributor(contributorName, contributorNumber.toString(), contributorAddress, areaCode,
                    areaOfAreaCode, area, parishOfResidence, districtSubdivisionOfResidence, districtOfResidence);
        } catch (DomainException e) {
            throw new ExistingServiceException();
        }

        return InfoContributor.newInfoFromDomain(storedContributor);
    }

    public ContributorType getContributorType() {
        return contributorType;
    }

    public void setContributorType(ContributorType contributorType) {
        this.contributorType = contributorType;
    }

    public String getContributorName() {
        return contributorName;
    }

    public void setContributorName(String contributorName) {
        this.contributorName = contributorName;
    }

    public String getContributorAddress() {
        return contributorAddress;
    }

    public void setContributorAddress(String contributorAddress) {
        this.contributorAddress = contributorAddress;
    }

    public String getContributorNumber() {
        return contributorNumber;
    }

    public void setContributorNumber(String contributorNumber) {
        this.contributorNumber = contributorNumber;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getAreaOfAreaCode() {
        return areaOfAreaCode;
    }

    public void setAreaOfAreaCode(String areaOfAreaCode) {
        this.areaOfAreaCode = areaOfAreaCode;
    }

    public String getDistrictOfResidence() {
        return districtOfResidence;
    }

    public void setDistrictOfResidence(String districtOfResidence) {
        this.districtOfResidence = districtOfResidence;
    }

    public String getDistrictSubdivisionOfResidence() {
        return districtSubdivisionOfResidence;
    }

    public void setDistrictSubdivisionOfResidence(String districtSubdivisionOfResidence) {
        this.districtSubdivisionOfResidence = districtSubdivisionOfResidence;
    }

    public String getParishOfResidence() {
        return parishOfResidence;
    }

    public void setParishOfResidence(String parishOfResidence) {
        this.parishOfResidence = parishOfResidence;
    }

    public String getDocumentIdNumber() {
        return documentIdNumber;
    }

    public IDDocumentType getDocumentType() {
        return documentType;
    }

    public void setDocumentIdNumber(String documentIdNumber) {
        this.documentIdNumber = documentIdNumber;
    }

    public void setDocumentType(IDDocumentType documentType) {
        this.documentType = documentType;
    }

}
