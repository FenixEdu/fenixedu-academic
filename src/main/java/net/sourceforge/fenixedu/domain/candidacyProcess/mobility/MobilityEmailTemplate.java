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
package net.sourceforge.fenixedu.domain.candidacyProcess.mobility;

import java.util.Collection;

import net.sourceforge.fenixedu.domain.candidacyProcess.DegreeOfficePublicCandidacyHashCode;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.period.MobilityApplicationPeriod;

import org.apache.commons.lang.StringUtils;
import org.fenixedu.bennu.core.domain.Bennu;

import pt.ist.fenixframework.Atomic;

public class MobilityEmailTemplate extends MobilityEmailTemplate_Base {

    protected MobilityEmailTemplate(final MobilityApplicationPeriod period, final MobilityProgram program,
            final MobilityEmailTemplateType type, final String subject, final String body) {
        super();
        setRootDomainObject(Bennu.getInstance());

        checkParameters(period, program, type, subject, body);
        setPeriod(period);
        setMobilityProgram(program);
        setType(type);
        setSubject(subject);
        setBody(body);
    }

    private void checkParameters(final MobilityApplicationPeriod period, final MobilityProgram program,
            final MobilityEmailTemplateType type, final String subject, final String body) {

        if (period == null) {
            throw new DomainException("error.mobility.MobilityEmailTemplate.period.is.required");
        }

        if (program == null) {
            throw new DomainException("error.mobility.MobilityEmailTemplate.program.is.required");
        }

        if (period.hasEmailTemplateFor(program, type) && period.getEmailTemplateFor(program, type) != this) {
            throw new DomainException("error.mobility.MobilityEmailTemplate.for.type.already.exists");
        }

        if (StringUtils.isEmpty(subject)) {
            throw new DomainException("error.mobility.MobilityEmailTemplate.subject.is.required");
        }

        if (StringUtils.isEmpty(body)) {
            throw new DomainException("error.mobility.MobilityEmailTemplate.body.is.required");
        }

    }

    public void update(final String subject, final String body) {
        checkParameters(getPeriod(), getMobilityProgram(), getType(), subject, body);
        setSubject(subject);
        setBody(body);
    }

    public void delete() {
        setMobilityProgram(null);
        setPeriod(null);
        setRootDomainObject(null);
        deleteDomainObject();
    }

    @Atomic
    public void sendEmailFor(final DegreeOfficePublicCandidacyHashCode hashCode) {
        getType().sendEmailFor(this, hashCode);
    }

    @Atomic
    public void sendMultiEmailFor(final Collection<MobilityIndividualApplicationProcess> processes) {
        getType().sendMultiEmailFor(this, processes);
    }

    public String getSubjectFor(final MobilityIndividualApplicationProcess process) {
        return getSubject();
    }

    public String getBodyFor(final MobilityIndividualApplicationProcess process) {
        return getBody();
    }

    public static MobilityEmailTemplate create(final MobilityApplicationPeriod period, final MobilityProgram program,
            final MobilityEmailTemplateType type, final String subject, final String body) {
        return new MobilityEmailTemplate(period, program, type, subject, body);
    }

    public boolean isFor(MobilityProgram program, MobilityEmailTemplateType type) {
        return getMobilityProgram() == program && getType().equals(type);
    }

}
