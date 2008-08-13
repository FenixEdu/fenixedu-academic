/*
 * Created on 21/Mar/2003
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.guide;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoGuide;
import net.sourceforge.fenixedu.dataTransferObject.InfoGuideWithPersonAndExecutionDegreeAndContributor;
import net.sourceforge.fenixedu.dataTransferObject.InfoGuideWithPersonAndExecutionDegreeAndContributorAndExecutionYear;
import net.sourceforge.fenixedu.dataTransferObject.guide.reimbursementGuide.InfoReimbursementGuide;
import net.sourceforge.fenixedu.domain.Guide;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.person.IDDocumentType;
import net.sourceforge.fenixedu.domain.reimbursementGuide.ReimbursementGuide;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class ChooseGuide extends Service {

    public List run(Integer guideNumber, Integer guideYear) throws FenixServiceException {
	List guides = null;

	guides = Guide.readByNumberAndYear(guideNumber, guideYear);

	if (guides == null || guides.isEmpty()) {
	    throw new NonExistingServiceException();
	}

	Iterator iterator = guides.iterator();
	List result = new ArrayList();
	while (iterator.hasNext()) {
	    Guide guide = (Guide) iterator.next();

	    InfoGuide infoGuide = InfoGuideWithPersonAndExecutionDegreeAndContributor.newInfoFromDomain(guide);
	    List infoReimbursementGuides = new ArrayList();
	    if (guide.getReimbursementGuides() != null) {
		Iterator iter = guide.getReimbursementGuides().iterator();
		while (iter.hasNext()) {
		    ReimbursementGuide reimbursementGuide = (ReimbursementGuide) iter.next();
		    InfoReimbursementGuide infoReimbursementGuide = InfoReimbursementGuide.newInfoFromDomain(reimbursementGuide);

		    infoReimbursementGuides.add(infoReimbursementGuide);

		}
	    }
	    infoGuide.setInfoReimbursementGuides(infoReimbursementGuides);
	    result.add(infoGuide);
	}
	return result;
    }

    public InfoGuide run(Integer guideNumber, Integer guideYear, Integer guideVersion) throws Exception {

	Guide guide = null;

	guide = Guide.readByNumberAndYearAndVersion(guideNumber, guideYear, guideVersion);

	if (guide == null) {
	    throw new NonExistingServiceException();
	}
	InfoGuide infoGuide = InfoGuideWithPersonAndExecutionDegreeAndContributorAndExecutionYear.newInfoFromDomain(guide);

	List infoReimbursementGuides = new ArrayList();
	if (guide.getReimbursementGuides() != null) {
	    Iterator iter = guide.getReimbursementGuides().iterator();
	    while (iter.hasNext()) {
		ReimbursementGuide reimbursementGuide = (ReimbursementGuide) iter.next();
		InfoReimbursementGuide infoReimbursementGuide = InfoReimbursementGuide.newInfoFromDomain(reimbursementGuide);
		infoReimbursementGuides.add(infoReimbursementGuide);

	    }

	}
	infoGuide.setInfoReimbursementGuides(infoReimbursementGuides);
	return infoGuide;
    }

    public List run(Integer guideYear) throws Exception {

	List guides = Guide.readByYear(guideYear);
	if (guides.isEmpty()) {
	    throw new NonExistingServiceException();
	}

	BeanComparator numberComparator = new BeanComparator("number");
	BeanComparator versionComparator = new BeanComparator("version");
	ComparatorChain chainComparator = new ComparatorChain();
	chainComparator.addComparator(numberComparator);
	chainComparator.addComparator(versionComparator);
	Collections.sort(guides, chainComparator);

	// CollectionUtils.filter(guides,)
	List result = getLatestVersions(guides);

	return result;

    }

    /**
     * 
     * This function expects to receive a list ordered by number (Ascending) and
     * version (Descending)
     * 
     * @param guides
     * @return The latest version for the guides
     */
    private List getLatestVersions(List guides) {
	List result = new ArrayList();
	Integer numberAux = null;

	Collections.reverse(guides);

	Iterator iterator = guides.iterator();
	while (iterator.hasNext()) {
	    Guide guide = (Guide) iterator.next();

	    if ((numberAux == null) || (numberAux.intValue() != guide.getNumber().intValue())) {
		numberAux = guide.getNumber();
		InfoGuide infoGuide = InfoGuideWithPersonAndExecutionDegreeAndContributor.newInfoFromDomain(guide);

		List infoReimbursementGuides = new ArrayList();
		if (guide.getReimbursementGuides() != null) {
		    Iterator iter = guide.getReimbursementGuides().iterator();
		    while (iter.hasNext()) {
			ReimbursementGuide reimbursementGuide = (ReimbursementGuide) iter.next();
			InfoReimbursementGuide infoReimbursementGuide = InfoReimbursementGuide
				.newInfoFromDomain(reimbursementGuide);
			infoReimbursementGuides.add(infoReimbursementGuide);

		    }

		}
		infoGuide.setInfoReimbursementGuides(infoReimbursementGuides);
		result.add(infoGuide);
	    }
	}
	Collections.reverse(result);
	return result;
    }

    public List run(String identificationDocumentNumber, IDDocumentType identificationDocumentType) throws Exception {

	// Check if person exists
	Person person = Person.readByDocumentIdNumberAndIdDocumentType(identificationDocumentNumber, identificationDocumentType);

	if (person == null) {
	    throw new NonExistingServiceException();
	}

	List<Guide> guides = new ArrayList<Guide>(person.getGuides());
	if (guides.size() == 0) {
	    return null;
	}

	BeanComparator numberComparator = new BeanComparator("number");
	BeanComparator versionComparator = new BeanComparator("version");
	ComparatorChain chainComparator = new ComparatorChain();
	chainComparator.addComparator(numberComparator);
	chainComparator.addComparator(versionComparator);
	Collections.sort(guides, chainComparator);

	return getLatestVersions(guides);
    }

}