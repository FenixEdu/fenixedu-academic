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
package net.sourceforge.fenixedu.domain.accessControl;

import java.util.Collections;
import java.util.Set;

import net.sourceforge.fenixedu.util.Bundle;

import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.domain.groups.PersistentGroup;
import org.fenixedu.bennu.core.groups.CustomGroup;
import org.fenixedu.bennu.core.groups.NobodyGroup;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A {@link GroupStrategy} is a stateless (pure-logic) {@link CustomGroup}, which is identified solely by its class.
 * 
 * Implementation note: GroupStrategy fails gracefully if the concrete strategy is removed: it issues a warning and returns a
 * strategy that behaves the same as {@link NobodyGroup}.
 * 
 * @author João Carvalho (joao.pedro.carvalho@tecnico.ulisboa.pt)
 *
 */
public abstract class GroupStrategy extends FenixGroup {

    private static final Logger logger = LoggerFactory.getLogger(GroupStrategy.class);

    private static final long serialVersionUID = -6389394974941030980L;

    /**
     * Retrieves a {@link GroupStrategy} by the name of its type.
     * 
     * If the given type is no longer available (or the typeName is invalid), it returns a strategy that behaves the same as
     * {@link NobodyGrou}.
     */
    public static GroupStrategy strategyForType(String typeName) {
        try {
            return (GroupStrategy) Class.forName(typeName).newInstance();
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException | ClassCastException e) {
            logger.error("Exception de-serializing GroupStrategy '" + typeName + "'. Returning AnyoneGroup.", e);
            return new NobodyGroupStrategy();
        }
    }

    private static final class NobodyGroupStrategy extends GroupStrategy {
        private static final long serialVersionUID = 584606595093061522L;

        @Override
        public String getPresentationName() {
            return BundleUtil.getString(Bundle.BENNU, "label.bennu.group.nobody");
        }

        @Override
        public Set<User> getMembers() {
            return Collections.emptySet();
        }

        @Override
        public boolean isMember(User user) {
            return false;
        }
    }

    @Override
    public final PersistentGroup toPersistentGroup() {
        return StrategyBasedGroup.getInstance(this);
    }

    @Override
    public Set<User> getMembers(DateTime when) {
        return getMembers();
    }

    @Override
    public boolean isMember(User user, DateTime when) {
        return isMember(user);
    }

    /*
     * Ensure that equality is based on class identity, not on state.
     */
    @Override
    public final boolean equals(Object other) {
        return other == null ? false : getClass().equals(other.getClass());
    }

    @Override
    public final int hashCode() {
        return this.getClass().hashCode();
    }

    /*
     * Return the className as the externalization. This ensures that no state is ever kept on the ValueType.
     */
    public final String externalize() {
        return getClass().getName();
    }

}
