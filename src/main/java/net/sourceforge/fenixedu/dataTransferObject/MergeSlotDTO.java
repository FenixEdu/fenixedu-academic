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
/**
 * 
 */
package net.sourceforge.fenixedu.dataTransferObject;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class MergeSlotDTO extends DataTranferObject {

    public static final String VALUE2 = "value2";

    public static final String VALUE1 = "value1";

    private String type;

    private String name;

    private String value1;

    private String value2;

    private String value1Link = "";

    private String value2Link = "";

    public MergeSlotDTO(String name, String type, String value1, String value2) {
        super();
        this.name = name;
        this.type = type;
        this.value1 = value1;
        this.value2 = value2;
    }

    public MergeSlotDTO(String name) {
        super();
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getValue1() {
        return value1;
    }

    public String getValue2() {
        return value2;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setValue1(String value1) {
        this.value1 = value1;
    }

    public void setValue2(String value2) {
        this.value2 = value2;
    }

    public void setValueProperty(String property, String value) {
        if (property.equals(VALUE1)) {
            setValue1(value);
        } else if (property.equals(VALUE2)) {
            setValue2(value);
        }
    }

    public void setValueLinkProperty(String property, String link) {
        if (property.equals(VALUE1)) {
            setValue1Link(link);
        } else if (property.equals(VALUE2)) {
            setValue2Link(link);
        }
    }

    public String getValue1Link() {
        return value1Link;
    }

    public void setValue1Link(String value1Link) {
        this.value1Link = value1Link;
    }

    public String getValue2Link() {
        return value2Link;
    }

    public void setValue2Link(String value2Link) {
        this.value2Link = value2Link;
    }

}
