package net.sourceforge.fenixedu.presentationTier.renderers.student.enrollment.bolonha;

import java.util.ResourceBundle;

import pt.ist.fenixWebFramework.renderers.InputRenderer;
import pt.ist.fenixWebFramework.renderers.layouts.Layout;
import pt.utl.ist.fenix.tools.util.i18n.Language;

public class BolonhaStudentEnrollmentInputRenderer extends InputRenderer {

    final ResourceBundle enumerationResources = ResourceBundle.getBundle("resources.EnumerationResources", Language.getLocale());
    final ResourceBundle studentResources = ResourceBundle.getBundle("resources.StudentResources", Language.getLocale());
    final ResourceBundle applicationResources = ResourceBundle.getBundle("resources.ApplicationResources", Language.getLocale());

    private Integer initialWidth = 70;
    private Integer widthDecreasePerLevel = 3;

    private String tablesClasses = "showinfo3 mvert0";
    private String groupRowClasses = "bgcolor2";
    private String concludedGroupRowClasses = "cc_groups";
    private String enrolmentClasses = "smalltxt, smalltxt aright, smalltxt aright, smalltxt aright, aright";
    private String temporaryEnrolmentClasses = "smalltxt, smalltxt aright, smalltxt aright, smalltxt aright, aright";
    private String impossibleEnrolmentClasses = "smalltxt, smalltxt aright, smalltxt aright, smalltxt aright, aright";
    private String curricularCoursesToEnrolClasses = "smalltxt, smalltxt aright, smalltxt aright, aright";

    private boolean encodeGroupRules = false;
    private boolean encodeCurricularRules = true;
    private String defaultLayout = null;

    public String getDefaultLayout() {
        return defaultLayout;
    }

    public void setDefaultLayout(String defaultLayout) {
        this.defaultLayout = defaultLayout;
    }

    public Integer getInitialWidth() {
        return initialWidth;
    }

    public void setInitialWidth(Integer initialWidth) {
        this.initialWidth = initialWidth;
    }

    public Integer getWidthDecreasePerLevel() {
        return widthDecreasePerLevel;
    }

    public void setWidthDecreasePerLevel(Integer widthDecreasePerLevel) {
        this.widthDecreasePerLevel = widthDecreasePerLevel;
    }

    public String getTablesClasses() {
        return tablesClasses;
    }

    public void setTablesClasses(String tablesClasses) {
        this.tablesClasses = tablesClasses;
    }

    public String getGroupRowClasses() {
        return groupRowClasses;
    }

    public void setGroupRowClasses(String groupRowClasses) {
        this.groupRowClasses = groupRowClasses;
    }

    public String getConcludedGroupRowClasses() {
        return concludedGroupRowClasses;
    }

    public void setConcludedGroupRowClasses(String concludedGroupRowClasses) {
        this.concludedGroupRowClasses = concludedGroupRowClasses;
    }

    private String[] getEnrolmentClasses() {
        return enrolmentClasses.split(",");
    }

    public void setEnrolmentClasses(String enrolmentClasses) {
        this.enrolmentClasses = enrolmentClasses;
    }

    String getEnrolmentNameClasses() {
        return getEnrolmentClasses()[0];
    }

    String getEnrolmentYearClasses() {
        return getEnrolmentClasses()[1];
    }

    String getEnrolmentSemesterClasses() {
        return getEnrolmentClasses()[2];
    }

    String getEnrolmentEctsClasses() {
        return getEnrolmentClasses()[3];
    }

    String getEnrolmentCheckBoxClasses() {
        return getEnrolmentClasses()[4];
    }

    private String[] getCurricularCourseToEnrolClasses() {
        return curricularCoursesToEnrolClasses.split(",");
    }

    public void setCurricularCourseToEnrolClasses(String curricularCoursesToEnrol) {
        this.curricularCoursesToEnrolClasses = curricularCoursesToEnrol;
    }

    String getCurricularCourseToEnrolNameClasses() {
        return getCurricularCourseToEnrolClasses()[0];
    }

    String getCurricularCourseToEnrolYearClasses() {
        return getCurricularCourseToEnrolClasses()[1];
    }

    String getCurricularCourseToEnrolEctsClasses() {
        return getCurricularCourseToEnrolClasses()[2];
    }

    String getCurricularCourseToEnrolCheckBoxClasses() {
        return getCurricularCourseToEnrolClasses()[3];
    }

    private String[] getTemporaryEnrolmentClasses() {
        return temporaryEnrolmentClasses.split(",");
    }

    public void setTemporaryEnrolmentClasses(String enrolmentClasses) {
        this.temporaryEnrolmentClasses = enrolmentClasses;
    }

    String getTemporaryEnrolmentNameClasses() {
        return getTemporaryEnrolmentClasses()[0];
    }

    String getTemporaryEnrolmentYearClasses() {
        return getTemporaryEnrolmentClasses()[1];
    }

    String getTemporaryEnrolmentSemesterClasses() {
        return getTemporaryEnrolmentClasses()[2];
    }

    String getTemporaryEnrolmentEctsClasses() {
        return getTemporaryEnrolmentClasses()[3];
    }

    String getTemporaryEnrolmentCheckBoxClasses() {
        return getTemporaryEnrolmentClasses()[4];
    }

    private String[] getImpossibleEnrolmentClasses() {
        return impossibleEnrolmentClasses.split(",");
    }

    public void setImpossibleEnrolmentClasses(String enrolmentClasses) {
        this.impossibleEnrolmentClasses = enrolmentClasses;
    }

    String getImpossibleEnrolmentNameClasses() {
        return getImpossibleEnrolmentClasses()[0];
    }

    String getImpossibleEnrolmentYearClasses() {
        return getImpossibleEnrolmentClasses()[1];
    }

    String getImpossibleEnrolmentSemesterClasses() {
        return getImpossibleEnrolmentClasses()[2];
    }

    String getImpossibleEnrolmentEctsClasses() {
        return getImpossibleEnrolmentClasses()[3];
    }

    String getImpossibleEnrolmentCheckBoxClasses() {
        return getImpossibleEnrolmentClasses()[4];
    }

    public BolonhaStudentEnrollmentInputRenderer() {
        super();
    }

    public boolean isEncodeCurricularRules() {
        return encodeCurricularRules;
    }

    public void setEncodeCurricularRules(boolean encodeCurricularRules) {
        this.encodeCurricularRules = encodeCurricularRules;
    }

    public boolean isEncodeGroupRules() {
        return encodeGroupRules;
    }

    public void setEncodeGroupRules(boolean encodeGroupRules) {
        this.encodeGroupRules = encodeGroupRules;
    }

    @Override
    protected Layout getLayout(Object object, Class type) {
        BolonhaStudentEnrolmentLayout thisLayout =
                (BolonhaStudentEnrolmentLayout) ((defaultLayout == null) ? new BolonhaStudentEnrolmentLayout() : createLayout());
        thisLayout.setRenderer(this);
        return thisLayout;
    }

    private Layout createLayout() {
        try {
            Class<?> clazz = Class.forName(defaultLayout);
            return (Layout) clazz.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

}
