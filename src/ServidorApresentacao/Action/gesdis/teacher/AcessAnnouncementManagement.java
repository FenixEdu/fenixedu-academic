package ServidorApresentacao.Action.gesdis.teacher;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import DataBeans.gesdis.InfoSite;
import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.Servico.UserView;
import ServidorApresentacao.Action.FenixAction;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
/**
 * @author ep15
 * @author Ivo Brandão
 */
public class AcessAnnouncementManagement extends FenixAction {
    public ActionForward execute(ActionMapping mapping, ActionForm form, 
    	HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = getSession(request);
        session.removeAttribute("Seccao");
        session.removeAttribute("Anuncio");
        session.removeAttribute("Anuncios");
        session.removeAttribute("ReferenciaBibliografica");
        session.removeAttribute("Curriculum");
        UserView userView = (UserView) session.getAttribute(SessionConstants.U_VIEW);
//        userView.setSeccaoNome(null);
//        userView.setAnunciosNome(null);
//        userView.setCurriculumNome(null);
        InfoSite infoSite = (InfoSite) session.getAttribute("Sitio");
        Object args[] = {infoSite};
        GestorServicos manager = GestorServicos.manager();
        List announcements = (List) manager.executar(userView, "ObterAnuncios", args);
        session.setAttribute("Anuncios", announcements);
//        userView.setAnunciosNome("Anuncios");
        return mapping.findForward("GestaoDeAnuncios");
    }
}