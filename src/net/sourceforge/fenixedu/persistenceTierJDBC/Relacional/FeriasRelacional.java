package net.sourceforge.fenixedu.persistenceTierJDBC.Relacional;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.Ferias;
import net.sourceforge.fenixedu.persistenceTierJDBC.IFeriasPersistente;

/**
 * @author Fernanda Quitério e Tania Pousão
 */
public class FeriasRelacional implements IFeriasPersistente {

    public boolean alterarFerias(Ferias ferias) {
        boolean resultado = false;

        try {
            PreparedStatement sql = UtilRelacional.prepararComando("UPDATE ass_FERIAS SET "
                    + "codigoInterno = ? , " + "chaveFuncionario = ? , " + "anoCorrente = ?, "
                    + "diasNormais = ?, " + "diasEpocaBaixa = ?, " + "diasHorasExtras = ?, "
                    + "diasAntiguidade = ?, " + "diasMeioDia = ?, " + "diasDispensaServico = ?, "
                    + "diasTolerancia = ?, " + "diasTransferidos = ?, " + "diasTransHorasExtras = ?, "
                    + "diasTransAntiguidade = ?, " + "WHERE codigoInterno = ? ");

            sql.setInt(1, ferias.getCodigoInterno());
            sql.setInt(2, ferias.getChaveFuncionario());
            sql.setInt(3, ferias.getAnoCorrente());
            sql.setInt(4, ferias.getDiasNormais());
            sql.setInt(5, ferias.getDiasEpocaBaixa());
            sql.setInt(6, ferias.getDiasHorasExtras());
            sql.setInt(7, ferias.getDiasAntiguidade());
            sql.setInt(8, ferias.getDiasMeioDia());
            sql.setInt(9, ferias.getDiasDispensaServico());
            sql.setInt(10, ferias.getDiasTolerancia());
            sql.setInt(11, ferias.getDiasTransferidos());
            sql.setInt(12, ferias.getDiasTransHorasExtras());
            sql.setInt(13, ferias.getDiasTransAntiguidade());
            sql.setInt(14, ferias.getCodigoInterno());

            sql.executeUpdate();
            sql.close();
            resultado = true;
        } catch (Exception e) {
            System.out.println("FeriasRelacional.alterarFerias: " + e.toString());
        }
        return resultado;
    } /* alterarFerias */

    public boolean apagarFerias(int codigoInterno) {
        boolean resultado = false;

        try {
            PreparedStatement sql = UtilRelacional.prepararComando("DELETE FROM ass_FERIAS "
                    + "WHERE codigoInterno = ?");

            sql.setInt(1, codigoInterno);

            sql.executeUpdate();
            sql.close();
            resultado = true;
        } catch (Exception e) {
            System.out.println("FeriasRelacional.apagarFerias: " + e.toString());
        }
        return resultado;
    } /* apagarFerias */

    public boolean escreverFerias(Ferias ferias) {
        boolean resultado = false;

        try {
            PreparedStatement sql = UtilRelacional.prepararComando("INSERT INTO ass_FERIAS "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

            sql.setInt(1, ferias.getCodigoInterno());
            sql.setInt(2, ferias.getChaveFuncionario());
            sql.setInt(3, ferias.getAnoCorrente());
            sql.setInt(4, ferias.getDiasNormais());
            sql.setInt(5, ferias.getDiasEpocaBaixa());
            sql.setInt(6, ferias.getDiasHorasExtras());
            sql.setInt(7, ferias.getDiasAntiguidade());
            sql.setInt(8, ferias.getDiasMeioDia());
            sql.setInt(9, ferias.getDiasDispensaServico());
            sql.setInt(10, ferias.getDiasTolerancia());
            sql.setInt(11, ferias.getDiasTransferidos());
            sql.setInt(12, ferias.getDiasTransHorasExtras());
            sql.setInt(13, ferias.getDiasTransAntiguidade());

            sql.executeUpdate();
            sql.close();
            resultado = true;
        } catch (Exception e) {
            System.out.println("FeriasRelacional.escreverFerias: " + e.toString());
        }
        return resultado;
    } /* escreverFerias */

    public List HistoricoFeriasPorFuncionario(int numFuncionario) {
        List historico = null;

        try {
            PreparedStatement sql = UtilRelacional
                    .prepararComando("SELECT codigoInterno FROM ass_FUNCIONARIO "
                            + "WHERE numeroMecanografico = ?");
            sql.setInt(1, numFuncionario);
            ResultSet resultado = sql.executeQuery();
            int chaveFuncionario = 0;
            if (resultado.next()) {
                chaveFuncionario = resultado.getInt("codigoInterno");
            } else {
                sql.close();
                return null;
            }
            sql.close();

            sql = UtilRelacional.prepararComando("SELECT * FROM ass_FERIAS WHERE chaveFuncionario = ?");
            sql.setInt(1, chaveFuncionario);

            resultado = sql.executeQuery();
            historico = new ArrayList();
            while (resultado.next()) {
                historico.add(new Ferias(resultado.getInt("codigoInterno"), resultado
                        .getInt("chaveFuncionario"), resultado.getInt("anoCorrente"), resultado
                        .getInt("diasNormais"), resultado.getInt("diasEpocaBaixa"), resultado
                        .getInt("diasHorasExtras"), resultado.getInt("diasAntiguidade"), resultado
                        .getInt("diasMeioDia"), resultado.getInt("diasDispensaServico"), resultado
                        .getInt("diasTolerancia"), resultado.getInt("diasTransferidos"), resultado
                        .getInt("diasTransHorasExtras"), resultado.getInt("diasTransAntiguidade")));
            }
            sql.close();
        } catch (Exception e) {
            System.out.println("FeriasRelacional.HistoricoFeriasPorFuncionario: " + e.toString());
        }
        return historico;
    } /* HistoricoFeriasPorFuncionario */

    public Ferias lerFerias(int codigoInterno) {
        Ferias ferias = null;

        try {
            PreparedStatement sql = UtilRelacional.prepararComando("SELECT * FROM ass_FERIAS "
                    + "WHERE codigoInterno = ?");

            sql.setInt(1, codigoInterno);

            ResultSet resultado = sql.executeQuery();
            if (resultado.next()) {
                ferias = new Ferias(resultado.getInt("codigoInterno"), resultado
                        .getInt("chaveFuncionario"), resultado.getInt("anoCorrente"), resultado
                        .getInt("diasNormais"), resultado.getInt("diasEpocaBaixa"), resultado
                        .getInt("diasHorasExtras"), resultado.getInt("diasAntiguidade"), resultado
                        .getInt("diasMeioDia"), resultado.getInt("diasDispensaServico"), resultado
                        .getInt("diasTolerancia"), resultado.getInt("diasTransferidos"), resultado
                        .getInt("diasTransHorasExtras"), resultado.getInt("diasTransAntiguidade"));
            }
            sql.close();
        } catch (Exception e) {
            System.out.println("FeriasRelacional.lerFerias: " + e.toString());
        }
        return ferias;
    } /* lerFerias */

    public Ferias lerFeriasAnoPorFuncionario(int ano, int numMecanografico) {
        Ferias ferias = null;

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

            sql = UtilRelacional.prepararComando("SELECT * FROM ass_FERIAS WHERE chaveFuncionario = ? "
                    + "AND ferias.anoCorrente = ?");

            sql.setInt(1, chaveFuncionario);
            sql.setInt(2, ano);

            resultado = sql.executeQuery();
            if (resultado.next()) {
                ferias = new Ferias(resultado.getInt("codigoInterno"), resultado
                        .getInt("chaveFuncionario"), resultado.getInt("anoCorrente"), resultado
                        .getInt("diasNormais"), resultado.getInt("diasEpocaBaixa"), resultado
                        .getInt("diasHorasExtras"), resultado.getInt("diasAntiguidade"), resultado
                        .getInt("diasMeioDia"), resultado.getInt("diasDispensaServico"), resultado
                        .getInt("diasTolerancia"), resultado.getInt("diasTransferidos"), resultado
                        .getInt("diasTransHorasExtras"), resultado.getInt("diasTransAntiguidade"));
            }
            sql.close();
        } catch (Exception e) {
            System.out.println("FeriasRelacional.lerFeriasAnoPorFuncionario: " + e.toString());
        }
        return ferias;
    } /* lerFeriasAnoPorFuncionario */

    public List lerFeriasPorAno(int ano) {
        List listaFerias = null;

        try {
            PreparedStatement sql = UtilRelacional.prepararComando("SELECT * FROM ass_FERIAS "
                    + "WHERE anoCorrente = ?");

            sql.setInt(1, ano);

            ResultSet resultado = sql.executeQuery();
            listaFerias = new ArrayList();
            while (resultado.next()) {
                listaFerias.add(new Ferias(resultado.getInt("codigoInterno"), resultado
                        .getInt("chaveFuncionario"), resultado.getInt("anoCorrente"), resultado
                        .getInt("diasNormais"), resultado.getInt("diasEpocaBaixa"), resultado
                        .getInt("diasHorasExtras"), resultado.getInt("diasAntiguidade"), resultado
                        .getInt("diasMeioDia"), resultado.getInt("diasDispensaServico"), resultado
                        .getInt("diasTolerancia"), resultado.getInt("diasTransferidos"), resultado
                        .getInt("diasTransHorasExtras"), resultado.getInt("diasTransAntiguidade")));
            }
            sql.close();
        } catch (Exception e) {
            System.out.println("FeriasRelacional.lerFeriasPorAno: " + e.toString());
        }
        return listaFerias;
    } /* lerFeriasPorAno */
}