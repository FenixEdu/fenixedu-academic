/*
 * Created on 5/Mar/2004
 */
package net.sourceforge.fenixedu.dataTransferObject.comparators;

import java.util.Comparator;

import net.sourceforge.fenixedu.dataTransferObject.onlineTests.InfoMetadata;
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
        InfoMetadata infoMetadata1 = (InfoMetadata) arg1;
        InfoMetadata infoMetadata2 = (InfoMetadata) arg2;

        if (column.equals("description")) {
            if (infoMetadata1.getDescription() == null && infoMetadata2.getDescription() != null)
                return -1 * ascendent;
            else if (infoMetadata1.getDescription() != null && infoMetadata2.getDescription() == null)
                return 1 * ascendent;
            else if (infoMetadata1.getDescription() == null && infoMetadata2.getDescription() == null)
                return 0;

            return infoMetadata1.getDescription().compareToIgnoreCase(infoMetadata2.getDescription()) * ascendent;

        } else if (column.equals("mainSubject")) {
            if (infoMetadata1.getMainSubject() == null && infoMetadata2.getMainSubject() != null)
                return -1 * ascendent;
            else if (infoMetadata1.getMainSubject() != null && infoMetadata2.getMainSubject() == null)
                return 1 * ascendent;
            else if (infoMetadata1.getMainSubject() == null && infoMetadata2.getMainSubject() == null)
                return 0;
            return infoMetadata1.getMainSubject().compareToIgnoreCase(infoMetadata2.getMainSubject()) * ascendent;

        } else if (column.equals("numberOfMembers")) {
            return infoMetadata1.getNumberOfMembers().compareTo(infoMetadata2.getNumberOfMembers()) * ascendent;
        } else if (column.equals("difficulty")) {
            QuestionDifficultyType difficulty = new QuestionDifficultyType();

            if (!difficulty.getAllTypesStrings().contains(infoMetadata1.getDifficulty())
                    && !difficulty.getAllTypesStrings().contains(infoMetadata2.getDifficulty())) {
                if (infoMetadata1.getDifficulty() == null || infoMetadata2.getDifficulty() == null)
                    return 0;
                return infoMetadata1.getDifficulty().compareToIgnoreCase(infoMetadata2.getDifficulty());
            } else if (difficulty.getAllTypesStrings().contains(infoMetadata1.getDifficulty())
                    && !difficulty.getAllTypesStrings().contains(infoMetadata2.getDifficulty())) {
                return -1 * ascendent;
            } else if (!difficulty.getAllTypesStrings().contains(infoMetadata1.getDifficulty())
                    && difficulty.getAllTypesStrings().contains(infoMetadata2.getDifficulty())) {
                return 1 * ascendent;
            }
            return new QuestionDifficultyType(infoMetadata1.getDifficulty()).getType().compareTo(
                    new QuestionDifficultyType(infoMetadata2.getDifficulty()).getType())
                    * ascendent;
        }
        return 0;
    }
}