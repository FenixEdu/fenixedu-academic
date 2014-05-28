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
package net.sourceforge.fenixedu.domain.accounting;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.ExecutionInterval;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.fenixedu.bennu.core.domain.Bennu;

import pt.ist.fenixframework.Atomic;

public class PaymentCodeMapping extends PaymentCodeMapping_Base {

    static public class PaymentCodeMappingBean implements Serializable {
        private ExecutionInterval executionInterval;
        private PaymentCode oldCode;
        private PaymentCode newCode;

        public PaymentCodeMappingBean() {
        }

        public ExecutionInterval getExecutionInterval() {
            return this.executionInterval;
        }

        public void setExecutionInterval(final ExecutionInterval executionInterval) {
            this.executionInterval = executionInterval;
        }

        public boolean hasExecutionInterval() {
            return getExecutionInterval() != null;
        }

        public PaymentCode getOldCode() {
            return this.oldCode;
        }

        public void setOldCode(final PaymentCode oldCode) {
            this.oldCode = oldCode;
        }

        public PaymentCode getNewCode() {
            return this.newCode;
        }

        public void setNewCode(PaymentCode newCode) {
            this.newCode = newCode;
        }

        public PaymentCodeMapping create() {
            return PaymentCodeMapping.create(getExecutionInterval(), getOldCode(), getNewCode());
        }

        public void clear() {
            setOldCode(null);
            setNewCode(null);
        }
    }

    protected PaymentCodeMapping() {
        super();
        setRootDomainObject(Bennu.getInstance());
    }

    public PaymentCodeMapping(final ExecutionInterval executionInterval, final PaymentCode oldCode, final PaymentCode newCode) {
        this();
        String[] args = {};
        if (executionInterval == null) {
            throw new DomainException("error.PaymentCodeMapping.invalid.execution.interval", args);
        }
        String[] args1 = {};
        if (oldCode == null) {
            throw new DomainException("error.PaymentCodeMapping.invalid.old.code", args1);
        }
        String[] args2 = {};
        if (newCode == null) {
            throw new DomainException("error.PaymentCodeMapping.invalid.new.code", args2);
        }
        check(executionInterval, oldCode, newCode);
        setExecutionInterval(executionInterval);
        setOldPaymentCode(oldCode);
        setNewPaymentCode(newCode);
    }

    private void check(final ExecutionInterval executionInterval, final PaymentCode oldCode, final PaymentCode newCode) {
        if (oldCode.equals(newCode)) {
            throw new DomainException("error.PaymentCodeMapping.old.code.equals.new.code");
        }
        final PaymentCode code = executionInterval.findNewCodeInPaymentCodeMapping(oldCode);
        if (code != null) {
            throw new DomainException("error.PaymentCodeMapping.find.existing.code", oldCode.getFormattedCode(),
                    code.getFormattedCode());
        }
    }

    @Atomic
    public void delete() {
        setExecutionInterval(null);
        setOldPaymentCode(null);
        setNewPaymentCode(null);
        setRootDomainObject(null);
        super.deleteDomainObject();
    }

    public boolean hasOldPaymentCode(final PaymentCode oldCode) {
        return hasOldPaymentCode() && getOldPaymentCode().equals(oldCode);
    }

    public boolean has(final ExecutionInterval executionInterval) {
        return getExecutionInterval().equals(executionInterval);
    }

    @Atomic
    static public PaymentCodeMapping create(final ExecutionInterval executionInterval, final PaymentCode oldCode,
            final PaymentCode newCode) {
        return new PaymentCodeMapping(executionInterval, oldCode, newCode);
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasExecutionInterval() {
        return getExecutionInterval() != null;
    }

    @Deprecated
    public boolean hasOldPaymentCode() {
        return getOldPaymentCode() != null;
    }

    @Deprecated
    public boolean hasNewPaymentCode() {
        return getNewPaymentCode() != null;
    }

}
