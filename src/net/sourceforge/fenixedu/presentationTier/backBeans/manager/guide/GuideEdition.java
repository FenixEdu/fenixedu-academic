package net.sourceforge.fenixedu.presentationTier.backBeans.manager.guide;

import java.util.List;

import javax.faces.application.Application;
import javax.faces.component.UIColumn;
import javax.faces.component.html.HtmlDataTable;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpSession;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoGuide;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.apache.struts.util.LabelValueBean;

public class GuideEdition {

    private Integer guideYear;

    private Integer guideNumber;

    private Integer guideVersion;

    private Integer guideID;

    private String newDegreeCurricularPlanID;

    private String newExecutionYear;

    private InfoGuide guide;

    private IUserView userView;

    private HtmlDataTable guideEntriesDataTable;

    public GuideEdition() {

        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext()
                .getSession(false);
        userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

    }

    public String chooseGuide() {

        try {

            if (guideVersion == null || guideVersion == 0) {
                Object[] args = { guideNumber, guideYear };
                List<InfoGuide> guidesList = (List) ServiceUtils.executeService(userView, "ChooseGuide",
                        args);
                guide = guidesList.get(0);
            } else {
                Object[] args = { guideNumber, guideYear, guideVersion };
                guide = (InfoGuide) ServiceUtils.executeService(userView, "ChooseGuide", args);
            }

            this.setNewDegreeCurricularPlanID(guide.getInfoExecutionDegree()
                    .getInfoDegreeCurricularPlan().getIdInternal()
                    + "");
            this.setNewExecutionYear(guide.getInfoExecutionDegree().getInfoExecutionYear().getYear());
            this.setGuideID(guide.getIdInternal());

            Application application = FacesContext.getCurrentInstance().getApplication();
            this.setGuideEntriesDataTable((HtmlDataTable) application
                    .createComponent(HtmlDataTable.COMPONENT_TYPE));

            this.getGuideEntriesDataTable().setValue(guide.getInfoGuideEntries());
            this.getGuideEntriesDataTable().setVar("guideEntry");

            UIColumn documentTypeColumn = (UIColumn) application
                    .createComponent(UIColumn.COMPONENT_TYPE);
            this.getGuideEntriesDataTable().getChildren().add(documentTypeColumn);

            HtmlOutputText documentOutputText = (HtmlOutputText) application
                    .createComponent(HtmlOutputText.COMPONENT_TYPE);
            // documentOutputText.setValue("a");
            documentOutputText.setValueBinding("value", (ValueBinding) application
                    .createValueBinding("#{guideEntry.documentType}"));            
            documentTypeColumn.getChildren().add(documentOutputText);

            return "guideChoosen";

        } catch (FenixFilterException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NonExistingServiceException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (FenixServiceException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return null;
    }

    public List getDegreeCurricularPlans() {

        try {
            Object[] argsEmpty = {};
            List<LabelValueBean> degreeCurricularPlans = (List) ServiceUtils.executeService(userView,
                    "ReadDegreeCurricularPlansLabelValueBeanList", argsEmpty);

            CollectionUtils.transform(degreeCurricularPlans, new Transformer() {
                public Object transform(Object arg0) {
                    LabelValueBean degreeCurricularPlan = (LabelValueBean) arg0;
                    return new SelectItem(degreeCurricularPlan.getValue(), degreeCurricularPlan
                            .getLabel());
                }
            });

            return degreeCurricularPlans;

        } catch (FenixFilterException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        } catch (FenixServiceException e1) {
            return null;
        }
    }

    public List getExecutionYears() {

        try {
            Object[] argsEmpty = {};
            List<LabelValueBean> executionYears = (List) ServiceUtils.executeService(userView,
                    "ReadExecutionYears", argsEmpty);

            CollectionUtils.transform(executionYears, new Transformer() {
                public Object transform(Object arg0) {
                    LabelValueBean executionYear = (LabelValueBean) arg0;
                    return new SelectItem(executionYear.getValue(), executionYear.getLabel());
                }
            });

            return executionYears;

        } catch (FenixFilterException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        } catch (FenixServiceException e1) {
            return null;
        }
    }

    public void editExecutionDegree(ActionEvent evt) {

        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");

        Object[] args = { this.getGuideID(), new Integer(this.getNewDegreeCurricularPlanID()),
                this.getNewExecutionYear() };

        try {
            ServiceUtils.executeService(userView, "EditGuideInformationInManager", args);
        } catch (FenixFilterException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (FenixServiceException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        restoreData();

    }

    public String deleteGuideVersion() {

        Object[] args = { this.getGuideID() };

        try {
            ServiceUtils.executeService(userView, "DeleteGuideVersionInManager", args);
        } catch (FenixFilterException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (FenixServiceException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return "firstPage";

    }

    private void restoreData() {

        Object[] args = { this.getGuideID() };
        try {
            guide = (InfoGuide) ServiceUtils.executeService(userView, "ReadGuide", args);

            this.setGuideNumber(guide.getNumber());
            this.setGuideYear(guide.getYear());
            this.setGuideVersion(guide.getVersion());
            this.setGuide(guide);

        } catch (FenixFilterException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (FenixServiceException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public Integer getGuideNumber() {
        return guideNumber;
    }

    public void setGuideNumber(Integer guideNumber) {
        this.guideNumber = guideNumber;
    }

    public Integer getGuideVersion() {
        return guideVersion;
    }

    public void setGuideVersion(Integer guideVersion) {
        this.guideVersion = guideVersion;
    }

    public Integer getGuideYear() {
        return guideYear;
    }

    public void setGuideYear(Integer guideYear) {
        this.guideYear = guideYear;
    }

    public InfoGuide getGuide() {
        return guide;
    }

    public void setGuide(InfoGuide guide) {
        this.guide = guide;
    }

    public String getNewDegreeCurricularPlanID() {
        return newDegreeCurricularPlanID;
    }

    public void setNewDegreeCurricularPlanID(String newDegreeCurricularPlanID) {
        this.newDegreeCurricularPlanID = newDegreeCurricularPlanID;
    }

    public String getNewExecutionYear() {
        return newExecutionYear;
    }

    public void setNewExecutionYear(String newExecutionYearID) {
        this.newExecutionYear = newExecutionYearID;
    }

    public Integer getGuideID() {
        return guideID;
    }

    public void setGuideID(Integer guideID) {
        this.guideID = guideID;
    }

    public HtmlDataTable getGuideEntriesDataTable() {
        return guideEntriesDataTable;
    }

    public void setGuideEntriesDataTable(HtmlDataTable guideEntriesDataTable) {
        this.guideEntriesDataTable = guideEntriesDataTable;
    }
    


}
