package org.fenixedu.academic.dto.person;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.fenixedu.academic.domain.contacts.PartyContact;
import org.fenixedu.academic.domain.contacts.PhysicalAddress;
import org.fenixedu.academic.domain.contacts.PhysicalAddressData;

public class PhysicalAddressForFiscalDataInCreateRegistrationBean extends PhysicalAddressData implements Serializable {

    public static Comparator<PhysicalAddressForFiscalDataInCreateRegistrationBean> COMPARATOR_BY_ADDRESS =
            new Comparator<PhysicalAddressForFiscalDataInCreateRegistrationBean>() {
                @Override
                public int compare(final PhysicalAddressForFiscalDataInCreateRegistrationBean contact,
                        final PhysicalAddressForFiscalDataInCreateRegistrationBean otherContact) {
                    final String address = contact.getAddress();
                    final String otherAddress = otherContact.getAddress();
                    int result = 0;
                    if (address != null && otherAddress != null) {
                        result = address.compareTo(otherAddress);
                    } else if (address != null) {
                        result = 1;
                    } else if (otherAddress != null) {
                        result = -1;
                    }
                    return result == 0 ? PartyContact.COMPARATOR_BY_TYPE.compare(contact.getPhysicalAddress(),
                            otherContact.getPhysicalAddress()) : result;
                }
            };

    private PhysicalAddress physicalAddress;

    public PhysicalAddressForFiscalDataInCreateRegistrationBean() {
        super();
    }

    public String getUiFiscalPresentationValue() {
        final List<String> compounds = new ArrayList<>();

        if (StringUtils.isNotEmpty(getAddress())) {
            compounds.add(getAddress());
        }

        if (StringUtils.isNotEmpty(getAreaCode())) {
            compounds.add(getAreaCode());
        }

        if (StringUtils.isNotEmpty(getDistrictSubdivisionOfResidence())) {
            compounds.add(getDistrictSubdivisionOfResidence());
        }

        if (getCountryOfResidence() != null) {
            compounds.add(getCountryOfResidence().getLocalizedName().getContent());
        }

        return String.join(" ", compounds);
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof PhysicalAddressForFiscalDataInCreateRegistrationBean)
                && getPhysicalAddress().equals(((PhysicalAddressForFiscalDataInCreateRegistrationBean) obj).getPhysicalAddress());
    }
    
    @Override
    public int hashCode() {
        return getPhysicalAddress().hashCode();
    }

    public PhysicalAddressForFiscalDataInCreateRegistrationBean(final PhysicalAddress address) {
        super(address);

        this.physicalAddress = address;
    }

    public PhysicalAddress getPhysicalAddress() {
        return physicalAddress;
    }

    public void setPhysicalAddress(PhysicalAddress physicalAddress) {
        this.physicalAddress = physicalAddress;
    }
}
