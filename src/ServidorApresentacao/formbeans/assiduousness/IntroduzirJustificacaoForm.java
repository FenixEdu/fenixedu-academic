package ServidorApresentacao.formbeans.assiduousness;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import Dominio.IStrategyJustificacoes;
import Dominio.ParamJustificacao;
import Dominio.SuporteStrategyJustificacoes;

/**
 *
 * @author  Fernanda Quitério & Tania Pousão
 */
public class IntroduzirJustificacaoForm extends ActionForm
{
    private String _numMecanografico = null;
    private String _siglaJustificacao = null;

    private String _horaInicio = null;
    private String _minutosInicio = null;
    private String _diaInicio = null;
    private String _mesInicio = null;
    private String _anoInicio = null;

    private String _horaFim = null;
    private String _minutosFim = null;
    private String _diaFim = null;
    private String _mesFim = null;
    private String _anoFim = null;

    private String _observacao = null;

    private ArrayList _listaSiglasJustificacao = new ArrayList();

    public String getNumMecanografico()
    {
        return (_numMecanografico);
    }
    public String getSiglaJustificacao()
    {
        return (_siglaJustificacao);
    }
    public String getHoraInicio()
    {
        return (_horaInicio);
    }
    public String getMinutosInicio()
    {
        return (_minutosInicio);
    }
    public String getDiaInicio()
    {
        return (_diaInicio);
    }
    public String getMesInicio()
    {
        return (_mesInicio);
    }
    public String getAnoInicio()
    {
        return (_anoInicio);
    }
    public String getHoraFim()
    {
        return (_horaFim);
    }
    public String getMinutosFim()
    {
        return (_minutosFim);
    }
    public String getDiaFim()
    {
        return (_diaFim);
    }
    public String getMesFim()
    {
        return (_mesFim);
    }
    public String getAnoFim()
    {
        return (_anoFim);
    }
    public String getObservacao()
    {
        return (_observacao);
    }
    public ArrayList getListaSiglasJustificacao()
    {
        return (_listaSiglasJustificacao);
    }

    public void setNumMecanografico(String numMecanografico)
    {
        _numMecanografico = numMecanografico;
    }
    public void setSiglaJustificacao(String siglaJustificacao)
    {
        _siglaJustificacao = siglaJustificacao;
    }
    public void setHoraInicio(String horaInicio)
    {
        _horaInicio = horaInicio;
    }
    public void setMinutosInicio(String minutosInicio)
    {
        _minutosInicio = minutosInicio;
    }
    public void setDiaInicio(String diaInicio)
    {
        _diaInicio = diaInicio;
    }
    public void setMesInicio(String mesInicio)
    {
        _mesInicio = mesInicio;
    }
    public void setAnoInicio(String anoInicio)
    {
        _anoInicio = anoInicio;
    }
    public void setHoraFim(String horaFim)
    {
        _horaFim = horaFim;
    }
    public void setMinutosFim(String minutosFim)
    {
        _minutosFim = minutosFim;
    }
    public void setDiaFim(String diaFim)
    {
        _diaFim = diaFim;
    }
    public void setMesFim(String mesFim)
    {
        _mesFim = mesFim;
    }
    public void setAnoFim(String anoFim)
    {
        _anoFim = anoFim;
    }
    public void setObservacao(String observacao)
    {
        _observacao = observacao;
    }
    public void setListaSiglasJustificacao(ArrayList listaSiglasJustificacao)
    {
        _listaSiglasJustificacao = listaSiglasJustificacao;
    }

    public void setForm(ArrayList listaSiglasJustificacao, String sigla)
    {
        setListaSiglasJustificacao(listaSiglasJustificacao);
        if (sigla == null)
        {
            setSiglaJustificacao((String) _listaSiglasJustificacao.get(0));
        } else
        {
            setSiglaJustificacao(sigla);
        }
    }

    public void reset(ActionMapping mapping, HttpServletRequest request)
    {
        setNumMecanografico("");
        //setSiglaJustificacao((String)_listaSiglasJustificacao.get(0));
        setHoraInicio("");
        setMinutosInicio("");
        setDiaInicio("");
        setMesInicio("");
        setAnoInicio("");
        setHoraFim("");
        setMinutosFim("");
        setDiaFim("");
        setMesFim("");
        setAnoFim("");
        setObservacao("");
    }

    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request)
    {

        ActionErrors errors = new ActionErrors();

        if ((getNumMecanografico() != null) && (getNumMecanografico().length() < 1))
        {
            errors.add("numeros", new ActionError("error.campos.obrigatorio"));
        } else
        {
            try
            {
                (new Integer(getNumMecanografico())).intValue();
            } catch (java.lang.NumberFormatException e)
            {
                errors.add("numero", new ActionError("error.numero.naoInteiro"));
            }
            HttpSession session = request.getSession();
            ArrayList listaJustificacoes = (ArrayList) session.getAttribute("listaTiposJustificacao");
            ArrayList listaSiglas = (ArrayList) session.getAttribute("listaSiglasJustificacao");
            int indiceLista = listaSiglas.indexOf(getSiglaJustificacao());

            // validacao dos outros campos
            IStrategyJustificacoes justificacaoStrategy =
                SuporteStrategyJustificacoes.getInstance().callStrategy(
                    ((ParamJustificacao) listaJustificacoes.get(indiceLista)).getTipo());
            errors = justificacaoStrategy.validateFormJustificacao(this);

            session.setAttribute(
                "tipoJustificacao",
                ((ParamJustificacao) listaJustificacoes.get(indiceLista)).getTipo());
        }
        return errors;
    }
}