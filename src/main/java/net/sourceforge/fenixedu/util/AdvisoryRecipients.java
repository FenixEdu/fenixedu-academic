/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * Created on Sep 6, 2003
 *
 */
package net.sourceforge.fenixedu.util;

/**
 * @author Luis Cruz
 * 
 */
public class AdvisoryRecipients extends FenixUtil {

    public static final int STUDENTS = 1;

    public static final int TEACHERS = 2;

    public static final int EMPLOYEES = 3;

    private Integer recipients;

    /**
     *  
     */
    public AdvisoryRecipients() {
        super();
        recipients = null;
    }

    public AdvisoryRecipients(Integer recipients) {
        super();
        this.recipients = recipients;
    }

    /**
     * @return
     */
    public Integer getRecipients() {
        return recipients;
    }

    /**
     * @param integer
     */
    public void setRecipients(Integer integer) {
        recipients = integer;
    }

    /**
     * @param i
     * @return
     */
    public boolean equals(int i) {
        if (recipients != null && recipients.intValue() == i) {
            return true;
        }

        return false;
    }

}