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
 * Created on Feb 18, 2004
 *
 */
package net.sourceforge.fenixedu.dataTransferObject;

import java.util.Calendar;

import net.sourceforge.fenixedu.domain.WrittenTest;
import net.sourceforge.fenixedu.util.DiaSemana;
import net.sourceforge.fenixedu.util.EvaluationType;

/**
 * @author Luis&Nuno
 * 
 */
public class InfoWrittenTest extends InfoWrittenEvaluation {

    protected String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void copyFromDomain(WrittenTest writtenTest) {
        super.copyFromDomain(writtenTest);
        if (writtenTest != null) {
            setDescription(writtenTest.getDescription());
            setEvaluationType(EvaluationType.TEST_TYPE);
        }
    }

    public static InfoWrittenTest newInfoFromDomain(WrittenTest writtenTest) {
        InfoWrittenTest infoWrittenTest = null;
        if (writtenTest != null) {
            infoWrittenTest = new InfoWrittenTest();
            infoWrittenTest.copyFromDomain(writtenTest);
        }
        return infoWrittenTest;
    }

    @Override
    public DiaSemana getDiaSemana() {
        Calendar day = this.getDay();
        return new DiaSemana(day.get(Calendar.DAY_OF_WEEK));
    }

}