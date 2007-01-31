package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.gratuity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoGratuitySituation;
import net.sourceforge.fenixedu.dataTransferObject.InfoGratuitySituationWithInfoPersonAndInfoExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.GratuitySituation;
import net.sourceforge.fenixedu.domain.GratuityValues;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.gratuity.GratuitySituationType;
import net.sourceforge.fenixedu.presentationTier.Action.masterDegree.utils.SessionConstants;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

/**
 * 
 * @author <a href="mailto:sana@ist.utl.pt">Shezad Anavarali </a>
 * @author <a href="mailto:naat@ist.utl.pt">Nadir Tarmahomed </a>
 * 
 */

public class ReadGratuitySituationListByExecutionDegreeAndSpecialization extends Service {

    /**
         * Constructor
         */
    public ReadGratuitySituationListByExecutionDegreeAndSpecialization() {

    }

    /*
         * Return an hash map with three objects: 1. at first position a list of
         * infoGratuitySituation 2. in second, a double with the total of list's
         * payed values 3. in third, a double with the total of list's remaning
         * values
         */
    public Object run(Integer executionDegreeId, String executionYearName,
	    String persistentSupportecializationName, String gratuitySituationTypeName)
	    throws FenixServiceException {

	// at least one of the arguments it's obligator
	if (executionDegreeId == null && executionYearName == null) {
	    throw new FenixServiceException(
		    "error.masterDegree.gratuity.impossible.studentsGratuityList");
	}

	HashMap result = null;

	try {

	    List executionDegreeList = new ArrayList();

	    if (executionDegreeId != null) {

		ExecutionDegree executionDegree = rootDomainObject
			.readExecutionDegreeByOID(executionDegreeId);
		executionDegreeList.add(executionDegree);

	    } else {
		// the execution degree wasn't supplied so
		// we have to show all execution degrees from the choosen year
		if (executionYearName != null) {
		    ExecutionYear executionYear = ExecutionYear
			    .readExecutionYearByName(executionYearName);
		    if (executionYear != null) {
			executionDegreeList = ExecutionDegree.getAllByExecutionYearAndDegreeType(
				executionYear.getYear(), DegreeType.MASTER_DEGREE);
		    }
		}
	    }

	    if (executionDegreeList == null || executionDegreeList.size() == 0) {
		throw new FenixServiceException(
			"error.masterDegree.gratuity.impossible.studentsGratuityList");
	    }

	    // GRATUITY SITUATION
	    GratuitySituationType gratuitySituationType = null;
	    if (!gratuitySituationTypeName.equals("all")) {
		gratuitySituationType = GratuitySituationType.valueOf(gratuitySituationTypeName);
	    }

	    List infoGratuitySituationList = new ArrayList();
	    double totalPayedValue = 0;
	    double totalRemaingValue = 0;

	    for (Iterator iter = executionDegreeList.iterator(); iter.hasNext();) {
		ExecutionDegree executionDegree = (ExecutionDegree) iter.next();
		GratuityValues gratuityValues = executionDegree.getGratuityValues();

		if (gratuityValues == null) {
		    continue;
		}

		List allStudentCurricularPlans = executionDegree.getDegreeCurricularPlan()
			.getStudentCurricularPlans();
		List filteredStudentCurricularPlans = (List) CollectionUtils.select(
			allStudentCurricularPlans, new Predicate() {

			    public boolean evaluate(Object arg0) {
				StudentCurricularPlan scp = (StudentCurricularPlan) arg0;
				return scp.getSpecialization() != null;
			    }
			});

		for (Iterator iterator = filteredStudentCurricularPlans.iterator(); iterator.hasNext();) {
		    StudentCurricularPlan studentCurricularPlan = (StudentCurricularPlan) iterator
			    .next();

		    GratuitySituation gratuitySituation = studentCurricularPlan
			    .getGratuitySituationByGratuityValuesAndGratuitySituationType(
				    gratuitySituationType, gratuityValues);

		    if (gratuitySituation == null) {
			// ignore them, because they will be created in the next
			// day
			// when the gratuity situation creator scheduled task
			// runs
			continue;
		    }

		    InfoGratuitySituation infoGratuitySituation = InfoGratuitySituationWithInfoPersonAndInfoExecutionDegree
			    .newInfoFromDomain(gratuitySituation);

		    fillSituationType(infoGratuitySituation);

		    List insuranceTransactionList = studentCurricularPlan.getRegistration()
			    .readAllNonReimbursedInsuranceTransactionsByExecutionYear(
				    executionDegree.getExecutionYear());

		    /*
                         * InsuranceTransaction insuranceTransaction =
                         * insuranceTransactionDAO
                         * .readByExecutionYearAndStudent(executionDegree
                         * .getExecutionYear(), studentCurricularPlan
                         * .getStudent());
                         */

		    if (insuranceTransactionList.size() > 0) {
			infoGratuitySituation.setInsurancePayed(SessionConstants.PAYED_INSURANCE);
		    } else {
			infoGratuitySituation.setInsurancePayed(SessionConstants.NOT_PAYED_INSURANCE);
		    }

		    gratuitySituation.updateValues();
		    final double payedValue = gratuitySituation.calculatePayedValue();
		    infoGratuitySituation.setPayedValue(payedValue);
		    totalPayedValue += payedValue;

		    infoGratuitySituationList.add(infoGratuitySituation);

		    totalRemaingValue += infoGratuitySituation.getRemainingValue().doubleValue();

		}

	    }

	    // build the result that is a hash map with a list, total payed
	    // and
	    // remaining value
	    result = new HashMap();
	    result.put(new Integer(0), infoGratuitySituationList);
	    result.put(new Integer(1), new Double(totalPayedValue));
	    result.put(new Integer(2), new Double(totalRemaingValue));
	} catch (Exception e) {
	    e.printStackTrace();
	    if (e.getMessage() != null && e.getMessage().length() > 0) {
		throw new FenixServiceException(e.getMessage());
	    }
	    throw new FenixServiceException(
		    "error.masterDegree.gratuity.impossible.studentsGratuityList");

	}

	return result;
    }

    private void fillSituationType(InfoGratuitySituation infoGratuitySituation) throws Exception { // infoGratuitySituation.getRemainingValue()
	// contains the total value that
	// a student has to

	// payed.
	if (infoGratuitySituation.getRemainingValue().longValue() > 0) {
	    infoGratuitySituation.setSituationType(GratuitySituationType.DEBTOR);
	} else if (infoGratuitySituation.getRemainingValue().longValue() == 0) {
	    infoGratuitySituation.setSituationType(GratuitySituationType.REGULARIZED);
	} else if (infoGratuitySituation.getRemainingValue().longValue() < 0) {
	    infoGratuitySituation.setSituationType(GratuitySituationType.CREDITOR);
	}
    }
}