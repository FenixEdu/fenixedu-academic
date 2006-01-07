/*
 * Created on 23/Jul/2003
 *
 */
package net.sourceforge.fenixedu.dataTransferObject.onlineTests;

import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.domain.onlineTests.Metadata;

/**
 * @author Susana Fernandes
 */

public class InfoMetadataWithExecutionCourse extends InfoMetadata {

    public void copyFromDomain(Metadata metadata) {
        super.copyFromDomain(metadata);
        if (metadata != null && metadata.getExecutionCourse() != null) {
            setInfoExecutionCourse(InfoExecutionCourse.newInfoFromDomain(metadata.getExecutionCourse()));
        }
    }

    public static InfoMetadata newInfoFromDomain(Metadata metadata) {
        InfoMetadataWithExecutionCourse infoMetadata = null;
        if (metadata != null) {
            infoMetadata = new InfoMetadataWithExecutionCourse();
            infoMetadata.copyFromDomain(metadata);
        }
        return infoMetadata;
    }

}