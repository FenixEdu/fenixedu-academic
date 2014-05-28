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
package net.sourceforge.fenixedu.domain.student.curriculum;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.student.RegistrationConclusionBean;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.registrationStates.RegistrationState.RegistrationStateCreator;
import net.sourceforge.fenixedu.domain.student.registrationStates.RegistrationStateType;

import org.fenixedu.bennu.core.domain.Bennu;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

public class RegistrationConclusionProcess extends RegistrationConclusionProcess_Base {

    private RegistrationConclusionProcess(final RegistrationConclusionBean bean) {
        super();
        super.setRootDomainObject(Bennu.getInstance());

        final Registration registration = bean.getRegistration();
        final ExecutionYear conclusionYear = bean.getConclusionYear();
        String[] args = {};

        if (registration == null) {
            throw new DomainException("error.RegistrationConclusionProcess.argument.must.not.be.null", args);
        }
        String[] args1 = {};
        if (conclusionYear == null) {
            throw new DomainException("error.RegistrationConclusionProcess.conclusionYear.cannot.be.null", args1);
        }

        super.setRegistration(registration);
        super.setConclusionYear(conclusionYear);
        addVersions(bean);
    }

    @Override
    public boolean isRegistrationConclusionProcess() {
        return true;
    }

    public static void conclude(final RegistrationConclusionBean bean) {
        if (bean.isConclusionProcessed()) {
            throw new DomainException("error.ConclusionProcess.already.concluded.must.update");
        }

        createRegistrationStates(new RegistrationConclusionProcess(bean));
    }

    @Override
    public void update(final RegistrationConclusionBean bean) {
        if (!bean.isConclusionProcessed()) {
            throw new DomainException("error.ConclusionProcess.is.not.concluded");
        }

        addVersions(bean);

    }

    private static void createRegistrationStates(final RegistrationConclusionProcess conclusionProcess) {
        final Registration reg = conclusionProcess.getRegistration();
        final Person responsible = conclusionProcess.getResponsible();
        final DateTime creation = conclusionProcess.getCreationDateTime();

        if (isSchoolPartConcludedDegreeType(reg)) {
            if (!reg.isSchoolPartConcluded() && !reg.isConcluded()) {
                RegistrationStateCreator.createState(reg, responsible, creation, RegistrationStateType.SCHOOLPARTCONCLUDED);
            }
        } else {
            if (!reg.isConcluded()) {
                RegistrationStateCreator.createState(reg, responsible, creation, RegistrationStateType.CONCLUDED);
            }
        }
    }

    private static List<DegreeType> SCHOOLPART_DEGREE_TYPES = Arrays.asList(

    DegreeType.MASTER_DEGREE,

    DegreeType.BOLONHA_ADVANCED_SPECIALIZATION_DIPLOMA);

    private static boolean isSchoolPartConcludedDegreeType(final Registration reg) {
        return SCHOOLPART_DEGREE_TYPES.contains(reg.getDegreeType());
    }

    @Override
    final public void update(final Person responsible, final Integer finalAverage, final LocalDate conclusionDate,
            final String notes) {
        addVersions(new RegistrationConclusionBean(getRegistration()));
        getLastVersion().update(responsible, finalAverage, conclusionDate, notes);
    }

    final public void update(final Person responsible, final Integer finalAverage, BigDecimal average,
            final LocalDate conclusionDate, final String notes) {
        addVersions(new RegistrationConclusionBean(getRegistration()));
        getLastVersion().update(responsible, finalAverage, average, conclusionDate, notes);
    }

    @Override
    protected void addSpecificVersionInfo() {
        getLastVersion().setMasterDegreeThesis(getRegistration().getMasterDegreeThesis());
    }

    @Override
    public void setRootDomainObject(Bennu rootDomainObject) {
        throw new DomainException("error.ConclusionProcess.method.not.allowed");
    }

    @Override
    public void setRegistration(Registration registration) {
        throw new DomainException("error.ConclusionProcess.method.not.allowed");
    }

    @Override
    public void setConclusionYear(ExecutionYear conclusionYear) {
        throw new DomainException("error.ConclusionProcess.method.not.allowed");
    }

    @Deprecated
    public boolean hasRegistration() {
        return getRegistration() != null;
    }

}
