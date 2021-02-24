/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Academic.
 *
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.academic.domain;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import org.fenixedu.academic.domain.photograph.AspectRatio;
import org.fenixedu.academic.domain.photograph.Picture;
import org.fenixedu.academic.domain.photograph.PictureMode;
import org.fenixedu.academic.domain.photograph.PictureOriginal;
import org.fenixedu.academic.domain.util.email.Message;
import org.fenixedu.academic.domain.util.email.Recipient;
import org.fenixedu.academic.domain.util.email.SystemSender;
import org.fenixedu.academic.predicate.AccessControl;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.academic.util.ContentType;
import org.fenixedu.bennu.core.domain.Avatar;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.imgscalr.Scalr;
import org.imgscalr.Scalr.Method;
import org.imgscalr.Scalr.Mode;
import org.joda.time.DateTime;

import pt.ist.fenixframework.Atomic;

public class Photograph extends Photograph_Base implements Comparable<Photograph> {

    private static final String REJECTION_MAIL_SUBJECT_KEY = "photo.email.subject.rejection";

    private static final String REJECTION_MAIL_BODY_KEY = "photo.email.body.rejection";

    private Photograph() {
        super();
        setRootDomainObject(Bennu.getInstance());
        setSubmission(new DateTime());
    }

    public Photograph(PhotoType photoType, ContentType contentType, byte[] original) {
        this();
        setPhotoType(photoType);
        new PictureOriginal(this, original, contentType);
    }

    @Override
    public Person getPerson() {
        if (super.getPerson() != null) {
            return super.getPerson();
        }
        if (getNext() != null) {
            return getNext().getPerson();
        }
        return null;
    }

    @Override
    public void setPhotoType(PhotoType photoType) {
        super.setPhotoType(photoType);
        if (photoType == PhotoType.INSTITUTIONAL) {
            setState(PhotoState.APPROVED);
        } else if (photoType == PhotoType.USER) {
            setState(PhotoState.PENDING);
        } else {
            setState(PhotoState.PENDING);
        }
    }

    @Override
    public void setState(PhotoState state) {
        if (getState() != state) {
            super.setState(state);
            setStateChange(new DateTime());
            if (state == PhotoState.PENDING) {
                setPendingHolder(Bennu.getInstance());
            } else {
                setPendingHolder(null);
            }
            if (state == PhotoState.REJECTED) {
                logState("log.personInformation.photo.rejected");
                Person person = AccessControl.getPerson();
                if (person != null) {
                    setRejector(person);
                }
                SystemSender systemSender = getRootDomainObject().getSystemSender();
                new Message(systemSender, systemSender.getConcreteReplyTos(),
                        new Recipient(getPerson().getUser().groupOf()).asCollection(),
                        BundleUtil.getString(Bundle.PERSONAL, REJECTION_MAIL_SUBJECT_KEY),
                        BundleUtil.getString(Bundle.PERSONAL, REJECTION_MAIL_BODY_KEY), "");

            }
            if (state == PhotoState.APPROVED) {
                Person person = AccessControl.getPerson();
                if (person != null) {
                    setApprover(person);
                }
                if (getPhotoType() != PhotoType.INSTITUTIONAL) {
                    logState("log.personInformation.photo.approved");
                }
            }
        }
    }

    @Override
    public void setPrevious(Photograph previous) {
        if (previous.getState() == PhotoState.PENDING) {
            previous.setState(PhotoState.USER_REJECTED);
        }
        super.setPrevious(previous);
    }

    @Atomic
    public void cancelSubmission() {
        if (getState() == PhotoState.PENDING) {
            setState(PhotoState.USER_REJECTED);
            logState("log.personInformation.photo.canceled");
        }
    }

    public void delete() {
        if (getPendingHolder() != null) {
            super.setPendingHolder(null);
        }

        Photograph prev = getPrevious();
        if (prev != null) {
            super.setPrevious(null);
            prev.delete();
        }

        Photograph next = getNext();
        if (next != null) {
            super.setNext(null);
        }

        if (getOriginal() != null) {
            getOriginal().delete();
        }

        super.setApprover(null);
        super.setPerson(null);
        super.setRootDomainObject(null);
        super.deleteDomainObject();
    }

    public byte[] getDefaultAvatar() {
        return getCustomAvatar(1, 1, 100, 100, PictureMode.FIT);
    }

    private BufferedImage read(PictureOriginal original) {
        BufferedImage image = Picture.readImage(original.getPictureData());
        BufferedImage result = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
        result.createGraphics().drawImage(image, 0, 0, Color.WHITE, null);
        return result;
    }

    public byte[] getCustomAvatar(int xRatio, int yRatio, int width, int height, PictureMode pictureMode) {
        PictureOriginal original = getOriginal();
        BufferedImage image = original.getPictureFileFormat() == ContentType.JPG ? Picture
                .readImage(original.getPictureData()) : read(original);
        return processImage(image, xRatio, yRatio, width, height, pictureMode);
    }

    public byte[] getCustomAvatar(AspectRatio aspectRatio, int width, int height, PictureMode pictureMode) {
        return getCustomAvatar(aspectRatio.getXRatio(), aspectRatio.getYRatio(), width, height, pictureMode);
    }

    public byte[] getCustomAvatar(int width, int height, PictureMode pictureMode) {
        return getCustomAvatar(width, height, width, height, pictureMode);
    }

    @Override
    public int compareTo(Photograph photo) {
        return getSubmission().compareTo(photo.getSubmission());
    }

    public void logCreate(Person person) {
        if (getState() == PhotoState.PENDING) {
            logState("log.personInformation.photo.created");
        } else if (getState() == PhotoState.APPROVED) {
            if (getPhotoType() == PhotoType.INSTITUTIONAL) {
                logState("log.personInformation.photo.created.noValidation");
            } else {
                logState("log.personInformation.photo.approved");
            }
        }
    }

    private void logState(String keyLabel) {
        final String personViewed = PersonInformationLog.getPersonNameForLogDescription(getPerson());
        PersonInformationLog.createLog(getPerson(), Bundle.MESSAGING, keyLabel, personViewed);
    }

    public byte[] exportAsJPEG(byte[] photo, Color color) {
        BufferedImage image = Picture.readImage(photo);
        BufferedImage jpeg = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
        jpeg.createGraphics().drawImage(image, 0, 0, color, null);
        return Picture.writeImage(jpeg, ContentType.JPG);
    }

    public byte[] exportAsJPEG(byte[] photo) {
        return exportAsJPEG(photo, Color.WHITE);
    }

    public static byte[] mysteryManPhoto(int xRatio, int yRatio, int width, int height, PictureMode pictureMode) {
        try (InputStream mm = Photograph.class.getClassLoader().getResourceAsStream("META-INF/resources/img/mysteryman.png")) {
            return Avatar.process(mm, "image/png", width);
        } catch (IOException e) {
            return null;
        }
    }

    private static byte[] processImage(BufferedImage image, int xRatio, int yRatio, int width, int height,
            PictureMode pictureMode) {
        final BufferedImage transformed, scaled;
        switch (pictureMode) {
        case FIT:
            transformed = Picture.transformFit(image, xRatio, yRatio);
            break;
        case ZOOM:
            transformed = Picture.transformZoom(image, xRatio, yRatio);
            break;
        default:
            transformed = Picture.transformFit(image, xRatio, yRatio);
            break;
        }
        scaled = Scalr.resize(transformed, Method.QUALITY, Mode.FIT_EXACT, width, height);
        return Picture.writeImage(scaled, ContentType.PNG);
    }

}
