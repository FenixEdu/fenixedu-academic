package net.sourceforge.fenixedu.domain;

import java.util.ResourceBundle;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.util.EvaluationType;
import net.sourceforge.fenixedu.util.LanguageUtils;

public enum GradeScale {

    TYPE20 {
	@Override
	protected boolean checkFinal(final String mark) {
	    if (mark.equals(NA) || mark.equals(RE)) {
		return true;
	    }
	    try {
		Integer markValue = Integer.valueOf(mark);
		if ((markValue >= 10) && (markValue <= 20)) {
		    return true;
		} else {
		    return false;
		}
	    } catch (NumberFormatException e) {
		return false;
	    }
	}

	@Override
	protected boolean checkNotFinal(final String mark) {
	    if (mark.equals(NA) || mark.equals(RE)) {
		return true;
	    }
	    try {
		Double markValue = Double.valueOf(mark);
		if ((markValue >= 0) && (markValue <= 20)) {
		    return true;
		} else {
		    return false;
		}
	    } catch (NumberFormatException e) {
		return false;
	    }
	}
	
	@Override
	protected String qualify(final String grade) {
	    try {
		final int gradeValue = Integer.valueOf(grade);
		final ResourceBundle applicationResources = ResourceBundle.getBundle("resources.ApplicationResources", LanguageUtils.getLocale());

		if (18 <= gradeValue && gradeValue <= 20) {
		    return applicationResources.getString("label.grade.a");
		} else if (16 <= gradeValue && gradeValue <= 17) {
		    return applicationResources.getString("label.grade.b");
		} else if (14 <= gradeValue && gradeValue <= 15) {
		    return applicationResources.getString("label.grade.c");
		} else if (10 <= gradeValue && gradeValue <= 13) {
		    return applicationResources.getString("label.grade.d");
		} else {
		    throw new DomainException("GradeScale.unable.to.qualify.given.grade");
		}
	    } catch (NumberFormatException e) {
		throw new DomainException("GradeScale.unable.to.qualify.given.grade");
	    }
	}
	
    },

    TYPE5 {
	@Override
	protected boolean checkFinal(final String mark) {
	    if (mark.equals(NA) || mark.equals(RE)) {
		return true;
	    }
	    try {
		Integer markValue = Integer.valueOf(mark);
		if ((markValue >= 3) && (markValue <= 5)) {
		    return true;
		} else {
		    return false;
		}
	    } catch (NumberFormatException e) {
		return false;
	    }
	}

	@Override
	protected boolean checkNotFinal(final String mark) {
	    if (mark.equals(NA) || mark.equals(RE)) {
		return true;
	    }
	    try {
		Double markValue = Double.valueOf(mark);
		if ((markValue >= 0) && (markValue <= 20)) {
		    return true;
		} else {
		    return false;
		}
	    } catch (NumberFormatException e) {
		return false;
	    }
	}
    
	@Override
	protected String qualify(final String grade) {
	    try {
		final int gradeValue = Integer.valueOf(grade);
		final ResourceBundle applicationResources = ResourceBundle.getBundle("resources.ApplicationResources", LanguageUtils.getLocale());

		if (gradeValue == 5) {
		    return applicationResources.getString("label.grade.a");
		} else if (gradeValue == 4) {
		    return applicationResources.getString("label.grade.b");
		} else if (gradeValue == 3) {
		    return applicationResources.getString("label.grade.c");
		} else {
		    throw new DomainException("GradeScale.unable.to.qualify.given.grade");
		}
	    } catch (NumberFormatException e) {
		throw new DomainException("GradeScale.unable.to.qualify.given.grade");
	    }
	}
	
    },

    TYPEAP {
	@Override
	protected boolean checkFinal(final String mark) {
	    if (mark.equals(NA) || mark.equals(RE) || mark.equals(AP)) {
		return true;
	    }
	    return false;
	}

	@Override
	protected boolean checkNotFinal(final String mark) {
	    if (mark.equals(NA) || mark.equals(RE) || mark.equals(AP)) {
		return true;
	    }
	    return false;
	}

	@Override
	protected String qualify(final String grade) {
	    final ResourceBundle applicationResources = ResourceBundle.getBundle("resources.ApplicationResources", LanguageUtils.getLocale());
	    if (grade.equals(AP)) {
		return applicationResources.getString("msg.approved");
	    } else if (grade.equals(RE)) {
		return applicationResources.getString("msg.notApproved");
	    } else if (grade.equals(NA)) {
		return applicationResources.getString("msg.notEvaluated");
	    } else {
		throw new DomainException("GradeScale.unable.to.qualify.given.grade");
	    }
	}
    
    };

    static final public String NA = "NA";
    static final public String RE = "RE";
    static final public String AP = "AP";

    public String getName() {
	return name();
    }

    public boolean isValid(String mark, EvaluationType evaluationType) {
	mark = mark.toUpperCase();
	if (EvaluationType.FINAL_TYPE.equals(evaluationType)) {
	    return checkFinal(mark);
	} else {
	    return checkNotFinal(mark);
	}

    }

    abstract protected boolean checkFinal(final String mark);

    abstract protected boolean checkNotFinal(final String mark);

    abstract protected String qualify(final String grade);

    final public String getQualifiedName(final String grade) {
	if (checkFinal(grade)) {
	    return qualify(grade);
	} else {
	    throw new DomainException("GradeScale.unable.to.qualify.given.grade");
	}
    }
    
}
