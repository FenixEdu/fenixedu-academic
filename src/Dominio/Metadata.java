package Dominio;

/**
 * @author Susana Fernandes
 */

public class Metadata extends DomainObject implements IMetadata {
	private String MetadataFile;
	private IExecutionCourse executionCourse;
	private Integer keyExecutionCourse;
	private Boolean visibility;

	public Metadata() {
	}

	public Metadata(Integer metadataId) {
		setIdInternal(metadataId);
	}

	public IExecutionCourse getExecutionCourse() {
		return executionCourse;
	}
	
	public Integer getKeyExecutionCourse() {
		return keyExecutionCourse;
	}

	public String getMetadataFile() {
		return MetadataFile;
	}

	public void setExecutionCourse(IExecutionCourse execucao) {
		executionCourse = execucao;
	}

	public void setKeyExecutionCourse(Integer integer) {
		keyExecutionCourse = integer;
	}

	public void setMetadataFile(String string) {
		MetadataFile = string;
	}

	public Boolean getVisibility() {
		return visibility;
	}

	public void setVisibility(Boolean boolean1) {
		visibility = boolean1;
	}

}
