/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.fenixedu.domain.internship;

import java.util.Collections;
import java.util.Random;

import net.sourceforge.fenixedu.dataTransferObject.internship.InternshipCandidacyBean;
import net.sourceforge.fenixedu.domain.util.email.Message;
import net.sourceforge.fenixedu.domain.util.email.SystemSender;
import net.sourceforge.fenixedu.util.Bundle;
import org.fenixedu.bennu.core.i18n.BundleUtil;

import org.fenixedu.bennu.core.domain.Bennu;
import org.joda.time.DateTime;

import pt.ist.fenixframework.Atomic;

public class InternshipCandidacy extends InternshipCandidacy_Base {
    private static final int MAX_CODE = 999999;

    private static final int MIN_CODE = 100000;

    private InternshipCandidacy(Integer code, InternshipCandidacySession session) {
        super();
        setCandidacyCode(code);
        setCandidacyDate(new DateTime(System.currentTimeMillis()));
        setInternshipCandidacySession(session);
        setRootDomainObject(Bennu.getInstance());
    }

    @Atomic
    public static Integer create(InternshipCandidacyBean bean) throws DuplicateInternshipCandidacy {
        Integer code = new Random(System.currentTimeMillis()).nextInt(MAX_CODE - MIN_CODE) + MIN_CODE;
        for (InternshipCandidacy other : bean.getSession().getInternshipCandidacySet()) {
            if (code.equals(other.getCandidacyCode())) {
                return create(bean); // try again;
            }
            if (bean.getStudentNumber().equals(other.getStudentNumber()) && bean.getUniversity().equals(other.getUniversity())) {
                throw new DuplicateInternshipCandidacy(bean.getStudentNumber(), bean.getUniversity().getName());
            }
        }

        InternshipCandidacy candidacy = new InternshipCandidacy(code, bean.getSession());
        beanToModel(bean, candidacy);

        SystemSender sender = Bennu.getInstance().getSystemSender();
        new Message(sender, sender.getConcreteReplyTos(), Collections.EMPTY_LIST, BundleUtil.getString(
                Bundle.GLOBAL, "iaeste.email.subject"), BundleUtil.getString(Bundle.GLOBAL,
                "iaeste.email.body", new String[] { candidacy.getName(), candidacy.getCandidacyCode().toString() }),
                candidacy.getEmail());
        return candidacy.getCandidacyCode();
    }

    @Atomic
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

    @Atomic
    public void delete() {
        setInternshipCandidacySession(null);
        setCountryOfBirth(null);
        setFirstDestination(null);
        setSecondDestination(null);
        setThirdDestination(null);
        setUniversity(null);
        setRootDomainObject(null);
        super.deleteDomainObject();
    }

    @Deprecated
    public boolean hasStreet() {
        return getStreet() != null;
    }

    @Deprecated
    public boolean hasBranch() {
        return getBranch() != null;
    }

    @Deprecated
    public boolean hasExpirationDateOfPassport() {
        return getExpirationDateOfPassport() != null;
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasTelephone() {
        return getTelephone() != null;
    }

    @Deprecated
    public boolean hasFirstDestination() {
        return getFirstDestination() != null;
    }

    @Deprecated
    public boolean hasFrench() {
        return getFrench() != null;
    }

    @Deprecated
    public boolean hasArea() {
        return getArea() != null;
    }

    @Deprecated
    public boolean hasCandidacyCode() {
        return getCandidacyCode() != null;
    }

    @Deprecated
    public boolean hasThirdDestination() {
        return getThirdDestination() != null;
    }

    @Deprecated
    public boolean hasPassportIdNumber() {
        return getPassportIdNumber() != null;
    }

    @Deprecated
    public boolean hasUniversity() {
        return getUniversity() != null;
    }

    @Deprecated
    public boolean hasBirthday() {
        return getBirthday() != null;
    }

    @Deprecated
    public boolean hasCandidacyDate() {
        return getCandidacyDate() != null;
    }

    @Deprecated
    public boolean hasCountryOfBirth() {
        return getCountryOfBirth() != null;
    }

    @Deprecated
    public boolean hasPreviousCandidacy() {
        return getPreviousCandidacy() != null;
    }

    @Deprecated
    public boolean hasMobilePhone() {
        return getMobilePhone() != null;
    }

    @Deprecated
    public boolean hasEmissionLocationOfPassport() {
        return getEmissionLocationOfPassport() != null;
    }

    @Deprecated
    public boolean hasExpirationDateOfDocumentId() {
        return getExpirationDateOfDocumentId() != null;
    }

    @Deprecated
    public boolean hasSpanish() {
        return getSpanish() != null;
    }

    @Deprecated
    public boolean hasStudentNumber() {
        return getStudentNumber() != null;
    }

    @Deprecated
    public boolean hasParishOfBirth() {
        return getParishOfBirth() != null;
    }

    @Deprecated
    public boolean hasStudentYear() {
        return getStudentYear() != null;
    }

    @Deprecated
    public boolean hasEmissionDateOfDocumentId() {
        return getEmissionDateOfDocumentId() != null;
    }

    @Deprecated
    public boolean hasEnglish() {
        return getEnglish() != null;
    }

    @Deprecated
    public boolean hasEmissionLocationOfDocumentId() {
        return getEmissionLocationOfDocumentId() != null;
    }

    @Deprecated
    public boolean hasName() {
        return getName() != null;
    }

    @Deprecated
    public boolean hasAreaCode() {
        return getAreaCode() != null;
    }

    @Deprecated
    public boolean hasEmail() {
        return getEmail() != null;
    }

    @Deprecated
    public boolean hasGender() {
        return getGender() != null;
    }

    @Deprecated
    public boolean hasDegree() {
        return getDegree() != null;
    }

    @Deprecated
    public boolean hasDocumentIdNumber() {
        return getDocumentIdNumber() != null;
    }

    @Deprecated
    public boolean hasGerman() {
        return getGerman() != null;
    }

    @Deprecated
    public boolean hasInternshipCandidacySession() {
        return getInternshipCandidacySession() != null;
    }

    @Deprecated
    public boolean hasSecondDestination() {
        return getSecondDestination() != null;
    }

    @Deprecated
    public boolean hasEmissionDateOfPassport() {
        return getEmissionDateOfPassport() != null;
    }

}
