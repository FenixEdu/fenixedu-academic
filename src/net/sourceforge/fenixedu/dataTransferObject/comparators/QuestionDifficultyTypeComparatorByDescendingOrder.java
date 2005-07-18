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
public class QuestionDifficultyTypeComparatorByDescendingOrder implements Comparator {

    public int compare(Object arg1, Object arg2) {
        InfoMetadata infoMetadata1 = (InfoMetadata) arg1;
        InfoMetadata infoMetadata2 = (InfoMetadata) arg2;

        QuestionDifficultyType difficulty = new QuestionDifficultyType();

        if (!difficulty.getAllTypesStrings().contains(infoMetadata1.getDifficulty())
                && !difficulty.getAllTypesStrings().contains(infoMetadata2.getDifficulty())) {
            if (infoMetadata1.getDifficulty() == null || infoMetadata2.getDifficulty() == null)
                return 0;
            return -infoMetadata1.getDifficulty().compareToIgnoreCase(infoMetadata2.getDifficulty());
        } else if (difficulty.getAllTypesStrings().contains(infoMetadata1.getDifficulty())
                && !difficulty.getAllTypesStrings().contains(infoMetadata2.getDifficulty()))
            return -1;
        else if (!difficulty.getAllTypesStrings().contains(infoMetadata1.getDifficulty())
                && difficulty.getAllTypesStrings().contains(infoMetadata2.getDifficulty()))
            return 1;
        return -new QuestionDifficultyType(infoMetadata1.getDifficulty()).getType().compareTo(
                new QuestionDifficultyType(infoMetadata2.getDifficulty()).getType());
    }

}