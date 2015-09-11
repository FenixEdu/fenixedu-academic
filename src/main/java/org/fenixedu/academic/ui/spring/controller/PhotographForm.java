package org.fenixedu.academic.ui.spring.controller;

import java.io.Serializable;

public class PhotographForm implements Serializable {

    private static final long serialVersionUID = 1L;

    private String encodedPhoto;

    public String getEncodedPhoto() {
        return encodedPhoto;
    }

    public void setEncodedPhoto(String encodedPhoto) {
        this.encodedPhoto = encodedPhoto;
    }

}
