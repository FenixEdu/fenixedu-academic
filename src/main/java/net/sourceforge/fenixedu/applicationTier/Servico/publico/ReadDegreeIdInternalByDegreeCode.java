/*
 * Created on 11/Nov/2004
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.publico;


import net.sourceforge.fenixedu.domain.Degree;
import pt.ist.fenixWebFramework.services.Service;

/**
 * @author Pedro Santos e Rita Carvalho
 */
public class ReadDegreeIdInternalByDegreeCode {

    @Service
    public static Integer run(String degreeCode) {
        Degree degree = Degree.readBySigla(degreeCode);

        return degree != null ? degree.getIdInternal() : null;
    }

}