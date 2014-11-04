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

import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.domain.alumni.CerimonyInquiry;
import net.sourceforge.fenixedu.domain.alumni.CerimonyInquiryPerson;

import org.fenixedu.bennu.core.annotation.GroupArgument;
import org.fenixedu.bennu.core.annotation.GroupOperator;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.domain.groups.PersistentGroup;
import org.joda.time.DateTime;

import com.google.common.base.Objects;

@GroupOperator("cerimonyInquiry")
public class CerimonyInquiryGroup extends FenixGroup {
    private static final long serialVersionUID = 228948654158148141L;

    @GroupArgument
    private CerimonyInquiry cerimonyInquiry;

    private CerimonyInquiryGroup() {
        super();
    }

    private CerimonyInquiryGroup(CerimonyInquiry cerimonyInquiry) {
        this();
        this.cerimonyInquiry = cerimonyInquiry;
    }

    public static CerimonyInquiryGroup get(CerimonyInquiry cerimonyInquiry) {
        return new CerimonyInquiryGroup(cerimonyInquiry);
    }

    @Override
    public String[] getPresentationNameKeyArgs() {
        return new String[] { cerimonyInquiry.getDescription() };
    }

    @Override
    public Set<User> getMembers() {
        Set<User> users = new HashSet<User>();
        for (final CerimonyInquiryPerson cerimonyInquiryPerson : cerimonyInquiry.getCerimonyInquiryPersonSet()) {
            User user = cerimonyInquiryPerson.getPerson().getUser();
            if (user != null) {
                users.add(user);
            }
        }
        return users;
    }

    @Override
    public Set<User> getMembers(DateTime when) {
        return getMembers();
    }

    @Override
    public boolean isMember(User user) {
        if (user == null) {
            return false;
        }
        for (final CerimonyInquiryPerson cerimonyInquiryPerson : user.getPerson().getCerimonyInquiryPersonSet()) {
            if (cerimonyInquiryPerson.getCerimonyInquiry().equals(cerimonyInquiry)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isMember(User user, DateTime when) {
        return isMember(user);
    }

    @Override
    public PersistentGroup toPersistentGroup() {
        return PersistentCerimonyInquiryGroup.getInstance(cerimonyInquiry);
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof CerimonyInquiryGroup) {
            return Objects.equal(cerimonyInquiry, ((CerimonyInquiryGroup) object).cerimonyInquiry);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(cerimonyInquiry);
    }
}
