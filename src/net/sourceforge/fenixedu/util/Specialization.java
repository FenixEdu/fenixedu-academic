/*
 * Specialization.java
 * 
 * Created on 18 de Novembro de 2002, 17:32
 */

/**
 * 
 * Authors : - Nuno Nunes (nmsn@rnl.ist.utl.pt) - Joana Mota
 * (jccm@rnl.ist.utl.pt)
 *  
 */

package net.sourceforge.fenixedu.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.struts.util.LabelValueBean;

public class Specialization extends FenixUtil {

    public static final int MESTRADO = 1;

    //    public static final int INTEGRADO = 2;

    public static final int ESPECIALIZACAO = 3;

    public static final String MESTRADO_STRING = "Mestrado";

    //    public static final String INTEGRADO_STRING = "Integrado";

    public static final String ESPECIALIZACAO_STRING = "Especialização";

    public static final String DEFAULT = "";

    public static final Specialization MESTRADO_TYPE = new Specialization(MESTRADO);

    //    public static final Specialization INTEGRADO_TYPE = new
    // Specialization(INTEGRADO);

    public static final Specialization ESPECIALIZACAO_TYPE = new Specialization(ESPECIALIZACAO);

    private Integer specialization;

    /** Creates a new instance of Especializacao */
    public Specialization() {
    }

    public Specialization(int especializacao) {
        this.specialization = new Integer(especializacao);
    }

    public Specialization(Integer especializacao) {
        this.specialization = especializacao;
    }

    public Specialization(String nomeEspecializacao) {
        if (nomeEspecializacao == null) {
            this.specialization = null;
        } else if (nomeEspecializacao.equals(Specialization.MESTRADO_STRING)) {
            this.specialization = new Integer(Specialization.MESTRADO);
            //        } else if
            // (nomeEspecializacao.equals(Specialization.INTEGRADO_STRING)) {
            //            this.specialization = new Integer(Specialization.INTEGRADO);
        } else if (nomeEspecializacao.equals(Specialization.ESPECIALIZACAO_STRING)) {
            this.specialization = new Integer(Specialization.ESPECIALIZACAO);
        }
    }

    public static List toArrayList() {
        List result = new ArrayList();
        result.add(new LabelValueBean(Specialization.DEFAULT, null));
        result.add(new LabelValueBean(Specialization.MESTRADO_STRING, Specialization.MESTRADO_STRING));
        //        result.add(new LabelValueBean(Specialization.INTEGRADO_STRING,
        // Specialization.INTEGRADO_STRING));
        result.add(new LabelValueBean(Specialization.ESPECIALIZACAO_STRING,
                Specialization.ESPECIALIZACAO_STRING));
        return result;
    }

    public static List toArrayListWithoutDefault() {
        List result = new ArrayList();
        result.add(new LabelValueBean(Specialization.MESTRADO_STRING, Specialization.MESTRADO_STRING));
        //        result.add(new LabelValueBean(Specialization.INTEGRADO_STRING,
        // Specialization.INTEGRADO_STRING));
        result.add(new LabelValueBean(Specialization.ESPECIALIZACAO_STRING,
                Specialization.ESPECIALIZACAO_STRING));
        return result;
    }

    public static List toIntValueArrayList() {
        List result = new ArrayList();
        result.add(new LabelValueBean(Specialization.MESTRADO_STRING, Specialization.MESTRADO + ""));
        result.add(new LabelValueBean(Specialization.ESPECIALIZACAO_STRING,
                Specialization.ESPECIALIZACAO + ""));
        return result;
    }

    public boolean equals(Object o) {
        if (o instanceof Specialization) {
            Specialization aux = (Specialization) o;
            return this.specialization.equals(aux.getSpecialization());
        }

        return false;

    }

    public String toString() {
        if (specialization.intValue() == Specialization.MESTRADO)
            return Specialization.MESTRADO_STRING;
        //        if (specialization.intValue() == Specialization.INTEGRADO)
        //            return Specialization.INTEGRADO_STRING;
        if (specialization.intValue() == Specialization.ESPECIALIZACAO)
            return Specialization.ESPECIALIZACAO_STRING;
        return "ERRO!"; // Nunca e atingido
    }

    /**
     * Returns the especializacao.
     * 
     * @return Integer
     */
    public Integer getSpecialization() {
        return specialization;
    }

    /**
     * Sets the especializacao.
     * 
     * @param especializacao
     *            The especializacao to set
     */
    public void setSpecialization(Integer especializacao) {
        this.specialization = especializacao;
    }
    
    public String getName(){
        if (specialization.intValue() == Specialization.MESTRADO)
            return "MESTRADO";
        if (specialization.intValue() == Specialization.ESPECIALIZACAO)
            return "ESPECIALIZACAO";
        return "ERRO!"; // Nunca e atingido
    }
    

}