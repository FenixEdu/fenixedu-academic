/*
 * Created on Apr 3, 2003
 *
 */
package ServidorApresentacao.TagLib.sop.examsMapNew;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.struts.util.MessageResources;

import DataBeans.InfoExamsMap;
import DataBeans.InfoRoomExamsMap;
import ServidorApresentacao.TagLib.sop.examsMapNew.renderers.ExamsMapContentRenderer;
import ServidorApresentacao.TagLib.sop.examsMapNew.renderers.ExamsMapSlotContentRenderer;

/**
 * @author Luis Cruz & Sara Ribeiro
 *
 */
public class RenderExamsMapTag extends TagSupport
{

    // Name of atribute containing ExamMap
    private String name;
    private String user;
    private String mapType;

    private ExamsMapSlotContentRenderer examsMapSlotContentRenderer = new ExamsMapContentRenderer();

    public int doStartTag() throws JspException
    {
        // Obtain InfoExamMap
        InfoExamsMap infoExamsMap = null;
        InfoRoomExamsMap infoRoomExamsMap = null;
        ExamsMap examsMap = null;
        IExamsMapRenderer renderer = null;
        String typeUser = "";
        String typeMapType = "";

        try
        {			
            infoExamsMap = (InfoExamsMap) pageContext.findAttribute(name);
            typeUser = user;
			typeMapType = mapType;
            examsMap = new ExamsMap(infoExamsMap);
            renderer = new ExamsMapRenderer(examsMap, this.examsMapSlotContentRenderer, typeUser, typeMapType);
        }
        catch (ClassCastException e)
        {		
            infoExamsMap = null;
        }
        try
        {        
            infoRoomExamsMap = (InfoRoomExamsMap) pageContext.findAttribute(name);
                     
            typeUser = user;
            examsMap = new ExamsMap(infoRoomExamsMap);
            renderer = new ExamsMapForRoomRenderer(examsMap, this.examsMapSlotContentRenderer, typeUser);
        }
        catch (ClassCastException e)
        {			
            infoRoomExamsMap = null;
        }
        if (infoExamsMap == null && infoRoomExamsMap == null)
        {
            throw new JspException(messages.getMessage("generateExamsMap.infoExamsMap.notFound", name));
        }
		
        // Generate Map from infoExamsMap
        JspWriter writer = pageContext.getOut();
        //ExamsMap examsMap = new ExamsMap(infoExamsMap);

        //		ExamsMapRenderer renderer =
        //			new ExamsMapRenderer(
        //				examsMap,
        //				this.examsMapSlotContentRenderer,
        //				typeUser);

        try
        {	
            writer.print(renderer.render());
        }
        catch (IOException e)
        {	
            throw new JspException(messages.getMessage("generateExamsMap.io", e.toString()));
        }

        return (SKIP_BODY);
    }

    public int doEndTag() throws JspException
    {
        return (EVAL_PAGE);
    }

    public void release()
    {
        super.release();
    }

    // Error Messages
    protected static MessageResources messages =
        MessageResources.getMessageResources("ApplicationResources");

    public String getName()
    {
        return (this.name);
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getUser()
    {
        return user;
    }

    public void setUser(String string)
    {
        user = string;
    }

	public String getMapType()
	{
		return mapType;
	}

	public void setMapType(String string)
	{
		mapType = string;
	}
}
