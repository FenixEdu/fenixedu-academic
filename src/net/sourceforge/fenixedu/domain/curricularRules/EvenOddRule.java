package net.sourceforge.fenixedu.domain.curricularRules;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import net.sourceforge.fenixedu.dataTransferObject.GenericPair;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import pt.utl.ist.fenix.tools.util.i18n.Language;
import net.sourceforge.fenixedu.domain.curricularRules.executors.verifyExecutors.EvenOddRuleVerifier;
import net.sourceforge.fenixedu.domain.curricularRules.executors.verifyExecutors.VerifyRuleExecutor;
import net.sourceforge.fenixedu.domain.degreeStructure.Context;
import net.sourceforge.fenixedu.domain.degreeStructure.CourseGroup;
import net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicPeriod;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class EvenOddRule extends EvenOddRule_Base {

    public EvenOddRule(final DegreeModule toApplyRule, final CourseGroup contextCourseGroup, final Integer semester,
	    final AcademicPeriod academicPeriod, final Boolean even, final ExecutionSemester begin, final ExecutionSemester end) {
	super();
	checkParameters(toApplyRule, semester, academicPeriod, even);
	init(toApplyRule, contextCourseGroup, begin, end, CurricularRuleType.EVEN_ODD);
	setEven(even);
	setCurricularPeriodOrder(semester);
	setAcademicPeriod(academicPeriod);
    }

    private void checkParameters(final DegreeModule toApplyRule, final Integer semester, final AcademicPeriod academicPeriod,
	    final Boolean even) {
	if (semester == null) {
	    throw new DomainException("curricular.rule.invalid.parameters");
	}
	if (even == null) {
	    throw new DomainException("curricular.rule.invalid.parameters");
	}
	if (academicPeriod == null) {
	    throw new DomainException("curricular.rule.invalid.parameters");
	}
	if (!toApplyRule.isLeaf()) {
	    throw new DomainException("curricular.rule.invalid.parameters");
	}

    }

    @Override
    public List<GenericPair<Object, Boolean>> getLabel() {
	final List<GenericPair<Object, Boolean>> labelList = new ArrayList<GenericPair<Object, Boolean>>();

	labelList.add(new GenericPair<Object, Boolean>("label.in", true));
	labelList.add(new GenericPair<Object, Boolean>(" ", false));
	labelList.add(new GenericPair<Object, Boolean>("label.semester." + getCurricularPeriodOrder(), true));
	labelList.add(new GenericPair<Object, Boolean>(", ", false));
	labelList.add(new GenericPair<Object, Boolean>("label.only.students", true));
	labelList.add(new GenericPair<Object, Boolean>(" ", false));
	String evenOdd = getEven() ? "even" : "odd";
	labelList.add(new GenericPair<Object, Boolean>("label." + evenOdd, true));
	labelList.add(new GenericPair<Object, Boolean>(" ", false));
	labelList.add(new GenericPair<Object, Boolean>("label.can.be.enroled", true));

	return labelList;
    }

    @Override
    protected void removeOwnParameters() {
    }

    @Override
    public boolean appliesToContext(Context context) {
	return super.appliesToContext(context) && appliesToPeriod(context);
    }

    private boolean appliesToPeriod(Context context) {
	return context.getCurricularPeriod().hasCurricularPeriod(getAcademicPeriod(), getCurricularPeriodOrder());

    }

    public VerifyRuleExecutor createVerifyRuleExecutor() {
	return new EvenOddRuleVerifier();
    }

    public String getEvenOddString() {
	final MultiLanguageString multiLanguageString = new MultiLanguageString();
	multiLanguageString.setContent(Language.pt, ResourceBundle.getBundle("resources/AcademicAdminOffice",
		new Locale("pt", "PT")).getString("label." + (getEven() ? "even" : "odd")));
	return multiLanguageString.toString();
    }

}
