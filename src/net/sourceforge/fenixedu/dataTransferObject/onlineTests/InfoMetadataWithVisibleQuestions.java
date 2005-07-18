/*
 * Created on 23/Jul/2003
 *
 */
package net.sourceforge.fenixedu.dataTransferObject.onlineTests;

import java.util.List;

import net.sourceforge.fenixedu.domain.onlineTests.IMetadata;
import net.sourceforge.fenixedu.domain.onlineTests.IQuestion;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

/**
 * @author Susana Fernandes
 */

public class InfoMetadataWithVisibleQuestions extends InfoMetadata {

    public void copyFromDomain(IMetadata metadata) {
        super.copyFromDomain(metadata);
        if (metadata != null && metadata.getVisibleQuestions() != null) {
            setVisibleQuestions((List) CollectionUtils.collect(metadata.getVisibleQuestions(), new Transformer() {

                public Object transform(Object question) {
                    return InfoQuestion.newInfoFromDomain((IQuestion) question);
                }
            })

            );

        }
    }

    public static InfoMetadata newInfoFromDomain(IMetadata metadata) {
        InfoMetadataWithVisibleQuestions infoMetadata = null;
        if (metadata != null) {
            infoMetadata = new InfoMetadataWithVisibleQuestions();
            infoMetadata.copyFromDomain(metadata);
        }
        return infoMetadata;
    }

}