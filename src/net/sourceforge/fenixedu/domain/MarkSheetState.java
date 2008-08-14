/*
 * Created on Apr 19, 2006
 */
package net.sourceforge.fenixedu.domain;

public enum MarkSheetState {

    CONFIRMED,

    NOT_CONFIRMED,

    RECTIFICATION,

    RECTIFICATION_NOT_CONFIRMED;

    public String getName() {
	return name();
    }
}
