package Dominio;

/**
 * @author Susana Fernandes
 */

public class Metadata extends DomainObject implements IMetadata {
	private String MetadataFile;
	private IDisciplinaExecucao executionCourse;
	private Integer keyExecutionCourse;
	
	public Metadata() {
	}
	
	public Metadata(Integer metadataId){
		setIdInternal(metadataId);	
	}

	public IDisciplinaExecucao getExecutionCourse() {
		return executionCourse;
	}
	public Integer getKeyExecutionCourse() {
		return keyExecutionCourse;
	}

	public String getMetadataFile() {
		return MetadataFile;
	}

	public void setExecutionCourse(IDisciplinaExecucao execucao) {
		executionCourse = execucao;
	}

	public void setKeyExecutionCourse(Integer integer) {
		keyExecutionCourse = integer;
	}

	public void setMetadataFile(String string) {
		MetadataFile = string;
	}

}
