package ServidorAplicacao.Servicos;

import junit.framework.TestCase;
import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.IUserView;
import Tools.dbaccess;


public class TestCaseServicos extends TestCase {
	protected GestorServicos _gestor = null;
	protected IUserView _userView = null;
	protected IUserView _userView2 = null;

	private dbaccess dbAcessPoint = null;


	public TestCaseServicos(String testName) {
		super(testName);
	}
	
	protected void setUp() {
		
		try {
			dbAcessPoint = new dbaccess();
			dbAcessPoint.openConnection();
			dbAcessPoint.backUpDataBaseContents("etc/testBackup.xml");
			dbAcessPoint.loadDataBase("etc/testDataSet.xml");
			dbAcessPoint.closeConnection();
		} catch (Exception ex) {
			System.out.println("Setup failed: " + ex);
		}

		_gestor = GestorServicos.manager();
		String argsAutenticacao[] = { "user", "pass" };
		String argsAutenticacao2[] = { "4", "a" };
		try {
			_userView = (IUserView) _gestor.executar(null, "Autenticacao", argsAutenticacao);
		} catch (Exception ex) {
			System.out.println("Servico no executado: " + ex);
		}
		try {
			_userView2 = (IUserView) _gestor.executar(null, "Autenticacao", argsAutenticacao2);
		} catch (Exception ex) {
			System.out.println("Servico no executado: " + ex);
		}
	}

	protected void tearDown() {
//		try {
//			dbAcessPoint.openConnection();
//			dbAcessPoint.loadDataBase("etc/testBackup.xml");
//			dbAcessPoint.closeConnection();
//		} catch (Exception ex) {
//			System.out.println("Tear down failed: " +ex);
//		}
	}
}