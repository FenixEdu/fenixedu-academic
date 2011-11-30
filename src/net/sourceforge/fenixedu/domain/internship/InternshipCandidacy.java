package net.sourceforge.fenixedu.domain.internship;

import java.util.Collections;
import java.util.Random;

import net.sourceforge.fenixedu.dataTransferObject.internship.InternshipCandidacyBean;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.util.email.Message;
import net.sourceforge.fenixedu.domain.util.email.SystemSender;

import org.joda.time.DateTime;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.services.Service;

public class InternshipCandidacy extends InternshipCandidacy_Base {
    private static final int MAX_CODE = 999999;

    private static final int MIN_CODE = 100000;

    private InternshipCandidacy(Integer code, InternshipCandidacySession session) {
	super();
	setCandidacyCode(code);
	setCandidacyDate(new DateTime(System.currentTimeMillis()));
	setInternshipCandidacySession(session);
	setRootDomainObject(RootDomainObject.getInstance());
    }

    @Service
    public static Integer create(InternshipCandidacyBean bean) throws DuplicateInternshipCandidacy {
	Integer code = new Random(System.currentTimeMillis()).nextInt(MAX_CODE - MIN_CODE) + MIN_CODE;
	for (InternshipCandidacy other : bean.getSession().getInternshipCandidacySet()) {
	    if (code.equals(other.getCandidacyCode()))
		return create(bean); // try again;
	    if (bean.getStudentNumber().equals(other.getStudentNumber()) && bean.getUniversity().equals(other.getUniversity()))
		throw new DuplicateInternshipCandidacy(bean.getStudentNumber(), bean.getUniversity().getName());
	}

	InternshipCandidacy candidacy = new InternshipCandidacy(code, bean.getSession());
	beanToModel(bean, candidacy);

	SystemSender sender = RootDomainObject.getInstance().getSystemSender();
	new Message(sender, sender.getConcreteReplyTos(), Collections.EMPTY_LIST, RenderUtils.getResourceString(
		"GLOBAL_RESOURCES", "iaeste.email.subject"), RenderUtils.getResourceString("GLOBAL_RESOURCES",
		"iaeste.email.body", new Object[] { candidacy.getName(), candidacy.getCandidacyCode() }), candidacy.getEmail());
	return candidacy.getCandidacyCode();
    }

    @Service
    public void edit(InternshipCandidacyBean bean) throws DuplicateInternshipCandidacy {
	beanToModel(bean, this);
    }

    private static void beanToModel(InternshipCandidacyBean bean, InternshipCandidacy candidacy) {
	candidacy.setStudentNumber(bean.getStudentNumber());
	candidacy.setUniversity(bean.getUniversity());
	candidacy.setStudentYear(bean.getStudentYear().ordinal() + 1);
	candidacy.setDegree(bean.getDegree());
	candidacy.setBranch(bean.getBranch());
	candidacy.setName(bean.getName());
	candidacy.setGender(bean.getGender());
	candidacy.setBirthday(bean.getBirthday());
	candidacy.setParishOfBirth(bean.getParishOfBirth());
	candidacy.setCountryOfBirth(bean.getCountryOfBirth());

	candidacy.setDocumentIdNumber(bean.getDocumentIdNumber());
	candidacy.setEmissionLocationOfDocumentId(bean.getEmissionLocationOfDocumentId());
	candidacy.setEmissionDateOfDocumentId(bean.getEmissionDateOfDocumentId());
	candidacy.setExpirationDateOfDocumentId(bean.getExpirationDateOfDocumentId());

	candidacy.setPassportIdNumber(bean.getPassportIdNumber());
	candidacy.setEmissionLocationOfPassport(bean.getEmissionLocationOfPassport());
	candidacy.setEmissionDateOfPassport(bean.getEmissionDateOfPassport());
	candidacy.setExpirationDateOfPassport(bean.getExpirationDateOfPassport());

	candidacy.setStreet(bean.getStreet());
	candidacy.setAreaCode(bean.getAreaCode());
	candidacy.setArea(bean.getArea());

	candidacy.setTelephone(bean.getTelephone());
	candidacy.setMobilePhone(bean.getMobilePhone());
	candidacy.setEmail(bean.getEmail());

	candidacy.setFirstDestination(bean.getFirstDestination());
	candidacy.setSecondDestination(bean.getSecondDestination());
	candidacy.setThirdDestination(bean.getThirdDestination());

	candidacy.setEnglish(bean.getEnglish());
	candidacy.setFrench(bean.getFrench());
	candidacy.setSpanish(bean.getSpanish());
	candidacy.setGerman(bean.getGerman());

	candidacy.setPreviousCandidacy(bean.getPreviousCandidacy());
    }

    @Service
    public void delete() {
	removeInternshipCandidacySession();
	removeCountryOfBirth();
	removeFirstDestination();
	removeSecondDestination();
	removeThirdDestination();
	removeUniversity();
	removeRootDomainObject();
	super.deleteDomainObject();
    }
}
