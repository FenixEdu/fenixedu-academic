/*
 * Created on Sep 16, 2004
 */
package net.sourceforge.fenixedu.domain.credits.event;

import org.apache.commons.lang.enums.ValuedEnum;

public class CreditsEvent extends ValuedEnum {

    public static final int SUPPORT_LESSONS_VALUE = 1;

    public static final int LESSONS_VALUE = 2;

    public static final int MASTER_DEGREE_LESSONS_VALUE = 3;

    public static final int OTHER_CREDIT_VALUE = 4;

    public static final int MANAGEMENT_POSITION_VALUE = 5;

    public static final int SERVICE_EXEMPTION_VALUE = 6;

    public static final int WORKING_TIME_VALUE = 7;

    public static final int DEGREE_FINAL_PROJECT_STUDENT_VALUE = 8;

    public static final CreditsEvent SUPPORT_LESSONS = new CreditsEvent("sp", SUPPORT_LESSONS_VALUE);

    public static final CreditsEvent LESSONS = new CreditsEvent("lessons", LESSONS_VALUE);

    public static final CreditsEvent MASTER_DEGREE_LESSONS = new CreditsEvent("master",
            MASTER_DEGREE_LESSONS_VALUE);

    public static final CreditsEvent OTHER_CREDIT = new CreditsEvent("others", OTHER_CREDIT_VALUE);

    public static final CreditsEvent MANAGEMENT_POSITION = new CreditsEvent("management position",
            MANAGEMENT_POSITION_VALUE);

    public static final CreditsEvent SERVICE_EXEMPTION = new CreditsEvent("service exemption",
            SERVICE_EXEMPTION_VALUE);

    public static final CreditsEvent WORKING_TIME = new CreditsEvent("working time", WORKING_TIME_VALUE);

    public static final CreditsEvent DEGREE_FINAL_PROJECT_STUDENT = new CreditsEvent(
            "degree final project", DEGREE_FINAL_PROJECT_STUDENT_VALUE);

    /**
     * @param arg0
     */
    private CreditsEvent(String name, int value) {
        super(name, value);
    }

}