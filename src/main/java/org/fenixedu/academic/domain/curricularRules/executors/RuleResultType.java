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
/**
 * 
 */
package net.sourceforge.fenixedu.domain.curricularRules.executors;

public enum RuleResultType {
    FALSE(0), TRUE(1), NA(2), WARNING(3);

    static private final RuleResultType[][] AND_TABLE = new RuleResultType[][] {

    { FALSE, FALSE, FALSE, FALSE },

    { FALSE, TRUE, TRUE, WARNING },

    { FALSE, TRUE, NA, WARNING },

    { FALSE, WARNING, WARNING, WARNING }

    };

    static private final RuleResultType[][] OR_TABLE = new RuleResultType[][] {

    { FALSE, TRUE, FALSE, WARNING },

    { TRUE, TRUE, TRUE, TRUE },

    { FALSE, TRUE, NA, WARNING },

    { WARNING, TRUE, WARNING, WARNING }

    };

    private int order;

    private RuleResultType(int order) {
        this.order = order;
    }

    public int order() {
        return this.order;
    }

    public String value() {
        return name();
    }

    public RuleResultType and(final RuleResultType ruleResultType) {
        return AND_TABLE[this.order][ruleResultType.order()];
    }

    public RuleResultType or(final RuleResultType ruleResultType) {
        return OR_TABLE[this.order][ruleResultType.order()];
    }

}