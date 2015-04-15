/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Academic.
 *
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.academic.domain.messaging;

import java.util.Locale;

import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.util.MultiLanguageString;
import org.fenixedu.bennu.core.groups.Group;
import org.fenixedu.commons.StringNormalizer;
import org.joda.time.DateTime;

public abstract class Forum extends Forum_Base {

    protected Forum() {

    }

    public Forum(MultiLanguageString name, MultiLanguageString description) {
        init(name, description);
    }

    public void init(MultiLanguageString name, MultiLanguageString description) {
        setCreationDate(new DateTime());
        setName(name);
        setDescription(description);
    }

    public boolean hasConversationThreadWithSubject(MultiLanguageString subject) {
        ConversationThread conversationThread = getConversationThreadBySubject(subject);

        return (conversationThread != null) ? true : false;
    }

    public ConversationThread getConversationThreadBySubject(MultiLanguageString subject) {
        for (ConversationThread conversationThread : getConversationThreadSet()) {
            final MultiLanguageString title = conversationThread.getTitle();
            if (title != null && title.equalInAnyLanguage(subject)) {
                return conversationThread;
            }
        }

        return null;
    }

    public int getConversationMessagesCount() {
        int total = 0;

        for (ConversationThread conversationThread : getConversationThreadSet()) {
            total += conversationThread.getMessageSet().size();
        }

        return total;
    }

    public void checkIfPersonCanWrite(Person person) {
        if (!getWritersGroup().isMember(person.getUser())) {
            throw new DomainException("forum.person.cannot.write");
        }
    }

    public void checkIfCanAddConversationThreadWithSubject(MultiLanguageString subject) {
        if (hasConversationThreadWithSubject(subject)) {
            throw new DomainException("forum.already.existing.conversation.thread");
        }
    }

    public void addEmailSubscriber(Person person) {
        if (!getReadersGroup().isMember(person.getUser())) {
            throw new DomainException("forum.cannot.subscribe.person.because.does.not.belong.to.readers");
        }

        ForumSubscription subscription = getPersonSubscription(person);
        if (subscription == null) {
            subscription = new ForumSubscription(person, this);
        }

        subscription.setReceivePostsByEmail(true);

    }

    public void removeEmailSubscriber(Person person) {
        ForumSubscription subscription = getPersonSubscription(person);

        if (subscription != null) {
            if (subscription.getFavorite() == false) {
                removeForumSubscriptions(subscription);
                subscription.delete();
            } else {
                subscription.setReceivePostsByEmail(false);
            }
        }

    }

    public ForumSubscription getPersonSubscription(Person person) {
        for (ForumSubscription subscription : getForumSubscriptionsSet()) {
            if (subscription.getPerson() == person) {
                return subscription;
            }
        }

        return null;
    }

    public boolean isPersonReceivingMessagesByEmail(Person person) {
        ForumSubscription subscription = getPersonSubscription(person);

        return (subscription != null) ? subscription.getReceivePostsByEmail() : false;
    }

    public ConversationThread createConversationThread(Person creator, MultiLanguageString subject) {
        checkIfPersonCanWrite(creator);
        checkIfCanAddConversationThreadWithSubject(subject);

        return new ConversationThread(this, creator, subject);
    }

    public abstract Group getReadersGroup();

    public abstract Group getWritersGroup();

    public abstract Group getAdminGroup();

    public MultiLanguageString getNormalizedName() {
        return normalize(getName());
    }

    public static MultiLanguageString normalize(final MultiLanguageString multiLanguageString) {
        if (multiLanguageString == null) {
            return null;
        }
        MultiLanguageString result = new MultiLanguageString();
        for (final Locale language : multiLanguageString.getAllLocales()) {
            result = result.with(language, normalize(multiLanguageString.getContent(language)));
        }
        return result;
    }

    public static String normalize(final String string) {
        return string == null ? null : StringNormalizer.normalize(string).replace(' ', '-');
    }

    public void delete() {
        for (final ForumSubscription forumSubscription : getForumSubscriptionsSet()) {
            forumSubscription.delete();
        }
        for (final ConversationThread thread : getConversationThreadSet()) {
            thread.delete();
        }
        setCreator(null);
        deleteDomainObject();
    }

}
