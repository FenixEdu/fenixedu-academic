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
	    AdministrativeOfficeType.DEGREE, false, false, false),

    MASTER_DEGREE(
	    GradeScale.TYPE5,
	    CurricularPeriodType.TWO_YEAR,
	    false,
	    true,
	    AdministrativeOfficeType.MASTER_DEGREE, false, false, false),

    BOLONHA_DEGREE(
	    GradeScale.TYPE20,
	    CurricularPeriodType.THREE_YEAR,
	    true,
	    false,
	    AdministrativeOfficeType.DEGREE, true, false, false),

    BOLONHA_MASTER_DEGREE(
	    GradeScale.TYPE20,
	    CurricularPeriodType.TWO_YEAR,
	    true,
	    false,
	    AdministrativeOfficeType.DEGREE, false, true, false),

    BOLONHA_INTEGRATED_MASTER_DEGREE(
	    GradeScale.TYPE20,
	    CurricularPeriodType.FIVE_YEAR,
	    true,
	    false,
	    AdministrativeOfficeType.DEGREE, true, true, false),

    BOLONHA_PHD_PROGRAM(
	    GradeScale.TYPE20,
	    CurricularPeriodType.YEAR,
	    true,
	    true,
	    AdministrativeOfficeType.MASTER_DEGREE, false, false, true),

    BOLONHA_ADVANCED_FORMATION_DIPLOMA(
	    GradeScale.TYPE20,
	    CurricularPeriodType.YEAR,
	    true,
	    true,
	    AdministrativeOfficeType.MASTER_DEGREE, false, false, true),

    BOLONHA_SPECIALIZATION_DEGREE(
	    GradeScale.TYPE20,
	    CurricularPeriodType.YEAR,
	    true,
	    true,
	    AdministrativeOfficeType.MASTER_DEGREE, false, false, false);

    private GradeScale gradeScale;

    private CurricularPeriodType curricularPeriodType;

    private boolean canCreateStudent;

    private boolean canCreateStudentOnlyWithCandidacy;

    private AdministrativeOfficeType administrativeOfficeType;
    
    private boolean isFirstCycle;
    
    private boolean isSecondCycle;
    
    private boolean isThirdCycle;
    
    private DegreeType(GradeScale gradeScale, CurricularPeriodType curricularPeriodType,
	    boolean canCreateStudent, boolean canCreateStudentOnlyWithCandidacy,
	    AdministrativeOfficeType administrativeOfficeType, boolean isFirstCycle, boolean isSecondCycle, 
	    boolean isThirdCycle) {
	this.gradeScale = gradeScale;
	this.curricularPeriodType = curricularPeriodType;
	this.canCreateStudent = canCreateStudent;
	this.canCreateStudentOnlyWithCandidacy = canCreateStudentOnlyWithCandidacy;
	this.administrativeOfficeType = administrativeOfficeType;
	this.isFirstCycle = isFirstCycle;
	this.isSecondCycle = isSecondCycle;
	this.isThirdCycle = isThirdCycle;
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

    public boolean isDegree() {
	return this == DegreeType.DEGREE || this == DegreeType.BOLONHA_DEGREE;
    }

    public boolean isDegreeOrBolonhaDegreeOrBolonhaIntegratedMasterDegree() {
	return this.isDegree() || this == DegreeType.BOLONHA_INTEGRATED_MASTER_DEGREE;
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

    final public String getFilteredName() {
	final StringBuilder result = new StringBuilder(getLocalizedName());
	final String toRemove;
	
	if (isBolonhaType()) {
	    toRemove = " Bolonha";
	} else if (this == DegreeType.DEGREE) {
	    final ResourceBundle applicationResources = ResourceBundle.getBundle("resources.ApplicationResources", LanguageUtils.getLocale());
	    toRemove = " (" + getYears() + " " + applicationResources.getString("years") + ")";
	} else {
	    toRemove = "";
	}
	
	if (result.toString().contains(toRemove)) {
	    result.replace(result.indexOf(toRemove), result.indexOf(toRemove) + toRemove.length(), "");
	}
	
	return result.toString();
    }

    final public String getSeniorTitle() {
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

   public boolean isFirstCycle() {
       return isFirstCycle;
   }

   public boolean isSecondCycle() {
       return isSecondCycle;
   }

   public boolean isThirdCycle() {
       return isThirdCycle;
   }
    
 }