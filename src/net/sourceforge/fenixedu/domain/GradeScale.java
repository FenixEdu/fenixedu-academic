package net.sourceforge.fenixedu.domain;

import java.util.ResourceBundle;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.util.EvaluationType;
import net.sourceforge.fenixedu.util.LanguageUtils;

import org.apache.commons.lang.StringUtils;

public enum GradeScale {

    TYPE20 {
	@Override
	protected boolean checkFinal(final Grade grade) {
	    final String value = grade.getValue();
	    if (value.equals(NA) || value.equals(RE)) {
		return true;
	    }

	    try {
		final int intValue = Integer.valueOf(value);
		if ((intValue >= 10) && (intValue <= 20)) {
		    return true;
		} else {
		    return false;
		}
	    } catch (NumberFormatException e) {
		return false;
	    }
	}

	@Override
	protected boolean checkNotFinal(final Grade grade) {
	    final String value = grade.getValue();
	    if (value.equals(NA) || value.equals(RE)) {
		return true;
	    }

	    try {
		final Double doubleValue = Double.valueOf(value);
		if ((doubleValue >= 0) && (doubleValue <= 20)) {
		    return true;
		} else {
		    return false;
		}
	    } catch (NumberFormatException e) {
		return false;
	    }
	}
	
	@Override
	protected String qualify(final Grade grade) {
	    if (grade.getGradeScale() != this) {
		return StringUtils.EMPTY;
	    }
	    
	    try {
		final int intValue = Integer.valueOf(grade.getValue());
		final ResourceBundle applicationResources = ResourceBundle.getBundle("resources.ApplicationResources", LanguageUtils.getLocale());

		if (18 <= intValue && intValue <= 20) {
		    return applicationResources.getString("label.grade.a");
		} else if (16 <= intValue && intValue <= 17) {
		    return applicationResources.getString("label.grade.b");
		} else if (14 <= intValue && intValue <= 15) {
		    return applicationResources.getString("label.grade.c");
		} else if (10 <= intValue && intValue <= 13) {
		    return applicationResources.getString("label.grade.d");
		} else {
		    throw new DomainException("GradeScale.unable.to.qualify.given.grade");
		}
	    } catch (NumberFormatException e) {
		throw new DomainException("GradeScale.unable.to.qualify.given.grade");
	    }
	}
	
	@Override
	protected boolean isNotEvaluated(final Grade grade) {
	    final String value = grade.getValue();
	    return grade.isEmpty() || value.equals(GradeScale.NA);
	}
	
	@Override
	protected boolean isNotApproved(final Grade grade) {
	    final String value = grade.getValue();
	    if (value.equals(GradeScale.RE) || isNotEvaluated(grade)) {
		return true;
	    }

	    try {
		return Integer.valueOf(value) < 10;
	    } catch (NumberFormatException e) {
		return false;
	    }
	}
	
	@Override
	protected boolean isApproved(final Grade grade) {
	    final String value = grade.getValue();
	    if (value.equals(GradeScale.AP)) {
		return true;
	    }
	    
	    try {
		final int intValue = Integer.valueOf(value);
		return 10 <= intValue && intValue <= 20;
	    } catch (NumberFormatException e) {
		return false;
	    }
	}
	
	@Override
	public boolean belongsTo(final String value) {
	    if (value.equals(NA) || value.equals(RE)) {
		return true;
	    }
	    
	    try {
		final Double doubleValue = Double.valueOf(value);
		if ((doubleValue >= 0) && (doubleValue <= 20)) {
		    return true;
		} else {
		    return false;
		}
	    } catch (NumberFormatException e) {
		return false;
	    }
	}
	
    },

    TYPE5 {
	@Override
	protected boolean checkFinal(final Grade grade) {
	    final String value = grade.getValue();
	    if (value.equals(NA) || value.equals(RE)) {
		return true;
	    }
	    
	    try {
		final int intValue = Integer.valueOf(value);
		if ((intValue >= 3) && (intValue <= 5)) {
		    return true;
		} else {
		    return false;
		}
	    } catch (NumberFormatException e) {
		return false;
	    }
	}

	@Override
	protected boolean checkNotFinal(final Grade grade) {
	    final String value = grade.getValue();
	    if (value.equals(NA) || value.equals(RE)) {
		return true;
	    }
	    
	    try {
		final Double doubleValue = Double.valueOf(value);
		if ((doubleValue >= 0) && (doubleValue <= 20)) {
		    return true;
		} else {
		    return false;
		}
	    } catch (NumberFormatException e) {
		return false;
	    }
	}
    
	@Override
	protected String qualify(final Grade grade) {
	    if (grade.getGradeScale() != this) {
		return StringUtils.EMPTY;
	    }
	    
	    try {
		final int intValue = Integer.valueOf(grade.getValue());
		final ResourceBundle applicationResources = ResourceBundle.getBundle("resources.ApplicationResources", LanguageUtils.getLocale());

		if (intValue == 5) {
		    return applicationResources.getString("label.grade.a");
		} else if (intValue == 4) {
		    return applicationResources.getString("label.grade.b");
		} else if (intValue == 3) {
		    return applicationResources.getString("label.grade.c");
		} else {
		    throw new DomainException("GradeScale.unable.to.qualify.given.grade");
		}
	    } catch (NumberFormatException e) {
		throw new DomainException("GradeScale.unable.to.qualify.given.grade");
	    }
	}
	
	@Override
	protected boolean isNotEvaluated(final Grade grade) {
	    if (grade.isEmpty()) {
		return true;
	    }
	    
	    return grade.getValue().equals(GradeScale.NA);
	}
	
	@Override
	protected boolean isNotApproved(final Grade grade) {
	    final String value = grade.getValue();
	    if (value.equals(GradeScale.RE) || isNotEvaluated(grade)) {
		return true;
	    }

	    try {
		return Integer.valueOf(value) < 3;
	    } catch (NumberFormatException e) {
		return false;
	    }
	}
	
	@Override
	protected boolean isApproved(final Grade grade) {
	    try {
		final int intValue = Integer.valueOf(grade.getValue());
		return 3 <= intValue && intValue <= 5;
	    } catch (NumberFormatException e) {
		return false;
	    }
	}
	
	@Override
	public boolean belongsTo(final String value) {
	    if (value.equals(NA) || value.equals(RE)) {
		return true;
	    }

	    try {
		final Double doubleValue = Double.valueOf(value);
		if ((doubleValue >= 0) && (doubleValue <= 5)) {
		    return true;
		} else {
		    return false;
		}
	    } catch (NumberFormatException e) {
		return false;
	    }
	}

	
    },

    TYPEAP {
	@Override
	protected boolean checkFinal(final Grade grade) {
	    final String value = grade.getValue();
	    return value.equals(NA) || value.equals(RE) || value.equals(AP);
	}

	@Override
	protected boolean checkNotFinal(final Grade grade) {
	    final String value = grade.getValue();
	    return value.equals(NA) || value.equals(RE) || value.equals(AP);
	}

	@Override
	protected String qualify(final Grade grade) {
	    if (grade.getGradeScale() != this) {
		return StringUtils.EMPTY;
	    }
	    
	    final ResourceBundle applicationResources = ResourceBundle.getBundle("resources.ApplicationResources", LanguageUtils.getLocale());
	    
	    final String value = grade.getValue();
	    if (value.equals(AP)) {
		return applicationResources.getString("msg.approved");
	    } else if (value.equals(RE)) {
		return applicationResources.getString("msg.notApproved");
	    } else if (value.equals(NA)) {
		return applicationResources.getString("msg.notEvaluated");
	    } else {
		throw new DomainException("GradeScale.unable.to.qualify.given.grade");
	    }
	}
	
	@Override
	protected boolean isNotEvaluated(final Grade grade) {
	    final String value = grade.getValue();
	    return grade.isEmpty() || value.equals(GradeScale.NA);
	}
	
	@Override
	protected boolean isNotApproved(final Grade grade) {
	    final String value = grade.getValue();
	    return value.equals(GradeScale.RE) || isNotEvaluated(grade);
	}
	
	@Override
	protected boolean isApproved(final Grade grade) {
	    final String value = grade.getValue();
	    return value.equals(GradeScale.AP);
	}
	
	@Override
	public boolean belongsTo(final String value) {
	    return value.equals(NA) || value.equals(RE) || value.equals(AP);
	}
	
    },

    TYPEAPT {
	@Override
	protected boolean checkFinal(final Grade grade) {
	    final String value = grade.getValue();
	    return value.equals(NA) || value.equals(RE) || value.equals(APT);
	}

	@Override
	protected boolean checkNotFinal(final Grade grade) {
	    final String value = grade.getValue();
	    return value.equals(NA) || value.equals(RE) || value.equals(APT);
	}

	@Override
	protected String qualify(final Grade grade) {
	    if (grade.getGradeScale() != this) {
		return StringUtils.EMPTY;
	    }
	    
	    final ResourceBundle applicationResources = ResourceBundle.getBundle("resources.ApplicationResources", LanguageUtils.getLocale());
	    
	    final String value = grade.getValue();
	    if (value.equals(APT)) {
		return applicationResources.getString("msg.apt");
	    } else if (value.equals(RE)) {
		return applicationResources.getString("msg.notApproved");
	    } else if (value.equals(NA)) {
		return applicationResources.getString("msg.notEvaluated");
	    } else {
		throw new DomainException("GradeScale.unable.to.qualify.given.grade");
	    }
	}
	
	@Override
	protected boolean isNotEvaluated(final Grade grade) {
	    final String value = grade.getValue();
	    return grade.isEmpty() || value.equals(GradeScale.NA);
	}
	
	@Override
	protected boolean isNotApproved(final Grade grade) {
	    final String value = grade.getValue();
	    return value.equals(GradeScale.RE) || isNotEvaluated(grade);
	}
	
	@Override
	protected boolean isApproved(final Grade grade) {
	    final String value = grade.getValue();
	    return value.equals(GradeScale.APT);
	}
	
	@Override
	public boolean belongsTo(final String value) {
	    return value.equals(NA) || value.equals(RE) || value.equals(APT);
	}
	
    };
    
    
    static final public String NA = "NA";

    static final public String RE = "RE";
    
    static final public String AP = "AP";
    
    static final public String APT = "APT";

    public String getName() {
	return name();
    }

    public boolean isValid(final String value, final EvaluationType evaluationType) {
	try {
	    final Grade grade = Grade.createGrade(value, this);
	    if (EvaluationType.FINAL_TYPE.equals(evaluationType)) {
		return checkFinal(grade);
	    } else {
		return checkNotFinal(grade);
	    }
	} catch (DomainException de) {
	    return false;
	}
    }

    abstract protected boolean checkFinal(final Grade grade);

    abstract protected boolean checkNotFinal(final Grade grade);

    abstract protected String qualify(final Grade grade);
    
    abstract protected boolean isNotEvaluated(final Grade grade);

    abstract protected boolean isNotApproved(final Grade grade);

    abstract protected boolean isApproved(final Grade grade);
    
    abstract public boolean belongsTo(final String value);
    
    final protected boolean belongsTo(final Grade grade) {
	return belongsTo(grade.getValue());
    }
    
    final public String getQualifiedName(final String value) {
	final Grade grade = Grade.createGrade(value, this);
	
	if (isApproved(grade)) {
	    return qualify(grade);
	} else {
	    throw new DomainException("GradeScale.unable.to.qualify.given.grade");
	}
    }

}
