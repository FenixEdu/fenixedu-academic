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
package org.fenixedu.academic.domain.curricularRules.executors;

import org.apache.commons.lang.ArrayUtils;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.bennu.core.i18n.BundleUtil;

public class RuleResultMessage {

    private String message;

    private String[] args;

    private boolean toTranslate;

    public RuleResultMessage(final String message, final String... args) {
        this(message, true, args);
    }

    public RuleResultMessage(final String message, final boolean toTranslate, final String... args) {
        super();
        this.message = message;
        this.args = args;
        this.toTranslate = toTranslate;
    }

    public String[] getArgs() {
        return args;
    }

    public void setArgs(String[] args) {
        this.args = args;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isToTranslate() {
        return toTranslate;
    }
    
    public String getMessageTranslated() {
        return isToTranslate() ? BundleUtil.getString(Bundle.APPLICATION, getMessage(), getArgs()) : getMessage();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof RuleResultMessage)) {
            return false;
        }

        final RuleResultMessage other = (RuleResultMessage) obj;
        return this.toTranslate == other.toTranslate && hasSameMessage(other) && hasSameArgs(other);
    }

    private boolean hasSameArgs(RuleResultMessage other) {
        if (this.args == null && other.args == null) {
            return true;
        }

        if (this.args != null && other.args != null && this.args.length == other.args.length) {
            return ArrayUtils.isEquals(this.args, other.args);
        }

        return false;
    }

    private boolean hasSameMessage(final RuleResultMessage other) {
        if (this.message == null && other.message == null) {
            return true;
        }

        if (this.message != null && other.message != null) {
            return this.message.equals(other.message);
        }

        return false;
    }

    @Override
    public int hashCode() {
        return this.message.hashCode() + (this.args != null ? this.args.length : 0);
    }
}
