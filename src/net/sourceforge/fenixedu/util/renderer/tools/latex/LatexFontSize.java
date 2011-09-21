package net.sourceforge.fenixedu.util.renderer.tools.latex;

public enum LatexFontSize {
	TINY("tiny"), 
	SCRIPTSIZE("scriptsize"), 
	FOOTNOTESIZE("footnotesize"), 
	SMALL("small"), 
	NORMALISE("normalise"), 
	LARGE("large"), 
	HUGE("huge");

	String size;

	LatexFontSize(String size) {
		this.size = size;
	}

}
