package Dominio;

import java.util.List;

public class DisciplinaExecucao implements IDisciplinaExecucao{

    private String nome;
    private String sigla;
    private String programa;
    private Double theoreticalHours;
    private Double praticalHours;
    private Double theoPratHours;
    private Double labHours;
    protected ICursoExecucao licenciaturaExecucao;

    private Integer chaveLicenciaturaExecucao;
    private Integer chaveResponsavel; 
    private Integer codigoInterno;

	private List associatedCurricularCourses = null;
	private Integer semester;
	
	private IExecutionPeriod executionPeriod;
	private Integer keyExecutionPeriod;

    /* Construtores */
    
    public DisciplinaExecucao() { }
    /**
     * @deprecated
     * @param nome
     * @param sigla
     * @param programa
     * @param licenciaturaExecucao
     * @param theoreticalHours
     * @param praticalHours
     * @param theoPratHours
     * @param labHours
     * @param executionPeriod
     */
    public DisciplinaExecucao(String nome, String sigla, String programa,
                              ICursoExecucao licenciaturaExecucao,Double theoreticalHours, 
                              Double praticalHours, Double theoPratHours, Double labHours,IExecutionPeriod executionPeriod) {
        setNome(nome);
        setSigla(sigla);
        setLicenciaturaExecucao(licenciaturaExecucao);
        setPrograma(programa);
		setChaveResponsavel(new Integer(-1));
		setTheoreticalHours(theoreticalHours);
		setPraticalHours(praticalHours);
		setTheoPratHours(theoPratHours);
		setLabHours(labHours);
		setExecutionPeriod(executionPeriod);
    }

	public DisciplinaExecucao(String nome, String sigla, String programa,
						Double theoreticalHours, 
								  Double praticalHours, Double theoPratHours, Double labHours,IExecutionPeriod executionPeriod) {
			setNome(nome);
			setSigla(sigla);
			setPrograma(programa);
			setChaveResponsavel(new Integer(-1));
			setTheoreticalHours(theoreticalHours);
			setPraticalHours(praticalHours);
			setTheoPratHours(theoPratHours);
			setLabHours(labHours);
			setExecutionPeriod(executionPeriod);
		}
    public boolean equals(Object obj) {
        boolean resultado = false;
        if (obj instanceof IDisciplinaExecucao ) {
            IDisciplinaExecucao de = (IDisciplinaExecucao) obj;
            resultado = (getNome().equals(de.getNome())) &&
                        (getSigla().equals(de.getSigla())) &&
                        (getPrograma().equals(de.getPrograma())) && 
                        (getTheoreticalHours().equals(de.getTheoreticalHours())) &&
                        (getPraticalHours().equals(de.getPraticalHours())) &&
                        (getTheoPratHours().equals(de.getTheoPratHours())) &&
                        (getSemester().equals(de.getSemester())) &&
                        (getLabHours().equals(de.getLabHours())) /*&&
                        (getLicenciaturaExecucao().getAnoLectivo().equals( de.getLicenciaturaExecucao().getAnoLectivo() ) ) &&
                        (getLicenciaturaExecucao().getSemestre().equals( de.getLicenciaturaExecucao().getSemestre() ) )&&
                        (getLicenciaturaExecucao().getLicenciatura().getNome().equals( de.getLicenciaturaExecucao().getLicenciatura().getNome() ) )&&
                        (getLicenciaturaExecucao().getLicenciatura().getSigla().equals( de.getLicenciaturaExecucao().getLicenciatura().getSigla()))*/;
        }
        return resultado;
    }    

    

	/**
	 * Returns the associatedCurricularCourses.
	 * @return List
	 */
	public List getAssociatedCurricularCourses() {
		return associatedCurricularCourses;
	}

	/**
	 * Returns the chaveLicenciaturaExecucao.
	 * @return int
	 */
	public Integer getChaveLicenciaturaExecucao() {
		return chaveLicenciaturaExecucao;
	}

	/**
	 * Returns the chaveResponsavel.
	 * @return int
	 */
	public Integer getChaveResponsavel() {
		return chaveResponsavel;
	}

	/**
	 * Returns the codigoInterno.
	 * @return Integer
	 */
	public Integer getCodigoInterno() {
		return codigoInterno;
	}

	/**
	 * Returns the licenciaturaExecucao.
	 * @return ICursoExecucao
	 */
	public ICursoExecucao getLicenciaturaExecucao() {
		return licenciaturaExecucao;
	}

	/**
	 * Returns the nome.
	 * @return String
	 */
	public String getNome() {
		return nome;
	}

	/**
	 * Returns the programa.
	 * @return String
	 */
	public String getPrograma() {
		return programa;
	}

	/**
	 * Returns the sigla.
	 * @return String
	 */
	public String getSigla() {
		return sigla;
	}

	/**
	 * Returns the theoreticalHours.
	 * @return double
	 */
	public Double getTheoreticalHours() {
		return theoreticalHours;
	}

	/**
	 * Returns the praticalHours.
	 * @return double
	 */
	public Double getPraticalHours() {
		return praticalHours;
	}

	/**
	 * Returns the theoPratHours.
	 * @return double
	 */
	public Double getTheoPratHours() {
		return theoPratHours;
	}

	/**
	 * Returns the labHours.
	 * @return double
	 */
	public Double getLabHours() {
		return labHours;
	}

	/**
	 * Sets the associatedCurricularCourses.
	 * @param associatedCurricularCourses The associatedCurricularCourses to set
	 */
	public void setAssociatedCurricularCourses(List associatedCurricularCourses) {
		this.associatedCurricularCourses = associatedCurricularCourses;
	}

	/**
	 * Sets the chaveLicenciaturaExecucao.
	 * @param chaveLicenciaturaExecucao The chaveLicenciaturaExecucao to set
	 */
	public void setChaveLicenciaturaExecucao(Integer chaveLicenciaturaExecucao) {
		this.chaveLicenciaturaExecucao = chaveLicenciaturaExecucao;
	}

	/**
	 * Sets the chaveResponsavel.
	 * @param chaveResponsavel The chaveResponsavel to set
	 */
	public void setChaveResponsavel(Integer chaveResponsavel) {
		this.chaveResponsavel = chaveResponsavel;
	}

	/**
	 * Sets the codigoInterno.
	 * @param codigoInterno The codigoInterno to set
	 */
	public void setCodigoInterno(Integer codigoInterno) {
		this.codigoInterno = codigoInterno;
	}

	/**
	 * Sets the licenciaturaExecucao.
	 * @param licenciaturaExecucao The licenciaturaExecucao to set
	 */
	public void setLicenciaturaExecucao(ICursoExecucao licenciaturaExecucao) {
		this.licenciaturaExecucao = licenciaturaExecucao;
	}

	/**
	 * Sets the nome.
	 * @param nome The nome to set
	 */
	public void setNome(String nome) {
		this.nome = nome;
	}

	/**
	 * Sets the programa.
	 * @param programa The programa to set
	 */
	public void setPrograma(String programa) {
		this.programa = programa;
	}

	/**
	 * Sets the sigla.
	 * @param sigla The sigla to set
	 */
	public void setSigla(String sigla) {
		this.sigla = sigla;
	}


	/**
	 * Sets the theoreticalHours.
	 * @param theoreticalHours The theoreticalHours to set 
	 */
	public void setTheoreticalHours(Double theoreticalHours) {
		this.theoreticalHours = theoreticalHours;
	}

	/**
	 * Sets the praticalHours.
	 * @param praticalHours The praticalHours to set
	 */
	public void setPraticalHours(Double praticalHours) {
		this.praticalHours = praticalHours;
	}

	/**
	* Sets the theoPratHours.
	 * @param theoPratHours The theoPratHours to set
	 */
	public void setTheoPratHours(Double theoPratHours) {
		this.theoPratHours = theoPratHours;
	}
	
	/**
	 * Sets the labHours.
	 * @param labHours The labHours to set
	 */
	public void setLabHours(Double labHours) {
		this.labHours = labHours;
	}
	/**
	 * @see Dominio.IDisciplinaExecucao#getSemester()
	 */
	public Integer getSemester() {
		return this.semester;
	}
	/**
	 * @see Dominio.IDisciplinaExecucao#setSemester(java.lang.Integer)
	 */
	public void setSemester(Integer semester) {
		this.semester = semester;
	}
	/**
	 * @see Dominio.IDisciplinaExecucao#getExecutionPeriod()
	 */
	public IExecutionPeriod getExecutionPeriod() {
		return this.executionPeriod;
	}
	/**
	 * @see Dominio.IDisciplinaExecucao#setExecutionPeriod(Dominio.IExecutionPeriod)
	 */
	public void setExecutionPeriod(IExecutionPeriod executionPeriod) {
		this.executionPeriod = executionPeriod;
	}


	/**
	 * Returns the keyExecutionPeriod.
	 * @return Integer
	 */
	public Integer getKeyExecutionPeriod() {
		return keyExecutionPeriod;
	}

	/**
	 * Sets the keyExecutionPeriod.
	 * @param keyExecutionPeriod The keyExecutionPeriod to set
	 */
	public void setKeyExecutionPeriod(Integer keyExecutionPeriod) {
		this.keyExecutionPeriod = keyExecutionPeriod;
	}

}