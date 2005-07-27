package net.sourceforge.fenixedu.persistenceTierJDBC.Relacional;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import net.sourceforge.fenixedu.domain.NonTeacherEmployee;
import net.sourceforge.fenixedu.persistenceTierJDBC.IFuncNaoDocentePersistente;

/**
 * @author Fernanda Quitério e Tania Pousão
 */
public class FuncNaoDocenteRelacional implements IFuncNaoDocentePersistente {

    public NonTeacherEmployee lerFuncNaoDocentePorNumMecanografico(int numMecanografico) {
        NonTeacherEmployee funcionario = null;

        try {
            PreparedStatement sql = UtilRelacional
                    .prepararComando("SELECT codigoInterno FROM ass_FUNCIONARIO "
                            + "WHERE numeroMecanografico = ?");
            sql.setInt(1, numMecanografico);
            ResultSet resultado = sql.executeQuery();
            int chaveFuncionario = 0;
            if (resultado.next()) {
                chaveFuncionario = resultado.getInt("codigoInterno");
            } else {
                sql.close();
                return null;
            }
            sql.close();

            sql = UtilRelacional
                    .prepararComando("SELECT * FROM ass_FUNC_NAO_DOCENTE WHERE chaveFuncionario = ?");
            sql.setInt(1, chaveFuncionario);

            resultado = sql.executeQuery();
            if (resultado.next()) {
                funcionario = new NonTeacherEmployee(resultado.getInt("codigoInterno"), resultado
                        .getInt("chaveFuncionario"));
            }
            sql.close();
        } catch (Exception e) {
            System.out.println("FuncNaoDocenteRelacional.lerFuncNaoDocentePorNumMecanografico: "
                    + e.toString());
        }
        return funcionario;
    }

}
