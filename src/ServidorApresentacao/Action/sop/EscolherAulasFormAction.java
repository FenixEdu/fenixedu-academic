package ServidorApresentacao.Action.sop;

import java.util.ArrayList;
import java.util.Collections;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.LabelValueBean;

import DataBeans.InfoExecutionCourse;
import DataBeans.InfoRoom;
import DataBeans.comparators.InfoLessonComparatorByWeekDayAndTime;
import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.IUserView;
import ServidorApresentacao.Action.FenixAction;
import ServidorApresentacao.Action.sop.utils.SessionUtils;
import Util.DiaSemana;
import Util.TipoAula;

/**
 * @author tfc130
 */
public class EscolherAulasFormAction extends FenixAction {

  public ActionForward execute(ActionMapping mapping, ActionForm form,
                                HttpServletRequest request,
                                HttpServletResponse response)
      throws Exception {
		SessionUtils.validSessionVerification(request, mapping);
    HttpSession sessao = request.getSession(false);
    if (sessao != null) {
        ArrayList diasSemana = new ArrayList();
        diasSemana.add(new LabelValueBean("segunda", (new Integer(DiaSemana.SEGUNDA_FEIRA)).toString() ));
        diasSemana.add(new LabelValueBean("terca", (new Integer(DiaSemana.TERCA_FEIRA)).toString() ));
        diasSemana.add(new LabelValueBean("quarta", (new Integer(DiaSemana.QUARTA_FEIRA)).toString() ));
        diasSemana.add(new LabelValueBean("quinta", (new Integer(DiaSemana.QUINTA_FEIRA)).toString() ));
        diasSemana.add(new LabelValueBean("sexta", (new Integer(DiaSemana.SEXTA_FEIRA)).toString() ));
        diasSemana.add(new LabelValueBean("sabado", (new Integer(DiaSemana.SABADO)).toString() ));
        sessao.setAttribute("diasSemana", diasSemana);

        ArrayList tiposAula = new ArrayList();
        tiposAula.add(new LabelValueBean("Teórica", (new Integer(TipoAula.TEORICA)).toString() ));
        tiposAula.add(new LabelValueBean("Prática", (new Integer(TipoAula.PRATICA)).toString() ));
        tiposAula.add(new LabelValueBean("Teórico-Prática", (new Integer(TipoAula.TEORICO_PRATICA)).toString() ));
        tiposAula.add(new LabelValueBean("Laboratorial", (new Integer(TipoAula.LABORATORIAL)).toString() ));
        tiposAula.add(new LabelValueBean("Dúvidas", (new Integer(TipoAula.DUVIDAS)).toString() ));
        tiposAula.add(new LabelValueBean("Reserva", (new Integer(TipoAula.RESERVA)).toString() ));
        sessao.setAttribute("tiposAula", tiposAula);

        ArrayList horas = new ArrayList();
        horas.add("8");
        horas.add("9");
        horas.add("10");
        horas.add("11");
        horas.add("12");
        horas.add("13");
        horas.add("14");
        horas.add("15");
        horas.add("16");
        horas.add("17");
        horas.add("18");
        horas.add("19");
        horas.add("20");
        horas.add("21");
        horas.add("22");
        horas.add("23");
        sessao.setAttribute("horas",horas);
        
        ArrayList minutos = new ArrayList();        
        minutos.add("00");
        minutos.add("30");
        sessao.setAttribute("minutos",minutos);


        IUserView userView = (IUserView) sessao.getAttribute("UserView");
        GestorServicos gestor = GestorServicos.manager();

		// Ler as Salas
        Object argsLerSalas[] = new Object[0];
        ArrayList infoSalas = (ArrayList) gestor.executar(userView, "LerSalas", argsLerSalas);

        ArrayList listaSalas = new ArrayList();
        for(int i = 0; i < infoSalas.size(); i++) {
            InfoRoom elem = (InfoRoom)infoSalas.get(i);
            listaSalas.add(new LabelValueBean(elem.getNome(), elem.getNome()));	        	
        }
        sessao.setAttribute("listaSalas", listaSalas);
        sessao.setAttribute("listaInfoSalas", infoSalas);    
        
        // Fim ler salas.

		// Ler Disciplinas em Execucao
        InfoExecutionCourse iDE = (InfoExecutionCourse) sessao.getAttribute("infoDisciplinaExecucao");

        Object argsLerAulas[] = new Object[1];
        argsLerAulas[0] = iDE;
        ArrayList infoAulas = (ArrayList) gestor.executar(userView, "LerAulasDeDisciplinaExecucao", argsLerAulas);

        sessao.removeAttribute("listaAulas");
        if (infoAulas != null && !infoAulas.isEmpty()) {
			Collections.sort(infoAulas, new InfoLessonComparatorByWeekDayAndTime());        	
	        sessao.setAttribute("listaAulas", infoAulas);
        }

		return mapping.findForward("Sucesso");
    } else
      throw new Exception();  // nao ocorre... pedido passa pelo filtro Autorizacao 
  }
}
