package ServidorApresentacao.Action.sop;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.LabelValueBean;

import DataBeans.InfoDegree;
import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.IUserView;


/**
 * @author tfc130
 */
public class PrepararEscolherContextoFormAction extends Action {

  public ActionForward execute(ActionMapping mapping, ActionForm form,
                                HttpServletRequest request,
                                HttpServletResponse response)
      throws Exception {
//    DynaActionForm criarSalaForm = (DynaActionForm) form;
    
    HttpSession sessao = request.getSession(false);
    if (sessao != null) {

        /* Criar o bean de semestres */
        ArrayList semestres = new ArrayList();
        semestres.add(new LabelValueBean("escolher", ""));
        semestres.add(new LabelValueBean("1 º", "1"));
        semestres.add(new LabelValueBean("2 º", "2"));
        sessao.setAttribute("semestres", semestres);

        /* Criar o bean de anos curricutares */
        ArrayList anosCurriculares = new ArrayList();
        anosCurriculares.add(new LabelValueBean("escolher", ""));
        anosCurriculares.add(new LabelValueBean("1 º", "1"));
        anosCurriculares.add(new LabelValueBean("2 º", "2"));
        anosCurriculares.add(new LabelValueBean("3 º", "3"));
        anosCurriculares.add(new LabelValueBean("4 º", "4"));
        anosCurriculares.add(new LabelValueBean("5 º", "5"));
        sessao.setAttribute("anosCurriculares", anosCurriculares);

        /* Cria o form bean com as licenciaturas em execucao.*/
        IUserView userView = (IUserView) sessao.getAttribute("UserView");
        GestorServicos gestor = GestorServicos.manager();
        Object argsLerLicenciaturas[] = new Object[0];
        List result = (List) gestor.executar(userView, "LerLicenciaturas", argsLerLicenciaturas);

        //Collections.sort(result);

        ArrayList licenciaturas = new ArrayList();
        licenciaturas.add(new LabelValueBean("escolher", ""));

        Iterator iterator = result.iterator();
        while(iterator.hasNext()) {
            InfoDegree elem = (InfoDegree)iterator.next();
            licenciaturas.add(new LabelValueBean(elem.getNome(), elem.getSigla()));
        }
        sessao.setAttribute("licenciaturas", licenciaturas);

      return mapping.findForward("Sucesso");
    } else
      throw new Exception();  // nao ocorre... pedido passa pelo filtro Autorizacao 
  }
}