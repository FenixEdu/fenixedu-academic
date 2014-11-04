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
 * Created on 17/Ago/2004
 */
package net.sourceforge.fenixedu.util;

/**
 * @author joaosa & rmalo
 */

public class ProposalState extends FenixUtil {

    public static final int CRIADOR = 1;
    public static final int ACEITE = 2;
    public static final int EM_ESPERA = 3;
    public static final int REJEITADO = 4;

    private final Integer state;

    public ProposalState(int proposal_state) {
        this.state = new Integer(proposal_state);
    }

    public ProposalState(Integer proposal_state) {
        this.state = proposal_state;
    }

    public Integer getState() {
        return this.state;
    }

    @Override
    public boolean equals(Object obj) {
        boolean resultado = false;
        if (obj instanceof ProposalState) {
            ProposalState state = (ProposalState) obj;
            resultado = (this.getState().intValue() == state.getState().intValue());
        }
        return resultado;
    }

    @Override
    public String toString() {
        int value = this.state.intValue();
        switch (value) {
        case CRIADOR:
            return "C";
        case ACEITE:
            return "A";
        case EM_ESPERA:
            return "EE";
        case REJEITADO:
            return "R";
        }
        return "Error: Invalid proposal state";
    }

    public String getSiglaProposalState() {
        int value = this.state.intValue();
        switch (value) {
        case CRIADOR:
            return "C";
        case ACEITE:
            return "A";
        case EM_ESPERA:
            return "EE";
        case REJEITADO:
            return "R";
        }
        return "Error: Invalid proposal state";
    }

    public String getFullNameProposalState() {
        int value = this.state.intValue();
        switch (value) {
        case CRIADOR:
            return "CRIADOR";
        case ACEITE:
            return "ACEITE";
        case EM_ESPERA:
            return "EM_ESPERA";
        case REJEITADO:
            return "REJEITADO";
        }
        return "Error: Invalid proposal state";
    }

}
