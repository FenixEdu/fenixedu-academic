/*
 * Created on 23/Mai/2003
 *
 * 
 */
package middleware.sigla;

import java.sql.Timestamp;

import Dominio.DomainObject;

/**
 * @author João Mota
 *
 */
public class Responsavel extends DomainObject{
	
	Integer ano_Lectivo;
	Integer codigo_Curso;
	Integer ano_Curricular;
	Integer semestre;
	Integer codigo_Ramo;
	String codigo_Disc;
	Integer no_Mec;
	Timestamp data_Act;
	String observacoes;

	/**
	 * 
	 */
	public Responsavel() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return
	 */
	public Integer getAno_Curricular() {
		return ano_Curricular;
	}

	/**
	 * @return
	 */
	public Integer getAno_Lectivo() {
		return ano_Lectivo;
	}

	/**
	 * @return
	 */
	public Integer getCodigo_Curso() {
		return codigo_Curso;
	}

	/**
	 * @return
	 */
	public String getCodigo_Disc() {
		return codigo_Disc;
	}

	/**
	 * @return
	 */
	public Integer getCodigo_Ramo() {
		return codigo_Ramo;
	}

	/**
	 * @return
	 */
	public Timestamp getData_Act() {
		return data_Act;
	}

	/**
	 * @return
	 */
	public Integer getNo_Mec() {
		return no_Mec;
	}

	/**
	 * @return
	 */
	public String getObservacoes() {
		return observacoes;
	}

	/**
	 * @return
	 */
	public Integer getSemestre() {
		return semestre;
	}

	/**
	 * @param integer
	 */
	public void setAno_Curricular(Integer integer) {
		ano_Curricular = integer;
	}

	/**
	 * @param integer
	 */
	public void setAno_Lectivo(Integer integer) {
		ano_Lectivo = integer;
	}

	/**
	 * @param integer
	 */
	public void setCodigo_Curso(Integer integer) {
		codigo_Curso = integer;
	}

	/**
	 * @param integer
	 */
	public void setCodigo_Disc(String string) {
		codigo_Disc = string;
	}

	/**
	 * @param integer
	 */
	public void setCodigo_Ramo(Integer integer) {
		codigo_Ramo = integer;
	}

	/**
	 * @param timestamp
	 */
	public void setData_Act(Timestamp timestamp) {
		data_Act = timestamp;
	}

	/**
	 * @param integer
	 */
	public void setNo_Mec(Integer integer) {
		no_Mec = integer;
	}

	/**
	 * @param string
	 */
	public void setObservacoes(String string) {
		observacoes = string;
	}

	/**
	 * @param integer
	 */
	public void setSemestre(Integer integer) {
		semestre = integer;
	}

}
