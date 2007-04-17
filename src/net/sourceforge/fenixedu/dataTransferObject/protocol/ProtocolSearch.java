package net.sourceforge.fenixedu.dataTransferObject.protocol;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.Country;
import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.organizationalStructure.UnitName;
import net.sourceforge.fenixedu.domain.protocols.Protocol;
import net.sourceforge.fenixedu.domain.protocols.util.ProtocolActionType;
import net.sourceforge.fenixedu.util.StringUtils;

import org.joda.time.Interval;
import org.joda.time.YearMonthDay;

public class ProtocolSearch implements Serializable {
    private String protocolNumber;

    private YearMonthDay beginProtocolBeginDate;

    private YearMonthDay endProtocolBeginDate;

    private YearMonthDay beginProtocolEndDate;

    private YearMonthDay endProtocolEndDate;

    private YearMonthDay beginSignedDate;

    private YearMonthDay endSignedDate;

    private List<ProtocolActionType> protocolActionTypes;

    private String otherProtocolActionTypes;

    private DomainReference<UnitName> partnerName;

    private String partnerNameString;

    private DomainReference<Country> country;

    public ProtocolSearch() {
	super();
    }

    public List<Protocol> getSearch() {
	List<Protocol> protocols = new ArrayList<Protocol>();
	for (Protocol protocol : RootDomainObject.getInstance().getProtocols()) {
	    if (satisfiedProtocolNumber(protocol)
		    && satisfiedDates(getBeginProtocolBeginDate(), getEndProtocolBeginDate(), protocol
			    .getLastProtocolHistory().getBeginDate())
		    && satisfiedDates(getBeginProtocolEndDate(), getEndProtocolEndDate(), protocol
			    .getLastProtocolHistory().getBeginDate())
		    && satisfiedDates(getBeginSignedDate(), getEndSignedDate(), protocol.getSignedDate())
		    && satisfiedOtherProtocolActionTypes(protocol)
		    && satiefiedProtocolActionTypes(protocol) && satisfiedProtocolPartner(protocol)
		    && satisfiedCountry(protocol)) {
		protocols.add(protocol);
	    }
	}
	return protocols;
    }

    private boolean satisfiedCountry(Protocol protocol) {
	if (getCountry() == null) {
	    return true;
	}
	for (Unit partner : protocol.getPartners()) {
	    if (partner.getNationality() != null && partner.getNationality().equals(getCountry())) {

	    }
	}
	return false;
    }

    private boolean satiefiedProtocolActionTypes(Protocol protocol) {
	return (getProtocolActionTypes() == null || protocol.getProtocolAction().contains(
		getProtocolActionTypes()));
    }

    private boolean satisfiedProtocolPartner(Protocol protocol) {
	if (getPartnerName() != null && getPartnerName().getUnit() != null) {
	    return (protocol.getPartners().contains(getPartnerName().getUnit()));
	}
	return true;
    }

    private boolean satisfiedOtherProtocolActionTypes(Protocol protocol) {
	return org.apache.commons.lang.StringUtils.isEmpty(getOtherProtocolActionTypes())
		|| StringUtils.verifyContainsWithEquality(protocol.getProtocolAction().getOtherTypes(),
			getOtherProtocolActionTypes());
    }

    private boolean satisfiedDates(YearMonthDay beginDate, YearMonthDay endDate, YearMonthDay date) {
	if (beginDate != null && date != null) {
	    if (endDate != null) {
		Interval interval = new Interval(beginDate.toDateTimeAtMidnight(), endDate
			.toDateTimeAtMidnight().plus(1));
		return interval.contains(date.toDateTimeAtMidnight());
	    } else {
		return !beginDate.isAfter(date);
	    }
	}
	return true;
    }

    private boolean satisfiedProtocolNumber(Protocol protocol) {
	return (org.apache.commons.lang.StringUtils.isEmpty(getProtocolNumber()) || StringUtils
		.normalize(protocol.getProtocolNumber()).indexOf(
			StringUtils.normalize(getProtocolNumber())) != -1);
    }

    public YearMonthDay getBeginProtocolBeginDate() {
	return beginProtocolBeginDate;
    }

    public void setBeginProtocolBeginDate(YearMonthDay beginProtocolBeginDate) {
	this.beginProtocolBeginDate = beginProtocolBeginDate;
    }

    public YearMonthDay getBeginProtocolEndDate() {
	return beginProtocolEndDate;
    }

    public void setBeginProtocolEndDate(YearMonthDay beginProtocolEndDate) {
	this.beginProtocolEndDate = beginProtocolEndDate;
    }

    public YearMonthDay getBeginSignedDate() {
	return beginSignedDate;
    }

    public void setBeginSignedDate(YearMonthDay beginSignedDate) {
	this.beginSignedDate = beginSignedDate;
    }

    public YearMonthDay getEndProtocolBeginDate() {
	return endProtocolBeginDate;
    }

    public void setEndProtocolBeginDate(YearMonthDay endProtocolBeginDate) {
	this.endProtocolBeginDate = endProtocolBeginDate;
    }

    public YearMonthDay getEndProtocolEndDate() {
	return endProtocolEndDate;
    }

    public void setEndProtocolEndDate(YearMonthDay endProtocolEndDate) {
	this.endProtocolEndDate = endProtocolEndDate;
    }

    public YearMonthDay getEndSignedDate() {
	return endSignedDate;
    }

    public void setEndSignedDate(YearMonthDay endSignedDate) {
	this.endSignedDate = endSignedDate;
    }

    public String getOtherProtocolActionTypes() {
	return otherProtocolActionTypes;
    }

    public void setOtherProtocolActionTypes(String otherProtocolActionTypes) {
	this.otherProtocolActionTypes = otherProtocolActionTypes;
    }

    public UnitName getPartnerName() {
	return partnerName != null ? partnerName.getObject() : null;
    }

    public void setPartnerName(UnitName partnerName) {
	this.partnerName = new DomainReference<UnitName>(partnerName);
    }

    public List<ProtocolActionType> getProtocolActionTypes() {
	return protocolActionTypes;
    }

    public void setProtocolActionTypes(List<ProtocolActionType> protocolActionTypes) {
	this.protocolActionTypes = protocolActionTypes;
    }

    public String getProtocolNumber() {
	return protocolNumber;
    }

    public void setProtocolNumber(String protocolNumber) {
	this.protocolNumber = protocolNumber;
    }

    public String getPartnerNameString() {
	return partnerNameString;
    }

    public void setPartnerNameString(String partnerNameString) {
	this.partnerNameString = partnerNameString;
    }

    public Country getCountry() {
	return country != null ? country.getObject() : null;
    }

    public void setCountry(Country country) {
	this.country = new DomainReference<Country>(country);
    }

}
