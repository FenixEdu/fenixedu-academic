package org.fenixedu.academic.ui.spring.controller.teacher.authorization;

import org.fenixedu.academic.domain.TeacherCategory;
import org.fenixedu.bennu.core.domain.User;

public class FormBean extends SearchBean {

    private User user;
    private TeacherCategory category;
    private Double lessonHours;
    private Boolean contracted;
    private Boolean revoked;

    public Boolean getContracted() {
        return contracted;
    }

    public void setContracted(Boolean contracted) {
        this.contracted = contracted;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public TeacherCategory getCategory() {
        return category;
    }

    public void setCategory(TeacherCategory category) {
        this.category = category;
    }

    public Double getLessonHours() {
        return lessonHours;
    }

    public void setLessonHours(Double lessonHours) {
        this.lessonHours = lessonHours;
    }

    public Boolean getRevoked() {
        return revoked;
    }

    public void setRevoked(Boolean revoked) {
        this.revoked = revoked;
    }

}
