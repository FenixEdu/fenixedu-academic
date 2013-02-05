package net.sourceforge.fenixedu.presentationTier.renderers.student.enrollment.bolonha;

import pt.ist.fenixWebFramework.renderers.layouts.Layout;

public class ErasmusBolonhaStudentEnrollmentInputRenderer extends BolonhaStudentEnrollmentInputRenderer {
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
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
