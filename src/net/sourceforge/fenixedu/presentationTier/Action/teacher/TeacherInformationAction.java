/*
 * Created on Nov 15, 2003
 *  
 */
package net.sourceforge.fenixedu.presentationTier.Action.teacher;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.NotAuthorizedFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoTeacher;
import net.sourceforge.fenixedu.dataTransferObject.SiteView;
import net.sourceforge.fenixedu.dataTransferObject.teacher.InfoOrientation;
import net.sourceforge.fenixedu.dataTransferObject.teacher.InfoPublicationsNumber;
import net.sourceforge.fenixedu.dataTransferObject.teacher.InfoServiceProviderRegime;
import net.sourceforge.fenixedu.dataTransferObject.teacher.InfoSiteTeacherInformation;
import net.sourceforge.fenixedu.dataTransferObject.teacher.InfoWeeklyOcupation;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.research.result.Result;
import net.sourceforge.fenixedu.domain.research.result.ResultTeacher;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;
import net.sourceforge.fenixedu.util.OrientationType;
import net.sourceforge.fenixedu.util.ProviderRegimeType;
import net.sourceforge.fenixedu.util.PublicationArea;
import net.sourceforge.fenixedu.util.PublicationType;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.struts.Globals;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 */
public class TeacherInformationAction extends FenixDispatchAction {
    private String getReadService() {
        return "ReadTeacherInformation";
    }

    private String getEditService() {
        return "EditTeacherInformation";
    }

    /**
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return forward to the action mapping configuration called
     *         successfull-edit
     * @throws Exception
     */
    public ActionForward edit(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        InfoServiceProviderRegime infoServiceProviderRegime = getInfoServiceProviderRegimeFromForm(form);
        InfoWeeklyOcupation infoWeeklyOcupation = getInfoWeeklyOcupationFromForm(form);
        List infoOrientations = getInfoOrientationsFromForm(form);
        List infoPublicationsNumber = getInfoPublicationsNumberFromForm(form);
        Object[] args = { infoServiceProviderRegime, infoWeeklyOcupation, infoOrientations,
                infoPublicationsNumber };
        ServiceUtils.executeService(SessionUtils.getUserView(request), getEditService(), args);
        return read(mapping, form, request, response);
    }

    /**
     * This method creates an InfoServiceProviderRegime using the form
     * properties.
     * 
     * @param form
     * @return InfoServiceProviderRegime created
     */
    protected InfoServiceProviderRegime getInfoServiceProviderRegimeFromForm(ActionForm form) {
        DynaActionForm dynaForm = (DynaActionForm) form;
        Integer teacherId = (Integer) dynaForm.get("teacherId");
        Integer serviceProviderRegimeId = (Integer) dynaForm.get("serviceProviderRegimeId");
        ProviderRegimeType providerRegimeType = ProviderRegimeType.getEnum((String) dynaForm
                .get("serviceProviderRegimeTypeName"));

        InfoTeacher infoTeacher = new InfoTeacher(RootDomainObject.getInstance().readTeacherByOID(teacherId));

        InfoServiceProviderRegime infoServiceProviderRegime = new InfoServiceProviderRegime();
        infoServiceProviderRegime.setIdInternal(serviceProviderRegimeId);
        infoServiceProviderRegime.setProviderRegimeType(providerRegimeType);
        infoServiceProviderRegime.setInfoTeacher(infoTeacher);

        return infoServiceProviderRegime;
    }

    /**
     * This method creates an List of infoOrientations using the form
     * properties.
     * 
     * @param form
     * @return InfoServiceProviderRegime created
     */
    protected List getInfoOrientationsFromForm(ActionForm form) {
        DynaActionForm dynaForm = (DynaActionForm) form;
        Integer teacherId = (Integer) dynaForm.get("teacherId");
        Integer degreeOrientationId = (Integer) dynaForm.get("degreeOrientationId");
        String value = (String) dynaForm.get("degreeStudentsNumber");
        Integer degreeStudentsNumber = value.equals("") ? null : new Integer(value);
        String degreeDescription = (String) dynaForm.get("degreeDescription");
        Integer masterOrientationId = (Integer) dynaForm.get("masterOrientationId");
        value = (String) dynaForm.get("masterStudentsNumber");
        Integer masterStudentsNumber = value.equals("") ? null : new Integer(value);
        String masterDescription = (String) dynaForm.get("masterDescription");
        Integer phdOrientationId = (Integer) dynaForm.get("phdOrientationId");
        value = (String) dynaForm.get("phdStudentsNumber");
        Integer phdStudentsNumber = value.equals("") ? null : new Integer(value);
        String phdDescription = (String) dynaForm.get("phdDescription");

        InfoTeacher infoTeacher = new InfoTeacher(RootDomainObject.getInstance().readTeacherByOID(teacherId));

        InfoOrientation degreeOrientation = new InfoOrientation();
        degreeOrientation.setIdInternal(degreeOrientationId);
        degreeOrientation.setNumberOfStudents(degreeStudentsNumber);
        degreeOrientation.setOrientationType(OrientationType.DEGREE);
        degreeOrientation.setDescription(degreeDescription);
        degreeOrientation.setInfoTeacher(infoTeacher);

        InfoOrientation masterOrientation = new InfoOrientation();
        masterOrientation.setIdInternal(masterOrientationId);
        masterOrientation.setNumberOfStudents(masterStudentsNumber);
        masterOrientation.setOrientationType(OrientationType.MASTER);
        masterOrientation.setDescription(masterDescription);
        masterOrientation.setInfoTeacher(infoTeacher);

        InfoOrientation phdOrientation = new InfoOrientation();
        phdOrientation.setIdInternal(phdOrientationId);
        phdOrientation.setNumberOfStudents(phdStudentsNumber);
        phdOrientation.setOrientationType(OrientationType.PHD);
        phdOrientation.setDescription(phdDescription);
        phdOrientation.setInfoTeacher(infoTeacher);

        List infoOrientations = new ArrayList();
        infoOrientations.add(degreeOrientation);
        infoOrientations.add(masterOrientation);
        infoOrientations.add(phdOrientation);
        return infoOrientations;
    }

    /**
     * This method creates an List of infoPublicationsNumber using the form
     * properties.
     * 
     * @param form
     * @return List created
     */
    protected List getInfoPublicationsNumberFromForm(ActionForm form) {
        DynaActionForm dynaForm = (DynaActionForm) form;
        Integer teacherId = (Integer) dynaForm.get("teacherId");
        Integer comunicationPublicationsNumberId = (Integer) dynaForm
                .get("comunicationPublicationsNumberId");
        String value = (String) dynaForm.get("comunicationNational");
        Integer comunicationNational = value.equals("") ? null : new Integer(value);
        value = (String) dynaForm.get("comunicationInternational");
        Integer comunicationInternational = value.equals("") ? null : new Integer(value);
        Integer magArticlePublicationsNumberId = (Integer) dynaForm
                .get("magArticlePublicationsNumberId");
        Integer magArticleNational = new Integer((String) dynaForm.get("magArticleNational"));
        Integer magArticleInternational = new Integer((String) dynaForm.get("magArticleInternational"));
        Integer authorBookPublicationsNumberId = (Integer) dynaForm
                .get("authorBookPublicationsNumberId");
        Integer authorBookNational = new Integer((String) dynaForm.get("authorBookNational"));
        Integer authorBookInternational = new Integer((String) dynaForm.get("authorBookInternational"));
        Integer editorBookPublicationsNumberId = (Integer) dynaForm
                .get("editorBookPublicationsNumberId");
        Integer editorBookNational = new Integer((String) dynaForm.get("editorBookNational"));
        Integer editorBookInternational = new Integer((String) dynaForm.get("editorBookInternational"));
        Integer articlesChaptersPublicationsNumberId = (Integer) dynaForm
                .get("articlesChaptersPublicationsNumberId");
        Integer articlesChaptersNational = new Integer((String) dynaForm.get("articlesChaptersNational"));
        Integer articlesChaptersInternational = new Integer((String) dynaForm
                .get("articlesChaptersInternational"));

        InfoTeacher infoTeacher = new InfoTeacher(RootDomainObject.getInstance().readTeacherByOID(teacherId));

        InfoPublicationsNumber comunicationPublicationsNumber = new InfoPublicationsNumber();
        comunicationPublicationsNumber.setIdInternal(comunicationPublicationsNumberId);
        comunicationPublicationsNumber.setNational(comunicationNational);
        comunicationPublicationsNumber.setInternational(comunicationInternational);
        comunicationPublicationsNumber.setPublicationType(PublicationType.COMUNICATION);
        comunicationPublicationsNumber.setInfoTeacher(infoTeacher);

        InfoPublicationsNumber magArticlePublicationsNumber = new InfoPublicationsNumber();
        magArticlePublicationsNumber.setIdInternal(magArticlePublicationsNumberId);
        magArticlePublicationsNumber.setNational(magArticleNational);
        magArticlePublicationsNumber.setInternational(magArticleInternational);
        magArticlePublicationsNumber.setPublicationType(PublicationType.MAG_ARTICLE);
        magArticlePublicationsNumber.setInfoTeacher(infoTeacher);

        InfoPublicationsNumber authorBookPublicationsNumber = new InfoPublicationsNumber();
        authorBookPublicationsNumber.setIdInternal(authorBookPublicationsNumberId);
        authorBookPublicationsNumber.setNational(authorBookNational);
        authorBookPublicationsNumber.setInternational(authorBookInternational);
        authorBookPublicationsNumber.setPublicationType(PublicationType.AUTHOR_BOOK);
        authorBookPublicationsNumber.setInfoTeacher(infoTeacher);

        InfoPublicationsNumber editorBookPublicationsNumber = new InfoPublicationsNumber();
        editorBookPublicationsNumber.setIdInternal(editorBookPublicationsNumberId);
        editorBookPublicationsNumber.setNational(editorBookNational);
        editorBookPublicationsNumber.setInternational(editorBookInternational);
        editorBookPublicationsNumber.setPublicationType(PublicationType.EDITOR_BOOK);
        editorBookPublicationsNumber.setInfoTeacher(infoTeacher);

        InfoPublicationsNumber articlesChaptersPublicationsNumber = new InfoPublicationsNumber();
        articlesChaptersPublicationsNumber.setIdInternal(articlesChaptersPublicationsNumberId);
        articlesChaptersPublicationsNumber.setNational(articlesChaptersNational);
        articlesChaptersPublicationsNumber.setInternational(articlesChaptersInternational);
        articlesChaptersPublicationsNumber.setPublicationType(PublicationType.ARTICLES_CHAPTERS);
        articlesChaptersPublicationsNumber.setInfoTeacher(infoTeacher);

        List infoPublicationsNumbers = new ArrayList();
        infoPublicationsNumbers.add(comunicationPublicationsNumber);
        infoPublicationsNumbers.add(magArticlePublicationsNumber);
        infoPublicationsNumbers.add(authorBookPublicationsNumber);
        infoPublicationsNumbers.add(editorBookPublicationsNumber);
        infoPublicationsNumbers.add(articlesChaptersPublicationsNumber);
        return infoPublicationsNumbers;
    }

    /**
     * This method creates an InfoWeeklyOcupation using the form properties.
     * 
     * @param form
     * @return InfoWeeklyOcupation created
     */
    protected InfoWeeklyOcupation getInfoWeeklyOcupationFromForm(ActionForm form) {
        DynaActionForm dynaForm = (DynaActionForm) form;
        Integer teacherId = (Integer) dynaForm.get("teacherId");
        Integer weeklyOcupationId = (Integer) dynaForm.get("weeklyOcupationId");
        Integer management = new Integer((String) dynaForm.get("management"));
        Integer other = new Integer((String) dynaForm.get("other"));
        Integer research = new Integer((String) dynaForm.get("research"));
        Integer support = new Integer((String) dynaForm.get("support"));
        Integer lecture = new Integer((String) dynaForm.get("lecture"));

        InfoTeacher infoTeacher = new InfoTeacher(RootDomainObject.getInstance().readTeacherByOID(teacherId));

        InfoWeeklyOcupation infoWeeklyOcupation = new InfoWeeklyOcupation();
        infoWeeklyOcupation.setIdInternal(weeklyOcupationId);
        infoWeeklyOcupation.setManagement(management);
        infoWeeklyOcupation.setOther(other);
        infoWeeklyOcupation.setResearch(research);
        infoWeeklyOcupation.setSupport(support);
        infoWeeklyOcupation.setLecture(lecture);
        infoWeeklyOcupation.setInfoTeacher(infoTeacher);

        return infoWeeklyOcupation;
    }

    /**
     * Tests if errors are presented.
     * 
     * @param request
     * @return
     */
    private boolean hasErrors(HttpServletRequest request) {

        return request.getAttribute(Globals.ERROR_KEY) != null;
    }

    /**
     * @param mapping
     * @param form
     * @param request
     */
    protected void populateFormFrom(ActionMapping mapping, InfoWeeklyOcupation infoWeeklyOcupation,
            InfoServiceProviderRegime infoServiceProviderRegime, List infoOrientations,
            List infoPublicationsNumbers, ActionForm form, HttpServletRequest request) {
        try {
            BeanUtils.copyProperties(form, infoWeeklyOcupation);
            BeanUtils.copyProperties(form, infoServiceProviderRegime);

            InfoTeacher infoTeacher = infoWeeklyOcupation.getInfoTeacher();

            DynaActionForm dynaForm = (DynaActionForm) form;
            dynaForm.set("teacherId", infoTeacher.getIdInternal());
            dynaForm.set("serviceProviderRegimeId", infoServiceProviderRegime.getIdInternal());
            ProviderRegimeType providerRegimeType = infoServiceProviderRegime.getProviderRegimeType();
            dynaForm.set("serviceProviderRegimeTypeName", providerRegimeType == null ? null
                    : providerRegimeType.getName());

            dynaForm.set("weeklyOcupationId", infoWeeklyOcupation.getIdInternal());
            Integer management = infoWeeklyOcupation.getManagement();
            dynaForm.set("management", management == null ? new String() : management.toString());
            Integer research = infoWeeklyOcupation.getResearch();
            dynaForm.set("research", research == null ? new String() : research.toString());
            Integer other = infoWeeklyOcupation.getOther();
            dynaForm.set("other", other == null ? new String() : other.toString());
            Integer support = infoWeeklyOcupation.getSupport();
            dynaForm.set("support", support == null ? new String() : support.toString());
            Integer lecture = infoWeeklyOcupation.getLecture();
            dynaForm.set("lecture", lecture == null ? new String() : lecture.toString());

            Iterator iter = infoOrientations.iterator();
            while (iter.hasNext()) {
                InfoOrientation infoOrientation = (InfoOrientation) iter.next();
                Integer orientationId = infoOrientation.getIdInternal();
                Integer numberOfStudents = infoOrientation.getNumberOfStudents();
                String description = infoOrientation.getDescription();
                if (infoOrientation.getOrientationType().equals(OrientationType.DEGREE)) {
                    dynaForm.set("degreeOrientationId", orientationId);
                    dynaForm.set("degreeDescription", description);
                    dynaForm.set("degreeStudentsNumber", parseInteger(numberOfStudents));
                } else if (infoOrientation.getOrientationType().equals(OrientationType.MASTER)) {
                    dynaForm.set("masterOrientationId", orientationId);
                    dynaForm.set("masterDescription", description);
                    dynaForm.set("masterStudentsNumber", parseInteger(numberOfStudents));
                } else {
                    dynaForm.set("phdOrientationId", orientationId);
                    dynaForm.set("phdDescription", description);
                    dynaForm.set("phdStudentsNumber", parseInteger(numberOfStudents));
                }
            }

            iter = infoPublicationsNumbers.iterator();
            while (iter.hasNext()) {
                InfoPublicationsNumber infoPublicationsNumber = (InfoPublicationsNumber) iter.next();
                Integer publicationsNumberId = infoPublicationsNumber.getIdInternal();
                Integer national = infoPublicationsNumber.getNational();
                Integer international = infoPublicationsNumber.getInternational();

                if (infoPublicationsNumber.getPublicationType().equals(PublicationType.COMUNICATION)) {
                    dynaForm.set("comunicationPublicationsNumberId", publicationsNumberId);
                    dynaForm.set("comunicationNational", national == null ? new String("0") : national
                            .toString());
                    dynaForm.set("comunicationInternational", international == null ? new String("0")
                            : international.toString());
                } else if (infoPublicationsNumber.getPublicationType().equals(
                        PublicationType.MAG_ARTICLE)) {
                    dynaForm.set("magArticlePublicationsNumberId", publicationsNumberId);
                    dynaForm.set("magArticleNational", national == null ? new String("0") : national
                            .toString());
                    dynaForm.set("magArticleInternational", international == null ? new String("0")
                            : international.toString());
                } else if (infoPublicationsNumber.getPublicationType().equals(
                        PublicationType.AUTHOR_BOOK)) {
                    dynaForm.set("authorBookPublicationsNumberId", publicationsNumberId);
                    dynaForm.set("authorBookNational", national == null ? new String("0") : national
                            .toString());
                    dynaForm.set("authorBookInternational", international == null ? new String("0")
                            : international.toString());
                } else if (infoPublicationsNumber.getPublicationType().equals(
                        PublicationType.EDITOR_BOOK)) {
                    dynaForm.set("editorBookPublicationsNumberId", publicationsNumberId);
                    dynaForm.set("editorBookNational", national == null ? new String("0") : national
                            .toString());
                    dynaForm.set("editorBookInternational", international == null ? new String("0")
                            : international.toString());
                } else {
                    dynaForm.set("articlesChaptersPublicationsNumberId", publicationsNumberId);
                    dynaForm.set("articlesChaptersNational", national == null ? new String("0")
                            : national.toString());
                    dynaForm.set("articlesChaptersInternational",
                            international == null ? new String("0") : international.toString());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String parseInteger(Integer numberOfStudents) {
        return (numberOfStudents == null || numberOfStudents.equals(new Integer(0))) ? new String()
                : numberOfStudents.toString();
    }

    /**
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return forward to the action mapping configuration called show-form
     * @throws Exception
     */
    public ActionForward prepareEdit(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        InfoSiteTeacherInformation infoSiteTeacherInformation = readInfoSiteTeacherInformation(mapping,
                form, request);
        if (!hasErrors(request) && infoSiteTeacherInformation != null) {
            InfoServiceProviderRegime infoServiceProviderRegime = infoSiteTeacherInformation
                    .getInfoServiceProviderRegime();
            InfoWeeklyOcupation infoWeeklyOcupation = infoSiteTeacherInformation
                    .getInfoWeeklyOcupation();
            List infoOrientations = new ArrayList();
            infoOrientations.add(infoSiteTeacherInformation.getInfoDegreeOrientation());
            infoOrientations.add(infoSiteTeacherInformation.getInfoMasterOrientation());
            infoOrientations.add(infoSiteTeacherInformation.getInfoPhdOrientation());
            List infoPublicationsNumbers = new ArrayList();
            infoPublicationsNumbers.add(infoSiteTeacherInformation
                    .getInfoComunicationPublicationsNumber());
            infoPublicationsNumbers
                    .add(infoSiteTeacherInformation.getInfoMagArticlePublicationsNumber());
            infoPublicationsNumbers.add(infoSiteTeacherInformation
                    .getInfoArticleChapterPublicationsNumber());
            infoPublicationsNumbers.add(infoSiteTeacherInformation.getInfoEditBookPublicationsNumber());
            infoPublicationsNumbers
                    .add(infoSiteTeacherInformation.getInfoAuthorBookPublicationsNumber());
            populateFormFrom(mapping, infoWeeklyOcupation, infoServiceProviderRegime, infoOrientations,
                    infoPublicationsNumbers, form, request);
        }
        setInfoSiteTeacherInformationToRequest(request, infoSiteTeacherInformation, mapping);
        request.setAttribute("providerRegimeTypeList", ProviderRegimeType.getEnumList());
        
        
        /* 
         * This code is to replace PublicationTeacher by ResultTeacher.
         * Sergio Patricio & Luis Santos
        /*
        List<ResultTeacher> teacherResults = getUserView(request).getPerson().getTeacher()
                .getTeacherResults();
        List<Result> didaticResults = new ArrayList<Result>();
        List<Result> cientificResults = new ArrayList<Result>();
        for (ResultTeacher resultTeacher : teacherResults) {
            if (resultTeacher.getPublicationArea().equals(PublicationArea.DIDATIC))
                didaticResults.add(resultTeacher.getResult());
            else
                // PublicationArea.CIENTIFIC
                cientificResults.add(resultTeacher.getResult());
        }
        request.setAttribute("didaticResults",didaticResults);
        request.setAttribute("cientificResults",cientificResults);*/
        
        /*END modification*/
        
        return mapping.findForward("show-form");
    }

    /**
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return forward to the action mapping configuration called
     *         successfull-read
     * @throws Exception
     */
    public ActionForward read(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        try {
            InfoSiteTeacherInformation infoSiteTeacherInformation = readInfoSiteTeacherInformation(
                    mapping, form, request);
            setInfoSiteTeacherInformationToRequest(request, infoSiteTeacherInformation, mapping);
            return mapping.findForward("successfull-read");
        } catch (NotAuthorizedFilterException e) {
            return mapping.findForward("unsuccessfull-read");
        }
    }

    /**
     * Reads the infoObject using de read service associated to the action
     * 
     * @param mapping
     * @param form
     * @return
     */
    private InfoSiteTeacherInformation readInfoSiteTeacherInformation(ActionMapping mapping,
            ActionForm form, HttpServletRequest request) throws FenixServiceException,
            FenixFilterException {
        IUserView userView = SessionUtils.getUserView(request);
        Object[] args = { userView.getUtilizador(), new String() };
        SiteView siteView = (SiteView) ServiceUtils.executeService(userView, getReadService(), args);
        return (InfoSiteTeacherInformation) siteView.getComponent();
    }

    /**
     * @param request
     * @param infoObject
     */
    private void setInfoSiteTeacherInformationToRequest(HttpServletRequest request,
            InfoSiteTeacherInformation infoSiteTeacherInformation, ActionMapping mapping) {
        if (infoSiteTeacherInformation != null) {
            request.setAttribute("infoSiteTeacherInformation", infoSiteTeacherInformation);
        }
    }
}