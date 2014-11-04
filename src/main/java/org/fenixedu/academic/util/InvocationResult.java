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
package net.sourceforge.fenixedu.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import pt.utl.ist.fenix.tools.resources.LabelFormatter;

public class InvocationResult {

    private boolean success;

    private List<LabelFormatter> messages;

    public InvocationResult() {
        this.success = false;
        this.messages = new ArrayList<LabelFormatter>();
    }

    public boolean isSuccess() {
        return success;
    }

    public InvocationResult setSuccess(boolean success) {
        this.success = success;
        return this;
    }

    public InvocationResult addMesssage(final LabelFormatter message) {
        this.messages.add(message);
        return this;
    }

    public InvocationResult addMessages(final List<LabelFormatter> messages) {
        this.messages.addAll(messages);
        return this;
    }

    public InvocationResult addMessage(final String bundle, final String key, final String... args) {
        this.messages.add(new LabelFormatter(bundle, key, args));

        return this;
    }

    public InvocationResult addMessage(final String text) {
        this.messages.add(new LabelFormatter(text));

        return this;
    }

    public List<LabelFormatter> getMessages() {
        return Collections.unmodifiableList(messages);
    }

    public LabelFormatter[] getMessagesAsArray() {
        return this.messages.toArray(new LabelFormatter[] {});
    }

    public InvocationResult and(final InvocationResult other) {
        InvocationResult result = new InvocationResult();
        result.setSuccess(isSuccess() && other.isSuccess());
        result.addMessages(getMessages());
        result.addMessages(other.getMessages());

        return result;

    }

    static public InvocationResult createSuccess() {
        return new InvocationResult().setSuccess(true);
    }

    static public InvocationResult createInsuccess() {
        return new InvocationResult().setSuccess(false);
    }
}
