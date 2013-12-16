package net.sourceforge.fenixedu.domain;

import java.awt.Color;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.ResourceBundle;
import java.util.Set;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;

import net.sourceforge.fenixedu.domain.accessControl.PersonGroup;
import net.sourceforge.fenixedu.domain.contacts.EmailAddress;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.photograph.AspectRatio;
import net.sourceforge.fenixedu.domain.photograph.Picture;
import net.sourceforge.fenixedu.domain.photograph.PictureMode;
import net.sourceforge.fenixedu.domain.photograph.PictureOriginal;
import net.sourceforge.fenixedu.domain.photograph.PictureSize;
import net.sourceforge.fenixedu.domain.util.email.Message;
import net.sourceforge.fenixedu.domain.util.email.Recipient;
import net.sourceforge.fenixedu.domain.util.email.Sender;
import net.sourceforge.fenixedu.domain.util.email.SystemSender;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.util.ByteArray;
import net.sourceforge.fenixedu.util.ContentType;

import org.imgscalr.Scalr;
import org.imgscalr.Scalr.Method;
import org.imgscalr.Scalr.Mode;
import org.joda.time.DateTime;

import pt.ist.bennu.core.domain.Bennu;
import pt.ist.fenixframework.Atomic;
import pt.utl.ist.fenix.tools.util.i18n.Language;

public class Photograph extends Photograph_Base implements Comparable<Photograph> {

    private static final int COMPRESSED_PHOTO_WIDTH = 100;

    private static final int COMPRESSED_PHOTO_HEIGHT = 100;

    private static final String RESOURCE_BUNDLE_NAME = "resources.PersonalInformationResources";

    private static final String REJECTION_MAIL_SUBJECT_KEY = "photo.email.subject.rejection";

    private static final String REJECTION_MAIL_BODY_KEY = "photo.email.body.rejection";

    private Photograph() {
        super();
        setRootDomainObject(Bennu.getInstance());
        setSubmission(new DateTime());
    }

    @Deprecated
    public Photograph(ContentType contentType, ByteArray content, ByteArray compressed, PhotoType photoType) {
        this();
        setContentType(contentType);
        setPhotoType(photoType);
        new PhotographContent(this, PhotographContentSize.DOC_SIZE, compressed);
        if (content != null) {
            new PhotographContent(this, PhotographContentSize.RAW, content);
        }
    }

    @Deprecated
    public Photograph(ContentType contentType, ByteArray content, PhotoType photoType) {
        this(contentType, content, compressImage(content.getBytes(), contentType), photoType);
    }

    public Photograph(PhotoType photoType, ContentType contentType, ByteArray original) {
        this();
        setPhotoType(photoType);
        new PictureOriginal(this, original, contentType);
    }

    @Override
    @Deprecated
    public ContentType getContentType() {
        return super.getContentType();
        //throw new DomainException("error.photograph.illegalCall");
    }

    @Override
    public Person getPerson() {
        if (super.getPerson() != null) {
            return super.getPerson();
        }
        if (hasNext()) {
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
                final EmailAddress institutionalOrDefaultEmailAddress = getPerson().getInstitutionalOrDefaultEmailAddress();
                if (institutionalOrDefaultEmailAddress != null) {
                    ResourceBundle bundle = ResourceBundle.getBundle(RESOURCE_BUNDLE_NAME, Language.getDefaultLocale());
                    Set<String> sendTo = Collections.singleton(institutionalOrDefaultEmailAddress.getValue());

                    SystemSender systemSender = getRootDomainObject().getSystemSender();
                    new Message(systemSender, systemSender.getConcreteReplyTos(),
                            new Recipient(new PersonGroup(getPerson())).asCollection(),
                            bundle.getString(REJECTION_MAIL_SUBJECT_KEY), bundle.getString(REJECTION_MAIL_BODY_KEY), "");
                }

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

    @Deprecated
    public byte[] getContents() {
        return getContents(PhotographContentSize.DOC_SIZE);
    }

    @Deprecated
    public byte[] getContents(PhotographContentSize size) {
        for (PhotographContent content : getContentSet()) {
            if (content.getSize().equals(size)) {
                return content.getBytes();
            }
        }
        throw new DomainException("error.photograph.has.no.content.of.requested.size");
    }

    @Atomic
    public void cancelSubmission() {
        if (getState() == PhotoState.PENDING) {
            setState(PhotoState.USER_REJECTED);
            logState("log.personInformation.photo.canceled");
        }
    }

    public void delete() {
        setRootDomainObject(null);
        if (hasPendingHolder()) {
            setPendingHolder(null);
        }
        setPerson(null);
        Photograph prev = getPrevious();
        if (prev != null) {
            setPrevious(null);
            prev.delete();
        }
        Photograph next = getNext();
        if (next != null) {
            setNext(null);
        }
        super.deleteDomainObject();
    }

    public byte[] getDefaultAvatar() {
        final PictureOriginal original = getOriginal();
        final BufferedImage image = original.getPictureFileFormat() == ContentType.JPG ?
                Picture.readImage(original.getPictureData().getBytes()) : read(original);

        final BufferedImage adjustedImage = Picture.transformFit(image, 1, 1);
        final BufferedImage avatar = Scalr.resize(adjustedImage, Method.QUALITY, Mode.FIT_EXACT, 100, 100);
                //Picture.fitTo(adjustedImage, 200, 200);
        return Picture.writeImage(avatar, ContentType.PNG).getBytes();
    }

    private BufferedImage read(PictureOriginal original) {
        BufferedImage image = Picture.readImage(original.getPictureData().getBytes());
        BufferedImage result = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
        result.createGraphics().drawImage(image, 0, 0, Color.WHITE, null);
        return result;
    }

    public byte[] getCustomAvatar(int xRatio, int yRatio, int width, int height, PictureMode pictureMode) {
        BufferedImage image, transformed, scaled;
        image = Picture.readImage(getOriginal().getPictureData().getBytes());
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
        scaled = Picture.fitTo(transformed, width, height);
        return Picture.writeImage(scaled, getOriginal().getPictureFileFormat()).getBytes();
    }

    public byte[] getCustomAvatar(AspectRatio aspectRatio, int width, int height, PictureMode pictureMode) {
        return getCustomAvatar(aspectRatio.getXRatio(), aspectRatio.getYRatio(), width, height, pictureMode);
    }

    public byte[] getCustomAvatar(int width, int height, PictureMode pictureMode) {
        return getCustomAvatar(width, height, width, height, pictureMode);
    }

    @Deprecated
    static private ByteArray compressImage(byte[] content, ContentType contentType) {
        BufferedImage image;
        try {
            image = ImageIO.read(new ByteArrayInputStream(content));
        } catch (IOException e) {
            throw new DomainException("photograph.compress.errorReadingImage", e);
        }
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        // calculate resize factor
        double resizeFactor =
                Math.min((double) COMPRESSED_PHOTO_WIDTH / image.getWidth(), (double) COMPRESSED_PHOTO_HEIGHT / image.getHeight());

        if (resizeFactor == 1) {
            return new ByteArray(content);
        }

        // resize image
        AffineTransform tx = new AffineTransform();
        tx.scale(resizeFactor, resizeFactor);
        AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
        image = op.filter(image, null);

        // set compression
        ImageWriter writer = ImageIO.getImageWritersByMIMEType(contentType.getMimeType()).next();
        ImageWriteParam param = writer.getDefaultWriteParam();
        if (contentType == ContentType.JPG) {
            param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            param.setCompressionQuality(1);
        }

        // write to stream
        try {
            writer.setOutput(ImageIO.createImageOutputStream(outputStream));
            writer.write(null, new IIOImage(image, null, null), param);
        } catch (IOException e) {
            throw new DomainException("photograph.compress.errorWritingImage", e);
        }
        return new ByteArray(outputStream.toByteArray());
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
        PersonInformationLog.createLog(getPerson(), "resources.MessagingResources", keyLabel, personViewed);
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.PhotographContent> getContent() {
        return getContentSet();
    }

    @Deprecated
    public boolean hasAnyContent() {
        return !getContentSet().isEmpty();
    }

    @Deprecated
    public boolean hasNext() {
        return getNext() != null;
    }

    @Deprecated
    public boolean hasPendingHolder() {
        return getPendingHolder() != null;
    }

    @Deprecated
    public boolean hasRejector() {
        return getRejector() != null;
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasStateChange() {
        return getStateChange() != null;
    }

    @Deprecated
    public boolean hasState() {
        return getState() != null;
    }

    @Deprecated
    public boolean hasPrevious() {
        return getPrevious() != null;
    }

    @Deprecated
    public boolean hasApprover() {
        return getApprover() != null;
    }

    @Deprecated
    public boolean hasSubmission() {
        return getSubmission() != null;
    }

    @Deprecated
    public boolean hasPhotoType() {
        return getPhotoType() != null;
    }

    @Deprecated
    public boolean hasPerson() {
        return getPerson() != null;
    }

    @Deprecated
    public boolean hasContentType() {
        return getContentType() != null;
    }

    public byte[] exportAsJPEG(byte[] photo, Color color) {
        BufferedImage image = Picture.readImage(photo);
        BufferedImage jpeg = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
        jpeg.createGraphics().drawImage(image, 0, 0, color, null);
        return Picture.writeImage(jpeg, ContentType.JPG).getBytes();
    }

    public byte[] exportAsJPEG(byte[] photo) {
        return exportAsJPEG(photo, Color.WHITE);
    }

}
