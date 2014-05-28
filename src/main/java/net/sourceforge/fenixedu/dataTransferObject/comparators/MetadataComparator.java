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
 * Created on 5/Mar/2004
 */
package net.sourceforge.fenixedu.dataTransferObject.comparators;

import java.util.Comparator;

import net.sourceforge.fenixedu.domain.onlineTests.Metadata;
import net.sourceforge.fenixedu.util.tests.QuestionDifficultyType;

/**
 * @author Susana Fernandes
 */
public class MetadataComparator implements Comparator<Metadata> {

    private final String column;

    private Integer ascendent = 1;

    public MetadataComparator(String column, String ascendent) {
        this.column = column;
        if (ascendent != null && ascendent.equals("false")) {
            this.ascendent = -1;
        }
    }

    @Override
    public int compare(Metadata metadata1, Metadata metadata2) {

        if (column.equals("description")) {
            if (metadata1.getDescription() == null && metadata2.getDescription() != null) {
                return -1 * ascendent;
            } else if (metadata1.getDescription() != null && metadata2.getDescription() == null) {
                return 1 * ascendent;
            } else if (metadata1.getDescription() == null && metadata2.getDescription() == null) {
                return 0;
            }

            return metadata1.getDescription().compareToIgnoreCase(metadata2.getDescription()) * ascendent;

        } else if (column.equals("mainSubject")) {
            if (metadata1.getMainSubject() == null && metadata2.getMainSubject() != null) {
                return -1 * ascendent;
            } else if (metadata1.getMainSubject() != null && metadata2.getMainSubject() == null) {
                return 1 * ascendent;
            } else if (metadata1.getMainSubject() == null && metadata2.getMainSubject() == null) {
                return 0;
            }
            return metadata1.getMainSubject().compareToIgnoreCase(metadata2.getMainSubject()) * ascendent;

        } else if (column.equals("numberOfMembers")) {
            return Integer.valueOf(metadata1.getVisibleQuestions().size()).compareTo(
                    Integer.valueOf(metadata2.getVisibleQuestions().size()))
                    * ascendent;
        } else if (column.equals("difficulty")) {
            QuestionDifficultyType difficulty = new QuestionDifficultyType();

            if (!difficulty.getAllTypesStrings().contains(metadata1.getDifficulty())
                    && !difficulty.getAllTypesStrings().contains(metadata2.getDifficulty())) {
                if (metadata1.getDifficulty() == null || metadata2.getDifficulty() == null) {
                    return 0;
                }
                return metadata1.getDifficulty().compareToIgnoreCase(metadata2.getDifficulty());
            } else if (difficulty.getAllTypesStrings().contains(metadata1.getDifficulty())
                    && !difficulty.getAllTypesStrings().contains(metadata2.getDifficulty())) {
                return -1 * ascendent;
            } else if (!difficulty.getAllTypesStrings().contains(metadata1.getDifficulty())
                    && difficulty.getAllTypesStrings().contains(metadata2.getDifficulty())) {
                return 1 * ascendent;
            }
            return new QuestionDifficultyType(metadata1.getDifficulty()).getType().compareTo(
                    new QuestionDifficultyType(metadata2.getDifficulty()).getType())
                    * ascendent;
        }
        return 0;
    }
}