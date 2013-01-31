package net.sourceforge.fenixedu.domain.vigilancy.strategies;

import java.util.List;

import net.sourceforge.fenixedu.domain.WrittenEvaluation;
import net.sourceforge.fenixedu.domain.vigilancy.VigilantWrapper;

abstract public class Strategy {

	abstract public StrategySugestion sugest(List<VigilantWrapper> vigilantWrapers, WrittenEvaluation writtenEvaluation);

}
