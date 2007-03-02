package net.sourceforge.fenixedu.applicationTier.Servico.degreeAdministrativeOffice.gradeSubmission;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.MarkSheet;
import net.sourceforge.fenixedu.domain.util.Email;

public class MarkSheetsToConfirmSendMail extends Service {
	
	public void run(Collection<MarkSheet> markSheets, String from, String cc, String subject, String message) {
		List<String> mails = new ArrayList<String>();
		for (MarkSheet markSheet : markSheets) {
			if(markSheet.getResponsibleTeacher().getPerson().getEmail() != null) {
				mails.add(markSheet.getResponsibleTeacher().getPerson().getEmail());
			}
		}
		
		String[] tokens = cc.split(",");
		List<String> ccs =  Arrays.asList(tokens);
		
		new Email(null, from, null, mails, ccs, null, subject, message);
	}

}
