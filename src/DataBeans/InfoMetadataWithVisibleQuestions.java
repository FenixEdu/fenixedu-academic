/*
 * Created on 23/Jul/2003
 *
 */
package DataBeans;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import Dominio.IMetadata;
import Dominio.IQuestion;

/**
 * @author Susana Fernandes
 */

public class InfoMetadataWithVisibleQuestions extends InfoMetadata {

	public void copyFromDomain(IMetadata metadata) {
		super.copyFromDomain(metadata);
		if (metadata != null && metadata.getVisibleQuestions() != null) {
			setVisibleQuestions((List) CollectionUtils.collect(metadata
					.getVisibleQuestions(), new Transformer() {

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