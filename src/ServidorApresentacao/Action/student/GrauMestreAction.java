
package ServidorApresentacao.Action.student;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import DataBeans.InfoDegree;
import DataBeans.InfoStudent;
import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.DataView;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionUtils;

/**
 * @author Ricardo Nortadas & Rui Figueiredo
 */

public class GrauMestreAction extends Action {

	// TODO : Not used at this time, and needs corrections

	public static String INFO_STUDENT_KEY = "infoStudent";

	public ActionForward execute(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {


		IUserView userView = SessionUtils.getUserView(request);

		HttpSession session = request.getSession();

		InfoStudent infoStudent = (InfoStudent) session.getAttribute(INFO_STUDENT_KEY);

		// Tipo de certidao que o aluno quer obter

		System.out.println("entrei no GrauMestre");

		String strTipo = (String) session.getAttribute("Tipo");

		System.out.println("ja tenho o tipo");
		System.out.println(strTipo);

		int Tipo = Integer.parseInt(strTipo);
		System.out.println(Tipo);
		// Temporario

		boolean Inscricao = true;

		System.out.println("passei");
		System.out.println(infoStudent.getNumber());
		Object[] argsReadDisciplinesByStudent = { infoStudent.getNumber(), infoStudent.getDegreeType() };
		Object[] argsReadCourseByStudent = { infoStudent.getNumber(), infoStudent.getDegreeType() };


		ArrayList DisciplinesList = new ArrayList();
		InfoDegree infoDegree = null;
		String result="Inexistente";

		System.out.println("iniciarChamada");

		// deve faltar remover antes de entrar se existir

		try {
			
			// obter titulo da dissertação
			
			
			// obter o curso de mestrado
			
			infoDegree =
				(InfoDegree) ServiceUtils.executeService(
						userView,
						"ReadCourseByStudent",
						argsReadCourseByStudent);
			if (infoDegree!=null) {
				System.out.println(infoDegree.toString());
				session.setAttribute("infoCourse", infoDegree);
			}
					System.out.println("acabeiServico");
				} catch (Exception e) {
					System.out.println("entrei" + e);
					return mapping.getInputForward();
				}

			// verificar se conclui o mestrado ou prestou provas
			
			// temporario
			//result="GrauMestre";
			result="PrestouProvas";

			Object argumentos[] = {};

			GestorServicos gestor = GestorServicos.manager();
			DataView data = (DataView) gestor.executar(null, "ObterData", argumentos);
			session.setAttribute("Data", data);
			System.out.println("Acabei de fazer request da data");


		return mapping.findForward(result);

	}

}