package net.sourceforge.fenixedu.presentationTier.formbeans.assiduousness;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import net.sourceforge.fenixedu.constants.assiduousness.Constants;

/**
 * 
 * @author Fernanda Quitério & Tania Pousão
 */
public class ConsultarMarcacaoPontoForm extends ActionForm {

    private List _listaFuncionarios = new ArrayList();

    private List _listaCartoes = new ArrayList();

    private List _listaEstados = new ArrayList();

    private String _diaInicio = null;

    private String _mesInicio = null;

    private String _anoInicio = null;

    private String _diaFim = null;

    private String _mesFim = null;

    private String _anoFim = null;

    private String[] _funcionariosEscolhidos = null;

    private String[] _cartoesEscolhidos = null;

    private String[] _estadosEscolhidos = null;

    public List getListaFuncionarios() {
        return (_listaFuncionarios);
    }

    public List getListaCartoes() {
        return (_listaCartoes);
    }

    public List getListaEstados() {
        return (_listaEstados);
    }

    public String getDiaInicio() {
        return (_diaInicio);
    }

    public String getMesInicio() {
        return (_mesInicio);
    }

    public String getAnoInicio() {
        return (_anoInicio);
    }

    public String getDiaFim() {
        return (_diaFim);
    }

    public String getMesFim() {
        return (_mesFim);
    }

    public String getAnoFim() {
        return (_anoFim);
    }

    public String[] getFuncionariosEscolhidos() {
        return (_funcionariosEscolhidos);
    }

    public String[] getCartoesEscolhidos() {
        return (_cartoesEscolhidos);
    }

    public String[] getEstadosEscolhidos() {
        return _estadosEscolhidos;
    }

    public void setListaFuncionarios(List listaFuncionarios) {
        _listaFuncionarios = listaFuncionarios;
    }

    public void setListaCartoes(List listaCartoes) {
        _listaCartoes = listaCartoes;
    }

    public void setListaEstados(List listaEstados) {
        _listaEstados = listaEstados;
    }

    public void setDiaInicio(String diaInicio) {
        _diaInicio = diaInicio;
    }

    public void setMesInicio(String mesInicio) {
        _mesInicio = mesInicio;
    }

    public void setAnoInicio(String anoInicio) {
        _anoInicio = anoInicio;
    }

    public void setDiaFim(String diaFim) {
        _diaFim = diaFim;
    }

    public void setMesFim(String mesFim) {
        _mesFim = mesFim;
    }

    public void setAnoFim(String anoFim) {
        _anoFim = anoFim;
    }

    public void setFuncionariosEscolhidos(String[] funcionariosEscolhidos) {
        _funcionariosEscolhidos = funcionariosEscolhidos;
    }

    public void setCartoesEscolhidos(String[] cartoesEscolhidos) {
        _cartoesEscolhidos = cartoesEscolhidos;
    }

    public void setEstadosEscolhidos(String[] estadosEscolhidos) {
        _estadosEscolhidos = estadosEscolhidos;
    }

    public void setForm(Date dataInicialConsulta, Date dataFinalConsulta, List listaFuncionarios,
            List listaCartoes) {
        setListaFuncionarios(listaFuncionarios);
        setListaCartoes(listaCartoes);

        List listaEstados = new ArrayList();
        listaEstados.add("valida");
        listaEstados.add("regularizada");
        listaEstados.add("cartaoFuncionarioInvalido");
        listaEstados.add("cartaoSubstitutoInvalido");
        listaEstados.add("cartaoDesconhecido");
        listaEstados.add("erroLeitura");
        setListaEstados(listaEstados);

        _estadosEscolhidos = new String[] { "valida" };

        Calendar calendario = Calendar.getInstance();
        calendario.setLenient(false);
        calendario.clear();
        calendario.setTimeInMillis(dataInicialConsulta.getTime());
        setDiaInicio(String.valueOf(calendario.get(Calendar.DAY_OF_MONTH)));
        setMesInicio(String.valueOf(calendario.get(Calendar.MONTH) + 1));
        setAnoInicio(String.valueOf(calendario.get(Calendar.YEAR)));

        calendario.clear();
        calendario.setTimeInMillis(dataFinalConsulta.getTime());
        setDiaFim(String.valueOf(calendario.get(Calendar.DAY_OF_MONTH)));
        setMesFim(String.valueOf(calendario.get(Calendar.MONTH) + 1));
        setAnoFim(String.valueOf(calendario.get(Calendar.YEAR)));
    }

    public void reset(ActionMapping mapping, HttpServletRequest request) {
        _diaInicio = "";
        _mesInicio = "";
        _anoInicio = "";

        _estadosEscolhidos = new String[] { "valida" };

        Calendar agora = Calendar.getInstance();
        setDiaFim((new Integer(agora.get(Calendar.DAY_OF_MONTH))).toString());
        setMesFim((new Integer(agora.get(Calendar.MONTH) + 1)).toString());
        setAnoFim((new Integer(agora.get(Calendar.YEAR))).toString());
    }

    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();

        Calendar calendar = Calendar.getInstance();
        calendar.setLenient(false);

        if ((getAnoInicio().length() > 0) && (getMesInicio().length() > 0)
                && (getDiaInicio().length() > 0) && (getAnoFim().length() > 0)
                && (getMesFim().length() > 0) && (getDiaFim().length() > 0)) {
            try {
                (new Integer(getDiaInicio())).intValue();
                (new Integer(getMesInicio())).intValue();
                (new Integer(getAnoInicio())).intValue();
                (new Integer(getDiaFim())).intValue();
                (new Integer(getMesFim())).intValue();
                (new Integer(getAnoFim())).intValue();
            } catch (java.lang.NumberFormatException e) {
                errors.add("numero", new ActionError("error.numero.naoInteiro"));
            }
            try {
                Calendar calendarInicio = Calendar.getInstance();
                calendarInicio.setLenient(false);
                calendarInicio.set((new Integer(getAnoInicio())).intValue(),
                        (new Integer(getMesInicio())).intValue() - 1, (new Integer(getDiaInicio()))
                                .intValue(), 00, 00, 00);
                Date dataInicio = calendarInicio.getTime();

                Calendar calendarFim = Calendar.getInstance();
                calendarFim.setLenient(false);
                calendarFim.set((new Integer(getAnoFim())).intValue(), (new Integer(getMesFim()))
                        .intValue() - 1, (new Integer(getDiaFim())).intValue(), 00, 00, 00);
                Date dataFim = calendarFim.getTime();

                if (!(dataInicio.getTime() <= dataFim.getTime())) {
                    if (!((calendarInicio.get(Calendar.DAY_OF_MONTH) == calendarFim
                            .get(Calendar.DAY_OF_MONTH))
                            && (calendarInicio.get(Calendar.MONTH) == calendarFim.get(Calendar.MONTH)) && (calendarInicio
                            .get(Calendar.YEAR) == (calendarFim.get(Calendar.YEAR) - 1)))) {
                        errors.add("data", new ActionError("error.dataConsulta.invalida"));
                    }
                } else {
                    HttpSession session = request.getSession();
                    session.setAttribute(Constants.INICIO_CONSULTA, new Date(calendarInicio
                            .getTimeInMillis()));
                    session
                            .setAttribute(Constants.FIM_CONSULTA,
                                    new Date(calendarFim.getTimeInMillis()));
                }
            } catch (java.lang.IllegalArgumentException ee) {
                errors.add("data", new ActionError("error.dataConsulta.invalida"));
            }
        } else {
            errors.add("data", new ActionError("error.dataConsulta.obrigatoria"));
        }
        return errors;
    }
}