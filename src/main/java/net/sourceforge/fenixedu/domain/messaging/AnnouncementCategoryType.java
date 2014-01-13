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
