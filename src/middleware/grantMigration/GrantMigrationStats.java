/*
 * Created on Feb 25, 2004
 */
package middleware.grantMigration;

/**
 * @author pica
 * @author barbosa
 */
public class GrantMigrationStats
{
    //Grant Owner stats
    private int mwGrantOwnerRead = 0;
    private int mwGrantOwnerMigrated = 0;
    private int mwGrantOwnerErrors = 0;
    //Person Stats
    private int mwPersonRead = 0;
    private int mwPersonMigrated = 0;
    private int mwPersonErrors = 0;
    //Teacher Stats
    private int mwTeacherRead = 0;
    private int mwTeacherMigrated = 0;
    private int mwTeacherErrors = 0;
    
	public GrantMigrationStats()
	{
	}

    /*****************************************************
     * Presenting stats
     ****************************************************/
    
    public String presentStats()
    {
    	String result = null;
    	//Printing stats
        result += ("\n\n-------------------- Grant Migration Stats ---------------------\n");
        result += presentGrantOwnerStats();
        result += presentGrantPersonStats();
        result += presentGrantTeacherStats();
        result += ("\n----------------------------------------------------------------\n");
        return result;
    }
    
    private String presentGrantOwnerStats()
    {
        String stats = "\n-MW Grant Owner Stats\n";
        stats += "\n\tObjects Read: ";
        stats += getMwGrantOwnerRead();
        stats += "\n\tObjects Fixed: ";
        stats += getMwGrantOwnerMigrated();
        stats += "\n\tErrors: ";
        stats += getMwGrantOwnerErrors();
        stats += "\n";
        return stats;
    }
    
    private String presentGrantPersonStats()
    {
        String stats = "\n-MW Person Stats\n";
        stats += "\n\tObjects Read: ";
        stats += getMwPersonRead();
        stats += "\n\tObjects Migrated: ";
        stats += getMwPersonMigrated();
        stats += "\n\tErrors: ";
        stats += getMwPersonErrors();
        stats += "\n";
        return stats;
    }
    private String presentGrantTeacherStats()
    {
        String stats = "\n-MW Teacher Stats\n";
        stats += "\n\tObjects Read: ";
        stats += getMwTeacherRead();
        stats += "\n\tObjects Migrated: ";
        stats += getMwTeacherMigrated();
        stats += "\n\tErrors: ";
        stats += getMwTeacherErrors();
        stats += "\n";
        return stats;
    }
    
    /*****************************************************
     *          Getters and setters, and increment
     * ****************************************************/
        
	
	/**
	 * @return Returns the mwGrantOwnerErrors.
	 */
	public int getMwGrantOwnerErrors()
	{
		return mwGrantOwnerErrors;
	}

	/**
	 * @param mwGrantOwnerErrors The mwGrantOwnerErrors to set.
	 */
	public void setMwGrantOwnerErrors(int mwGrantOwnerErrors)
	{
		this.mwGrantOwnerErrors = mwGrantOwnerErrors;
	}
    public void incrementMwGrantOwnerErrors()
    {
        this.mwGrantOwnerErrors++;
    }
	/**
	 * @return Returns the mwGrantOwnerMigrated.
	 */
	public int getMwGrantOwnerMigrated()
	{
		return mwGrantOwnerMigrated;
	}

	/**
	 * @param mwGrantOwnerMigrated The mwGrantOwnerMigrated to set.
	 */
	public void setMwGrantOwnerMigrated(int mwGrantOwnerMigrated)
	{
		this.mwGrantOwnerMigrated = mwGrantOwnerMigrated;
	}
    public void incrementMwGrantOwnerMigrated()
    {
        this.mwGrantOwnerMigrated++;
    }
	/**
	 * @return Returns the mwGrantOwnerRead.
	 */
	public int getMwGrantOwnerRead()
	{
		return mwGrantOwnerRead;
	}

	/**
	 * @param mwGrantOwnerRead The mwGrantOwnerRead to set.
	 */
	public void setMwGrantOwnerRead(int mwGrantOwnerRead)
	{
		this.mwGrantOwnerRead = mwGrantOwnerRead;
	}
    public void incrementMwGrantOwnerRead()
    {
        this.mwGrantOwnerRead++;
    }
    
	/**
	 * @return Returns the mwPersonErrors.
	 */
	public int getMwPersonErrors()
	{
		return mwPersonErrors;
	}

	/**
	 * @param mwPersonErrors The mwPersonErrors to set.
	 */
	public void setMwPersonErrors(int mwPersonErrors)
	{
		this.mwPersonErrors = mwPersonErrors;
	}
    public void incrementMwPersonErrors()
    {
        this.mwPersonErrors++;
    }
    
    
	/**
	 * @return Returns the mwPersonMigrated.
	 */
	public int getMwPersonMigrated()
	{
		return mwPersonMigrated;
	}

	/**
	 * @param mwPersonMigrated The mwPersonMigrated to set.
	 */
	public void setMwPersonMigrated(int mwPersonMigrated)
	{
		this.mwPersonMigrated = mwPersonMigrated;
	}
    public void incrementMwPersonMigrated()
    {
        this.mwPersonMigrated++;
    }
    
	/**
	 * @return Returns the mwPersonRead.
	 */
	public int getMwPersonRead()
	{
		return mwPersonRead;
	}

	/**
	 * @param mwPersonRead The mwPersonRead to set.
	 */
	public void setMwPersonRead(int mwPersonRead)
	{
		this.mwPersonRead = mwPersonRead;
	}
    public void incrementMwPersonRead()
    {
        this.mwPersonRead++;
    }
    public void incrementMwPersonRead(int n)
    {
        this.mwPersonRead += n;
    }
    
	/**
	 * @return Returns the mwTeacherErrors.
	 */
	public int getMwTeacherErrors()
	{
		return mwTeacherErrors;
	}

	/**
	 * @param mwTeacherErrors The mwTeacherErrors to set.
	 */
	public void setMwTeacherErrors(int mwTeacherErrors)
	{
		this.mwTeacherErrors = mwTeacherErrors;
	}
    public void incrementMwTeacherErrors()
    {
        this.mwTeacherErrors++;
    }
    
	/**
	 * @return Returns the mwTeacherMigrated.
	 */
	public int getMwTeacherMigrated()
	{
		return mwTeacherMigrated;
	}

	/**
	 * @param mwTeacherMigrated The mwTeacherMigrated to set.
	 */
	public void setMwTeacherMigrated(int mwTeacherMigrated)
	{
		this.mwTeacherMigrated = mwTeacherMigrated;
	}
    public void incrementMwTeacherMigrated()
    {
        this.mwTeacherMigrated++;
    }
    
	/**
	 * @return Returns the mwTeacherRead.
	 */
	public int getMwTeacherRead()
	{
		return mwTeacherRead;
	}

	/**
	 * @param mwTeacherRead The mwTeacherRead to set.
	 */
	public void setMwTeacherRead(int mwTeacherRead)
	{
		this.mwTeacherRead = mwTeacherRead;
	}
    public void incrementMwTeacherRead()
    {
        this.mwTeacherRead++;
    }
}
