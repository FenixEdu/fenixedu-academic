/*
 * Created on 22/Out/2003
 *
 */
package ServidorAplicacao.Servicos.sop.exams;

import java.util.List;

import DataBeans.InfoExecutionYear;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.sop.ReadExecutionDegreesByExecutionYear;
import ServidorAplicacao.Servicos.ServiceTestCase;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Ana e Ricardo
 *
 */
public class ReadExecutionDegreesByExecutionYearTest extends ServiceTestCase
{

    /**
     * @param name
     */
    public ReadExecutionDegreesByExecutionYearTest(java.lang.String testName)
    {
        super(testName);
    }

    protected String getNameOfServiceToBeTested()
    {
        return "ReadExecutionDegreesByExecutionYear";
    }

    protected String getDataSetFilePath()
    {
		return "etc/datasets_templates/servicos/sop/testCommonDataSet.xml";
    }

    public void testSuccessfullReadExecutionDegreesByExecutionYear()
    {
		Integer id = new Integer(1);
		InfoExecutionYear info = new InfoExecutionYear();
		info.setYear("2002/2003");
        info.setIdInternal(id);

        ReadExecutionDegreesByExecutionYear service = new ReadExecutionDegreesByExecutionYear();

        ISuportePersistente sp;

        try
        {
            sp = SuportePersistenteOJB.getInstance();
            sp.iniciarTransaccao();

            List listExecutionDegrees = service.run(info);

            sp.confirmarTransaccao();

            compareDataSetUsingExceptedDataSetTableColumns(getDataSetFilePath());
            assertEquals(listExecutionDegrees.size(), 4);

        }
        catch (FenixServiceException ex)
        {
            fail("testSuccessfullReadExecutionDegreesByExecutionYear" + ex);
        }
        catch (Exception ex)
        {
            fail("testSuccessfullReadExecutionDegreesByExecutionYear error on compareDataSet" + ex);
        }
    }

    public void testSuccessfullReadExecutionDegreeByExecutionYearEmpty()
    {
		Integer id = new Integer(5);
		InfoExecutionYear info = new InfoExecutionYear();
		info.setYear("2004/2005");
        info.setIdInternal(id);
        
		ReadExecutionDegreesByExecutionYear service = new ReadExecutionDegreesByExecutionYear();

		ISuportePersistente sp;

		try
		{
			sp = SuportePersistenteOJB.getInstance();
			sp.iniciarTransaccao();

			List listExecutionDegrees = service.run(info);

			sp.confirmarTransaccao();


            compareDataSetUsingExceptedDataSetTableColumns(getDataSetFilePath());
            assertEquals(listExecutionDegrees.size(), 0);

        }
        catch (FenixServiceException ex)
        {
            fail("testSuccessfullReadExecutionDegreeByExecutionYearEmpty" + ex);
        }
        catch (Exception ex)
        {
            fail("testSuccessfullReadExecutionDegreeByExecutionYearEmpty error on compareDataSet" + ex);
        }
    }

    public void testUnsuccessfullReadExecutionDegreeByExecutionYear()
    {
        Integer id = new Integer(6);
        InfoExecutionYear info = new InfoExecutionYear();
        info.setYear("2007/2008");
        info.setIdInternal(id);
        
		ReadExecutionDegreesByExecutionYear service = new ReadExecutionDegreesByExecutionYear();

		ISuportePersistente sp;

		try
		{
			sp = SuportePersistenteOJB.getInstance();
			sp.iniciarTransaccao();

			List listExecutionDegrees = service.run(info);
            if (listExecutionDegrees == null)
            {
            	fail("testUnsuccessfullReadExecutionDegreeByExecutionYear reading of an unexisting Execution Degree");
            }
            else
            {
                compareDataSetUsingExceptedDataSetTableColumns(getDataSetFilePath());
                assertEquals(listExecutionDegrees.size(), 0);
            }

        }
        catch (FenixServiceException ex)
        {
            fail("testUnsuccessfullReadCurricularYearByOID error on test" + ex);
        }
        catch (Exception ex)
        {
            fail("testUnsuccessfullReadExecutionDegreeByExecutionYear error on compareDataSet" + ex);
        }
    }

}
