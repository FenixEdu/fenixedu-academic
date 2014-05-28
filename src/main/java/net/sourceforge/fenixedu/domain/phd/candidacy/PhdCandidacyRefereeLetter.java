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
package net.sourceforge.fenixedu.domain.phd.candidacy;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.fenixedu.bennu.core.domain.Bennu;

import pt.ist.fenixframework.Atomic;

public class PhdCandidacyRefereeLetter extends PhdCandidacyRefereeLetter_Base {

    private PhdCandidacyRefereeLetter() {
        super();
        setRootDomainObject(Bennu.getInstance());
    }

    public PhdCandidacyRefereeLetter(final PhdCandidacyReferee referee, final PhdCandidacyRefereeLetterBean bean) {
        this();
        edit(referee, bean);
    }

    private void edit(final PhdCandidacyReferee referee, final PhdCandidacyRefereeLetterBean bean) {

        check(referee, bean);

        setCandidacyReferee(referee);
        setPhdProgramCandidacyProcess(referee.getPhdProgramCandidacyProcess());

        setHowLongKnownApplicant(bean.getHowLongKnownApplicant());
        setCapacity(bean.getCapacity());
        setComparisonGroup(bean.getComparisonGroup());
        setRankInClass(bean.getRankInClass());

        setAcademicPerformance(bean.getAcademicPerformance());
        setSocialAndCommunicationSkills(bean.getSocialAndCommunicationSkills());
        setPotencialToExcelPhd(bean.getPotencialToExcelPhd());

        setComments(bean.getComments());
        if (bean.hasFileContent()) {
            if (hasFile()) {
                getFile().delete();
            }
            setFile(new PhdCandidacyRefereeLetterFile(getPhdProgramCandidacyProcess(), bean.getFilename(), bean.getFileContent()));
        }

        setRefereeName(bean.getRefereeName());
        setRefereePosition(bean.getRefereePosition());
        setRefereeInstitution(bean.getRefereeInstitution());
        setRefereeAddress(bean.getRefereeAddress());
        setRefereeCity(bean.getRefereeCity());
        setRefereeZipCode(bean.getRefereeZipCode());
        setRefereeCountry(bean.getRefereeCountry());
    }

    private void check(final PhdCandidacyReferee referee, final PhdCandidacyRefereeLetterBean bean) {
        String[] args = {};
        if (referee == null) {
            throw new DomainException("error.PhdCandidacyRefereeLetter.invalid.referee", args);
        }
        if (referee.hasLetter()) {
            throw new DomainException("error.PhdCandidacyRefereeLetter.referee.already.has.letter");
        }
        Object obj = referee.getPhdProgramCandidacyProcess();
        String[] args1 = {};

        if (obj == null) {
            throw new DomainException("error.PhdCandidacyRefereeLetter.invalid.process", args1);
        }
        String obj5 = bean.getHowLongKnownApplicant();
        String[] args6 = {};

        if (obj5 == null || obj5.isEmpty()) {
            throw new DomainException("error.PhdCandidacyRefereeLetter.invalid.howLongKnownApplicant", args6);
        }
        String obj6 = bean.getCapacity();
        String[] args7 = {};
        if (obj6 == null || obj6.isEmpty()) {
            throw new DomainException("error.PhdCandidacyRefereeLetter.invalid.capacity", args7);
        }
        String obj7 = bean.getComparisonGroup();
        String[] args8 = {};
        if (obj7 == null || obj7.isEmpty()) {
            throw new DomainException("error.PhdCandidacyRefereeLetter.invalid.comparisonGroup", args8);
        }
        Object obj1 = bean.getAcademicPerformance();
        String[] args2 = {};

        if (obj1 == null) {
            throw new DomainException("error.PhdCandidacyRefereeLetter.invalid.academicPerformance", args2);
        }
        Object obj2 = bean.getSocialAndCommunicationSkills();
        String[] args3 = {};
        if (obj2 == null) {
            throw new DomainException("error.PhdCandidacyRefereeLetter.invalid.socialAndCommunicationSkills", args3);
        }
        Object obj3 = bean.getPotencialToExcelPhd();
        String[] args4 = {};
        if (obj3 == null) {
            throw new DomainException("error.PhdCandidacyRefereeLetter.invalid.potencialToExcelPhd", args4);
        }
        String obj8 = bean.getRefereeName();
        String[] args9 = {};

        if (obj8 == null || obj8.isEmpty()) {
            throw new DomainException("error.PhdCandidacyRefereeLetter.invalid.refereeName", args9);
        }
        String obj9 = bean.getRefereeInstitution();
        String[] args10 = {};
        if (obj9 == null || obj9.isEmpty()) {
            throw new DomainException("error.PhdCandidacyRefereeLetter.invalid.refereeInstitution", args10);
        }
        String obj10 = bean.getRefereeAddress();
        String[] args11 = {};
        if (obj10 == null || obj10.isEmpty()) {
            throw new DomainException("error.PhdCandidacyRefereeLetter.invalid.refereeAddress", args11);
        }
        String obj11 = bean.getRefereeCity();
        String[] args12 = {};
        if (obj11 == null || obj11.isEmpty()) {
            throw new DomainException("error.PhdCandidacyRefereeLetter.invalid.refereeCity", args12);
        }
        String obj12 = bean.getRefereeZipCode();
        String[] args13 = {};
        if (obj12 == null || obj12.isEmpty()) {
            throw new DomainException("error.PhdCandidacyRefereeLetter.invalid.refereeZipCode", args13);
        }
        Object obj4 = bean.getRefereeCountry();
        String[] args5 = {};
        if (obj4 == null) {
            throw new DomainException("error.PhdCandidacyRefereeLetter.invalid.refereeCountry", args5);
        }
    }

    public String getRefereeEmail() {
        return getCandidacyReferee().getEmail();
    }

    public void delete() {
        if (hasFile()) {
            getFile().delete();
        }
        setRefereeCountry(null);
        setCandidacyReferee(null);
        setPhdProgramCandidacyProcess(null);
        setRootDomainObject(null);
        super.deleteDomainObject();
    }

    @Atomic
    static public PhdCandidacyRefereeLetter create(PhdCandidacyRefereeLetterBean bean) {
        return new PhdCandidacyRefereeLetter(bean.getCandidacyReferee(), bean);
    }

    @Deprecated
    public boolean hasComments() {
        return getComments() != null;
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasCandidacyReferee() {
        return getCandidacyReferee() != null;
    }

    @Deprecated
    public boolean hasFile() {
        return getFile() != null;
    }

    @Deprecated
    public boolean hasRefereePosition() {
        return getRefereePosition() != null;
    }

    @Deprecated
    public boolean hasComparisonGroup() {
        return getComparisonGroup() != null;
    }

    @Deprecated
    public boolean hasRefereeAddress() {
        return getRefereeAddress() != null;
    }

    @Deprecated
    public boolean hasRefereeCountry() {
        return getRefereeCountry() != null;
    }

    @Deprecated
    public boolean hasHowLongKnownApplicant() {
        return getHowLongKnownApplicant() != null;
    }

    @Deprecated
    public boolean hasPotencialToExcelPhd() {
        return getPotencialToExcelPhd() != null;
    }

    @Deprecated
    public boolean hasSocialAndCommunicationSkills() {
        return getSocialAndCommunicationSkills() != null;
    }

    @Deprecated
    public boolean hasRefereeZipCode() {
        return getRefereeZipCode() != null;
    }

    @Deprecated
    public boolean hasRankInClass() {
        return getRankInClass() != null;
    }

    @Deprecated
    public boolean hasAcademicPerformance() {
        return getAcademicPerformance() != null;
    }

    @Deprecated
    public boolean hasRefereeName() {
        return getRefereeName() != null;
    }

    @Deprecated
    public boolean hasRefereeCity() {
        return getRefereeCity() != null;
    }

    @Deprecated
    public boolean hasCapacity() {
        return getCapacity() != null;
    }

    @Deprecated
    public boolean hasPhdProgramCandidacyProcess() {
        return getPhdProgramCandidacyProcess() != null;
    }

    @Deprecated
    public boolean hasRefereeInstitution() {
        return getRefereeInstitution() != null;
    }

}
