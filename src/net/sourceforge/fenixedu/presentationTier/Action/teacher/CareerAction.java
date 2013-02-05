/*
 * Created on 17/Nov/2003
 *  
 */
package net.sourceforge.fenixedu.presentationTier.Action.teacher;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.ReadCategories;
import net.sourceforge.fenixedu.presentationTier.Action.framework.CRUDActionByOID;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.security.UserView;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 * 
 */
@Mapping(module = "teacher", path = "/teachingCareer", input = "/teachingCareer.do?page=0&method=prepareEdit",
        attribute = "teachingCareerForm", formBean = "teachingCareerForm", scope = "request", parameter = "method",
        customMappingClass = net.sourceforge.fenixedu.presentationTier.mapping.framework.CRUDMapping.class,
        customMappingProperties = { "editService", "EditCareer", "deleteService", "DeleteCareer", "readService", "ReadCareer",
                "oidProperty", "idInternal", "requestAttribute", "infoTeachingCareer", "infoObjectClassName",
                "net.sourceforge.fenixedu.dataTransferObject.teacher.InfoTeachingCareer" })
@Forwards(value = { @Forward(name = "successfull-delete", path = "/readCareers.do?careerType=TEACHING&page=0"),
        @Forward(name = "successfull-edit", path = "/readCareers.do?careerType=TEACHING&page=0"),
        @Forward(name = "successfull-read", path = "/readCareers.do?careerType=TEACHING&page=0"),
        @Forward(name = "show-form", path = "edit-teaching-career") })
public class CareerAction extends CRUDActionByOID {
    @Override
    protected void prepareFormConstants(ActionMapping mapping, ActionForm form, HttpServletRequest request)
            throws FenixServiceException, FenixFilterException {
        IUserView userView = UserView.getUser();
        List categories = ReadCategories.run();

        request.setAttribute("categories", categories);
    }
}