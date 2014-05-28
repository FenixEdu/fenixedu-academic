/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.fenixedu.presentationTier.renderers;

import java.util.List;
import java.util.Properties;

import net.sourceforge.fenixedu.domain.Person;
import pt.ist.fenixWebFramework.renderers.OutputRenderer;
import pt.ist.fenixWebFramework.renderers.components.HtmlBlockContainer;
import pt.ist.fenixWebFramework.renderers.components.HtmlComponent;
import pt.ist.fenixWebFramework.renderers.contexts.PresentationContext;
import pt.ist.fenixWebFramework.renderers.layouts.Layout;
import pt.ist.fenixWebFramework.renderers.utils.RenderKit;
import pt.ist.fenixWebFramework.renderers.utils.RenderMode;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;

public class MultiplePersonalCardRenderer extends OutputRenderer {

    private String noPhotoAvailableImage;

    private String formatImageURL;

    private String imageDescription;

    private String imageName;

    private String imageHeight;

    private String imageWidth;

    private String photoCellClasses;

    private String infoCellClasses;

    private String classes;

    private boolean contextRelative;

    private String eachLayout;

    private String eachSchema;

    private String divClasses;

    private String sortBy;

    public String getDivClasses() {
        return divClasses;
    }

    public void setDivClasses(String divClasses) {
        this.divClasses = divClasses;
    }

    @Override
    public String getClasses() {
        return classes;
    }

    @Override
    public void setClasses(String classes) {
        this.classes = classes;
    }

    public boolean isContextRelative() {
        return contextRelative;
    }

    public void setContextRelative(boolean contextRelative) {
        this.contextRelative = contextRelative;
    }

    public String getEachLayout() {
        return eachLayout;
    }

    public void setEachLayout(String eachLayout) {
        this.eachLayout = eachLayout;
    }

    public String getEachSchema() {
        return eachSchema;
    }

    public void setEachSchema(String eachSchema) {
        this.eachSchema = eachSchema;
    }

    public String getFormatImageURL() {
        return formatImageURL;
    }

    public void setFormatImageURL(String formatImageURL) {
        this.formatImageURL = formatImageURL;
    }

    public String getImageDescription() {
        return imageDescription;
    }

    public void setImageDescription(String imageDescription) {
        this.imageDescription = imageDescription;
    }

    public String getImageHeight() {
        return imageHeight;
    }

    public void setImageHeight(String imageHeight) {
        this.imageHeight = imageHeight;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getImageWidth() {
        return imageWidth;
    }

    public void setImageWidth(String imageWidth) {
        this.imageWidth = imageWidth;
    }

    public String getInfoCellClasses() {
        return infoCellClasses;
    }

    public void setInfoCellClasses(String infoCellClasses) {
        this.infoCellClasses = infoCellClasses;
    }

    public String getNoPhotoAvailableImage() {
        return noPhotoAvailableImage;
    }

    public void setNoPhotoAvailableImage(String noPhotoAvailableImage) {
        this.noPhotoAvailableImage = noPhotoAvailableImage;
    }

    public String getPhotoCellClasses() {
        return photoCellClasses;
    }

    public void setPhotoCellClasses(String photoCellClasses) {
        this.photoCellClasses = photoCellClasses;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    @Override
    protected Layout getLayout(Object object, Class type) {
        List<Person> people =
                (getSortBy() == null) ? (List<Person>) object : RenderUtils.sortCollectionWithCriteria((List<Person>) object,
                        getSortBy());
        return new MultiplePersonalCardLayout(people);
    }

    public class MultiplePersonalCardLayout extends Layout {

        List<Person> people;

        private PersonalCardRenderer getCardRenderer() {

            PersonalCardRenderer card = new PersonalCardRenderer();
            card.setSubLayout(getEachLayout());
            card.setSubSchema(getEachSchema());
            card.setNoPhotoAvailableImage(getNoPhotoAvailableImage());
            card.setFormatImageURL(getFormatImageURL());
            card.setImageDescription(getImageDescription());
            card.setImageName(getImageName());
            card.setImageHeight(getImageHeight());
            card.setImageWidth(getImageWidth());
            card.setPhotoCellClasses(getPhotoCellClasses());
            card.setInfoCellClasses(getInfoCellClasses());
            card.setClasses(getDivClasses());
            card.setContextRelative(isContextRelative());
            return card;
        }

        public MultiplePersonalCardLayout(List<Person> people) {
            this.people = people;
        }

        @Override
        public HtmlComponent createComponent(Object object, Class type) {
            HtmlBlockContainer container = new HtmlBlockContainer();

            PresentationContext newContext = getContext().createSubContext(getContext().getMetaObject());
            newContext.setProperties(new Properties());
            newContext.setRenderMode(RenderMode.getMode("output"));
            PersonalCardRenderer renderer = getCardRenderer();

            for (Person person : people) {
                container.addChild(RenderKit.getInstance().renderUsing(renderer, newContext, person, Person.class));
            }
            return container;
        }
    }
}
