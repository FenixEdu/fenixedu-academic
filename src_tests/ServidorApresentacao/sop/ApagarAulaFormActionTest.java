package ServidorApresentacao.sop;
import java.util.ArrayList;
import java.util.HashSet;

import junit.framework.Test;
import junit.framework.TestSuite;
import DataBeans.InfoExecutionCourse;
import DataBeans.InfoExecutionPeriod;
import DataBeans.InfoExecutionYear;
import DataBeans.InfoLesson;
import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.UserView;
import ServidorApresentacao.TestCasePresentation;
/**
@author tfc130
*/
public class ApagarAulaFormActionTest extends TestCasePresentation {
	
	
  public static void main(java.lang.String[] args) {
    junit.textui.TestRunner.run(suite());
  }
   
  public static Test suite() {
    TestSuite suite = new TestSuite(ApagarAulaFormActionTest.class);
        
    return suite;
  }
  public void setUp() throws Exception {
   super.setUp();
    // define ficheiro de configuracao Struts a utilizar
    setServletConfigFile("/WEB-INF/tests/web-sop.xml");
    
    
  }

 

  public ApagarAulaFormActionTest(String testName) {
    super(testName);
  }

  public void testSuccessfulApagarAula() {
  	
  	// Necessario para colocar form manipularAulasForm em sessao
  	setRequestPathInfo("/sop", "/manipularAulasForm");
  	addRequestParameter("indexAula", new Integer(0).toString());
  	addRequestParameter("operation", "Apagar Aula");
  	actionPerform();
	
    // define mapping de origem
    setRequestPathInfo("/sop", "/apagarAula");
    // coloca credenciais na sessao
    HashSet privilegios = new HashSet();
    privilegios.add("ApagarAula");
    privilegios.add("LerSalas");
    privilegios.add("LerAulasDeDisciplinaExecucao");
    privilegios.add("LerDisciplinasExecucaoDeCursoExecucaoEAnoCurricular");
    IUserView userView = new UserView("user", privilegios);

    getSession().setAttribute("UserView", userView);

    try {
    	GestorServicos gestor = GestorServicos.manager();
    	InfoExecutionCourse iDE = 
    	new InfoExecutionCourse("Trabalho Final de Curso I","TFCI",
    				"programa1",
    				new Double(0),
					new Double(0),
					new Double(0),
					new Double(0),
					new InfoExecutionPeriod("2º Semestre",new InfoExecutionYear("2002/2003")));

    	getSession().setAttribute("infoDisciplinaExecucao", iDE);
    	Object argsLerSalas[] = new Object[0];
    	ArrayList infoSalas = (ArrayList) gestor.executar(userView, "LerSalas", argsLerSalas);
    	getSession().setAttribute("listaSalas", infoSalas);

    	Object argsLerAulas[] = new Object[1];
    	argsLerAulas[0] = iDE;
    	ArrayList infoAulas = (ArrayList) gestor.executar(userView, "LerAulasDeDisciplinaExecucao", argsLerAulas);
    	getSession().setAttribute("listaAulas", infoAulas);

    	InfoLesson infoAula = (InfoLesson) infoAulas.get(0);
    	getSession().setAttribute("infoAula", infoAula);


    } catch (Exception ex) {
    	System.out.println("Erro na invocacao do servico " + ex);
    }

    // invoca acção
    actionPerform();
    
    // verifica reencaminhamento
    verifyForward("Sucesso");
    
    //verifica ausencia de erros
    verifyNoActionErrors();
  }

  
    
  
}