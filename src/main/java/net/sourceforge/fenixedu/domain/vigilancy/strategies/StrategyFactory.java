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
package net.sourceforge.fenixedu.domain.vigilancy.strategies;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class StrategyFactory {

    private static StrategyFactory singleton = null;

    private Map<String, Strategy> strategyMap = new HashMap<String, Strategy>();

    private StrategyFactory() {
        // To add a new strategy put the name attribute of convokeAlgorithm
        // as the key and the objects of the new strategy.
        strategyMap.put("Por pontos", new ConvokeByPoints());
    }

    public static StrategyFactory getInstance() {
        if (singleton == null) {
            singleton = new StrategyFactory();
        }
        return singleton;
    }

    public Strategy getStrategy(String name) {
        return (strategyMap.containsKey(name)) ? strategyMap.get(name) : null;
    }

    public Set<String> getAvailableStrategies() {
        return strategyMap.keySet();
    }
}
