package net.sourceforge.fenixedu.domain;

import java.util.Collection;

import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleType;

public abstract class AcademicProgram extends AcademicProgram_Base {
	public AcademicProgram() {
		super();
	}

	public abstract DegreeType getDegreeType();

	public abstract Collection<CycleType> getCycleTypes();
}
