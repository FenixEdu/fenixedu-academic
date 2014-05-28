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
/*
 * Created on Oct 10, 2003
 *
 */
package net.sourceforge.fenixedu.domain;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import net.sourceforge.fenixedu.domain.masterDegree.MasterDegreeClassification;
import net.sourceforge.fenixedu.domain.organizationalStructure.ExternalContract;
import net.sourceforge.fenixedu.util.State;

import org.apache.commons.beanutils.BeanComparator;
import org.fenixedu.bennu.core.domain.Bennu;

/**
 * @author - Shezad Anavarali (sana@mega.ist.utl.pt) - Nadir Tarmahomed
 *         (naat@mega.ist.utl.pt)
 */
public class MasterDegreeProofVersion extends MasterDegreeProofVersion_Base {

    @SuppressWarnings("unchecked")
    final static Comparator<MasterDegreeProofVersion> LAST_MODIFICATION_COMPARATOR = new BeanComparator("lastModification");

    public MasterDegreeProofVersion() {
        super();
        setRootDomainObject(Bennu.getInstance());
    }

    public MasterDegreeProofVersion(MasterDegreeThesis masterDegreeThesis, Employee responsibleEmployee, Date lastModification,
            Date proofDate, Date thesisDeliveryDate, MasterDegreeClassification finalResult, Integer attachedCopiesNumber,
            State currentState, List<Teacher> juries, List<ExternalContract> externalJuries) {
        this();
        this.setMasterDegreeThesis(masterDegreeThesis);
        this.setResponsibleEmployee(responsibleEmployee);
        this.setLastModification(lastModification);
        this.setProofDate(proofDate);
        this.setThesisDeliveryDate(thesisDeliveryDate);
        this.setFinalResult(finalResult);
        this.setAttachedCopiesNumber(attachedCopiesNumber);
        this.setCurrentState(currentState);
        this.getJuries().addAll(juries);
        this.getExternalJuries().addAll(externalJuries);
    }

    public boolean isConcluded() {
        return getFinalResult().equals(MasterDegreeClassification.APPROVED);
    }

    public static List<MasterDegreeProofVersion> getConcludedForDegreeAndSinceYear(Degree degree, Integer year) {

        List<MasterDegreeProofVersion> result = new ArrayList<MasterDegreeProofVersion>();

        for (final DegreeCurricularPlan degreeCurricularPlan : degree.getDegreeCurricularPlansSet()) {
            for (final StudentCurricularPlan studentCurricularPlan : degreeCurricularPlan.getStudentCurricularPlansSet()) {

                final MasterDegreeThesis thesis = studentCurricularPlan.getMasterDegreeThesis();
                if (thesis != null) {
                    final MasterDegreeProofVersion masterDegreeProofVersion = thesis.getActiveMasterDegreeProofVersion();
                    if (masterDegreeProofVersion != null && masterDegreeProofVersion.isConcluded()
                            && masterDegreeProofVersion.getProofDateYearMonthDay() != null
                            && masterDegreeProofVersion.getProofDateYearMonthDay().getYear() == year) {
                        result.add(masterDegreeProofVersion);
                    }
                }
            }
        }

        return result;
    }

    @Deprecated
    public java.util.Date getLastModification() {
        org.joda.time.DateTime dt = getLastModificationDateTime();
        return (dt == null) ? null : new java.util.Date(dt.getMillis());
    }

    @Deprecated
    public void setLastModification(java.util.Date date) {
        if (date == null) {
            setLastModificationDateTime(null);
        } else {
            setLastModificationDateTime(new org.joda.time.DateTime(date.getTime()));
        }
    }

    @Deprecated
    public java.util.Date getProofDate() {
        org.joda.time.YearMonthDay ymd = getProofDateYearMonthDay();
        return (ymd == null) ? null : new java.util.Date(ymd.getYear() - 1900, ymd.getMonthOfYear() - 1, ymd.getDayOfMonth());
    }

    @Deprecated
    public void setProofDate(java.util.Date date) {
        if (date == null) {
            setProofDateYearMonthDay(null);
        } else {
            setProofDateYearMonthDay(org.joda.time.YearMonthDay.fromDateFields(date));
        }
    }

    @Deprecated
    public java.util.Date getThesisDeliveryDate() {
        org.joda.time.YearMonthDay ymd = getThesisDeliveryDateYearMonthDay();
        return (ymd == null) ? null : new java.util.Date(ymd.getYear() - 1900, ymd.getMonthOfYear() - 1, ymd.getDayOfMonth());
    }

    @Deprecated
    public void setThesisDeliveryDate(java.util.Date date) {
        if (date == null) {
            setThesisDeliveryDateYearMonthDay(null);
        } else {
            setThesisDeliveryDateYearMonthDay(org.joda.time.YearMonthDay.fromDateFields(date));
        }
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.Teacher> getJuries() {
        return getJuriesSet();
    }

    @Deprecated
    public boolean hasAnyJuries() {
        return !getJuriesSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.organizationalStructure.ExternalContract> getExternalJuries() {
        return getExternalJuriesSet();
    }

    @Deprecated
    public boolean hasAnyExternalJuries() {
        return !getExternalJuriesSet().isEmpty();
    }

    @Deprecated
    public boolean hasThesisDeliveryDateYearMonthDay() {
        return getThesisDeliveryDateYearMonthDay() != null;
    }

    @Deprecated
    public boolean hasAttachedCopiesNumber() {
        return getAttachedCopiesNumber() != null;
    }

    @Deprecated
    public boolean hasMasterDegreeThesis() {
        return getMasterDegreeThesis() != null;
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasFinalResult() {
        return getFinalResult() != null;
    }

    @Deprecated
    public boolean hasResponsibleEmployee() {
        return getResponsibleEmployee() != null;
    }

    @Deprecated
    public boolean hasLastModificationDateTime() {
        return getLastModificationDateTime() != null;
    }

    @Deprecated
    public boolean hasCurrentState() {
        return getCurrentState() != null;
    }

    @Deprecated
    public boolean hasProofDateYearMonthDay() {
        return getProofDateYearMonthDay() != null;
    }

}
