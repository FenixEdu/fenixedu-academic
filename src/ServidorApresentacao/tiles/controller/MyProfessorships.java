/*
 * Created on 27/Mai/2003 by jpvl
 *  
 */
package ServidorApresentacao.tiles.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.tiles.ComponentContext;
import org.apache.struts.tiles.ControllerSupport;

import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionUtils;

/**
 * @author jpvl
 */
public class MyProfessorships extends ControllerSupport
{

    /*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.struts.tiles.Controller#perform(org.apache.struts.tiles.ComponentContext,
	 *      javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse,
	 *      javax.servlet.ServletContext)
	 */
    public void perform(
        ComponentContext tileContext,
        HttpServletRequest request,
        HttpServletResponse response,
        ServletContext servletContext)
        throws ServletException, IOException
    {

        IUserView userView = SessionUtils.getUserView(request);

        Object[] args = { userView };
        List professorShipList = new ArrayList();
        try
        {
            professorShipList = (List) ServiceUtils.executeService(userView, "ReadProfessorships", args);
        }
        catch (FenixServiceException e)
        {
            e.printStackTrace();
        }
        tileContext.putAttribute("professorshipList", professorShipList);
    }

}
