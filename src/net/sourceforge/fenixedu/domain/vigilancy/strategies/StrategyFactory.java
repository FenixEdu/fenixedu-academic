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
