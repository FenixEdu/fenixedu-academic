package ServidorApresentacao.Action.assiduousness;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ServidorApresentacao.formbeans.assiduousness.MostrarListaForm;
import constants.assiduousness.Constants;

public class MostrarListaAction extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws IOException, ServletException {
        HttpSession session = request.getSession();

        if (session.getAttribute(Constants.USERNAME) == null) {
            return mapping.findForward("PortalAssiduidadeAction");
        }
        List listagem = (ArrayList) request.getAttribute("listagem");
        ActionForward forward = (ActionForward) request.getAttribute("forward");

        MostrarListaForm formMostrarLista = (MostrarListaForm) form;

        if (listagem != null) {

            formMostrarLista.setHeaders((ArrayList) (listagem.get(0)));
            formMostrarLista.setBody((ArrayList) (listagem.get(1)));
            if (listagem.size() > 2) {
                formMostrarLista.setHeaders2((ArrayList) (listagem.get(2)));
                formMostrarLista.setBody2((ArrayList) (listagem.get(3)));
            }
            if (listagem.size() > 4) {
                formMostrarLista.setHeaders3((ArrayList) (listagem.get(4)));
                formMostrarLista.setBody3((ArrayList) (listagem.get(5)));
            }
        }
        return (mapping.findForward(forward.getName()));
    }
}