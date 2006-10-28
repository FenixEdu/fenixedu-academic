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
public class MetadataComparator implements Comparator {

    private String column;

    private Integer ascendent = 1;

    public MetadataComparator(String column, String ascendent) {
        this.column = column;
        if (ascendent != null && ascendent.equals("false")) {
            this.ascendent = -1;
        }
    }

    public int compare(Object arg1, Object arg2) {
        Metadata metadata1 = (Metadata) arg1;
        Metadata metadata2 = (Metadata) arg2;

        if (column.equals("description")) {
            if (metadata1.getDescription() == null && metadata2.getDescription() != null)
                return -1 * ascendent;
            else if (metadata1.getDescription() != null && metadata2.getDescription() == null)
                return 1 * ascendent;
            else if (metadata1.getDescription() == null && metadata2.getDescription() == null)
                return 0;

            return metadata1.getDescription().compareToIgnoreCase(metadata2.getDescription())
                    * ascendent;

        } else if (column.equals("mainSubject")) {
            if (metadata1.getMainSubject() == null && metadata2.getMainSubject() != null)
                return -1 * ascendent;
            else if (metadata1.getMainSubject() != null && metadata2.getMainSubject() == null)
                return 1 * ascendent;
            else if (metadata1.getMainSubject() == null && metadata2.getMainSubject() == null)
                return 0;
            return metadata1.getMainSubject().compareToIgnoreCase(metadata2.getMainSubject())
                    * ascendent;

        } else if (column.equals("numberOfMembers")) {
            return new Integer(metadata1.getVisibleQuestions().size()).compareTo(new Integer(metadata2
                    .getVisibleQuestions().size()))
                    * ascendent;
        } else if (column.equals("difficulty")) {
            QuestionDifficultyType difficulty = new QuestionDifficultyType();

            if (!difficulty.getAllTypesStrings().contains(metadata1.getDifficulty())
                    && !difficulty.getAllTypesStrings().contains(metadata2.getDifficulty())) {
                if (metadata1.getDifficulty() == null || metadata2.getDifficulty() == null)
                    return 0;
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