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
package net.sourceforge.fenixedu.domain;

import java.sql.Timestamp;

/**
 * 
 * @author Fernanda Quitério e Tania Pousão
 */
public class Regime {
    private int _codigoInterno;

    private String _sigla;

    private String _designacao;

    private int _quem;

    private Timestamp _quando;

    /* Construtores */
    public Regime() {
        _codigoInterno = 0;
        _sigla = null;
        _designacao = null;
        _quem = 0;
        _quando = null;
    }

    public Regime(String designacao) {
        _codigoInterno = 0;
        _sigla = null;
        _designacao = designacao;
        _quem = 0;
        _quando = null;
    }

    public Regime(String designacao, int quem, Timestamp quando) {
        _codigoInterno = 0;
        _sigla = null;
        _designacao = designacao;
        _quem = quem;
        _quando = quando;
    }

    public Regime(int codigoInterno, String designacao) {
        _codigoInterno = codigoInterno;
        _designacao = designacao;
        _quem = 0;
        _quando = null;
    }

    public Regime(int codigoInterno, String designacao, int quem, Timestamp quando) {
        _codigoInterno = codigoInterno;
        _sigla = null;
        _designacao = designacao;
        _quem = quem;
        _quando = quando;
    }

    /* Selectores */
    public int getCodigoInterno() {
        return _codigoInterno;
    }

    public String getSigla() {
        return _sigla;
    }

    public String getDesignacao() {
        return _designacao;
    }

    public int getQuem() {
        return _quem;
    }

    public Timestamp getQuando() {
        return _quando;
    }

    /* Modificadores */
    public void setCodigoInterno(int codigoInterno) {
        _codigoInterno = codigoInterno;
    }

    public void setSigla(String sigla) {
        _sigla = sigla;
    }

    public void setDesignacao(String designacao) {
        _designacao = designacao;
    }

    public void setQuem(int quem) {
        _quem = quem;
    }

    public void setQuando(Timestamp quando) {
        _quando = quando;
    }

    /* teste da igualdade */
    @Override
    public boolean equals(Object obj) {
        boolean resultado = false;

        if (obj instanceof Regime) {
            Regime regime = (Regime) obj;

            resultado = (this.getCodigoInterno() == regime.getCodigoInterno() && this.getDesignacao() == regime.getDesignacao());
        }
        return resultado;
    }
}