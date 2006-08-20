/*
 * Created on 3/Jun/2005 - 16:22:30
 * 
 */

package net.sourceforge.fenixedu.dataTransferObject;

import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.inquiries.InfoInquiriesRegistry;
import net.sourceforge.fenixedu.domain.student.Registration;

/**
 * @author João Fialho & Rita Ferreira
 *
 */
public class InfoStudentWithAttendsAndInquiriesRegistries extends InfoStudent {
	
	private List<InfoFrequenta> attends;
	private List<InfoInquiriesRegistry> inquiriesRegistries;
	
	public InfoStudentWithAttendsAndInquiriesRegistries(final Registration registration) {
		super(registration);
	}

	public List<InfoFrequenta> getAttends() {
		return attends;
	}

	public void setAttends(List<InfoFrequenta> attends) {
		this.attends = attends;
	}

	public List<InfoInquiriesRegistry> getInquiriesRegistries() {
		return inquiriesRegistries;
	}

	public void setInquiriesRegistries(List<InfoInquiriesRegistry> inquiriesRegistries) {
		this.inquiriesRegistries = inquiriesRegistries;
	}

}
