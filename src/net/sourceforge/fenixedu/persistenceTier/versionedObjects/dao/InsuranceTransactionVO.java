package net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.GuideEntry;
import net.sourceforge.fenixedu.domain.Student;
import net.sourceforge.fenixedu.domain.gratuity.ReimbursementGuideState;
import net.sourceforge.fenixedu.domain.reimbursementGuide.ReimbursementGuideEntry;
import net.sourceforge.fenixedu.domain.transactions.InsuranceTransaction;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.transactions.IPersistentInsuranceTransaction;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.VersionedObjectsBase;

/**
 * @author - Shezad Anavarali (sana@mega.ist.utl.pt) - Nadir Tarmahomed
 *         (naat@mega.ist.utl.pt)
 */
public class InsuranceTransactionVO extends VersionedObjectsBase implements
		IPersistentInsuranceTransaction {

	/*
	 * public InsuranceTransaction readByExecutionYearAndStudent(
	 * ExecutionYear executionYear, Student student) throws
	 * ExcepcaoPersistencia { Criteria crit = new Criteria();
	 * crit.addEqualTo("executionYear.idInternal", executionYear
	 * .getIdInternal()); crit.addEqualTo("student.idInternal",
	 * student.getIdInternal()); return (InsuranceTransaction)
	 * queryObject(InsuranceTransaction.class, crit); }
	 */

	public List readAllNonReimbursedByExecutionYearAndStudent(Integer executionYearID, Integer studentID)
			throws ExcepcaoPersistencia {

		List<InsuranceTransaction> insuranceTransactions = (List) readAll(InsuranceTransaction.class);
		List nonReimbursedInsuranceTransactions = new ArrayList();

		for (InsuranceTransaction insuranceTransaction : insuranceTransactions) {
			if (insuranceTransaction.getExecutionYear().getIdInternal().equals(executionYearID)
					&& insuranceTransaction.getStudent().getIdInternal().equals(studentID)) {
				GuideEntry guideEntry = insuranceTransaction.getGuideEntry();
				if (guideEntry == null || guideEntry.getReimbursementGuideEntries() == null
						|| guideEntry.getReimbursementGuideEntries().isEmpty()) {
					nonReimbursedInsuranceTransactions.add(insuranceTransaction);
				} else {
					boolean isReimbursed = false;
					List<ReimbursementGuideEntry> reimbursementGuideEntries = guideEntry
							.getReimbursementGuideEntries();
					for (ReimbursementGuideEntry reimbursementGuideEntry : reimbursementGuideEntries) {
						if (reimbursementGuideEntry.getReimbursementGuide()
								.getActiveReimbursementGuideSituation().getReimbursementGuideState()
								.equals(ReimbursementGuideState.PAYED)) {
							isReimbursed = true;
							break;
						}
					}
					if (!isReimbursed) {
						nonReimbursedInsuranceTransactions.add(insuranceTransaction);
					}
				}
			}
		}
		return nonReimbursedInsuranceTransactions;
	}

	public List readAllByExecutionYearAndStudent(Integer executionYearID, Integer studentID)
			throws ExcepcaoPersistencia {

		Student student = (Student)readByOID(Student.class, studentID);
		List<InsuranceTransaction> result = new ArrayList();
		
		if(student!=null){
			List<InsuranceTransaction> insuranceTransactions =  student.getPerson().getAssociatedPersonAccount().getInsuranceTransactions();
			for (InsuranceTransaction insuranceTransaction : insuranceTransactions) {
				if (insuranceTransaction.getExecutionYear().getIdInternal().equals(executionYearID)
						&& insuranceTransaction.getStudent().getIdInternal().equals(studentID)) {
					result.add(insuranceTransaction);
				}
			}
		}
		return result;
	}

}