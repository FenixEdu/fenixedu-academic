package org.fenixedu.academic.domain.studentCurriculum;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.fenixedu.academic.domain.student.curriculum.ICurriculumEntry;

public class EctsAndWeightProviderRegistry {

    private static Map<Class<? extends ICurriculumEntry>, Function<ICurriculumEntry, BigDecimal>> ectsProviders = new HashMap<>();

    private static Map<Class<? extends ICurriculumEntry>, Function<ICurriculumEntry, BigDecimal>> ectsForCurriculumProviders =
            new HashMap<>();

    private static Map<Class<? extends ICurriculumEntry>, Function<ICurriculumEntry, BigDecimal>> weightProviders =
            new HashMap<>();

    private static Map<Class<? extends ICurriculumEntry>, Function<ICurriculumEntry, BigDecimal>> weightForCurriculumProviders =
            new HashMap<>();

    static public void setEctsProvider(Class<? extends ICurriculumEntry> type, Function<ICurriculumEntry, BigDecimal> provider) {
        ectsProviders.put(type, provider);
    }

    static public Function<ICurriculumEntry, BigDecimal> getEctsProvider(Class<? extends ICurriculumEntry> type) {
        return ectsProviders.get(type);
    }

    static public void setEctsForCurriculumProvider(Class<? extends ICurriculumEntry> type,
            Function<ICurriculumEntry, BigDecimal> provider) {
        ectsForCurriculumProviders.put(type, provider);
    }

    static public Function<ICurriculumEntry, BigDecimal> getEctsForCurriculumProvider(Class<? extends ICurriculumEntry> type) {
        return ectsForCurriculumProviders.get(type);
    }

    static public void setWeightProvider(Class<? extends ICurriculumEntry> type,
            Function<ICurriculumEntry, BigDecimal> provider) {
        weightProviders.put(type, provider);
    }

    static public Function<ICurriculumEntry, BigDecimal> getWeightProvider(Class<? extends ICurriculumEntry> type) {
        return weightProviders.get(type);
    }

    static public void setWeightForCurriculumProvider(Class<? extends ICurriculumEntry> type,
            Function<ICurriculumEntry, BigDecimal> provider) {
        weightForCurriculumProviders.put(type, provider);
    }

    static public Function<ICurriculumEntry, BigDecimal> getWeightForCurriculumProvider(Class<? extends ICurriculumEntry> type) {
        return weightForCurriculumProviders.get(type);
    }

}
