package ServidorAplicacao.Servicos.teacher;

import ServidorAplicacao.Servicos.ServiceTestCase;

public class InsertItemDbUnitTest extends ServiceTestCase {

	public InsertItemDbUnitTest(String name) {
		super(name);
	}

	protected String getDataSetFilePath() {
		return "etc/sfsf.xml";		
	}
	
	public void testSuccessfullInsertItem() {
		
	}

	/* (non-Javadoc)
	 * @see ServidorAplicacao.Servicos.ServiceTestCase#getNameOfServiceToBeTested()
	 */
	protected String getNameOfServiceToBeTested() {
		// TODO Auto-generated method stub
		return null;
	}

}
