package Util;

import java.util.Calendar;

/**
 * 
 * @author Fernanda Quitério & Tania Pousão
 */
public abstract class FormataCalendar extends FenixUtil {

    /** Creates a new instance of FormataCalendar */
    public FormataCalendar() {
    }

    public static String data(Calendar calendario) {
        String data = new String();
        data = data.concat("&nbsp;");
        if (calendario.get(Calendar.DAY_OF_MONTH) < 10) {
            data = data.concat("0");
        }
        data = data.concat(String.valueOf(calendario.get(Calendar.DAY_OF_MONTH)));
        data = data.concat("-");
        if ((calendario.get(Calendar.MONTH) + 1) < 10) {
            data = data.concat("0");
        }
        data = data.concat(String.valueOf(calendario.get(Calendar.MONTH) + 1));
        data = data.concat("-");
        if (calendario.get(Calendar.YEAR) < 10) {
            data = data.concat("0");
        }
        data = data.concat(String.valueOf(calendario.get(Calendar.YEAR)));
        data = data.concat("&nbsp;");
        return data;
    } /* data */

    public static String dataHoras(Calendar calendario) {
        String data = new String();
        data = data.concat("&nbsp;");
        if (calendario.get(Calendar.DAY_OF_MONTH) < 10) {
            data = data.concat("0");
        }
        data = data.concat(String.valueOf(calendario.get(Calendar.DAY_OF_MONTH)));
        data = data.concat("-");
        if ((calendario.get(Calendar.MONTH) + 1) < 10) {
            data = data.concat("0");
        }
        data = data.concat(String.valueOf(calendario.get(Calendar.MONTH) + 1));
        data = data.concat("-");
        if (calendario.get(Calendar.YEAR) < 10) {
            data = data.concat("0");
        }
        data = data.concat(String.valueOf(calendario.get(Calendar.YEAR)));
        data = data.concat("&nbsp;");
        if (calendario.get(Calendar.HOUR_OF_DAY) < 10) {
            data = data.concat("0");
        }
        data = data.concat(String.valueOf(calendario.get(Calendar.HOUR_OF_DAY)));
        data = data.concat(":");
        if (calendario.get(Calendar.MINUTE) < 10) {
            data = data.concat("0");
        }
        data = data.concat(String.valueOf(calendario.get(Calendar.MINUTE)));
        data = data.concat(":");
        if (calendario.get(Calendar.SECOND) < 10) {
            data = data.concat("0");
        }
        data = data.concat(String.valueOf(calendario.get(Calendar.SECOND)));
        data = data.concat("&nbsp;");
        return data;
    } /* dataHoras */

    public static String horasSaldo(Calendar calendario) {
        String data = new String();
        if (calendario.getTimeInMillis() < 0) {
            data = data.concat("-");
            calendario.setTimeInMillis(-calendario.getTimeInMillis());
        } else {
            data = data.concat("&nbsp;");
        }
        //	o calendario tem sempre uma hora a mais quando se pretende a duracao,
        // entao acerta-se
        calendario.add(Calendar.HOUR_OF_DAY, -1);
        int dias = 0;
        int horas = 0;
        if ((calendario.get(Calendar.MONTH)) > 0) {
            int mesActual = calendario.get(Calendar.MONTH);
            calendario.set(Calendar.MONTH, 0);
            while (mesActual >= calendario.get(Calendar.MONTH)) {
                dias = dias + calendario.getActualMaximum(Calendar.MONTH);
                calendario.add(Calendar.MONTH, 1);
            }
        }
        dias = dias + calendario.get(Calendar.DAY_OF_MONTH) - 1;
        if (dias > 0) {
            horas = dias * 24;
        }
        horas = horas + calendario.get(Calendar.HOUR_OF_DAY);
        if (horas < 10) {
            data = data.concat("0");
        }
        data = data.concat(String.valueOf(horas));
        data = data.concat(":");
        if (calendario.get(Calendar.MINUTE) < 10) {
            data = data.concat("0");
        }
        data = data.concat(String.valueOf(calendario.get(Calendar.MINUTE)));
        data = data.concat("&nbsp;");
        return data;
    } /* horasSaldo */

    public static String horas(Calendar calendario) {
        String horas = new String();
        horas = horas.concat("&nbsp;");
        if (calendario.get(Calendar.HOUR_OF_DAY) < 10) {
            horas = horas.concat("0");
        }
        horas = horas.concat(String.valueOf(calendario.get(Calendar.HOUR_OF_DAY)));
        horas = horas.concat(":");
        if (calendario.get(Calendar.MINUTE) < 10) {
            horas = horas.concat("0");
        }
        horas = horas.concat(String.valueOf(calendario.get(Calendar.MINUTE)));
        horas = horas.concat(":");
        if (calendario.get(Calendar.SECOND) < 10) {
            horas = horas.concat("0");
        }
        horas = horas.concat(String.valueOf(calendario.get(Calendar.SECOND)));
        horas = horas.concat("&nbsp;");
        return horas;
    } /* horas */

    public static String horasMinutos(Calendar calendario) {
        String horas = new String();
        if (calendario.getTimeInMillis() < 0) {
            horas = horas.concat("-");
            calendario.setTimeInMillis(-calendario.getTimeInMillis());
        } else {
            horas = horas.concat("&nbsp;");
        }
        if (calendario.get(Calendar.HOUR_OF_DAY) < 10) {
            horas = horas.concat("0");
        }
        horas = horas.concat(String.valueOf(calendario.get(Calendar.HOUR_OF_DAY)));
        horas = horas.concat(":");
        if (calendario.get(Calendar.MINUTE) < 10) {
            horas = horas.concat("0");
        }
        horas = horas.concat(String.valueOf(calendario.get(Calendar.MINUTE)));
        horas = horas.concat("&nbsp;");
        return horas;
    } /* horasMinutos */

    public static String horasMinutosDuracao(Calendar calendario) {
        String horas = new String();
        if (calendario.getTimeInMillis() < 0) {
            horas = horas.concat("-");
            calendario.setTimeInMillis(-calendario.getTimeInMillis());
        } else {
            horas = horas.concat("&nbsp;");
        }
        //	o calendario tem sempre uma hora a mais quando se pretende a duracao,
        // entao acerta-se
        calendario.add(Calendar.HOUR_OF_DAY, -1);
        if (calendario.get(Calendar.HOUR_OF_DAY) < 10) {
            horas = horas.concat("0");
        }
        horas = horas.concat(String.valueOf(calendario.get(Calendar.HOUR_OF_DAY)));
        horas = horas.concat(":");
        if (calendario.get(Calendar.MINUTE) < 10) {
            horas = horas.concat("0");
        }
        horas = horas.concat(String.valueOf(calendario.get(Calendar.MINUTE)));
        horas = horas.concat("&nbsp;");
        return horas;
    } /* horasMinutos */
}