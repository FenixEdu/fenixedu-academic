package Tools;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;

/*
 * Created on Oct 25, 2003
 *  
 */

/**
 * @author Shezad Anavarali (sana@mega.ist.utl.pt) 
 * @author Nadir Tarmahomed (naat@mega.ist.utl.pt)
 */
public class SmartDataSetGeneratorMain
{

    /**
	 *  
	 */
    public SmartDataSetGeneratorMain()
    {
        super();
    }

    public static void main(String[] args)
    {
        SmartDataSetGenerator dataSetGen = null;

        try
        {
            if (args.length > 0)
            {
                String filePath = args[0];
                filePath = filePath.replaceAll("\\", "/");
                dataSetGen = new SmartDataSetGenerator(filePath);
            }
            else
            {
                dataSetGen = new SmartDataSetGenerator();
            }

            dataSetGen.writeDataSet();
        }
        catch (NullPointerException e)
        {
            System.out.println("Configuration file not found.Wrong Format");
        }
        catch (FileNotFoundException e)
        {
            System.out.println("Configuration file not found.");
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        catch (SQLException e)
        {
            
            e.printStackTrace();
        }
        catch (Exception e)
        {
            
            e.printStackTrace();
        }
    }
}
