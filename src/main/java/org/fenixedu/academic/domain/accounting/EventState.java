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
package org.fenixedu.academic.domain.accounting;

import org.joda.time.DateTime;

public enum EventState {
    OPEN, CLOSED, CANCELLED;

    public static final String EVENT_STATE_CHANGED = EventState.class.getSimpleName() + ".changed";

    public String getName() {
        return name();
    }

    public String getQualifiedName() {
        return EventState.class.getSimpleName() + "." + name();
    }

    public String getFullyQualifiedName() {
        return EventState.class.getName() + "." + name();
    }

    public static class ChangeStateEvent {

        private final EventState oldState;
        private final EventState newState;
        private final Event event;
        private final DateTime when;

        public ChangeStateEvent(EventState newState, Event event, DateTime when) {
            this.oldState = event.getEventState();
            this.newState = newState;
            this.event = event;
            this.when = when;
        }

        public EventState getNewState() {
            return newState;
        }

        public EventState getOldState() {
            return oldState;
        }

        public Event getEvent() {
            return event;
        }

        public DateTime getWhen() {
            return when;
        }
    }
}
