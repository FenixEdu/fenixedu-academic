package net.sourceforge.fenixedu.persistenceTierJDBC.Relacional;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import net.sourceforge.fenixedu.domain.CostCenter;
import net.sourceforge.fenixedu.persistenceTierJDBC.ICentroCustoPersistente;

/**
 * @author Fernanda Quitério e Tania Pousão
 */
public class CentroCustoRelacional implements ICentroCustoPersistente {

    public CostCenter lerCentroCusto(int codigoInterno) {
        CostCenter centroCusto = null;

        try {
            PreparedStatement sql = UtilRelacional
                    .prepararComando("SELECT * FROM ass_CENTRO_CUSTO WHERE codigoInterno = ?");

            sql.setInt(1, codigoInterno);

            ResultSet resultado = sql.executeQuery();
            if (resultado.next()) {
                centroCusto = new CostCenter(resultado.getString("sigla"), resultado
                        .getString("departamento"), resultado.getString("seccao1"), resultado
                        .getString("seccao2"));
                centroCusto.setIdInternal(new Integer(resultado.getInt("codigoInterno")));
            }
            sql.close();
        } catch (Exception e) {
            System.out.println("CentroCustoRelacional.lerCentroCusto: " + e.toString());
        }
        return centroCusto;

    } /* lerCentroCusto */

}
