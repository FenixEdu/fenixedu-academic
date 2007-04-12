package net.sourceforge.fenixedu.dataTransferObject.protocol;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.protocols.Protocol;
import net.sourceforge.fenixedu.domain.protocols.ProtocolHistory;
import net.sourceforge.fenixedu.domain.util.FactoryExecutor;

import org.apache.struts.action.ActionMessage;
import org.joda.time.YearMonthDay;

public abstract class ProtocolHistoryFactory implements Serializable, FactoryExecutor {

    private DomainReference<Protocol> protocol;

    public ProtocolHistoryFactory(Protocol protocol) {
	setProtocol(protocol);
    }

    public static class ProtocolHistoryEditorFactory extends ProtocolHistoryFactory {
	private YearMonthDay beginDate;

	private YearMonthDay endDate;

	public ProtocolHistoryEditorFactory(Protocol protocol) {
	    super(protocol);
	    setBeginDate(protocol.getLastProtocolHistory().getBeginDate());
	    setEndDate(protocol.getLastProtocolHistory().getEndDate());
	}

	public Object execute() {
	    if (getProtocol() != null && getBeginDate() != null && getEndDate() != null
		    && getBeginDate().isBefore(getEndDate())) {
		ProtocolHistory protocolHistory = getProtocol().getLastProtocolHistory();
		if (getProtocol().getProtocolHistories().size() <= 1
			|| getBeginDate().isAfter(getProtocol().getLastProtocolHistory().getBeginDate())) {
		    protocolHistory.setBeginDate(getBeginDate());
		    protocolHistory.setEndDate(getEndDate());
		    return null;
		}
		return new ActionMessage("error.protocol.invalidBeginDate");
	    }
	    return new ActionMessage("error.protocol.invalidDates");
	}

	public YearMonthDay getBeginDate() {
	    return beginDate;
	}

	public void setBeginDate(YearMonthDay beginDate) {
	    this.beginDate = beginDate;
	}

	public YearMonthDay getEndDate() {
	    return endDate;
	}

	public void setEndDate(YearMonthDay endDate) {
	    this.endDate = endDate;
	}

    }

    public static class ProtocolHistoryRenewerFactory extends ProtocolHistoryFactory {
	public static enum RenewTime {
	    YEARS, MONTHS;
	}

	private Integer duration;

	private RenewTime renewTime;

	public ProtocolHistoryRenewerFactory(Protocol protocol) {
	    super(protocol);
	}

	public Object execute() {
	    if (getProtocol() != null && getDuration() != null && getRenewTime() != null) {
		YearMonthDay beginDate = getProtocol().getLastProtocolHistory().getEndDate();
		YearMonthDay endDate = beginDate;
		if (getRenewTime().equals(RenewTime.YEARS)) {
		    endDate = endDate.plusYears(getDuration());
		} else if (getRenewTime().equals(RenewTime.MONTHS)) {
		    endDate = endDate.plusMonths(getDuration());
		}
		ProtocolHistory protocolHistory = new ProtocolHistory(beginDate, endDate);

		getProtocol().addProtocolHistories(protocolHistory);
		return null;
	    }
	    return new ActionMessage("error.protocol.invalidDates");
	}

	public Integer getDuration() {
	    return duration;
	}

	public void setDuration(Integer duration) {
	    this.duration = duration;
	}

	public RenewTime getRenewTime() {
	    return renewTime;
	}

	public void setRenewTime(RenewTime renewTime) {
	    this.renewTime = renewTime;
	}
    }

    public Protocol getProtocol() {
	return protocol != null ? protocol.getObject() : null;
    }

    public void setProtocol(Protocol protocol) {
	if (protocol != null) {
	    this.protocol = new DomainReference<Protocol>(protocol);
	} else {
	    this.protocol = null;
	}
    }

}
