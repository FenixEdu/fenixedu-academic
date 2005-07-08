package net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.domain.GuideEntry;
import net.sourceforge.fenixedu.domain.IExecutionYear;
import net.sourceforge.fenixedu.domain.IGuideEntry;
import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.domain.Student;
import net.sourceforge.fenixedu.domain.gratuity.ReimbursementGuideState;
import net.sourceforge.fenixedu.domain.reimbursementGuide.IReimbursementGuideEntry;
import net.sourceforge.fenixedu.domain.reimbursementGuide.IReimbursementGuideSituation;
import net.sourceforge.fenixedu.domain.reimbursementGuide.ReimbursementGuideEntry;
import net.sourceforge.fenixedu.domain.reimbursementGuide.ReimbursementGuideSituation;
import net.sourceforge.fenixedu.domain.transactions.IInsuranceTransaction;
import net.sourceforge.fenixedu.domain.transactions.InsuranceTransaction;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.OJB.ObjectFenixOJB;
import net.sourceforge.fenixedu.persistenceTier.transactions.IPersistentInsuranceTransaction;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.VersionedObjectsBase;
import net.sourceforge.fenixedu.util.State;

import org.apache.ojb.broker.query.Criteria;

/**
 * @author - Shezad Anavarali (sana@mega.ist.utl.pt) - Nadir Tarmahomed
 *         (naat@mega.ist.utl.pt)
 */
public class InsuranceTransactionVO extends VersionedObjectsBase implements
		IPersistentInsuranceTransaction {

	/*
	 * public IInsuranceTransaction readByExecutionYearAndStudent(
	 * IExecutionYear executionYear, IStudent student) throws
	 * ExcepcaoPersistencia { Criteria crit = new Criteria();
	 * crit.addEqualTo("executionYear.idInternal", executionYear
	 * .getIdInternal()); crit.addEqualTo("student.idInternal",
	 * student.getIdInternal()); return (IInsuranceTransaction)
	 * queryObject(InsuranceTransaction.class, crit); }
	 */

	public List readAllNonReimbursedByExecutionYearAndStudent(Integer executionYearID, Integer studentID)
			throws ExcepcaoPersistencia {

		List<IInsuranceTransaction> insuranceTransactions = (List) readAll(InsuranceTransaction.class);
		List nonReimbursedInsuranceTransactions = new ArrayList();

		for (IInsuranceTransaction insuranceTransaction : insuranceTransactions) {
			if (insuranceTransaction.getExecutionYear().getIdInternal().equals(executionYearID)
					&& insuranceTransaction.getStudent().getIdInternal().equals(studentID)) {
				IGuideEntry guideEntry = insuranceTransaction.getGuideEntry();
				if (guideEntry == null || guideEntry.getReimbursementGuideEntries() == null
						|| guideEntry.getReimbursementGuideEntries().isEmpty()) {
					nonReimbursedInsuranceTransactions.add(insuranceTransaction);
				} else {
					boolean isReimbursed = false;
					List<IReimbursementGuideEntry> reimbursementGuideEntries = guideEntry
							.getReimbursementGuideEntries();
					for (IReimbursementGuideEntry reimbursementGuideEntry : reimbursementGuideEntries) {
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

		IStudent student = (IStudent)readByOID(Student.class, studentID);
		List<IInsuranceTransaction> result = new ArrayList();
		
		if(student!=null){
			List<IInsuranceTransaction> insuranceTransactions =  student.getPerson().getAssociatedPersonAccount().getInsuranceTransactions();
			for (IInsuranceTransaction insuranceTransaction : insuranceTransactions) {
				if (insuranceTransaction.getExecutionYear().getIdInternal().equals(executionYearID)
						&& insuranceTransaction.getStudent().getIdInternal().equals(studentID)) {
					result.add(insuranceTransaction);
				}
			}
		}
		return result;
	}

}