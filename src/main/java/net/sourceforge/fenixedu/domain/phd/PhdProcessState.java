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
package net.sourceforge.fenixedu.domain.phd;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.phd.exceptions.PhdDomainOperationException;
import net.sourceforge.fenixedu.presentationTier.Action.phd.PhdProcessStateBean;
import net.sourceforge.fenixedu.util.Bundle;

import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.joda.time.DateTime;

import pt.ist.fenixframework.Atomic;

abstract public class PhdProcessState extends PhdProcessState_Base {

    static final public Comparator<PhdProcessState> COMPARATOR_BY_DATE = new Comparator<PhdProcessState>() {
        @Override
        public int compare(PhdProcessState o1, PhdProcessState o2) {
            int result = o1.getWhenCreated().compareTo(o2.getWhenCreated());
            return result != 0 ? result : o1.getExternalId().compareTo(o2.getExternalId());
        }
    };

    protected PhdProcessState() {
        super();
        setRootDomainObject(Bennu.getInstance());
        setWhenCreated(new DateTime());
    }

    protected void init(final Person person, final String remarks, final DateTime stateDate, final PhdProcessStateType type) {
        String[] args = {};
        if (person == null) {
            throw new DomainException("error.PhdProcessState.invalid.person", args);
        }
        String[] args1 = {};
        if (stateDate == null) {
            throw new DomainException("error.PhdProcessState.invalid.stateDate", args1);
        }

        checkStateDate(stateDate, type);

        setPerson(person);
        setRemarks(remarks);
        setStateDate(stateDate);
    }

    private void checkStateDate(DateTime stateDate, final PhdProcessStateType type) {
        Collection<? extends PhdProcessState> orderedStates = getProcess().getOrderedStates();

        for (PhdProcessState phdProcessState : orderedStates) {
            if (phdProcessState == this) {
                continue;
            }

            if (phdProcessState.getStateDate() != null && phdProcessState.getStateDate().isAfter(stateDate)) {
                String newStateDate = stateDate.toString("dd/MM/yyyy") + " - " + type.getLocalizedName();
                String actualStateDate =
                        phdProcessState.getStateDate().toString("dd/MM/yyyy") + " - "
                                + phdProcessState.getType().getLocalizedName();

                throw new PhdDomainOperationException("error.PhdProcessState.state.date.is.previous.of.actual.state.on.process",
                        newStateDate, actualStateDate);
            }
        }
    }

    public void delete() {
        disconnect();
        deleteDomainObject();
    }

    protected void disconnect() {
        setPerson(null);
        setRootDomainObject(null);
    }

    abstract public PhdProcessStateType getType();

    abstract public boolean isLast();

    public abstract PhdProgramProcess getProcess();

    @Atomic
    public void editStateDate(PhdProcessStateBean bean) {
        if (bean.getStateDate() == null) {
            throw new PhdDomainOperationException("error.PhdProcessState.state.date.required");
        }

        setStateDate(bean.getStateDate());
    }

    protected static String buildExpectedStatesDescription(List<? extends PhdProcessStateType> possibleNextStates) {

        if (possibleNextStates.isEmpty()) {
            return BundleUtil.getString(Bundle.PHD, Locale.getDefault(), "message.phd.process.state.none");
        }

        StringBuilder builder = new StringBuilder();

        for (PhdProcessStateType expectedState : possibleNextStates) {
            Locale locale = Locale.getDefault();
            builder.append(expectedState.getLocalizedName(locale)).append(", ");
        }

        builder.delete(builder.length() - 2, builder.length());

        return builder.toString();
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasStateDate() {
        return getStateDate() != null;
    }

    @Deprecated
    public boolean hasWhenCreated() {
        return getWhenCreated() != null;
    }

    @Deprecated
    public boolean hasRemarks() {
        return getRemarks() != null;
    }

    @Deprecated
    public boolean hasPerson() {
        return getPerson() != null;
    }

}
