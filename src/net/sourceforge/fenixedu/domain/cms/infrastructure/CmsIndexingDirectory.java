/**
 * 
 */
package net.sourceforge.fenixedu.domain.cms.infrastructure;

import java.io.IOException;

import org.apache.lucene.store.Directory;
import org.apache.lucene.store.InputStream;
import org.apache.lucene.store.Lock;
import org.apache.lucene.store.OutputStream;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt">Goncalo Luiz</a>
 * <br/>
 * <br/>
 * <br/>
 * Created on 17:05:00,21/Nov/2005
 * @version $Id$
 */
public class CmsIndexingDirectory extends Directory
{

	/* (non-Javadoc)
	 * @see org.apache.lucene.store.Directory#list()
	 */
	@Override
	public String[] list() throws IOException
	{
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.apache.lucene.store.Directory#fileExists(java.lang.String)
	 */
	@Override
	public boolean fileExists(String arg0) throws IOException
	{
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see org.apache.lucene.store.Directory#fileModified(java.lang.String)
	 */
	@Override
	public long fileModified(String arg0) throws IOException
	{
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see org.apache.lucene.store.Directory#touchFile(java.lang.String)
	 */
	@Override
	public void touchFile(String arg0) throws IOException
	{
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see org.apache.lucene.store.Directory#deleteFile(java.lang.String)
	 */
	@Override
	public void deleteFile(String arg0) throws IOException
	{
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see org.apache.lucene.store.Directory#renameFile(java.lang.String, java.lang.String)
	 */
	@Override
	public void renameFile(String arg0, String arg1) throws IOException
	{
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see org.apache.lucene.store.Directory#fileLength(java.lang.String)
	 */
	@Override
	public long fileLength(String arg0) throws IOException
	{
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see org.apache.lucene.store.Directory#createFile(java.lang.String)
	 */
	@Override
	public OutputStream createFile(String arg0) throws IOException
	{
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.apache.lucene.store.Directory#openFile(java.lang.String)
	 */
	@Override
	public InputStream openFile(String arg0) throws IOException
	{
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.apache.lucene.store.Directory#makeLock(java.lang.String)
	 */
	@Override
	public Lock makeLock(String arg0)
	{
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.apache.lucene.store.Directory#close()
	 */
	@Override
	public void close() throws IOException
	{
		// TODO Auto-generated method stub
		
	}



}
