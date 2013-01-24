package net.sourceforge.fenixedu.domain.phd;

import net.sourceforge.fenixedu.domain.organizationalStructure.AccountabilityType;
import net.sourceforge.fenixedu.domain.organizationalStructure.AccountabilityTypeEnum;
import net.sourceforge.fenixedu.domain.organizationalStructure.PartyTypeEnum;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;

import org.joda.time.YearMonthDay;

import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class PhdProgramUnit extends PhdProgramUnit_Base {

    private PhdProgramUnit() {
	super();
	super.setType(PartyTypeEnum.PHD_PROGRAM_UNIT);
    }

    static PhdProgramUnit create(final PhdProgram program, final MultiLanguageString unitName, final YearMonthDay beginDate,
	    final YearMonthDay endDate, final Unit parent) {

	final PhdProgramUnit programUnit = new PhdProgramUnit();
	programUnit.init(unitName, null, null, null, beginDate, endDate, null, null, null, null, null);
	programUnit.setPhdProgram(program);
	programUnit.addParentUnit(parent, AccountabilityType.readByType(AccountabilityTypeEnum.ACADEMIC_STRUCTURE));

	return programUnit;
    }

    @Override
    public void delete() {
	removePhdProgram();
	super.delete();
    }

}