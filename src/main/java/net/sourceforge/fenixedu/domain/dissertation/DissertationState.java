package net.sourceforge.fenixedu.domain.dissertation;

import org.joda.time.DateTime;

public class DissertationState extends DissertationState_Base {

	public  DissertationState() {
        super();
    }
    
    private DateTime from = null;
    private DissertationStateValue dissertationStateValue = null;
    private DissertationState dissertationState = null;
    
	public DateTime getFrom() {
		return from;
	}
	public void setFrom(DateTime from) {
		this.from = from;
	}
	public DissertationStateValue getDissertationStateValue() {
		return dissertationStateValue;
	}
	public void setDissertationStateValue(
			DissertationStateValue dissertationStateValue) {
		this.dissertationStateValue = dissertationStateValue;
	}
	public DissertationState getDissertationState() {
		return dissertationState;
	}
	public void setDissertationState(DissertationState dissertationState) {
		this.dissertationState = dissertationState;
	}
}
