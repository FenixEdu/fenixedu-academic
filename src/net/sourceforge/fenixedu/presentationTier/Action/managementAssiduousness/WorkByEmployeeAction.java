/*
 * Created on 11/Dez/2004
 */
package net.sourceforge.fenixedu.presentationTier.Action.managementAssiduousness;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import net.sourceforge.fenixedu.applicationTier.Executor;
import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.PersistenceException;
import net.sourceforge.fenixedu.applicationTier.Servico.assiduousness.ServicoAutorizacaoLer;
import net.sourceforge.fenixedu.applicationTier.Servico.assiduousness.ServicoSeguroConsultarFuncionario;
import net.sourceforge.fenixedu.applicationTier.Servico.assiduousness.ServicoSeguroConsultarVerbete;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotExecuteException;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;
import net.sourceforge.fenixedu.util.FormataCalendar;
import net.sourceforge.fenixedu.constants.assiduousness.Constants;

/**
 * @author Tânia Pousão
 * 
 */
public class WorkByEmployeeAction extends FenixDispatchAction {
    public static String TODO_EXTRA_WORK_CONSULT = "consult";

    public static String TODO_EXTRA_WORK_AUTHORIZE = "authorize";

    public ActionForward prepareInputs(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        return mapping.findForward("inputDateAndEmployee");
    }

    public ActionForward workSheetByEmployeeAndMoth(ActionMapping mapping,
            ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        try{
        ActionErrors actionErrors = new ActionErrors();
        HttpSession session = request.getSession();

        IUserView userView = SessionUtils.getUserView(request);
        String usernameWhoKey = userView.getUtilizador();

        DynaActionForm workByEmployeeFormBean = (DynaActionForm) form;
        Integer employeeNumber = (Integer) workByEmployeeFormBean
                .get("employeeNumber");
        System.out.println("---->" + employeeNumber);

        String beginDateForm = (String) workByEmployeeFormBean.get("beginDate");
        String endDateForm = (String) workByEmployeeFormBean.get("endDate");

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        simpleDateFormat.setLenient(false);

        Date bedinDate = simpleDateFormat.parse(beginDateForm);
        Date endDate = simpleDateFormat.parse(endDateForm);
        System.out.println("-->bedinDate: " + bedinDate);
        System.out.println("-->endDate: " + endDate);

        Locale locale = request.getLocale();

        // »»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»
        // Dados Funcionário
        // «««««««««««««««««««««««««««««««««««««««««««««««««««««««««««««««««««««
        ServicoAutorizacaoLer servicoAutorizacaoLer = new ServicoAutorizacaoLer();
        ServicoSeguroConsultarFuncionario servicoSeguroConsultarFuncionario = new ServicoSeguroConsultarFuncionario(
                servicoAutorizacaoLer, employeeNumber.intValue());
        try {
            Executor.getInstance().doIt(servicoSeguroConsultarFuncionario);
        } catch (NotExecuteException nee) {
            actionErrors.add(ActionErrors.GLOBAL_ERROR, new ActionError(nee
                    .getMessage()));
        } catch (PersistenceException pe) {
            actionErrors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
                    "error.server"));
        } finally {
            if (!actionErrors.isEmpty()) {
                saveErrors(request, actionErrors);

                return mapping.getInputForward();
            }
        }

        // »»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»»
        // Verbete
        // «««««««««««««««««««««««««««««««««««««««««««««««««««««««««««««««««««««
        // ATENCAO: Servico acede à BD Oracle para ler as marcacoes ponto
        ServicoSeguroConsultarVerbete servicoSeguroConsultarVerbete = new ServicoSeguroConsultarVerbete(
                servicoAutorizacaoLer, employeeNumber, new Timestamp(bedinDate
                        .getTime()), new Timestamp(endDate
                        .getTime()), locale);

        try {
            Executor.getInstance().doIt(servicoSeguroConsultarVerbete);
        } catch (NotExecuteException nee) {
            actionErrors.add(ActionErrors.GLOBAL_ERROR, new ActionError(nee
                    .getMessage()));
        } catch (PersistenceException pe) {
            actionErrors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
                    "error.server"));
        } finally {
            if (!actionErrors.isEmpty()) {
                saveErrors(request, actionErrors);
                return (new ActionForward(mapping.getInput()));
            }
        }

        session.setAttribute("numMecanografico", employeeNumber);
        session.setAttribute("pessoa", servicoSeguroConsultarFuncionario
                .getPessoa());
        session.setAttribute("centroCusto", servicoSeguroConsultarFuncionario
                .getCentroCusto());
        session.setAttribute(Constants.USERNAME, userView.getUtilizador());

        List listaHeaders = new ArrayList();
        listaHeaders.add("Data");
        listaHeaders.add("Sigla");
        listaHeaders.add("Saldo HN");
        listaHeaders.add("Saldo PF");
        listaHeaders.add("Justifição");
        listaHeaders.add("Marcações de Ponto");

        List listaHeadersResumo = new ArrayList();
        listaHeadersResumo.add("Saldo HN");
        listaHeadersResumo.add("Saldo PF");
        listaHeadersResumo.add("Saldo Nocturno");
        listaHeadersResumo.add("DSC");
        listaHeadersResumo.add("DS");

        List listaHeadersTrabExtra = new ArrayList();
        listaHeadersTrabExtra.add("Primeiro Escalão");
        listaHeadersTrabExtra.add("Segundo Escalão");
        listaHeadersTrabExtra.add("Depois Segundo Escalão");
        listaHeadersTrabExtra.add("Primeiro Escalão");
        listaHeadersTrabExtra.add("Segundo Escalão");
        listaHeadersTrabExtra.add("Depois Segundo Escalão");

        List listaSaldos = servicoSeguroConsultarVerbete.getListaSaldos();
        List listaResumo = new ArrayList();
        Calendar calendario = Calendar.getInstance();
        calendario.setLenient(false);

        // saldoHN
        calendario.clear();
        calendario.setTimeInMillis(((Long) listaSaldos.get(0)).longValue());
        listaResumo.add(0, FormataCalendar.horasSaldo(calendario));
        // saldoPF
        calendario.clear();
        calendario.setTimeInMillis(((Long) listaSaldos.get(1)).longValue());
        listaResumo.add(1, FormataCalendar.horasSaldo(calendario));
        // saldo nocturno normal
        calendario.clear();
        calendario.setTimeInMillis(((Long) listaSaldos.get(7)).longValue());
        listaResumo.add(2, FormataCalendar.horasSaldo(calendario));
        // saldo DSC ou Feriado
        calendario.clear();
        calendario.setTimeInMillis(((Long) listaSaldos.get(5)).longValue());
        listaResumo.add(3, FormataCalendar.horasSaldo(calendario));
        // saldo DS
        calendario.clear();
        calendario.setTimeInMillis(((Long) listaSaldos.get(6)).longValue());
        listaResumo.add(4, FormataCalendar.horasSaldo(calendario));

        List listaTrabExtra = new ArrayList();

        // saldo primeiro escalao diurno
        calendario.clear();
        calendario.setTimeInMillis(((Long) listaSaldos.get(2)).longValue());
        listaTrabExtra.add(0, FormataCalendar.horasSaldo(calendario));
        // saldo segundo escalao diurno
        calendario.clear();
        calendario.setTimeInMillis(((Long) listaSaldos.get(3)).longValue());
        listaTrabExtra.add(1, FormataCalendar.horasSaldo(calendario));
        // saldo para alem do segundo escalao diurno
        calendario.clear();
        calendario.setTimeInMillis(((Long) listaSaldos.get(4)).longValue());
        listaTrabExtra.add(2, FormataCalendar.horasSaldo(calendario));
        // saldo primeiro escalao nocturno
        calendario.clear();
        calendario.setTimeInMillis(((Long) listaSaldos.get(8)).longValue());
        listaTrabExtra.add(3, FormataCalendar.horasSaldo(calendario));
        // saldo segundo escalao nocturno
        calendario.clear();
        calendario.setTimeInMillis(((Long) listaSaldos.get(9)).longValue());
        listaTrabExtra.add(4, FormataCalendar.horasSaldo(calendario));
        // saldo para alem do segundo escalao nocturno
        calendario.clear();
        calendario.setTimeInMillis(((Long) listaSaldos.get(10)).longValue());
        listaTrabExtra.add(5, FormataCalendar.horasSaldo(calendario));

        List listagem = new ArrayList();
        listagem.add(listaHeaders);
        listagem.add(servicoSeguroConsultarVerbete.getVerbete());
        listagem.add(listaHeadersResumo);
        listagem.add(listaResumo);
        listagem.add(listaHeadersTrabExtra);
        listagem.add(listaTrabExtra);

        request.setAttribute("headers", (ArrayList) (listagem.get(0)));
        request.setAttribute("body", (ArrayList) (listagem.get(1)));
        request.setAttribute("headers2", (ArrayList) (listagem.get(2)));
        request.setAttribute("body2", (ArrayList) (listagem.get(3)));
        request.setAttribute("headers3", (ArrayList) (listagem.get(4)));
        request.setAttribute("body3", (ArrayList) (listagem.get(5)));

        } catch(Exception e) {
            e.printStackTrace();
        }
        
        return (mapping.findForward("workSheet"));
    }
}
