package net.sourceforge.fenixedu.persistenceTierJDBC.Relacional;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.HorarioTipo;
import net.sourceforge.fenixedu.persistenceTierJDBC.IHorarioTipoPersistente;

/**
 * @author Fernanda Quitério e Tania Pousão
 */
public class HorarioTipoRelacional implements IHorarioTipoPersistente {

    public boolean alterarHorarioTipo(HorarioTipo horarioTipo) {
        boolean resultado = false;

        try {
            PreparedStatement sql = UtilRelacional.prepararComando("UPDATE ass_HORARIO_TIPO SET "
                    + "codigoInterno = ? , " + "sigla = ? , " + "modalidade = ? , "
                    + "duracaoSemanal = ? , " + "inicioPF1 = ? , " + "fimPF1 = ? , "
                    + "inicioPF2 = ? , " + "fimPF2 = ? , " + "inicioHN1 = ? , " + "fimHN1 = ? , "
                    + "inicioHN2 = ? , " + "fimHN2 = ? " + "inicioRefeicao = ?" + "fimRefeicao = ?"
                    + "descontoObrigatorio = ?" + "descontoMinimo = ?" + "inicioExpediente = ?"
                    + "fimExpediente = ?" + "trabalhoConsecutivo = ? , " + "WHERE sigla = ? ");

            sql.setInt(1, horarioTipo.getCodigoInterno());
            sql.setString(2, horarioTipo.getSigla());
            sql.setString(3, horarioTipo.getModalidade());
            sql.setFloat(4, horarioTipo.getDuracaoSemanal());
            sql.setTimestamp(5, horarioTipo.getInicioPF1());
            sql.setTimestamp(6, horarioTipo.getFimPF1());
            sql.setTimestamp(7, horarioTipo.getInicioPF2());
            sql.setTimestamp(8, horarioTipo.getFimPF2());
            sql.setTimestamp(9, horarioTipo.getInicioHN1());
            sql.setTimestamp(10, horarioTipo.getFimHN1());
            sql.setTimestamp(11, horarioTipo.getInicioHN2());
            sql.setTimestamp(12, horarioTipo.getFimHN2());
            sql.setTimestamp(13, horarioTipo.getInicioRefeicao());
            sql.setTimestamp(14, horarioTipo.getFimRefeicao());
            sql.setTime(15, horarioTipo.getDescontoObrigatorioRefeicao());
            sql.setTime(16, horarioTipo.getIntervaloMinimoRefeicao());
            sql.setTimestamp(17, horarioTipo.getInicioExpediente());
            sql.setTimestamp(18, horarioTipo.getFimExpediente());
            sql.setTime(19, horarioTipo.getTrabalhoConsecutivo());
            sql.setString(20, horarioTipo.getSigla());

            sql.executeUpdate();
            sql.close();
            resultado = true;
        } catch (Exception e) {
            System.out.println("HorarioTipoRelacional.alterarHorarioTipo: " + e.toString());
        }
        return resultado;
    } /* alterarHorarioTipo */

    public boolean apagarHorarioTipo(String sigla) {
        boolean resultado = false;

        try {
            PreparedStatement sql = UtilRelacional.prepararComando("DELETE FROM ass_HORARIO_TIPO "
                    + "WHERE sigla = ?");

            sql.setString(1, sigla);

            sql.executeUpdate();
            sql.close();
            resultado = true;
        } catch (Exception e) {
            System.out.println("HorarioTipoRelacional.apagarHorarioTipo: " + e.toString());
        }
        return resultado;
    } /* apagarHorarioTipo */

    public boolean associarHorarioTipoRegime(int chaveHorarioTipo, int chaveRegime) {
        boolean resultado = false;

        try {
            PreparedStatement sql = UtilRelacional.prepararComando("INSERT INTO "
                    + "ass_HORARIOTIPO_REGIME VALUES (?, ?)");

            sql.setInt(1, chaveHorarioTipo);
            sql.setInt(2, chaveRegime);

            sql.executeUpdate();
            sql.close();
            resultado = true;
        } catch (Exception e) {
            System.out.println("HorarioTipoRelacional.associarHorarioTipoRegime: " + e.toString());
        }
        return resultado;
    } /* associarHorarioTipoRegime */

    public boolean escreverHorarioTipo(HorarioTipo horarioTipo) {
        boolean resultado = false;

        try {
            PreparedStatement sql = UtilRelacional.prepararComando("INSERT INTO ass_HORARIO_TIPO "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

            sql.setInt(1, horarioTipo.getCodigoInterno());
            sql.setString(2, horarioTipo.getSigla());
            sql.setString(3, horarioTipo.getModalidade());
            sql.setFloat(4, horarioTipo.getDuracaoSemanal());
            sql.setTimestamp(5, horarioTipo.getInicioPF1());
            sql.setTimestamp(6, horarioTipo.getFimPF1());
            sql.setTimestamp(7, horarioTipo.getInicioPF2());
            sql.setTimestamp(8, horarioTipo.getFimPF2());
            sql.setTimestamp(9, horarioTipo.getInicioHN1());
            sql.setTimestamp(10, horarioTipo.getFimHN1());
            sql.setTimestamp(11, horarioTipo.getInicioHN2());
            sql.setTimestamp(12, horarioTipo.getFimHN2());
            sql.setTimestamp(13, horarioTipo.getInicioRefeicao());
            sql.setTimestamp(14, horarioTipo.getFimRefeicao());
            sql.setTime(15, horarioTipo.getDescontoObrigatorioRefeicao());
            sql.setTime(16, horarioTipo.getIntervaloMinimoRefeicao());
            sql.setTimestamp(17, horarioTipo.getInicioExpediente());
            sql.setTimestamp(18, horarioTipo.getFimExpediente());
            sql.setTime(19, horarioTipo.getTrabalhoConsecutivo());

            sql.executeUpdate();
            sql.close();
            resultado = true;
        } catch (Exception e) {
            System.out.println("HorarioTipoRelacional.escreverHorarioTipo: " + e.toString());
        }
        return resultado;
    } /* escreverHorarioTipo */

    public HorarioTipo lerHorarioTipo(String sigla) {
        HorarioTipo horarioTipo = null;

        try {
            PreparedStatement sql = UtilRelacional.prepararComando("SELECT * FROM ass_HORARIO_TIPO "
                    + "WHERE sigla = ?");

            sql.setString(1, sigla);

            ResultSet resultado = sql.executeQuery();
            if (resultado.next()) {
                Time descontoObrigatorio = null;
                Time descontoMinimo = null;
                Time trabalhoConsecutivo = null;
                if (resultado.getTime("descontoObrigatorio") != null
                        && resultado.getTime("descontoMinimo") != null) {
                    //				duracao, logo adiciona-se uma hora
                    descontoObrigatorio = new Time(
                            resultado.getTime("descontoObrigatorio").getTime() + 3600 * 1000);
                    descontoMinimo = new Time(
                            resultado.getTime("descontoMinimo").getTime() + 3600 * 1000);
                }
                if (resultado.getTime("trabalhoConsecutivo") != null) {
                    trabalhoConsecutivo = new Time(
                            resultado.getTime("trabalhoConsecutivo").getTime() + 3600 * 1000);
                }

                horarioTipo = new HorarioTipo(resultado.getInt("codigoInterno"), resultado
                        .getString("sigla"), resultado.getString("modalidade"), resultado
                        .getFloat("duracaoSemanal"), resultado.getTimestamp("inicioPF1"), resultado
                        .getTimestamp("fimPF1"), resultado.getTimestamp("inicioPF2"), resultado
                        .getTimestamp("fimPF2"), resultado.getTimestamp("inicioHN1"), resultado
                        .getTimestamp("fimHN1"), resultado.getTimestamp("inicioHN2"), resultado
                        .getTimestamp("fimHN2"), resultado.getTimestamp("inicioRefeicao"), resultado
                        .getTimestamp("fimRefeicao"), descontoObrigatorio, descontoMinimo, resultado
                        .getTimestamp("inicioExpediente"), resultado.getTimestamp("fimExpediente"),
                        trabalhoConsecutivo);
            }
            sql.close();
        } catch (Exception e) {
            System.out.println("HorarioTipoRelacional.lerHorarioTipo" + e.toString());
        }
        return horarioTipo;
    } /* lerHorarioTipo */

    public HorarioTipo lerHorarioTipo(int codigoInterno) {
        HorarioTipo horarioTipo = null;

        try {
            PreparedStatement sql = UtilRelacional.prepararComando("SELECT * FROM ass_HORARIO_TIPO "
                    + "WHERE codigoInterno = ?");

            sql.setInt(1, codigoInterno);

            ResultSet resultado = sql.executeQuery();
            if (resultado.next()) {
                Time descontoObrigatorio = null;
                Time descontoMinimo = null;
                Time trabalhoConsecutivo = null;
                if (resultado.getTime("descontoObrigatorio") != null
                        && resultado.getTime("descontoMinimo") != null) {
                    //				duracao, logo adiciona-se uma hora
                    descontoObrigatorio = new Time(
                            resultado.getTime("descontoObrigatorio").getTime() + 3600 * 1000);
                    descontoMinimo = new Time(
                            resultado.getTime("descontoMinimo").getTime() + 3600 * 1000);
                }
                if (resultado.getTime("trabalhoConsecutivo") != null) {
                    trabalhoConsecutivo = new Time(
                            resultado.getTime("trabalhoConsecutivo").getTime() + 3600 * 1000);
                }

                horarioTipo = new HorarioTipo(resultado.getInt("codigoInterno"), resultado
                        .getString("sigla"), resultado.getString("modalidade"), resultado
                        .getFloat("duracaoSemanal"), resultado.getTimestamp("inicioPF1"), resultado
                        .getTimestamp("fimPF1"), resultado.getTimestamp("inicioPF2"), resultado
                        .getTimestamp("fimPF2"), resultado.getTimestamp("inicioHN1"), resultado
                        .getTimestamp("fimHN1"), resultado.getTimestamp("inicioHN2"), resultado
                        .getTimestamp("fimHN2"), resultado.getTimestamp("inicioRefeicao"), resultado
                        .getTimestamp("fimRefeicao"), descontoObrigatorio, descontoMinimo, resultado
                        .getTimestamp("inicioExpediente"), resultado.getTimestamp("fimExpediente"),
                        trabalhoConsecutivo);
            }

            sql.close();
        } catch (Exception e) {
            System.out.println("HorarioTipoRelacional.lerHorarioTipo: " + e.toString());
        }
        return horarioTipo;
    } /* lerHorarioTipo */

    public List lerHorariosTipo() {
        List horariosTipo = null;

        try {
            PreparedStatement sql = UtilRelacional
                    .prepararComando("SELECT * FROM ass_HORARIO_TIPO ORDER BY modalidade");

            ResultSet resultado = sql.executeQuery();
            horariosTipo = new ArrayList();
            while (resultado.next()) {
                Time descontoObrigatorio = null;
                Time descontoMinimo = null;
                Time trabalhoConsecutivo = null;

                if (resultado.getTime("descontoObrigatorio") != null
                        && resultado.getTime("descontoMinimo") != null) {
                    // duracao, logo adiciona-se uma hora
                    descontoObrigatorio = new Time(
                            resultado.getTime("descontoObrigatorio").getTime() + 3600 * 1000);
                    descontoMinimo = new Time(
                            resultado.getTime("descontoMinimo").getTime() + 3600 * 1000);
                }
                if (resultado.getTime("trabalhoConsecutivo") != null) {
                    trabalhoConsecutivo = new Time(
                            resultado.getTime("trabalhoConsecutivo").getTime() + 3600 * 1000);
                }

                horariosTipo.add(new HorarioTipo(resultado.getInt("codigoInterno"), resultado
                        .getString("sigla"), resultado.getString("modalidade"), resultado
                        .getFloat("duracaoSemanal"), resultado.getTimestamp("inicioPF1"), resultado
                        .getTimestamp("fimPF1"), resultado.getTimestamp("inicioPF2"), resultado
                        .getTimestamp("fimPF2"), resultado.getTimestamp("inicioHN1"), resultado
                        .getTimestamp("fimHN1"), resultado.getTimestamp("inicioHN2"), resultado
                        .getTimestamp("fimHN2"), resultado.getTimestamp("inicioRefeicao"), resultado
                        .getTimestamp("fimRefeicao"), descontoObrigatorio, descontoMinimo, resultado
                        .getTimestamp("inicioExpediente"), resultado.getTimestamp("fimExpediente"),
                        trabalhoConsecutivo));
            }
            sql.close();
        } catch (Exception e) {
            System.out.println("HorarioTipoRelacional.lerHorariosTipo: " + e.toString());
            return null;
        }
        return horariosTipo;
    } /* lerHorariosTipo */

    public List lerHorariosTipoSemTurnos() {
        List horariosTipo = null;

        try {
            PreparedStatement sql = UtilRelacional.prepararComando("SELECT * FROM ass_HORARIO_TIPO "
                    + "WHERE modalidade <> \"turnos\" ORDER BY modalidade");

            ResultSet resultado = sql.executeQuery();
            horariosTipo = new ArrayList();
            while (resultado.next()) {
                Time descontoObrigatorio = null;
                Time descontoMinimo = null;
                Time trabalhoConsecutivo = null;

                if (resultado.getTime("descontoObrigatorio") != null
                        && resultado.getTime("descontoMinimo") != null) {
                    //duracao, logo adiciona-se uma hora
                    descontoObrigatorio = new Time(
                            resultado.getTime("descontoObrigatorio").getTime() + 3600 * 1000);
                    descontoMinimo = new Time(
                            resultado.getTime("descontoMinimo").getTime() + 3600 * 1000);
                }
                if (resultado.getTime("trabalhoConsecutivo") != null) {
                    trabalhoConsecutivo = new Time(
                            resultado.getTime("trabalhoConsecutivo").getTime() + 3600 * 1000);
                }

                horariosTipo.add(new HorarioTipo(resultado.getInt("codigoInterno"), resultado
                        .getString("sigla"), resultado.getString("modalidade"), resultado
                        .getFloat("duracaoSemanal"), resultado.getTimestamp("inicioPF1"), resultado
                        .getTimestamp("fimPF1"), resultado.getTimestamp("inicioPF2"), resultado
                        .getTimestamp("fimPF2"), resultado.getTimestamp("inicioHN1"), resultado
                        .getTimestamp("fimHN1"), resultado.getTimestamp("inicioHN2"), resultado
                        .getTimestamp("fimHN2"), resultado.getTimestamp("inicioRefeicao"), resultado
                        .getTimestamp("fimRefeicao"), descontoObrigatorio, descontoMinimo, resultado
                        .getTimestamp("inicioExpediente"), resultado.getTimestamp("fimExpediente"),
                        trabalhoConsecutivo));
            }
            sql.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("HorarioTipoRelacional.lerHorariosTipo: " + e.toString());
            return null;
        }
        return horariosTipo;
    } /* lerHorariosTipoSemTurnos */

    public List lerHorarioTipoRegime(int chaveHorarioTipo) {
        List horarioRegime = null;

        try {
            PreparedStatement sql = UtilRelacional
                    .prepararComando("SELECT * FROM ass_HORARIOTIPO_REGIME "
                            + "WHERE chaveHorarioTipo = ?");

            sql.setInt(1, chaveHorarioTipo);

            ResultSet resultado = sql.executeQuery();
            horarioRegime = new ArrayList();
            while (resultado.next()) {
                horarioRegime.add(new Integer(resultado.getInt("chaveRegime")));
            }
            sql.close();
        } catch (Exception e) {
            System.out.println("HorarioTipoRelacional.lerHorarioTipoRegime: " + e.toString());
            return null;
        }
        return horarioRegime;
    } /* lerHorarioTipoRegime */

    public List lerRegimes(int chaveHorario) {
        List listaRegimes = null;

        try {
            PreparedStatement sql = UtilRelacional
                    .prepararComando("SELECT * FROM ass_HORARIOTIPO_REGIME "
                            + "WHERE chaveHorarioTipo = ?");

            sql.setInt(1, chaveHorario);

            ResultSet resultado = sql.executeQuery();
            listaRegimes = new ArrayList();
            while (resultado.next()) {
                listaRegimes.add(new Integer(resultado.getInt("chaveRegime")));
            }
            sql.close();
        } catch (Exception e) {
            System.out.println("HorarioTipoRelacional.lerRegimes: " + e.toString());
            return null;
        }
        return listaRegimes;
    } /* lerRegimes */

    public List lerTodosHorariosTipoComRegime(int chaveRegime) {
        List listaHorarios = null;

        try {
            PreparedStatement sql = UtilRelacional
                    .prepararComando("SELECT ass_HORARIO_TIPO.codigoInterno FROM ass_HORARIO_TIPO "
                            + "LEFT JOIN ass_HORARIOTIPO_REGIME "
                            + "ON (ass_HORARIO_TIPO.codigoInterno = ass_HORARIOTIPO_REGIME.chaveHorarioTipo)"
                            + "WHERE ass_HORARIOTIPO_REGIME.chaveRegime = ?");

            sql.setInt(1, chaveRegime);

            ResultSet resultado = sql.executeQuery();

            listaHorarios = new ArrayList();
            while (resultado.next()) {
                listaHorarios.add(new Integer(resultado.getInt(1)));
            }
            sql.close();
        } catch (Exception e) {
            System.out.println("HorarioTipoRelacional.lerTodosHorariosTipoComRegime: " + e.toString());
            return null;
        }
        return listaHorarios;
    } /* lerTodosHorariosTipoComRegime */
}