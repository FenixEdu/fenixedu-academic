/*
 * Created on 23/Jul/2003
 *
 */

package ServidorAplicacao.Servico.teacher;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import DataBeans.ExecutionCourseSiteView;
import DataBeans.InfoMetadata;
import DataBeans.InfoSiteMetadatas;
import DataBeans.SiteView;
import DataBeans.util.Cloner;

import Dominio.ExecutionCourse;
import Dominio.IExecutionCourse;
import Dominio.IMetadata;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.InvalidArgumentsServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExecutionCourse;
import ServidorPersistente.IPersistentMetadata;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import UtilTests.ParseMetadata;

/**
 * @author Susana Fernandes
 */

public class ReadMetadatas implements IServico
{

    private static ReadMetadatas service = new ReadMetadatas();
    private String path = new String();
    public static ReadMetadatas getService()
    {
        return service;
    }

    public String getNome()
    {
        return "ReadMetadatas";
    }
    public SiteView run(Integer executionCourseId, String path) throws FenixServiceException
    {
        this.path = path.replace('\\', '/');
        try
        {
            ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();
            IPersistentExecutionCourse persistentExecutionCourse =
                persistentSuport.getIPersistentExecutionCourse();
            IExecutionCourse executionCourse = new ExecutionCourse(executionCourseId);
            executionCourse =
                (IExecutionCourse) persistentExecutionCourse.readByOId(executionCourse, false);
            if (executionCourse == null)
            {
                throw new InvalidArgumentsServiceException();
            }

            IPersistentMetadata persistentMetadata = persistentSuport.getIPersistentMetadata();

            List metadatas = persistentMetadata.readByExecutionCourseAndVisibility(executionCourse);
            List result = new ArrayList();
            Iterator iter = metadatas.iterator();
            while (iter.hasNext())
            {
                IMetadata metadata = (IMetadata) iter.next();
                InfoMetadata infoMetadata = Cloner.copyIMetadata2InfoMetadata(metadata);
                ParseMetadata p = new ParseMetadata();
                try
                {
                    infoMetadata = p.parseMetadata(metadata.getMetadataFile(), infoMetadata, path);
                } catch (Exception e)
                {
                    throw new FenixServiceException(e);
                }
                result.add(infoMetadata);
            }
            InfoSiteMetadatas bodyComponent = new InfoSiteMetadatas();
            bodyComponent.setInfoMetadatas(result);
            bodyComponent.setExecutionCourse(
                Cloner.copyIExecutionCourse2InfoExecutionCourse(executionCourse));
            SiteView siteView = new ExecutionCourseSiteView(bodyComponent, bodyComponent);

            return siteView;
        } catch (ExcepcaoPersistencia e)
        {
            throw new FenixServiceException(e);
        }
    }
}
