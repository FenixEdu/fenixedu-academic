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
package net.sourceforge.fenixedu.presentationTier.TagLib.sop.v3;

import java.util.Iterator;
import java.util.LinkedList;

import net.sourceforge.fenixedu.dataTransferObject.InfoLesson;

import org.fenixedu.spaces.domain.Space;

public class CellInfo {

    private LinkedList lessonViewList;

    // cuidado ao modificar os constructores, olhar para o getContent
    /**
     * 
     * @deprecated
     */
    @Deprecated
    public CellInfo(int rowSpan, int colSpan, InfoLesson lessonView) {
    }

    /**
     * 
     * @param rowSpan
     * @param lessonView
     * @deprecated
     */
    @Deprecated
    public CellInfo(int rowSpan, InfoLesson lessonView) {
        this(rowSpan, 0, lessonView);
    }

    public CellInfo() {
        this.lessonViewList = new LinkedList();
    }

    public void addLessonView(InfoLesson lessonView) {
        this.lessonViewList.add(lessonView);
    }

    public String getContent() {
        StringBuilder buffer = new StringBuilder("");

        if (lessonViewList.isEmpty()) {
            buffer = buffer.append("&nbsp;");
        } else {
            Iterator iterator = this.lessonViewList.iterator();
            while (iterator.hasNext()) {

                InfoLesson infoLesson = (InfoLesson) iterator.next();

                buffer = buffer.append(infoLesson.getInfoShift().getInfoDisciplinaExecucao().getSigla());
                buffer = buffer.append(" (").append(infoLesson.getInfoShift().getShiftTypesCodePrettyPrint());
                buffer = buffer.append(") ");
                final Space allocatableSpace = infoLesson.getAllocatableSpace();
                if (allocatableSpace != null) {
                    buffer = buffer.append(allocatableSpace.getName());
                }
                if (iterator.hasNext()) {
                    buffer.append("<br/>");
                }
            }
        }
        return buffer.toString();
    }

}