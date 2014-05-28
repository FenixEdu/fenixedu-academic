/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
/**
 * 
 */
package net.sourceforge.fenixedu.presentationTier.Action.gep.inquiries;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.inquiries.ResultsFileBean;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.inquiries.InquiryResult;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.gep.GepApplication.GepInquiriesApp;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.portal.EntryPoint;
import org.fenixedu.bennu.portal.StrutsFunctionality;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.utl.ist.fenix.tools.util.FileUtils;

/**
 * @author - Ricardo Rodrigues (ricardo.rodrigues@ist.utl.pt)
 * 
 */
@StrutsFunctionality(app = GepInquiriesApp.class, path = "upload-results", titleKey = "link.inquiries.uploadResults")
@Mapping(path = "/uploadInquiriesResults", module = "gep")
@Forwards(@Forward(name = "prepareUploadPage", path = "/gep/inquiries/uploadInquiriesResults.jsp"))
public class UploadInquiriesResultsDA extends FenixDispatchAction {

    private static final Logger logger = LoggerFactory.getLogger(UploadInquiriesResultsDA.class);

    @EntryPoint
    public ActionForward prepare(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        request.setAttribute("uploadFileBean", new ResultsFileBean());
        return mapping.findForward("prepareUploadPage");
    }

    public ActionForward submitResultsFile(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        ResultsFileBean resultsBean = getRenderedObject("uploadFileBean");
        RenderUtils.invalidateViewState("uploadFileBean");

        try {
            final String stringResults = readFile(resultsBean);
            if (resultsBean.getNewResults()) {
                InquiryResult.importResults(stringResults, resultsBean.getResultsDate());
            } else {
                InquiryResult.updateRows(stringResults, resultsBean.getResultsDate());
            }
            request.setAttribute("success", "true");
        } catch (IOException e) {
            addErrorMessage(request, e.getMessage(), e.getMessage());
        } catch (DomainException e) {
            logger.error(e.getMessage(), e);
            addErrorMessage(request, e.getKey(), e.getKey(), e.getArgs());
        }
        return prepare(mapping, actionForm, request, response);
    }

    private String readFile(ResultsFileBean resultsBean) throws IOException {
        return FileUtils.readFile(resultsBean.getInputStream());
    }
}
