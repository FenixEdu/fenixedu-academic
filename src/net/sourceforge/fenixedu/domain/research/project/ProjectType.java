/*
 * Created on Jan 26, 2006
 *	by mrsp
 */
package net.sourceforge.fenixedu.domain.research.project;

public enum ProjectType {

    INTERNAL_PROJECT, EXTERNAL_PROJECT;

    public String getName() {
	return name();
    }

    public static ProjectType getDefaultType() {
	return INTERNAL_PROJECT;
    }
}
