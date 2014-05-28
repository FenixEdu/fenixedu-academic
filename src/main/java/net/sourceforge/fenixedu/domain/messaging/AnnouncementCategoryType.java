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
package net.sourceforge.fenixedu.domain.messaging;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.fenixedu.bennu.core.domain.Bennu;

public enum AnnouncementCategoryType {
    COMPETITION, CONFERENCE, CONGRESS, CULTURAL_EVENT, SPORTS, TRIBUTE, LECTURE, SEMINAR, AGREEMENTS_CONTRACTS, APPLICATIONS,
    FUNDING, INFORMATION, AWARD, PROJECTS, SPECIAL_EDITIONS, NEWSLETTER;

    public static AnnouncementCategory getAnnouncementCategoryByType(final AnnouncementCategoryType type) {

        return (AnnouncementCategory) CollectionUtils.find(Bennu.getInstance().getCategoriesSet(), new Predicate() {

            @Override
            public boolean evaluate(Object arg0) {
                return ((AnnouncementCategory) arg0).getType().equals(type);
            }

        });
    }
}
