package net.sourceforge.fenixedu.presentationTier.renderers.student.enrollment.bolonha;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ist.fenixWebFramework.renderers.layouts.Layout;

public class ErasmusBolonhaStudentEnrollmentInputRenderer extends BolonhaStudentEnrollmentInputRenderer {

    private static final Logger logger = LoggerFactory.getLogger(ErasmusBolonhaStudentEnrollmentInputRenderer.class);

    @Override
    protected Layout getLayout(Object object, Class type) {
        ErasmusBolonhaStudentEnrolmentLayout thisLayout =
                (ErasmusBolonhaStudentEnrolmentLayout) ((getDefaultLayout() == null) ? new ErasmusBolonhaStudentEnrolmentLayout() : createLayout());
        thisLayout.setRenderer(this);
        return thisLayout;
    }

    private Layout createLayout() {
        try {
            Class<?> clazz = Class.forName(getDefaultLayout());
            return (Layout) clazz.newInstance();
        } catch (InstantiationException e) {
            logger.error(e.getMessage(), e);
        } catch (IllegalAccessException e) {
            logger.error(e.getMessage(), e);
        } catch (ClassNotFoundException e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }
}
