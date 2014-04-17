package net.sourceforge.fenixedu.presentationTier.Action.student.thesis;

import java.io.InputStream;
import java.io.Serializable;
import java.util.Locale;

import net.sourceforge.fenixedu.domain.thesis.Thesis;

public class ThesisFileBean implements Serializable {

    /**
     * Serial Version id.
     */
    private static final long serialVersionUID = 1L;

    private String title;
    private String subTitle;
    private Locale language;
    private String fileName;
    private Long fileSize;

    transient private InputStream file;

    public ThesisFileBean() {
    }

    public ThesisFileBean(final Thesis thesis) {
        title = thesis.getFinalTitle().getContent();
        subTitle = thesis.getFinalSubtitle() != null ? thesis.getFinalSubtitle().getContent() : null;
        language = thesis.getFinalFullTitle().getContentLocale();
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubTitle() {
        return this.subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public InputStream getFile() {
        return this.file;
    }

    public void setFile(InputStream file) {
        this.file = file;
    }

    public String getFileName() {
        return this.fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Long getFileSize() {
        return this.fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public Locale getLanguage() {
        return this.language;
    }

    public void setLanguage(Locale language) {
        this.language = language;
    }

    public String getSimpleFileName() {
        String name = getFileName();

        if (name == null) {
            return null;
        }

        char separator;
        if (name.matches("[\\p{Alpha}]:\\\\.*")) {
            separator = '\\';
        } else {
            separator = '/';
        }

        return name.substring(name.lastIndexOf(separator) + 1);
    }

}
