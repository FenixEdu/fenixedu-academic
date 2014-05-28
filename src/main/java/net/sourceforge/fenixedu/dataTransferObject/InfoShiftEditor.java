/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.fenixedu.dataTransferObject;

import java.util.List;

import net.sourceforge.fenixedu.domain.ShiftType;

public class InfoShiftEditor extends InfoObject {

    protected String nome;

    protected ShiftType tipo;

    protected List<ShiftType> tipos;

    protected Integer lotacao;

    protected String comment;

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

    public List<ShiftType> getTipos() {
        return tipos;
    }

    public void setTipos(List<ShiftType> tipos) {
        this.tipos = tipos;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

}