package net.sourceforge.fenixedu.util.renderer.tools.latex;

public enum LatexFontSize {
    	TINY("tiny"), 
    	SCRIPTSIZE("scriptsize"), 
    	FOOTNOTESIZE("footnotesize"), 
    	SMALL("small"), 
    	NORMALSIZE("normalsize"), 
    	LARGE("large"),
    	LARGER("Large"),
 LARGEST("LARGE"),
    	HUGE("huge");

	String size;

	LatexFontSize(String size) {
	this.size = size;
    }

}
