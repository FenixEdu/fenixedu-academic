package net.sourceforge.fenixedu.dataTransferObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.ShiftType;

public class InfoShiftEditor extends InfoObject {

    protected String nome;

    protected ShiftType tipo;

    protected Integer lotacao;

    protected Integer ocupation;

    protected Double percentage;

    protected Integer availabilityFinal;

    protected InfoExecutionCourse infoDisciplinaExecucao;

    protected List<InfoLesson> infoLessons;

    protected List infoClasses;

    public InfoShiftEditor() {
    }

	public Integer getAvailabilityFinal() {
		return availabilityFinal;
	}

	public void setAvailabilityFinal(Integer availabilityFinal) {
		this.availabilityFinal = availabilityFinal;
	}

	public List getInfoClasses() {
		return infoClasses;
	}

	public void setInfoClasses(List infoClasses) {
		this.infoClasses = infoClasses;
	}

	public InfoExecutionCourse getInfoDisciplinaExecucao() {
		return infoDisciplinaExecucao;
	}

	public void setInfoDisciplinaExecucao(InfoExecutionCourse infoDisciplinaExecucao) {
		this.infoDisciplinaExecucao = infoDisciplinaExecucao;
	}

	public List<InfoLesson> getInfoLessons() {
		return infoLessons;
	}

	public void setInfoLessons(List<InfoLesson> infoLessons) {
		this.infoLessons = infoLessons;
	}

	public Integer getLotacao() {
		return lotacao;
	}

	public void setLotacao(Integer lotacao) {
		this.lotacao = lotacao;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Integer getOcupation() {
		return ocupation;
	}

	public void setOcupation(Integer ocupation) {
		this.ocupation = ocupation;
	}

	public Double getPercentage() {
		return percentage;
	}

	public void setPercentage(Double percentage) {
		this.percentage = percentage;
	}

	public ShiftType getTipo() {
		return tipo;
	}

	public void setTipo(ShiftType tipo) {
		this.tipo = tipo;
	}

}