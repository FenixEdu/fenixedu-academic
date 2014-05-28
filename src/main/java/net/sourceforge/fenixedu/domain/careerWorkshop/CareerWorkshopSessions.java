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
package net.sourceforge.fenixedu.domain.careerWorkshop;

import java.util.HashMap;
import java.util.Map;

import org.joda.time.DateTime;

public enum CareerWorkshopSessions {

    /* Data from 2011
    FEB_26_11(new DateTime(2011,2,26,0,0,0,0)),
    
    MAR_05_11(new DateTime(2011,3,5,0,0,0,0)),
    
    MAR_12_11(new DateTime(2011,3,12,0,0,0,0)),
    
    MAR_19_11(new DateTime(2011,3,19,0,0,0,0)),
    
    MAR_26_11(new DateTime(2011,3,26,0,0,0,0)),
    
    APR_02_11(new DateTime(2011,4,2,0,0,0,0)),
    
    APR_09_11(new DateTime(2011,4,9,0,0,0,0)),
    
    APR_16_11(new DateTime(2011,4,16,0,0,0,0)),
    
    APR_23_11(new DateTime(2011,4,23,0,0,0,0)),
    
    APR_30_11(new DateTime(2011,4,30,0,0,0,0))
    */

    /* Data from 2012 
    FEB_11_12(new DateTime(2012,2,11,0,0,0,0)),

    FEB_18_12(new DateTime(2012,2,18,0,0,0,0)),

    FEB_25_12(new DateTime(2012,2,25,0,0,0,0)),

    MAR_03_12(new DateTime(2012,3,3,0,0,0,0)),

    MAR_10_12(new DateTime(2012,3,10,0,0,0,0)),

    MAR_17_12(new DateTime(2012,3,17,0,0,0,0)),

    MAR_24_12(new DateTime(2012,3,24,0,0,0,0))
    */

    /* Data from 2013 */
    FEB_23_13(new DateTime(2013, 2, 23, 0, 0, 0, 0)),

    MAR_02_13(new DateTime(2013, 3, 2, 0, 0, 0, 0)),

    MAR_09_13(new DateTime(2013, 3, 9, 0, 0, 0, 0)),

    MAR_16_13(new DateTime(2013, 3, 16, 0, 0, 0, 0)),

    MAR_23_13(new DateTime(2013, 3, 23, 0, 0, 0, 0)),

    MAR_30_13(new DateTime(2013, 3, 30, 0, 0, 0, 0))

    ;

    private DateTime date;

    private CareerWorkshopSessions(DateTime date) {
        this.date = date;
    }

    public DateTime getDate() {
        return date;
    }

    public String getDescription() {
        return getDate().toString("dd-MM-yyyy");
    }

    static public Map<Integer, CareerWorkshopSessions> getEmptyRankings() {
        Map<Integer, CareerWorkshopSessions> rankings = new HashMap<Integer, CareerWorkshopSessions>();
        for (int i = 0; i < getTotalOptions(); i++) {
            rankings.put(i, null);
        }
        return rankings;
    }

    static public int getTotalOptions() {
        return CareerWorkshopSessions.values().length;
    }

}
