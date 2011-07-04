package net.sourceforge.fenixedu.domain.student.curriculum;

import java.math.BigDecimal;

import org.joda.time.LocalDate;

public class RemoteConclusionProcess extends RemoteConclusionProcess_Base {

    public RemoteConclusionProcess() {
	super();
    }

    public BigDecimal getFinalAverage() {
	return toBigDecimal(readRemoteMethod("getFinalAverage", null));
    }

    public LocalDate getConclusionDate() {
	return toLocalDate(readRemoteMethod("getConclusionDate", null));
    }

}
