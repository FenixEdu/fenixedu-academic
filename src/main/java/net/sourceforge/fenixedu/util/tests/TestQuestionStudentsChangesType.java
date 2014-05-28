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
 * Created on Oct 22, 2003
 */
package net.sourceforge.fenixedu.util.tests;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.util.FenixUtil;

import org.apache.struts.util.LabelValueBean;

/**
 * @author Susana Fernandes
 */
public class TestQuestionStudentsChangesType extends FenixUtil {

    public static final int THIS_STUDENT = 1;

    public static final int STUDENTS_FROM_TEST_VARIATION = 2;

    public static final int STUDENTS_FROM_TEST = 3;

    public static final int ALL_STUDENTS = 4;

    public static final String THIS_STUDENT_STRING = "Apenas neste aluno";

    public static final String STUDENTS_FROM_TEST_VARIATION_STRING =
            "Todos os alunos desta ficha distribuída que tenham esta variação";

    public static final String STUDENTS_FROM_TEST_STRING = "Todos os alunos desta ficha distribuída";

    public static final String ALL_STUDENTS_STRING = "Todos os alunos que tenham esta variação";

    private Integer type;

    // private String typeString;

    public TestQuestionStudentsChangesType() {
    }

    public TestQuestionStudentsChangesType(int type) {
        this.type = new Integer(type);
    }

    public TestQuestionStudentsChangesType(Integer type) {
        this.type = type;
    }

    public TestQuestionStudentsChangesType(String typeString) {
        this.type = getTypeCode(typeString);
    }

    public Integer getType() {
        return type;
    }

    public void setType(int type) {
        this.type = new Integer(type);
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public List<LabelValueBean> getAllTypes() {
        List<LabelValueBean> result = new ArrayList<LabelValueBean>();
        result.add(new LabelValueBean(THIS_STUDENT_STRING, new Integer(THIS_STUDENT).toString()));
        result.add(new LabelValueBean(STUDENTS_FROM_TEST_VARIATION_STRING, new Integer(STUDENTS_FROM_TEST_VARIATION).toString()));
        result.add(new LabelValueBean(STUDENTS_FROM_TEST_STRING, new Integer(STUDENTS_FROM_TEST).toString()));
        result.add(new LabelValueBean(ALL_STUDENTS_STRING, new Integer(ALL_STUDENTS).toString()));
        return result;
    }

    public Integer getTypeCode(String typeName) {
        if (typeName.equals(THIS_STUDENT_STRING)) {
            return new Integer(THIS_STUDENT);
        } else if (typeName.equals(STUDENTS_FROM_TEST_VARIATION_STRING)) {
            return new Integer(STUDENTS_FROM_TEST_VARIATION);
        } else if (typeName.equals(STUDENTS_FROM_TEST_STRING)) {
            return new Integer(STUDENTS_FROM_TEST);
        } else if (typeName.equals(ALL_STUDENTS_STRING)) {
            return new Integer(ALL_STUDENTS);
        }
        return null;
    }

    public String getTypeString() {
        if (type.intValue() == THIS_STUDENT) {
            return new String(THIS_STUDENT_STRING);
        } else if (type.intValue() == STUDENTS_FROM_TEST_VARIATION) {
            return new String(STUDENTS_FROM_TEST_VARIATION_STRING);
        } else if (type.intValue() == STUDENTS_FROM_TEST) {
            return new String(STUDENTS_FROM_TEST_STRING);
        } else if (type.intValue() == ALL_STUDENTS) {
            return new String(ALL_STUDENTS_STRING);
        }
        return null;
    }

}