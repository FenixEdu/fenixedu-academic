/*
 * Created on 24/Jul/2003
 *
 */
package Dominio;

/**
 * @author Susana Fernandes
 */
public class Question extends DomainObject implements IQuestion {
	private String xmlFile;
	private String xmlFileName;
	private IMetadata metadata;
	private Integer keyMetadata;

	public Question() {
	}
	public Question(Integer questionId) {
		setIdInternal(questionId);
	}
	public Integer getKeyMetadata() {
		return keyMetadata;
	}

	public IMetadata getMetadata() {
		return metadata;
	}

	public String getXmlFile() {
		return xmlFile;
	}

	public String getXmlFileName() {
		return xmlFileName;
	}

	public void setKeyMetadata(Integer integer) {
		keyMetadata = integer;
	}

	public void setMetadata(IMetadata metadata) {
		this.metadata = metadata;
	}

	public void setXmlFile(String string) {
		xmlFile = string;
	}

	public void setXmlFileName(String string) {
		xmlFileName = string;
	}
}
