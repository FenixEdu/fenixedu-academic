/*
 * DegreeType.java
 *
 * Created on 20 de Dezembro de 2002, 14:12
 */

package net.sourceforge.fenixedu.domain.degree;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import net.sourceforge.fenixedu.domain.GradeScale;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOfficeType;
import net.sourceforge.fenixedu.domain.curricularPeriod.CurricularPeriodType;
import net.sourceforge.fenixedu.util.LanguageUtils;

/**
 * 
 * @author Nuno Nunes & Joana Mota
 */
public enum DegreeType {

    DEGREE(
	    GradeScale.TYPE20,
	    CurricularPeriodType.FIVE_YEAR,
	    true,
	    false,
	    AdministrativeOfficeType.DEGREE),

    MASTER_DEGREE(
	    GradeScale.TYPE5,
	    CurricularPeriodType.TWO_YEAR,
	    false,
	    true,
	    AdministrativeOfficeType.MASTER_DEGREE),

    BOLONHA_DEGREE(
	    GradeScale.TYPE20,
	    CurricularPeriodType.THREE_YEAR,
	    true,
	    false,
	    AdministrativeOfficeType.DEGREE),

    BOLONHA_MASTER_DEGREE(
	    GradeScale.TYPE20,
	    CurricularPeriodType.TWO_YEAR,
	    true,
	    false,
	    AdministrativeOfficeType.DEGREE),

    BOLONHA_INTEGRATED_MASTER_DEGREE(
	    GradeScale.TYPE20,
	    CurricularPeriodType.FIVE_YEAR,
	    true,
	    false,
	    AdministrativeOfficeType.DEGREE),

    BOLONHA_PHD_PROGRAM(
	    GradeScale.TYPE20,
	    CurricularPeriodType.YEAR,
	    true,
	    true,
	    AdministrativeOfficeType.MASTER_DEGREE),

    BOLONHA_ADVANCED_FORMATION_DIPLOMA(
	    GradeScale.TYPE20,
	    CurricularPeriodType.YEAR,
	    true,
	    true,
	    AdministrativeOfficeType.MASTER_DEGREE),

    BOLONHA_SPECIALIZATION_DEGREE(
	    GradeScale.TYPE20,
	    CurricularPeriodType.YEAR,
	    true,
	    true,
	    AdministrativeOfficeType.MASTER_DEGREE);

    private GradeScale gradeScale;

    private CurricularPeriodType curricularPeriodType;

    private boolean canCreateStudent;

    private boolean canCreateStudentOnlyWithCandidacy;

    private AdministrativeOfficeType administrativeOfficeType;

    private DegreeType(GradeScale gradeScale, CurricularPeriodType curricularPeriodType,
	    boolean canCreateStudent, boolean canCreateStudentOnlyWithCandidacy,
	    AdministrativeOfficeType administrativeOfficeType) {
	this.gradeScale = gradeScale;
	this.curricularPeriodType = curricularPeriodType;
	this.canCreateStudent = canCreateStudent;
	this.canCreateStudentOnlyWithCandidacy = canCreateStudentOnlyWithCandidacy;
	this.administrativeOfficeType = administrativeOfficeType;
    }

    public String getName() {
	return name();
    }

    public GradeScale getGradeScale() {
	return this.gradeScale;
    }

    public CurricularPeriodType getCurricularPeriodType() {
	return curricularPeriodType;
    }

    public int getYears() {
	return (int) this.curricularPeriodType.getWeight();
    }

    public double getDefaultEctsCredits() {
	switch (getCurricularPeriodType()) {
	case YEAR:
	    return 30;
	case TWO_YEAR:
	    return 120;
	case THREE_YEAR:
	    return 180;
	case FIVE_YEAR:
	    return 300;
	default:
	    return 0;
	}
    }

    public boolean isBolonhaType() {
	return this != DegreeType.DEGREE && this != DegreeType.MASTER_DEGREE;
    }

    public boolean isDegreeOrBolonhaDegreeOrBolonhaIntegratedMasterDegree() {
	return this == DegreeType.DEGREE || this == DegreeType.BOLONHA_DEGREE || this == DegreeType.BOLONHA_INTEGRATED_MASTER_DEGREE;
    }

    public boolean canCreateStudent() {
	return canCreateStudent;
    }

    public boolean canCreateStudentOnlyWithCandidacy() {
	return canCreateStudentOnlyWithCandidacy;
    }

    public AdministrativeOfficeType getAdministrativeOfficeType() {
	return administrativeOfficeType;
    }

    public String getLocalizedName() {
	return ResourceBundle.getBundle("resources.EnumerationResources", LanguageUtils.getLocale()).getString(getQualifiedName());
    }

    public String getQualifiedName() {
	return DegreeType.class.getSimpleName() + "." + name();
    }

    public String getSeniorTitle() {
	return ResourceBundle.getBundle("resources.EnumerationResources", LanguageUtils.getLocale()).getString(getQualifiedName() + ".senior.title");
    }

   public static List<DegreeType> getBolonhaDegreeTypes() {
	final List<DegreeType> result = new ArrayList<DegreeType>();

	for (final DegreeType degreeType : values()) {
	    if (degreeType.isBolonhaType()) {
		result.add(degreeType);
	    }
	}

	return result;
    }
    
 }