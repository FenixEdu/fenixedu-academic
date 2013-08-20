package net.sourceforge.fenixedu.dataTransferObject.alumni.formation;

import java.io.Serializable;
import java.util.Comparator;

import net.sourceforge.fenixedu.domain.EducationArea;

public class AlumniEducationArea implements Serializable {

    public static Comparator<AlumniEducationArea> COMPARATOR_BY_CODE = new Comparator<AlumniEducationArea>() {
        @Override
        public int compare(AlumniEducationArea leftEducationArea, AlumniEducationArea rightEducationArea) {
            int comparationResult =
                    leftEducationArea.getEducationArea().getCode().compareTo(rightEducationArea.getEducationArea().getCode());
            return (comparationResult == 0) ? leftEducationArea.getEducationArea().getExternalId()
                    .compareTo(rightEducationArea.getEducationArea().getExternalId()) : comparationResult;
        }
    };

    private EducationArea educationArea;

    public AlumniEducationArea(EducationArea area) {
        educationArea = area;
    }

    public EducationArea getEducationArea() {
        return this.educationArea;
    }

    public void setEducationArea(EducationArea educationArea) {
        this.educationArea = educationArea;
    }

    // FIXME only used for presentation display purposes
    public String getCodeIndentationValue() {
        String indent = "";
        for (int i = 1; i < getEducationArea().getCode().length(); i++) {
            indent += "&nbsp;&nbsp;";
        }
        return indent;
    }

}
