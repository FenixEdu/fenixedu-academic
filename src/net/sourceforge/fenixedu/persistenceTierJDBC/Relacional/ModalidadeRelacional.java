package net.sourceforge.fenixedu.persistenceTierJDBC.Relacional;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.Modalidade;
import net.sourceforge.fenixedu.persistenceTierJDBC.IModalidadePersistente;

/**
 * @author Fernanda Quitério e Tania Pousão
 */
public class ModalidadeRelacional implements IModalidadePersistente {

    public boolean alterarModalidade(Modalidade modalidade) {
        boolean resultado = false;

        try {
            PreparedStatement sql = UtilRelacional.prepararComando("UPDATE ass_MODALIDADE SET "
                    + "codigoInterno = ? , " + "designacao = ? " + "WHERE designacao = ? ");

            sql.setInt(1, modalidade.getCodigoInterno());
            sql.setString(2, modalidade.getDesignacao());
            sql.setString(3, modalidade.getDesignacao());

            sql.executeUpdate();
            sql.close();
            resultado = true;
        } catch (Exception e) {
            System.out.println("ModalidadeRelacional.alterarModalidade: " + e.toString());
        }
        return resultado;
    } /* alterarModalidade */

    public boolean apagarModalidade(String designacao) {
        boolean resultado = false;

        try {
            PreparedStatement sql = UtilRelacional
                    .prepararComando("DELETE FROM ass_MODALIDADE WHERE designacao = ?");

            sql.setString(1, designacao);

            sql.executeUpdate();
            sql.close();
            resultado = true;
        } catch (Exception e) {
            System.out.println("ModalidadeRelacional.apagarModalidade: " + e.toString());
        }
        return resultado;
    } /* apagarModalidade */

    public boolean escreverModalidade(Modalidade modalidade) {
        boolean resultado = false;

        try {
            PreparedStatement sql = UtilRelacional
                    .prepararComando("INSERT INTO ass_MODALIDADE VALUES (?, ?)");

            sql.setInt(1, modalidade.getCodigoInterno());
            sql.setString(2, modalidade.getDesignacao());

            sql.executeUpdate();
            sql.close();
            resultado = true;
        } catch (Exception e) {
            System.out.println("ModalidadeRelacional.escreverModalidade: " + e.toString());
        }
        return resultado;
    } /* escreverModalidade */

    public Modalidade lerModalidade(String designacao) {
        Modalidade modalidade = null;

        try {
            PreparedStatement sql = UtilRelacional
                    .prepararComando("SELECT * FROM ass_MODALIDADE WHERE designacao = ?");

            sql.setString(1, designacao);

            ResultSet resultado = sql.executeQuery();
            if (resultado.next()) {
                modalidade = new Modalidade(resultado.getInt("codigoInterno"), resultado
                        .getString("designacao"));
            }
            sql.close();
        } catch (Exception e) {
            System.out.println("ModalidadeRelacional.lerModalidade" + e.toString());
        }
        return modalidade;
    } /* lerModalidade */

    public Modalidade lerModalidade(int codigoInterno) {
        Modalidade modalidade = null;

        try {
            PreparedStatement sql = UtilRelacional
                    .prepararComando("SELECT * FROM ass_MODALIDADE WHERE codigoInterno = ?");

            sql.setInt(1, codigoInterno);

            ResultSet resultado = sql.executeQuery();
            if (resultado.next()) {
                modalidade = new Modalidade(resultado.getInt("codigoInterno"), resultado
                        .getString("designacao"));
            }
            sql.close();
        } catch (Exception e) {
            System.out.println("ModalidadeRelacional.lerModalidade" + e.toString());
        }
        return modalidade;
    } /* lerModalidade */

    public List lerModalidades() {
        List modalidades = null;

        try {
            PreparedStatement sql = UtilRelacional.prepararComando("SELECT * FROM ass_MODALIDADE");

            ResultSet resultado = sql.executeQuery();
            modalidades = new ArrayList();
            while (resultado.next()) {
                modalidades.add(new String(resultado.getString(2)));
            }
            sql.close();
        } catch (Exception e) {
            System.out.println("ModalidadeRelacional.lerModalidades" + e.toString());
        }
        return modalidades;
    } /* lerModalidades */
}