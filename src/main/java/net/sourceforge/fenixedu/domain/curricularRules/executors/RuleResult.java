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
package net.sourceforge.fenixedu.domain.curricularRules.executors;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import net.sourceforge.fenixedu.domain.curricularRules.executors.ruleExecutors.EnrolmentResultType;
import net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class RuleResult {

    private RuleResultType result;

    private Set<RuleResultMessage> messages;

    private Map<DegreeModule, EnrolmentResultType> enrolmentResultTypeByDegreeModule;

    private RuleResult() {
        this.enrolmentResultTypeByDegreeModule = new HashMap<DegreeModule, EnrolmentResultType>();
        this.messages = new HashSet<RuleResultMessage>();
    }

    private RuleResult(final RuleResultType resultType,
            final Map<DegreeModule, EnrolmentResultType> enrolmentResultTypeByDegreeModule, final Set<RuleResultMessage> messages) {
        this();
        this.result = resultType;
        this.enrolmentResultTypeByDegreeModule = enrolmentResultTypeByDegreeModule;
        this.messages = messages;

    }

    private RuleResult(final RuleResultType result, final EnrolmentResultType enrolmentResultType, final DegreeModule degreeModule) {
        this();

        if (result == null) {
            throw new DomainException("error.curricularRules.executors.RuleResult.result.cannot.be.null");
        }

        if (enrolmentResultType == null) {
            throw new DomainException("error.curricularRules.ruleExecutors.RuleResult.enrolmentResultType.cannot.be.null");
        }

        if (degreeModule == null) {
            throw new DomainException("error.curricularRules.executors.RuleResult.degreeModule.cannot.be.null");
        }

        this.result = result;
        this.enrolmentResultTypeByDegreeModule.put(degreeModule, enrolmentResultType);
    }

    private RuleResult(final RuleResultType result, final EnrolmentResultType enrolmentResultType,
            final DegreeModule degreeModule, final Set<RuleResultMessage> messages) {
        this(result, enrolmentResultType, degreeModule);
        this.messages.addAll(messages);
    }

    public RuleResultType getResult() {
        return this.result;
    }

    public Set<RuleResultMessage> getMessages() {
        return messages;
    }

    public RuleResult and(final RuleResult ruleResult) {
        final RuleResultType andResult = this.getResult().and(ruleResult.getResult());
        final Set<RuleResultMessage> messages = new HashSet<RuleResultMessage>(getMessages());
        messages.addAll(ruleResult.getMessages());

        return new RuleResult(andResult, andMerge(this.enrolmentResultTypeByDegreeModule,
                ruleResult.enrolmentResultTypeByDegreeModule), messages);
    }

    private Map<DegreeModule, EnrolmentResultType> andMerge(final Map<DegreeModule, EnrolmentResultType> left,
            final Map<DegreeModule, EnrolmentResultType> right) {

        for (final Entry<DegreeModule, EnrolmentResultType> each : right.entrySet()) {
            if (left.containsKey(each.getKey())) {
                left.put(each.getKey(), left.get(each.getKey()).and(each.getValue()));
            } else {
                left.put(each.getKey(), each.getValue());
            }
        }

        return left;
    }

    public RuleResult or(final RuleResult ruleResult) {
        final RuleResultType orResult = this.getResult().or(ruleResult.getResult());
        final Set<RuleResultMessage> messages = new HashSet<RuleResultMessage>(getMessages());
        messages.addAll(ruleResult.getMessages());

        return new RuleResult(orResult, orMerge(this.enrolmentResultTypeByDegreeModule,
                ruleResult.enrolmentResultTypeByDegreeModule), messages);
    }

    private Map<DegreeModule, EnrolmentResultType> orMerge(final Map<DegreeModule, EnrolmentResultType> left,
            final Map<DegreeModule, EnrolmentResultType> right) {

        for (final Entry<DegreeModule, EnrolmentResultType> each : right.entrySet()) {
            if (left.containsKey(each.getKey())) {
                left.put(each.getKey(), left.get(each.getKey()).or(each.getValue()));
            } else {
                left.put(each.getKey(), each.getValue());
            }
        }

        return left;
    }

    public boolean isTrue() {
        return getResult() == RuleResultType.TRUE;
    }

    public boolean isFalse() {
        return getResult() == RuleResultType.FALSE;
    }

    public boolean isNA() {
        return getResult() == RuleResultType.NA;
    }

    public boolean isWarning() {
        return getResult() == RuleResultType.WARNING;
    }

    public EnrolmentResultType getEnrolmentResultTypeFor(DegreeModule degreeModule) {
        return this.enrolmentResultTypeByDegreeModule.containsKey(degreeModule) ? this.enrolmentResultTypeByDegreeModule
                .get(degreeModule) : EnrolmentResultType.VALIDATED;
    }

    public boolean isTemporaryEnrolmentResultType(final DegreeModule degreeModule) {
        return getEnrolmentResultTypeFor(degreeModule) == EnrolmentResultType.TEMPORARY;
    }

    public boolean isImpossibleEnrolmentResultType(final DegreeModule degreeModule) {
        return getEnrolmentResultTypeFor(degreeModule) == EnrolmentResultType.IMPOSSIBLE;
    }

    public boolean isValidated(final DegreeModule degreeModule) {
        return getEnrolmentResultTypeFor(degreeModule) == EnrolmentResultType.VALIDATED;

    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof RuleResult) {
            return this.result == ((RuleResult) obj).getResult();
        }
        return false;
    }

    public boolean hasAnyImpossibleEnrolment() {
        return this.enrolmentResultTypeByDegreeModule.values().contains(EnrolmentResultType.IMPOSSIBLE);
    }

    @SuppressWarnings("unchecked")
    static public RuleResult createInitialTrue() {
        return new RuleResult(RuleResultType.TRUE, new HashMap<DegreeModule, EnrolmentResultType>(), Collections.EMPTY_SET);
    }

    static public RuleResult createTrue(final DegreeModule degreeModule) {
        return new RuleResult(RuleResultType.TRUE, EnrolmentResultType.VALIDATED, degreeModule);
    }

    static public RuleResult createTrue(final EnrolmentResultType enrolmentResultType, final DegreeModule degreeModule) {
        return new RuleResult(RuleResultType.TRUE, enrolmentResultType, degreeModule);
    }

    static public RuleResult createImpossible(final DegreeModule degreeModule, String message, String... args) {
        return createTrue(EnrolmentResultType.IMPOSSIBLE, degreeModule, message, args);
    }

    static public RuleResult createImpossibleWithLiteralMessage(final DegreeModule degreeModule, String message) {
        return new RuleResult(RuleResultType.TRUE, EnrolmentResultType.IMPOSSIBLE, degreeModule,
                Collections.singleton(new RuleResultMessage(message, false)));
    }

    static public RuleResult createTrue(final EnrolmentResultType enrolmentResultType, final DegreeModule degreeModule,
            final String message, final String... args) {
        return new RuleResult(RuleResultType.TRUE, enrolmentResultType, degreeModule,
                Collections.singleton(new RuleResultMessage(message, true, args)));
    }

    static public RuleResult createFalse(final DegreeModule degreeModuleToEvaluate) {
        return createFalse(EnrolmentResultType.VALIDATED, degreeModuleToEvaluate);
    }

    @SuppressWarnings("unchecked")
    static public RuleResult createInitialFalse() {
        return new RuleResult(RuleResultType.FALSE, new HashMap<DegreeModule, EnrolmentResultType>(), Collections.EMPTY_SET);
    }

    static public RuleResult createFalse(final EnrolmentResultType enrolmentResultType, final DegreeModule degreeModule) {
        return new RuleResult(RuleResultType.FALSE, enrolmentResultType, degreeModule);
    }

    static public RuleResult createFalse(final DegreeModule degreeModule, final String message, final String... args) {
        return createFalse(EnrolmentResultType.VALIDATED, degreeModule, message, args);
    }

    static public RuleResult createFalse(final EnrolmentResultType enrolmentResultType, final DegreeModule degreeModule,
            final String message, final String... args) {
        return new RuleResult(RuleResultType.FALSE, enrolmentResultType, degreeModule,
                Collections.singleton(new RuleResultMessage(message, args)));
    }

    static public RuleResult createFalseWithLiteralMessage(final DegreeModule degreeModule, final String message) {
        return new RuleResult(RuleResultType.FALSE, EnrolmentResultType.VALIDATED, degreeModule,
                Collections.singleton(new RuleResultMessage(message, false)));
    }

    static public RuleResult createNA(final DegreeModule degreeModule) {
        return new RuleResult(RuleResultType.NA, EnrolmentResultType.NULL, degreeModule);
    }

    static public RuleResult createWarning(final DegreeModule degreeModule, final Set<RuleResultMessage> ruleResultMessages) {
        return new RuleResult(RuleResultType.WARNING, EnrolmentResultType.VALIDATED, degreeModule, ruleResultMessages);
    }

    static public RuleResult createWarning(final DegreeModule degreeModule, final String message, final String... args) {
        return createWarning(degreeModule, Collections.singleton(new RuleResultMessage(message, args)));

    }

}
