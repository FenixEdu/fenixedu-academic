package net.sourceforge.fenixedu.domain;

import java.sql.Timestamp;
import java.util.List;

import net.sourceforge.fenixedu.presentationTier.formbeans.assiduousness.IntroduzirJustificacaoForm;
import net.sourceforge.fenixedu.presentationTier.formbeans.assiduousness.LerDadosJustificacaoForm;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;

/**
 * 
 * @author Fernanda Quitério e Tania Pousão
 */
public interface IStrategyJustificacoes {

    public void completaListaMarcacoes(Timestamp dataConsulta, Justificacao justificacao,
            List listaMarcacoesPonto);

    public void setJustificacao(Justificacao justificacao, IntroduzirJustificacaoForm form);

    public void setJustificacao(Justificacao justificacao, LerDadosJustificacaoForm form);

    public void setListaJustificacoesBody(ParamJustificacao paramJustificacao,
            Justificacao justificacao, List listaJustificacoesBody);

    public void updateSaldosHorarioVerbeteBody(Justificacao justificacao,
            ParamJustificacao paramJustificacao, Horario horario, List listaRegimes,
            List listaMarcacoesPonto, List listaSaldos);

    public ActionErrors validateFormJustificacao(IntroduzirJustificacaoForm form);

    public ActionErrors validateFormJustificacao(LerDadosJustificacaoForm form);

    public String formataDuracao(Horario horario, Justificacao justificacao);

    public void setLerDadosJustificacaoForm(Justificacao justificacao,
            ActionForm lerDadosJustificacaoForm);
}