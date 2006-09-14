package net.sourceforge.fenixedu.domain.vigilancy.strategies;

import java.util.List;

import net.sourceforge.fenixedu.domain.WrittenEvaluation;
import net.sourceforge.fenixedu.domain.vigilancy.Vigilant;

abstract public class Strategy {

    abstract public StrategySugestion sugest(List<Vigilant> vigilants,
            WrittenEvaluation writtenEvaluation);

}
