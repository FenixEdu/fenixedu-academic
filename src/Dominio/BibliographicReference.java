package Dominio;

/**
 * @author PTRLV
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class BibliographicReference implements IBibliographicReference {

	protected String title;
	protected String authors;
	protected String reference;
	protected Boolean optional;
	protected IDisciplinaExecucao executionCourse;
	protected String year;
	private Integer internalCode;
	private Integer keyExecutionCourse;

	/** Creates a new instance of ReferenciaBibliografica */
	public BibliographicReference() {
	}

	public BibliographicReference(
		IDisciplinaExecucao executionCourse,
		String title,
		String authors,
		String reference,
		String year,
		Boolean facultative) {
		setTitle(title);
		setAuthors(authors);
		setReference(reference);
		setYear(year);
		setOptional(facultative);
		setExecutionCourse(executionCourse);
	}

	public Integer getInternalCode() {
		return internalCode;
	}

	public void setInternalCode(Integer internalCode) {
		this.internalCode = internalCode;
	}

	public Integer getKeyExecutionCourse() {
		return keyExecutionCourse;
	}

	public void setKeyExecutionCourse(Integer keyExecutionCourse) {
		this.keyExecutionCourse = keyExecutionCourse;
	}

	public IDisciplinaExecucao getExecutionCourse() {
		return executionCourse;
	}

	public String getYear() {
		return year;
	}

	public String getAuthors() {
		return authors;
	}

	public Boolean getOptional() {
		return optional;
	}

	public String getReference() {
		return reference;
	}

	public String getTitle() {
		return title;
	}

	public void setExecutionCourse(IDisciplinaExecucao executionCourse) {
		this.executionCourse = executionCourse;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public void setAuthors(String author) {
		this.authors = author;
	}

	public void setOptional(Boolean facultative) {
		this.optional = facultative;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public boolean equals(Object obj) {
		boolean result = false;
		if (obj instanceof IBibliographicReference) {
			IBibliographicReference refBiblio = (IBibliographicReference) obj;
			result =
				getTitle().equals(refBiblio.getTitle())
					&& getAuthors().equals(refBiblio.getAuthors())
					&& getReference().equals(refBiblio.getReference())
					&& (getYear() == refBiblio.getYear());
		}
		return result;
	}

	//    public String toString() {
	//        String result = "[REFERENCIA BIBLIOGRAFICA";
	//        result += ", codInt=" + _codigoInterno;
	//        result += ", sitio=" + ((Curriculum)_curriculum).getSitio().getNome();
	//        result += ", titulo=" + _titulo;
	//        result += ", autor=" + _autores;
	//        result += ", ano=" + _ano;
	//        result += ", referencia=" + _referencia;
	//        result += ", facultativo=" + _facultativo;
	//        result += "]";
	//        return result;
	//    }
}
