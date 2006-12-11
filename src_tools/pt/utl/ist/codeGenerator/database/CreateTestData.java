package pt.utl.ist.codeGenerator.database;

import java.util.Date;
import java.util.Set;

import net.sourceforge.fenixedu._development.MetadataManager;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.GradeScale;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.degree.degreeCurricularPlan.DegreeCurricularPlanState;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;
import net.sourceforge.fenixedu.util.MarkType;
import net.sourceforge.fenixedu.util.PeriodState;

import org.joda.time.YearMonthDay;

public class CreateTestData {

    public static void main(String[] args) {

	MetadataManager.init("build/WEB-INF/classes/domain_model.dml");
	SuportePersistenteOJB.fixDescriptors();
	RootDomainObject.init();

	ISuportePersistente persistentSupport = null;
	try {
	    persistentSupport = PersistenceSupportFactory.getDefaultPersistenceSupport();
	    persistentSupport.iniciarTransaccao();
            clearData();
            createTestData();
	    persistentSupport.confirmarTransaccao();
	} catch (Exception ex) {
	    ex.printStackTrace();

	    try {
		if (persistentSupport != null) {
		    persistentSupport.cancelarTransaccao();
		}
	    } catch (ExcepcaoPersistencia e) {
		throw new Error(e);
	    }
	}

	System.out.println("Creation of test data complete.");
	System.exit(0);
    }

    private static void clearData() {
        final RootDomainObject rootDomainObject = RootDomainObject.getInstance();
        for (final Set<ExecutionYear> executionYears = rootDomainObject.getExecutionYearsSet(); !executionYears.isEmpty(); executionYears.iterator().next().delete());
        for (final Set<DegreeCurricularPlan> degreeCurricularPlanss = rootDomainObject.getDegreeCurricularPlansSet(); !degreeCurricularPlanss.isEmpty(); degreeCurricularPlanss.iterator().next().delete());
        for (final Set<Degree> degrees = rootDomainObject.getDegreesSet(); !degrees.isEmpty(); degrees.iterator().next().delete());
    }

    private static void createTestData() {
        createDegrees();
        createExecutionYears();
    }

    private static void createExecutionYears() {
        final ExecutionYear executionYear = new ExecutionYear();
        executionYear.setYear(constructExecutionYearString());
        executionYear.setState(PeriodState.CURRENT);
        final ExecutionPeriod executionPeriod1 = new ExecutionPeriod();
        executionPeriod1.setSemester(Integer.valueOf(1));
        executionPeriod1.setExecutionYear(executionYear);
        final ExecutionPeriod executionPeriod2 = new ExecutionPeriod();
        executionPeriod2.setSemester(Integer.valueOf(2));
        executionPeriod2.setExecutionYear(executionYear);
        executionPeriod1.setNextExecutionPeriod(executionPeriod2);

        final YearMonthDay yearMonthDay = new YearMonthDay();
        if (yearMonthDay.getMonthOfYear() < 8) {
            final YearMonthDay previousYear = yearMonthDay.minusYears(1);
            executionPeriod1.setBeginDateYearMonthDay(new YearMonthDay(previousYear.getYear(), 9, 1));
            executionPeriod1.setEndDateYearMonthDay(new YearMonthDay(yearMonthDay.getYear(), 1, 31));
            executionPeriod2.setBeginDateYearMonthDay(new YearMonthDay(yearMonthDay.getYear(), 2, 1));
            executionPeriod2.setEndDateYearMonthDay(new YearMonthDay(yearMonthDay.getYear(), 7, 31));
            if (yearMonthDay.getMonthOfYear() < 2) {
                executionPeriod1.setState(PeriodState.CURRENT);
                executionPeriod2.setState(PeriodState.OPEN);
            } else {
                executionPeriod1.setState(PeriodState.OPEN);
                executionPeriod2.setState(PeriodState.CURRENT);                
            }
        } else {
            executionPeriod1.setBeginDateYearMonthDay(new YearMonthDay(yearMonthDay.getYear(), 9, 1));
            executionPeriod1.setEndDateYearMonthDay(new YearMonthDay(yearMonthDay.getYear() + 1, 1, 31));
            executionPeriod2.setBeginDateYearMonthDay(new YearMonthDay(yearMonthDay.getYear() + 1, 2, 1));
            executionPeriod2.setEndDateYearMonthDay(new YearMonthDay(yearMonthDay.getYear() + 1, 7, 31));
            executionPeriod1.setState(PeriodState.CURRENT);
            executionPeriod2.setState(PeriodState.OPEN);
        }
    }

    private static void createDegrees() {
        final Person person = Person.readPersonByUsernameWithOpenedLogin("admin");

        int i = 0;
        for (final DegreeType degreeType : DegreeType.values()) {
            final GradeScale gradeScale = degreeType.getGradeScale();
            final Degree degree;
            if (degreeType.isBolonhaType()) {
                degree = new Degree("Nome do Curso", "Degree Name", "CODE" + (++i), degreeType, 0d, gradeScale, null);
                degree.createBolonhaDegreeCurricularPlan("DegreeCurricularPlanName", gradeScale, person);
            } else {
                degree = new Degree("Nome do Curso", "Degree Name", "CODE" + (++i), degreeType, gradeScale, DegreeCurricularPlan.class.getName());
                degree.createPreBolonhaDegreeCurricularPlan("DegreeCurricularPlanName", DegreeCurricularPlanState.ACTIVE,
                        new Date(), null, degreeType.getYears(), Integer.valueOf(1), Double.valueOf(degreeType.getDefaultEctsCredits()),
                        MarkType.TYPE20_OBJ, Integer.valueOf(100), null, gradeScale);
            }
        }
    }

    private static String constructExecutionYearString() {
        final YearMonthDay yearMonthDay = new YearMonthDay();
        return yearMonthDay.getMonthOfYear() < 8 ?
                constructExecutionYearString(yearMonthDay.minusYears(1), yearMonthDay) :
                constructExecutionYearString(yearMonthDay, yearMonthDay.plusYears(1)) ;
    }

    private static String constructExecutionYearString(final YearMonthDay year1, final YearMonthDay year2) {
        return year1.toString("yyyy") + "/" + year2.toString("yyyy");
    }

}
