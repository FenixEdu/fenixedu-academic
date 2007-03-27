package net.sourceforge.fenixedu.domain.contacts;

import net.sourceforge.fenixedu.domain.organizationalStructure.Party;

public class PhysicalAddress extends PhysicalAddress_Base {
    
    protected PhysicalAddress() {
        super();
    }
    
    public PhysicalAddress(final Party party, final PartyContactType type, final boolean visible, final boolean defaultContact) {
	this();
	super.init(party, type, visible, defaultContact);
    }

    public PhysicalAddress(final Party party, final PartyContactType type, final boolean visible, final boolean defaultContact, final PhysicalAddressData physicalAddressData) {
	this();
	init(party, type, visible, defaultContact, physicalAddressData);
    }

    private void init(final Party party, final PartyContactType type, final boolean visible, final boolean defaultContact, final PhysicalAddressData data) {
	super.init(party, type, visible, defaultContact);
	edit(data);
    }
    
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
    
    @Override
    public void delete() {
        removeCountryOfResidence();
        super.delete();
    }
}
