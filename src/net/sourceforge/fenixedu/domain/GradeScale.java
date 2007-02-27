package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.util.EvaluationType;

public enum GradeScale {
	
	TYPE20  { 
		protected boolean checkFinal(final String mark) {
			if(mark.equals(NA) || mark.equals(RE)) {
				return true;
			}
			try {
				Integer markValue = Integer.valueOf(mark);
				if((markValue >= 10) && (markValue <= 20)) {
					return true;
				} else {
					return false;
				}
			} catch(NumberFormatException e) {
				return false;
			}
		}
		
		protected boolean checkNotFinal(final String mark) {
            if(mark.equals(NA) || mark.equals(RE)) {
                return true;
            }
			try {
				Double markValue = Double.valueOf(mark);
				if((markValue >= 0) && (markValue <= 20)) {
					return true;
				} else {
					return false;
				}
			} catch(NumberFormatException e) {
				return false;
			}
		}
	},
	
	TYPE5{
		protected boolean checkFinal(final String mark) {
			if(mark.equals(NA) || mark.equals(RE)) {
				return true;
			}
			try {
				Integer markValue = Integer.valueOf(mark);
				if((markValue >= 3) && (markValue <= 5)) {
					return true;
				} else {
					return false;
				}
			} catch(NumberFormatException e) {
				return false;
			}
		}
		
		protected boolean checkNotFinal(final String mark) {
            if(mark.equals(NA) || mark.equals(RE)) {
                return true;
            }
			try {
				Double markValue = Double.valueOf(mark);
				if((markValue >= 0) && (markValue <= 20)) {
					return true;
				} else {
					return false;
				}
			} catch(NumberFormatException e) {
				return false;
			}
		}
	},
	
	TYPEAP{
		protected boolean checkFinal(final String mark) {
			if(mark.equals(NA) || mark.equals(RE) || mark.equals(AP)) {
				return true;
			}
			return false;
		}
		
		protected boolean checkNotFinal(final String mark) {
            if(mark.equals(NA) || mark.equals(RE) || mark.equals(AP)) {
                return true;
            }
			return false;
		}
	};
	

	public static final String NA = "NA";
	public static final String RE = "RE";
	public static final String AP = "AP";
	
	public String getName() {
        return name();
    }
    
    public boolean isValid(String mark, EvaluationType	evaluationType) {
    	mark = mark.toUpperCase();
    	if(EvaluationType.FINAL_TYPE.equals(evaluationType)) {
    		return checkFinal(mark);
    	} else {
    		return checkNotFinal(mark);
    	}
    	
    }
    
    protected abstract boolean checkFinal(final String mark);
    
    protected abstract boolean checkNotFinal(final String mark);

}
