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
/*
 * InfoExecutionDegree.java
 *
 * Created on 24 de Novembro de 2002, 23:05
 */

package net.sourceforge.fenixedu.dataTransferObject;

/**
 * 
 * @author tfc130
 */
public class CurricularYearAndSemesterAndInfoExecutionDegree extends InfoObject {
    protected Integer _anoCurricular;

    protected Integer _semestre;

    protected InfoExecutionDegree _infoLicenciaturaExecucao;

    public CurricularYearAndSemesterAndInfoExecutionDegree() {
    }

    public CurricularYearAndSemesterAndInfoExecutionDegree(Integer anoCurricular, Integer semestre,
            InfoExecutionDegree infoLicenciaturaExecucao) {
        setAnoCurricular(anoCurricular);
        setSemestre(semestre);
        setInfoLicenciaturaExecucao(infoLicenciaturaExecucao);
    }

    public Integer getAnoCurricular() {
        return _anoCurricular;
    }

    public void setAnoCurricular(Integer anoCurricular) {
        _anoCurricular = anoCurricular;
    }

    public Integer getSemestre() {
        return _semestre;
    }

    public void setSemestre(Integer semestre) {
        _semestre = semestre;
    }

    public InfoExecutionDegree getInfoLicenciaturaExecucao() {
        return _infoLicenciaturaExecucao;
    }

    public void setInfoLicenciaturaExecucao(InfoExecutionDegree infoLicenciaturaExecucao) {
        _infoLicenciaturaExecucao = infoLicenciaturaExecucao;
    }

    @Override
    public boolean equals(Object obj) {
        boolean resultado = false;
        if (obj instanceof CurricularYearAndSemesterAndInfoExecutionDegree) {
            CurricularYearAndSemesterAndInfoExecutionDegree aCSiLE = (CurricularYearAndSemesterAndInfoExecutionDegree) obj;
            resultado =
                    (getAnoCurricular().equals(aCSiLE.getAnoCurricular())) && (getSemestre().equals(aCSiLE.getSemestre()))
                            && (getInfoLicenciaturaExecucao().equals(aCSiLE.getInfoLicenciaturaExecucao()));
        }
        return resultado;
    }

    @Override
    public String toString() {
        String result = "[INFOLICENCIATURAEXECUCAO";
        result += ", anoCurricular=" + _anoCurricular;
        result += ", semestre=" + _semestre;
        result += ", infoLicenciaturaExecucao=" + _infoLicenciaturaExecucao;
        result += "]";
        return result;
    }

}