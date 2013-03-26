package net.sourceforge.fenixedu.domain.phd.serviceRequests;

import java.io.Serializable;

public class PhdAcademicServiceRequestDisplacementBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer horizontalOffset;
    private Integer verticalOffset;

    public PhdAcademicServiceRequestDisplacementBean(final PhdAcademicServiceRequest academicServiceRequest) {
        setHorizontalOffset(academicServiceRequest.getHorizontalOffset());
        setVerticalOffset(academicServiceRequest.getVerticalOffset());
    }

    public Integer getHorizontalOffset() {
        return horizontalOffset;
    }

    public void setHorizontalOffset(Integer xOffset) {
        this.horizontalOffset = xOffset;
    }

    public Integer getVerticalOffset() {
        return verticalOffset;
    }

    public void setVerticalOffset(Integer yOffset) {
        this.verticalOffset = yOffset;
    }

}
