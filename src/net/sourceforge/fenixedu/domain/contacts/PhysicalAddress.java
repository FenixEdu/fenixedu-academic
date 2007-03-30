package net.sourceforge.fenixedu.domain.contacts;

import java.util.Comparator;

import org.apache.commons.lang.StringUtils;

import net.sourceforge.fenixedu.domain.Country;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;
import net.sourceforge.fenixedu.injectionCode.Checked;

public class PhysicalAddress extends PhysicalAddress_Base {
    
    public static Comparator<PhysicalAddress> COMPARATOR_BY_ADDRESS = new Comparator<PhysicalAddress>() {
	public int compare(PhysicalAddress contact, PhysicalAddress otherContact) {
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
	    return (result == 0) ? COMPARATOR_BY_TYPE.compare(contact, otherContact) : result;
	}};
    
    protected PhysicalAddress() {
        super();
    }
    
    public PhysicalAddress(final Party party, final PartyContactType type, final Boolean visible, final String address, final String areaCode,
	    final String areaOfAreaCode, final String area, final String parishOfResidence, final String districtSubdivisionOfResidence,
	    final String districtOfResidence, final Country countryOfResidence) {
	
	this(party, type, visible.booleanValue(), false, new PhysicalAddressData(address, areaCode, areaOfAreaCode, area, parishOfResidence,
			districtSubdivisionOfResidence, districtOfResidence, countryOfResidence));
    }
    
    @Checked("PartyContactPredicates.checkPermissionsToManage")
    public PhysicalAddress(final Party party, final PartyContactType type, final boolean visible, final boolean defaultContact) {
	this();
	super.init(party, type, visible, defaultContact);
    }

    @Checked("PartyContactPredicates.checkPermissionsToManage")
    public PhysicalAddress(final Party party, final PartyContactType type, final boolean visible, final boolean defaultContact, final PhysicalAddressData physicalAddressData) {
	this();
	init(party, type, visible, defaultContact, physicalAddressData);
    }

    private void init(final Party party, final PartyContactType type, final boolean visible, final boolean defaultContact, final PhysicalAddressData data) {
	super.init(party, type, visible, defaultContact);
	edit(data);
    }
    
    @Checked("PartyContactPredicates.checkPermissionsToManage")
    public void edit(final PhysicalAddressData data) {
	super.setAddress(data.getAddress());
	super.setAreaCode(data.getAreaCode());
	super.setAreaOfAreaCode(data.getAreaOfAreaCode());
	super.setArea(data.getArea());
	super.setParishOfResidence(data.getParishOfResidence());
	super.setDistrictSubdivisionOfResidence(data.getDistrictSubdivisionOfResidence());
	super.setDistrictOfResidence(data.getDistrictOfResidence());
	super.setCountryOfResidence(data.getCountryOfResidence());
    }
    
    @Override
    public boolean isPhysicalAddress() {
        return true;
    }
    
    public String getCountryOfResidenceName() {
	return hasCountryOfResidence() ? getCountryOfResidence().getName() : StringUtils.EMPTY;
    }
    
    @Override
    @Checked("PartyContactPredicates.checkPermissionsToManage")
    public void delete() {
        removeCountryOfResidence();
        super.delete();
    }
}
