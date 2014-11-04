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
/**
 * 
 * Autores : - Nuno Nunes (nmsn@rnl.ist.utl.pt) - Joana Mota
 * (jccm@rnl.ist.utl.pt)
 *  
 */

package net.sourceforge.fenixedu.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.struts.util.LabelValueBean;

public class SituationName extends FenixUtil {

    public static final int PENDENTE = 1;

    public static final int ADMITIDO = 2;

    public static final int SUPLENTE = 3;

    public static final int NAO_ACEITE = 4;

    public static final int DESISTIU = 5;

    public static final int SUPRA_NUMERARIO = 6;

    public static final int EXTRAORDINARIO = 7;

    public static final int DOCENTE_ENSINO_SUPERIOR = 8;

    public static final int PRE_CANDIDATO = 9;

    public static final int FALTA_CERTIFICADO = 10;

    public static final int PENDENT_COM_DADOS = 11;

    public static final int PENDENT_CONFIRMADO = 12;

    public static final int ANNULLED = 13;

    public static final int ADMITED_CONDICIONAL_CURRICULAR = 14;

    public static final int ADMITED_CONDICIONAL_FINALIST = 15;

    public static final int ADMITED_CONDICIONAL_OTHER = 16;

    public static final int SUBSTITUTE_CONDICIONAL_CURRICULAR = 17;

    public static final int SUBSTITUTE_CONDICIONAL_FINALIST = 18;

    public static final int SUBSTITUTE_CONDICIONAL_OTHER = 19;

    public static final int ADMITED_SPECIALIZATION = 20;

    public static final int ENROLLED = 21;

    public static final int PENDENT_CONDICIONAL_CURRICULAR = 22;

    public static final SituationName PENDENTE_OBJ = new SituationName(SituationName.PENDENTE);

    public static final SituationName ADMITIDO_OBJ = new SituationName(SituationName.ADMITIDO);

    public static final SituationName SUPLENTE_OBJ = new SituationName(SituationName.SUPLENTE);

    public static final SituationName NAO_ACEITE_OBJ = new SituationName(SituationName.NAO_ACEITE);

    public static final SituationName DESISTIU_OBJ = new SituationName(SituationName.DESISTIU);

    public static final SituationName SUPRA_NUMERARIO_OBJ = new SituationName(SituationName.SUPRA_NUMERARIO);

    public static final SituationName EXTRAORDINARIO_OBJ = new SituationName(SituationName.EXTRAORDINARIO);

    public static final SituationName DOCENTE_ENSINO_SUPERIOR_OBJ = new SituationName(SituationName.DOCENTE_ENSINO_SUPERIOR);

    public static final SituationName PRE_CANDIDATO_OBJ = new SituationName(SituationName.PRE_CANDIDATO);

    public static final SituationName FALTA_CERTIFICADO_OBJ = new SituationName(SituationName.FALTA_CERTIFICADO);

    public static final SituationName PENDENT_COM_DADOS_OBJ = new SituationName(SituationName.PENDENT_COM_DADOS);

    public static final SituationName PENDENT_CONFIRMADO_OBJ = new SituationName(SituationName.PENDENT_CONFIRMADO);

    public static final SituationName ANNULLED_OBJ = new SituationName(SituationName.ANNULLED);

    public static final SituationName ADMITED_CONDICIONAL_CURRICULAR_OBJ = new SituationName(
            SituationName.ADMITED_CONDICIONAL_CURRICULAR);

    public static final SituationName ADMITED_CONDICIONAL_FINALIST_OBJ = new SituationName(
            SituationName.ADMITED_CONDICIONAL_FINALIST);

    public static final SituationName ADMITED_CONDICIONAL_OTHER_OBJ = new SituationName(SituationName.ADMITED_CONDICIONAL_OTHER);

    public static final SituationName SUBSTITUTE_CONDICIONAL_CURRICULAR_OBJ = new SituationName(
            SituationName.SUBSTITUTE_CONDICIONAL_CURRICULAR);

    public static final SituationName SUBSTITUTE_CONDICIONAL_FINALIST_OBJ = new SituationName(
            SituationName.SUBSTITUTE_CONDICIONAL_FINALIST);

    public static final SituationName SUBSTITUTE_CONDICIONAL_OTHER_OBJ = new SituationName(
            SituationName.SUBSTITUTE_CONDICIONAL_OTHER);

    public static final SituationName ADMITED_SPECIALIZATION_OBJ = new SituationName(SituationName.ADMITED_SPECIALIZATION);

    public static final SituationName ENROLLED_OBJ = new SituationName(SituationName.ENROLLED);

    public static final SituationName PENDENT_CONDICIONAL_CURRICULAR_OBJ = new SituationName(
            SituationName.PENDENT_CONDICIONAL_CURRICULAR);

    public static final String PENDENTE_STRING = "Pendente";

    public static final String ADMITIDO_STRING = "Admitido";

    public static final String SUPLENTE_STRING = "Suplente";

    public static final String NAO_ACEITE_STRING = "Não Admitido";

    public static final String DESISTIU_STRING = "Desistiu";

    public static final String SUPRA_NUMERARIO_STRING = "Supra Numerário";

    public static final String EXTRAORDINARIO_STRING = "Extraordinário";

    public static final String DOCENTE_ENSINO_SUPERIOR_STRING = "Docente do Ensino Superior";

    public static final String PRE_CANDIDATO_STRING = "Pré-Candidato";

    public static final String FALTA_CERTIFICADO_STRING = "Falta Certificado";

    public static final String DEFAULT = "[Escolha uma Situação]";

    public static final String PENDENTE_COM_DADOS_STRING = "Pendente com dados preenchidos";

    public static final String PENDENTE_CONFIRMADO_STRING = "Pendente com dados confirmados";

    public static final String ANNULLED_STRING = "Anulada";

    public static final String ADMITED_CONDICIONAL_CURRICULAR_STRING = "Admitido Condicional - Apreciação Curricular";

    public static final String ADMITED_SPECIALIZATION_STRING = "Admitido Para Especialização";

    public static final String ADMITED_CONDICIONAL_FINALIST_STRING = "Admitido Condicional - Finalista";

    public static final String ADMITED_CONDICIONAL_OTHER_STRING = "Admitido Condicional - Outro";

    public static final String SUBSTITUTE_CONDICIONAL_CURRICULAR_STRING = "Suplente Condicional - Apreciação Curricular";

    public static final String SUBSTITUTE_CONDICIONAL_FINALIST_STRING = "Suplente Condicional - Finalista";

    public static final String SUBSTITUTE_CONDICIONAL_OTHER_STRING = "Suplente Condicional - Outro";

    public static final String ENROLLED_STRING = "Inscrito";

    public static final String PENDENT_CONDICIONAL_CURRICULAR_STRING = "Pendente Condicional - Apreciação Curricular";

    private final Integer situationName;

    public SituationName(int nomeSituacao) {
        this.situationName = new Integer(nomeSituacao);
    }

    public SituationName(Integer nomeSituacao) {
        this.situationName = nomeSituacao;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof SituationName) {
            SituationName aux = (SituationName) o;
            return this.situationName.equals(aux.getSituationName());
        }

        return false;

    }

    public static boolean checkIfSubstitute(String situationName) {
        List substituteList = new ArrayList();
        substituteList.add(SituationName.SUPLENTE_STRING);
        substituteList.add(SituationName.SUBSTITUTE_CONDICIONAL_CURRICULAR_STRING);
        substituteList.add(SituationName.SUBSTITUTE_CONDICIONAL_FINALIST_STRING);
        substituteList.add(SituationName.SUBSTITUTE_CONDICIONAL_OTHER_STRING);

        return substituteList.contains(situationName);
    }

    public SituationName(String nomeSituacao) {
        if (nomeSituacao.equals(SituationName.PENDENTE_STRING)) {
            this.situationName = new Integer(SituationName.PENDENTE);
        } else if (nomeSituacao.equals(SituationName.ADMITIDO_STRING)) {
            this.situationName = new Integer(SituationName.ADMITIDO);
        } else if (nomeSituacao.equals(SituationName.SUPLENTE_STRING)) {
            this.situationName = new Integer(SituationName.SUPLENTE);
        } else if (nomeSituacao.equals(SituationName.NAO_ACEITE_STRING)) {
            this.situationName = new Integer(SituationName.NAO_ACEITE);
        } else if (nomeSituacao.equals(SituationName.DESISTIU_STRING)) {
            this.situationName = new Integer(SituationName.DESISTIU);
        } else if (nomeSituacao.equals(SituationName.SUPRA_NUMERARIO_STRING)) {
            this.situationName = new Integer(SituationName.SUPRA_NUMERARIO);
        } else if (nomeSituacao.equals(SituationName.EXTRAORDINARIO_STRING)) {
            this.situationName = new Integer(SituationName.EXTRAORDINARIO);
        } else if (nomeSituacao.equals(SituationName.DOCENTE_ENSINO_SUPERIOR_STRING)) {
            this.situationName = new Integer(SituationName.DOCENTE_ENSINO_SUPERIOR);
        } else if (nomeSituacao.equals(SituationName.PRE_CANDIDATO_STRING)) {
            this.situationName = new Integer(SituationName.PRE_CANDIDATO);
        } else if (nomeSituacao.equals(SituationName.FALTA_CERTIFICADO_STRING)) {
            this.situationName = new Integer(SituationName.FALTA_CERTIFICADO);
        } else if (nomeSituacao.equals(SituationName.PENDENTE_COM_DADOS_STRING)) {
            this.situationName = new Integer(SituationName.PENDENT_COM_DADOS);
        } else if (nomeSituacao.equals(SituationName.PENDENTE_CONFIRMADO_STRING)) {
            this.situationName = new Integer(SituationName.PENDENT_CONFIRMADO);
        } else if (nomeSituacao.equals(SituationName.ANNULLED_STRING)) {
            this.situationName = new Integer(SituationName.ANNULLED);
        } else if (nomeSituacao.equals(SituationName.ADMITED_SPECIALIZATION_STRING)) {
            this.situationName = new Integer(SituationName.ADMITED_SPECIALIZATION);
        } else if (nomeSituacao.equals(SituationName.ADMITED_CONDICIONAL_CURRICULAR_STRING)) {
            this.situationName = new Integer(SituationName.ADMITED_CONDICIONAL_CURRICULAR);
        } else if (nomeSituacao.equals(SituationName.ADMITED_CONDICIONAL_FINALIST_STRING)) {
            this.situationName = new Integer(SituationName.ADMITED_CONDICIONAL_FINALIST);
        } else if (nomeSituacao.equals(SituationName.ADMITED_CONDICIONAL_OTHER_STRING)) {
            this.situationName = new Integer(SituationName.ADMITED_CONDICIONAL_OTHER);
        } else if (nomeSituacao.equals(SituationName.SUBSTITUTE_CONDICIONAL_CURRICULAR_STRING)) {
            this.situationName = new Integer(SituationName.SUBSTITUTE_CONDICIONAL_CURRICULAR);
        } else if (nomeSituacao.equals(SituationName.SUBSTITUTE_CONDICIONAL_FINALIST_STRING)) {
            this.situationName = new Integer(SituationName.SUBSTITUTE_CONDICIONAL_FINALIST);
        } else if (nomeSituacao.equals(SituationName.SUBSTITUTE_CONDICIONAL_OTHER_STRING)) {
            this.situationName = new Integer(SituationName.SUBSTITUTE_CONDICIONAL_OTHER);
        } else if (nomeSituacao.equals(SituationName.ENROLLED_STRING)) {
            this.situationName = new Integer(SituationName.ENROLLED);
        } else if (nomeSituacao.equals(SituationName.PENDENT_CONDICIONAL_CURRICULAR_STRING)) {
            this.situationName = new Integer(SituationName.PENDENT_CONDICIONAL_CURRICULAR);
        } else {
            throw new UnsupportedOperationException();
        }
    }

    @Override
    public String toString() {
        if (situationName.intValue() == SituationName.PENDENTE) {
            return SituationName.PENDENTE_STRING;
        }
        if (situationName.intValue() == SituationName.ADMITIDO) {
            return SituationName.ADMITIDO_STRING;
        }
        if (situationName.intValue() == SituationName.SUPLENTE) {
            return SituationName.SUPLENTE_STRING;
        }
        if (situationName.intValue() == SituationName.NAO_ACEITE) {
            return SituationName.NAO_ACEITE_STRING;
        }
        if (situationName.intValue() == SituationName.DESISTIU) {
            return SituationName.DESISTIU_STRING;
        }
        if (situationName.intValue() == SituationName.SUPRA_NUMERARIO) {
            return SituationName.SUPRA_NUMERARIO_STRING;
        }
        if (situationName.intValue() == SituationName.EXTRAORDINARIO) {
            return SituationName.EXTRAORDINARIO_STRING;
        }
        if (situationName.intValue() == SituationName.DOCENTE_ENSINO_SUPERIOR) {
            return SituationName.DOCENTE_ENSINO_SUPERIOR_STRING;
        }
        if (situationName.intValue() == SituationName.PRE_CANDIDATO) {
            return SituationName.PRE_CANDIDATO_STRING;
        }
        if (situationName.intValue() == SituationName.FALTA_CERTIFICADO) {
            return SituationName.FALTA_CERTIFICADO_STRING;
        }
        if (situationName.intValue() == SituationName.PENDENT_COM_DADOS) {
            return SituationName.PENDENTE_COM_DADOS_STRING;
        }
        if (situationName.intValue() == SituationName.PENDENT_CONFIRMADO) {
            return SituationName.PENDENTE_CONFIRMADO_STRING;
        }
        if (situationName.intValue() == SituationName.ANNULLED) {
            return SituationName.ANNULLED_STRING;
        }
        if (situationName.intValue() == SituationName.ADMITED_CONDICIONAL_CURRICULAR) {
            return SituationName.ADMITED_CONDICIONAL_CURRICULAR_STRING;
        }
        if (situationName.intValue() == SituationName.ADMITED_SPECIALIZATION) {
            return SituationName.ADMITED_SPECIALIZATION_STRING;
        }
        if (situationName.intValue() == SituationName.ADMITED_CONDICIONAL_FINALIST) {
            return SituationName.ADMITED_CONDICIONAL_FINALIST_STRING;
        }
        if (situationName.intValue() == SituationName.ADMITED_CONDICIONAL_OTHER) {
            return SituationName.ADMITED_CONDICIONAL_OTHER_STRING;
        }
        if (situationName.intValue() == SituationName.SUBSTITUTE_CONDICIONAL_CURRICULAR) {
            return SituationName.SUBSTITUTE_CONDICIONAL_CURRICULAR_STRING;
        }
        if (situationName.intValue() == SituationName.SUBSTITUTE_CONDICIONAL_FINALIST) {
            return SituationName.SUBSTITUTE_CONDICIONAL_FINALIST_STRING;
        }
        if (situationName.intValue() == SituationName.SUBSTITUTE_CONDICIONAL_OTHER) {
            return SituationName.SUBSTITUTE_CONDICIONAL_OTHER_STRING;
        }
        if (situationName.intValue() == SituationName.ENROLLED) {
            return SituationName.ENROLLED_STRING;
        }
        if (situationName.intValue() == SituationName.PENDENT_CONDICIONAL_CURRICULAR) {
            return SituationName.PENDENT_CONDICIONAL_CURRICULAR_STRING;
        }

        return "ERRO!"; // Nunca e atingido
    }

    public static List toArrayList() {
        List result = new ArrayList();
        result.add(new LabelValueBean(SituationName.DEFAULT, null));
        result.add(new LabelValueBean(SituationName.PENDENTE_STRING, SituationName.PENDENTE_STRING));
        result.add(new LabelValueBean(SituationName.ADMITIDO_STRING, SituationName.ADMITIDO_STRING));
        result.add(new LabelValueBean(SituationName.ADMITED_SPECIALIZATION_STRING, SituationName.ADMITED_SPECIALIZATION_STRING));
        result.add(new LabelValueBean(SituationName.ADMITED_CONDICIONAL_CURRICULAR_STRING,
                SituationName.ADMITED_CONDICIONAL_CURRICULAR_STRING));
        result.add(new LabelValueBean(SituationName.ADMITED_CONDICIONAL_FINALIST_STRING,
                SituationName.ADMITED_CONDICIONAL_FINALIST_STRING));
        result.add(new LabelValueBean(SituationName.ADMITED_CONDICIONAL_OTHER_STRING,
                SituationName.ADMITED_CONDICIONAL_OTHER_STRING));
        result.add(new LabelValueBean(SituationName.SUPLENTE_STRING, SituationName.SUPLENTE_STRING));
        result.add(new LabelValueBean(SituationName.SUBSTITUTE_CONDICIONAL_CURRICULAR_STRING,
                SituationName.SUBSTITUTE_CONDICIONAL_CURRICULAR_STRING));
        result.add(new LabelValueBean(SituationName.SUBSTITUTE_CONDICIONAL_FINALIST_STRING,
                SituationName.SUBSTITUTE_CONDICIONAL_FINALIST_STRING));
        result.add(new LabelValueBean(SituationName.SUBSTITUTE_CONDICIONAL_OTHER_STRING,
                SituationName.SUBSTITUTE_CONDICIONAL_OTHER_STRING));
        result.add(new LabelValueBean(SituationName.NAO_ACEITE_STRING, SituationName.NAO_ACEITE_STRING));
        result.add(new LabelValueBean(SituationName.DESISTIU_STRING, SituationName.DESISTIU_STRING));
        result.add(new LabelValueBean(SituationName.SUPRA_NUMERARIO_STRING, SituationName.SUPRA_NUMERARIO_STRING));
        result.add(new LabelValueBean(SituationName.EXTRAORDINARIO_STRING, SituationName.EXTRAORDINARIO_STRING));
        result.add(new LabelValueBean(SituationName.DOCENTE_ENSINO_SUPERIOR_STRING, SituationName.DOCENTE_ENSINO_SUPERIOR_STRING));
        result.add(new LabelValueBean(SituationName.PRE_CANDIDATO_STRING, SituationName.PRE_CANDIDATO_STRING));
        result.add(new LabelValueBean(SituationName.FALTA_CERTIFICADO_STRING, SituationName.FALTA_CERTIFICADO_STRING));
        result.add(new LabelValueBean(SituationName.PENDENTE_CONFIRMADO_STRING, SituationName.PENDENTE_CONFIRMADO_STRING));
        result.add(new LabelValueBean(SituationName.PENDENTE_COM_DADOS_STRING, SituationName.PENDENTE_COM_DADOS_STRING));
        result.add(new LabelValueBean(SituationName.ANNULLED_STRING, SituationName.ANNULLED_STRING));
        result.add(new LabelValueBean(SituationName.ENROLLED_STRING, SituationName.ENROLLED_STRING));
        result.add(new LabelValueBean(SituationName.PENDENT_CONDICIONAL_CURRICULAR_STRING,
                SituationName.PENDENT_CONDICIONAL_CURRICULAR_STRING));

        return result;
    }

    /**
     * Returns the situationName.
     * 
     * @return Integer
     */
    public Integer getSituationName() {
        return situationName;
    }

    @Override
    public int hashCode() {
        return getSituationName().hashCode();
    }

}