/*
 * Created on Feb 27, 2004
 */
package middleware.grantMigration;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;

/**
 * @author pica
 * @author barbosa
 */
public class GrantMigrationLog
{
    private String filename;
    private FileWriter fileWriter;
    private File file;

	public GrantMigrationLog()
	{
        this.filename = "grants_log_migration.txt";
        file = new File(filename);
        Calendar calendar = Calendar.getInstance();
        try
		{
        	writeLog("\n\n\t Inicio de migração: " + calendar.getTime().toString() + "\n\n");
		}catch(Exception e){}
	}
    public GrantMigrationLog(String filename)
    {
    	this.filename = filename;
        file = new File(filename);
    }
    
    public void writeLog(String text) throws Exception
    {
    	writeFile(text);
    }
    
    public void writeLogLn(String text) throws Exception
    {
    	String ln = text;
    	ln += "\n";
    	writeFile(ln);
    }
    
    /*
     * open a file, writes, and close it.
     */
    private void writeFile(String text) throws Exception
    {
        if(!checkFile()) 
            throw new IOException();
        
        openFileTOWrite(); 
        
    	fileWriter.write(text); //Write to file
        
        closeFileTOWrite();
    }
        
    /*
     * Check is file exists... if not attempt to create
     */
    private boolean checkFile() 
    {
    	if(!file.exists())
        {
            try
            {
            	file.createNewFile();
            }
            catch (IOException e)
            {
                return false;
            }
        }   
        return true;
    }
    
    /*
     * Opens and closes a file
     */
    private void openFileTOWrite() throws Exception
    {
        fileWriter = new FileWriter(filename, true);
    }
    
    private void closeFileTOWrite() throws Exception
    {
        fileWriter.close();
    }
}
