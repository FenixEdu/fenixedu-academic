/*
 * Created on Jul 13, 2004
 *
 */
package ServidorApresentacao.Action.student.schoolRegistration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import ServidorAplicacao.IUserView;
import ServidorApresentacao.Action.base.FenixAction;
import ServidorApresentacao.Action.sop.utils.SessionUtils;

/**
 * @author Nuno Correia
 * @author Ricardo Rodrigues
 */
public class InquiryAction extends FenixAction {

   /* public ActionForward prepareViewQuestions(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        IUserView userView = SessionUtils.getUserView(request);

        System.out.println("Eu venho aqui, depois daqui who knows...");
        Object args[] = {};      

        List inquiryQuestions = (List) ServiceUtils.executeService(userView, "ReadInquiryQuestions", args);
        
        DynaActionForm inquiryForm = (DynaActionForm) form;
        HashMap answersMap = new HashMap();
        inquiryForm.set("answersMap", answersMap);
       
        
        request.setAttribute("inquiryQuestions", inquiryQuestions);

        System.out.println("inquiryQuestions.size()= " + inquiryQuestions.size());

        DynaActionForm inquiryForm = (DynaActionForm) form;
        Integer answers[] = new Integer[inquiryQuestions.size()];
        inquiryForm.set("answers", answers);
        request.setAttribute("inquiryForm", inquiryForm);

        return mapping.findForward("viewQuestions");
    }*/

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

        IUserView userView = SessionUtils.getUserView(request);
        System.out.println("As respostas sao: ");

        DynaActionForm inquiryForm = (DynaActionForm) form;
        HashMap answersMap = (HashMap) inquiryForm.get("answersMap");
        

        //* INICIO P/ APAGAR
        List list = new ArrayList();
        Iterator iterator = answersMap.keySet().iterator();

        while(iterator.hasNext()) {
           
            String key = (String) iterator.next(); 
            System.out.println("O obejcto é isto: " + key);
            System.out.println("A entrada com id: " + key + "tem o valor: " + answersMap.get(key));
        }
            
        //FIM

        return mapping.findForward("");
    }
}