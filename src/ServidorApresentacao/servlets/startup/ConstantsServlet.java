package ServidorApresentacao.servlets.startup;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.UnavailableException;
import javax.servlet.http.HttpServlet;

import ServidorAplicacao.Executor;
import ServidorAplicacao.Servico.person.ServicoAutorizacaoLerCargos;
import ServidorAplicacao.Servico.person.ServicoSeguroLerCargos;
import constants.assiduousness.Constants;

public final class ConstantsServlet extends HttpServlet {

    private List cargos = null;

    private List sexo = null;

    private List estadoCivil = null;

    private List dias = null;

    private Hashtable meses = null;

    private List anos = null;

    private List tipoDoc = null;

    private int debug = 0;

    public void destroy() {

        getServletContext().removeAttribute(Constants.CARGOS);
        getServletContext().removeAttribute(Constants.SEXO);
        getServletContext().removeAttribute(Constants.ESTADOCIVIL);
        getServletContext().removeAttribute(Constants.DIAS);
        getServletContext().removeAttribute(Constants.MESES);
        getServletContext().removeAttribute(Constants.ANOS);
        getServletContext().removeAttribute(Constants.TIPODOC);
    }

    public void init() throws ServletException {

        String value;
        value = getServletConfig().getInitParameter("debug");
        try {
            debug = Integer.parseInt(value);
        } catch (Throwable t) {
            debug = 0;
        }

        try {
            load();
            getServletContext().setAttribute(Constants.CARGOS, cargos);
            getServletContext().setAttribute(Constants.SEXO, sexo);
            getServletContext().setAttribute(Constants.ESTADOCIVIL, estadoCivil);
            getServletContext().setAttribute(Constants.DIAS, dias);
            getServletContext().setAttribute(Constants.MESES, meses);
            getServletContext().setAttribute(Constants.ANOS, anos);
            getServletContext().setAttribute(Constants.TIPODOC, tipoDoc);
        } catch (Exception e) {
            throw new UnavailableException("Cannot load");
        }
    }

    public int getDebug() {
        return (this.debug);
    }

    private synchronized void load() throws Exception {
        cargos = new ArrayList();
        sexo = new ArrayList();
        estadoCivil = new ArrayList();
        dias = new ArrayList();
        meses = new Hashtable();
        anos = new ArrayList();
        tipoDoc = new ArrayList();

        ServicoAutorizacaoLerCargos servicoAutorizacaoLerCargos = new ServicoAutorizacaoLerCargos();
        ServicoSeguroLerCargos servicoSeguroLerCargos = new ServicoSeguroLerCargos(
                servicoAutorizacaoLerCargos, cargos);

        Executor.getInstance().doIt(servicoSeguroLerCargos);

        cargos = servicoSeguroLerCargos.getCargos();

        sexo.add("masculino");
        sexo.add("feminino");

        estadoCivil.add("SOLTEIRO");
        estadoCivil.add("CASADO");
        estadoCivil.add("VIÚVO");
        estadoCivil.add("DIVORCIADO");

        int i = 1;
        while (i <= 31) {
            dias.add(new Integer(i));
            i++;
        }

        meses.put(new Integer(11), "DEZEMBRO");
        meses.put(new Integer(10), "NOVEMBRO");
        meses.put(new Integer(9), "OUTUBRO");
        meses.put(new Integer(8), "SETEMBRO");
        meses.put(new Integer(7), "AGOSTO");
        meses.put(new Integer(6), "JULHO");
        meses.put(new Integer(5), "JUNHO");
        meses.put(new Integer(4), "MAIO");
        meses.put(new Integer(3), "ABRIL");
        meses.put(new Integer(2), "MARÇO");
        meses.put(new Integer(1), "FEVEREIRO");
        meses.put(new Integer(0), "JANEIRO");
        Calendar calendar = Calendar.getInstance();
        i = calendar.get(Calendar.YEAR);

        while (i > 1950) {
            anos.add(new Integer(i));
            i--;
        }

        // isto deve estar numa tabela
        tipoDoc.add("BIMD");
        tipoDoc.add("BIPT");
        tipoDoc.add("PASSMD");
        tipoDoc.add("PASSPT");
    }
}