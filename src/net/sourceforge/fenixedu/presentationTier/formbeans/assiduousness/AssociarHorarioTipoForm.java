package net.sourceforge.fenixedu.presentationTier.formbeans.assiduousness;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.ListIterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.sourceforge.fenixedu.domain.Horario;
import net.sourceforge.fenixedu.domain.HorarioTipo;
import net.sourceforge.fenixedu.domain.IStrategyHorarios;
import net.sourceforge.fenixedu.domain.SuporteStrategyHorarios;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 * 
 * @author Fernanda Quitério & Tania Pousão
 */
public class AssociarHorarioTipoForm extends ActionForm {
    private String _numMecanografico = null;

    private String _numDias = null;

    private String _posicao = null;

    private List _listaSiglas = new ArrayList();

    private String _diaInicio = null;

    private String _mesInicio = null;

    private String _anoInicio = null;

    private String _diaFim = null;

    private String _mesFim = null;

    private String _anoFim = null;

    private String _sigla = null;

    private boolean _excepcaoHorario = false;

    private String alterar = null;

    private String indiceRotacao = null;

    public String getIndiceRotacao() {
        return indiceRotacao;
    }

    public void setIndiceRotacao(String indiceRotacao) {
        this.indiceRotacao = indiceRotacao;
    }

    public String getAlterar() {
        return alterar;
    }

    public void setAlterar(String alterar) {
        this.alterar = alterar;
    }

    public String getNumMecanografico() {
        return (_numMecanografico);
    }

    public String getNumDias() {
        return _numDias;
    }

    public String getPosicao() {
        return _posicao;
    }

    public List getListaSiglas() {
        return (_listaSiglas);
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

    public String getSigla() {
        return (_sigla);
    }

    public boolean isExcepcaoHorario() {
        return _excepcaoHorario;
    }

    public void setNumMecanografico(String numMecanografico) {
        _numMecanografico = numMecanografico;
    }

    public void setNumDias(String numDias) {
        _numDias = numDias;
    }

    public void setPosicao(String posicao) {
        _posicao = posicao;
    }

    public void setListaSiglas(List listaSiglas) {
        _listaSiglas = listaSiglas;
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

    public void setSigla(String sigla) {
        _sigla = sigla;
    }

    public void setExcepcaoHorario(boolean excepcaoHorario) {
        _excepcaoHorario = excepcaoHorario;
    }

    public void reset(ActionMapping mapping, HttpServletRequest request) {
        _numMecanografico = "";

        _numDias = "";
        _posicao = "";

        //		setSigla((String) _listaSiglas.get(0));

        _diaFim = "";
        _mesFim = "";
        _anoFim = "";

        _excepcaoHorario = false;
    }

    public void setForm(List listaSiglas, String sigla, Horario horario, Boolean excepcao,
            String numeroMecanografico) {
        setListaSiglas(listaSiglas);

        if (sigla != null) {
            setSigla(sigla);
        } else {
            setSigla((String) _listaSiglas.get(0));
        }

        //		testes para o caso de alteraçao de horario tipo que so pode ocorrer
        // logo apos a associacao do horario
        Calendar data = Calendar.getInstance();
        if (horario != null) {
            setPosicao(String.valueOf(horario.getPosicao()));
            setNumDias(String.valueOf(horario.getNumDias()));

            if (horario.getDataFim() != null) {
                data.setTime(horario.getDataFim());
                setDiaFim((new Integer(data.get(Calendar.DAY_OF_MONTH))).toString());
                setMesFim((new Integer(data.get(Calendar.MONTH) + 1)).toString());
                setAnoFim((new Integer(data.get(Calendar.YEAR))).toString());
            }
            if (horario.getDataInicio() != null) {
                data.clear();
                data.setTime(horario.getDataInicio());
            }
            if (excepcao != null) {
                setExcepcaoHorario(excepcao.booleanValue());
            }
            if (numeroMecanografico != null) {
                setNumMecanografico(numeroMecanografico);
            }
            if (sigla == null) {
                setSigla(horario.getSigla());
            }
        }
        setDiaInicio((new Integer(data.get(Calendar.DAY_OF_MONTH))).toString());
        setMesInicio((new Integer(data.get(Calendar.MONTH) + 1)).toString());
        setAnoInicio((new Integer(data.get(Calendar.YEAR))).toString());
    }

    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();

        HttpSession session = request.getSession();

        if (mapping.getPath().equals(new String("/associarHorarioTipo"))) {
            if (request.getParameter("numMecanografico") != null) {
                if ((request.getParameter("numMecanografico")).length() < 1) {
                    errors.add("numMecanografico", new ActionError("error.numero.obrigatorio"));
                    return errors;
                }
                try {
                    (new Integer(request.getParameter("numMecanografico"))).intValue();
                } catch (java.lang.NumberFormatException e) {
                    errors.add("numMecanografico", new ActionError("error.numero.naoInteiro"));
                    return errors;
                }

            }
        } else if (mapping.getPath().equals(new String("/adicionarHorarioTipoRotacao"))) {
            if ((request.getParameter("numDias") != null) && (request.getParameter("posicao") != null)) {
                if (((request.getParameter("numDias")).length() < 1)
                        || ((request.getParameter("posicao")).length() < 1)) {
                    errors.add("numero", new ActionError("error.numero.obrigatorio"));
                    return errors;
                }
                int numDias = 0;
                int posicao = 0;
                try {
                    numDias = (new Integer(getNumDias())).intValue();
                    posicao = (new Integer(getPosicao())).intValue();
                } catch (java.lang.NumberFormatException e) {
                    errors.add("numero", new ActionError("error.numero.naoInteiro"));
                    return errors;
                }

                // testa a posição e a duração da nova rotação
                List rotacaoHorario = new ArrayList();
                if ((rotacaoHorario = (ArrayList) session.getAttribute("rotacaoHorario")) != null) {
                    List novaLista = (List) ((ArrayList) rotacaoHorario).clone();
                    Horario horario = null;
                    int posMaxPrimeiroHorario = 0;

                    // se for uma alteracao de horario a validacao das posicoes
                    // nao pode contar com o horario anterior
                    if (request.getParameter("alterar") != null) {
                        novaLista.remove(Integer.valueOf(request.getParameter("indiceRotacao"))
                                .intValue());
                    }
                    ListIterator iterador = novaLista.listIterator();

                    if (iterador.hasNext()) {
                        horario = (Horario) iterador.next();
                        if ((posicao >= horario.getPosicao())
                                && (posicao <= (horario.getPosicao() + horario.getNumDias() - 1))) {
                            errors.add("numero", new ActionError("error.rotacaoHorario"));
                            return errors;
                        }

                        if ((posicao >= posMaxPrimeiroHorario) && (posicao < horario.getPosicao())) {
                            if (numDias >= (horario.getPosicao() - posMaxPrimeiroHorario)) {
                                errors.add("rotacao", new ActionError("error.rotacaoHorario"));
                                return errors;
                            }
                            /* dia de descanso entre a rotacao dos horários */
                            if (((posicao - posMaxPrimeiroHorario) == 1)
                                    || (((horario.getPosicao()) - (posicao + numDias - 1)) == 1)) {
                                errors.add("dia de descanso", new ActionError(
                                        "error.rotacaoHorario.diaDescanso"));
                                return errors;
                            }

                        }

                        posMaxPrimeiroHorario = horario.getPosicao() + horario.getNumDias() - 1;
                    }

                    while (iterador.hasNext()) {
                        horario = (Horario) iterador.next();
                        if ((posicao >= horario.getPosicao())
                                && (posicao <= (horario.getPosicao() + horario.getNumDias() - 1))) {
                            errors.add("rotacao", new ActionError("error.rotacaoHorario"));
                            return errors;
                        }

                        if ((posicao > posMaxPrimeiroHorario) && (posicao < horario.getPosicao())) {
                            if (numDias >= (horario.getPosicao() - posMaxPrimeiroHorario)) {
                                errors.add("rotacao", new ActionError("error.rotacaoHorario"));
                                return errors;
                            }
                            /* dia de descanso entre a rotacao dos horários */
                            if (((posicao - posMaxPrimeiroHorario) == 1)
                                    || (((horario.getPosicao()) - (posicao + numDias - 1)) == 1)) {
                                errors.add("dia de descanso", new ActionError(
                                        "error.rotacaoHorario.diaDescanso"));
                                return errors;
                            }

                        }
                        posMaxPrimeiroHorario = horario.getPosicao() + horario.getNumDias() - 1;
                    }

                    if (posicao > posMaxPrimeiroHorario) {
                        // dia de descanso entre a rotacao dos horários
                        if ((posicao - posMaxPrimeiroHorario) == 1) {
                            errors.add("dia de descanso", new ActionError(
                                    "error.rotacaoHorario.diaDescanso"));
                            return errors;
                        }
                    }
                }

            }
        }

        List listaSiglas = (ArrayList) session.getAttribute("listaSiglas");
        List listaHorarios = (ArrayList) session.getAttribute("listaHorarios");
        List listaRegimesHorarios = (ArrayList) session.getAttribute("listaRegimesHorarios");

        int indiceLista = 0;
        if (listaSiglas != null) {
            indiceLista = listaSiglas.indexOf(getSigla());
        }

        HorarioTipo horarioTipo = null;
        if (listaHorarios != null) {
            horarioTipo = (HorarioTipo) listaHorarios.get(indiceLista);
            // validacao dos outros campos
            IStrategyHorarios horario = SuporteStrategyHorarios.getInstance().callStrategy(
                    horarioTipo.getModalidade());
            errors = horario.validateAssociarHorarioTipo(this, horarioTipo,
                    (ArrayList) listaRegimesHorarios.get(indiceLista));
        }
        return errors;
    } /* validate */
}