package ServidorApresentacao.formbeans.assiduousness;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import constants.assiduousness.Constants;

/**
 *
 * @author  Fernanda Quitério & Tania Pousão
 */
public class ImprimirVerbetesForm extends ActionForm {

	private String _mesImpressao = null;
	private String _anoImpressao = null;
	private Timestamp _dataInicioImpressao = null;
	private Timestamp _dataFimImpressao = null;

	public String getMesImpressao() {
		return (_mesImpressao);
	}
	public String getAnoImpressao() {
		return (_anoImpressao);
	}
	public Timestamp getDataInicioImpressao() {
		return (_dataInicioImpressao);
	}
	public Timestamp getDataFimImpressao() {
		return (_dataFimImpressao);
	}

	public void setMesImpressao(String mesImpressao) {
		_mesImpressao = mesImpressao;
	}
	public void setAnoImpressao(String anoImpressao) {
		_anoImpressao = anoImpressao;
	}
	public void setDataInicioImpressao(Timestamp dataInicioImpressao) {
		_dataInicioImpressao = dataInicioImpressao;
	}
	public void setDataFimImpressao(Timestamp dataFimImpressao) {
		_dataFimImpressao = dataFimImpressao;
	}

	public void reset(ActionMapping mapping, HttpServletRequest request) {
		_mesImpressao = "";
		_anoImpressao = "";
	}

	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
		ActionErrors errors = new ActionErrors();
		int mesImpressao = 0;
		int anoImpressao = 0;

		try {
			// se for um pedido de impressao de apenas um verbete
			if (request.getParameter("impressaoFuncionario") != null) {
				HttpSession session = request.getSession();
				setDataInicioImpressao(new Timestamp(((Date) session.getAttribute(Constants.INICIO_CONSULTA)).getTime()));
				setDataFimImpressao(new Timestamp(((Date) session.getAttribute(Constants.FIM_CONSULTA)).getTime()));
			} else {

				if ((this.getMesImpressao().length() < 1) || (this.getAnoImpressao().length() < 1)) {
					errors.add("dates", new ActionError("error.dataImpressao.invalida"));
				} else {
					try {
						mesImpressao = (new Integer(this.getMesImpressao())).intValue();
						anoImpressao = (new Integer(this.getAnoImpressao())).intValue();
					} catch (java.lang.NumberFormatException e) {
						errors.add("numero", new ActionError("error.numero.naoInteiro"));
					}

					Calendar calendarImpressao = Calendar.getInstance();
					calendarImpressao.setLenient(false);

					calendarImpressao.clear();
					calendarImpressao.set(anoImpressao, mesImpressao - 1, 1, 00, 00, 00);
					Timestamp dataImpressao = new Timestamp(calendarImpressao.getTimeInMillis());
					setDataInicioImpressao(dataImpressao);
					System.out.print("a data de impressao vai de " + dataImpressao);

					int diaMaximo = calendarImpressao.getActualMaximum(Calendar.DAY_OF_MONTH);
					calendarImpressao.clear();
					calendarImpressao.set(anoImpressao, mesImpressao - 1, diaMaximo, 23, 59, 59);
					dataImpressao = new Timestamp(calendarImpressao.getTimeInMillis());
					setDataFimImpressao(dataImpressao);
					System.out.println(" a " + dataImpressao);
				}
			}
		} catch (java.lang.IllegalArgumentException e) {
			errors.add("horasData", new ActionError("error.dataImpressao.incorrecta"));
		}
		return errors;
	}
}