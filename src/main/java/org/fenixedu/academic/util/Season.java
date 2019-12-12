/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Academic.
 *
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * Season.java
 *
 * Created on 2003/03/26
 */

/**
 * @author Luis Cruz & Sara Ribeiro
 */
package org.fenixedu.academic.util;

import java.io.Serializable;

public class Season implements Serializable {

    public static final int SEASON1 = 1;
    public static final int SEASON2 = 2;
    public static final int SPECIAL_SEASON = 3;

    private static String[] SEASON_KEYS = { "label.firstSeason", "label.secondSeason", "label.specialSeason" };

    public static final String SEASON1_STRING = "1ª Época";
    public static final String SEASON2_STRING = "2ª Época";
    public static final String SPECIAL_SEASON_STRING = "Época Especial";
    public static final String DEFAULT = "[Escolha a Epoca]";

    public static final Season SEASON1_OBJ = new Season(SEASON1);
    public static final Season SEASON2_OBJ = new Season(SEASON2);
    public static final Season SPECIAL_SEASON_OBJ = new Season(SPECIAL_SEASON);

    private final Integer season;

    public Season(int season) {
        this.season = new Integer(season);
    }

    public Season(Integer season) {
        this.season = season;
    }

    public Season(String season) {
        if (season.equals(Season.SEASON1_STRING)) {
            this.season = Integer.valueOf(Season.SEASON1);
        } else if (season.equals(Season.SEASON2_STRING)) {
            this.season = Integer.valueOf(Season.SEASON2);
        } else if (season.equals(Season.SPECIAL_SEASON_STRING)) {
            this.season = Integer.valueOf(Season.SPECIAL_SEASON);
        } else {
            throw new UnsupportedOperationException();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Season) {
            Season aux = (Season) o;
            return this.season.equals(aux.getseason());
        }
        return false;
    }

    @Override
    public String toString() {
        if (season.intValue() == Season.SEASON1) {
            return Season.SEASON1_STRING;
        }
        if (season.intValue() == Season.SEASON2) {
            return Season.SEASON2_STRING;
        }
        if (season.intValue() == Season.SPECIAL_SEASON) {
            return Season.SPECIAL_SEASON_STRING;
        }
        return "ERROR";
    }

    public java.lang.Integer getseason() {
        return season;
    }

    public java.lang.Integer getSeason() {
        return season;
    }

    public String getKey() {
        return SEASON_KEYS[this.season - 1];
    }

    public LabelFormatter getDescription() {
        return new LabelFormatter().appendLabel(SEASON_KEYS[this.season - 1], Bundle.APPLICATION);

    }

}