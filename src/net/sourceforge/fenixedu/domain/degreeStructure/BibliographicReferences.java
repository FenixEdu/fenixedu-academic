/*
 * Created on Jan 24, 2006
 */
package net.sourceforge.fenixedu.domain.degreeStructure;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class BibliographicReferences {       
    private List<BibliographicReference> bibliographicReferencesList;
    
    public List<BibliographicReference> getBibliographicReferencesList() {
        if (this.bibliographicReferencesList == null) {
            this.bibliographicReferencesList = new ArrayList<BibliographicReference>();
        }
        return this.bibliographicReferencesList;
    }
    
    public int getBibliographicReferencesListCount() {
        return getBibliographicReferencesList().size();
    }
    
    public BibliographicReference getBibliographicReference(int oid) {
        return getBibliographicReferencesList().get(oid);
    }
    
    public void createBibliographicReference(String year, String title, String authors, String reference,
            String url, BibliographicReferenceType type) {
        getBibliographicReferencesList().add(
                new BibliographicReference(year, title, authors, reference, url, type, getBibliographicReferencesListCount()));
    }
    
    public void createBibliographicReference(String year, String title, String authors, String reference,
            String url, BibliographicReferenceType type, int order) {
        getBibliographicReferencesList().add(
                new BibliographicReference(year, title, authors, reference, url, type, order));
    }

    public void editBibliographicReference(int oid, String year, String title, String authors,
            String reference, String url, BibliographicReferenceType type) {
        getBibliographicReferencesList().get(oid).edit(year, title, authors, reference, url, type);
    }
    
    public void deleteBibliographicReference(int oid) {
        getBibliographicReferencesList().remove(oid);
        reOrderBibliographicReferences();
    }
    
    public void switchBibliographicReferencePosition(int oldPosition, int newPosition) {
        try {
            if (validPositions(oldPosition, newPosition)) {                
                if (moveTop(oldPosition, newPosition)) {
                    moveTo(getBibliographicReferencesList().get(oldPosition), oldPosition, 0);
                } else if (moveEnd(oldPosition, newPosition)) {
                    moveTo(getBibliographicReferencesList().get(oldPosition), oldPosition, getBibliographicReferencesListCount() - 1);
                } else { //moveUp or moveDown
                    final BibliographicReference oldBibliographicReference = getBibliographicReferencesList().get(oldPosition);                
                    final BibliographicReference newBibliographicReference = getBibliographicReferencesList().get(newPosition);
                    oldBibliographicReference.setOrder(newPosition);
                    newBibliographicReference.setOrder(oldPosition);
                    getBibliographicReferencesList().set(newPosition, oldBibliographicReference);
                    getBibliographicReferencesList().set(oldPosition, newBibliographicReference);
                }                    
            }            
        } catch (IndexOutOfBoundsException e) {
            throw new DomainException("bibliographicReferences.invalid.reference.positions");
        }
    }

    private boolean validPositions(int oldPosition, int newPosition) {
        if (oldPosition == newPosition || newPosition < 0  || newPosition == getBibliographicReferencesListCount()) {
            return false;
        }
        return true;
    }
    
    private void moveTo(BibliographicReference bibliographicReference, int oldPosition, int newPosition) {
        getBibliographicReferencesList().remove(oldPosition);
        getBibliographicReferencesList().add(newPosition, bibliographicReference);
        reOrderBibliographicReferences();
    }
    
    private boolean moveTop(int oldPosition, int newPosition) {
        if (Math.abs(newPosition - oldPosition) > 1 && newPosition < oldPosition) {
            return true;
        }
        return false;
    }
    
    private boolean moveEnd(int oldPosition, int newPosition) {
        if (Math.abs(newPosition - oldPosition) > 1 && newPosition > oldPosition) {
            return true;
        }
        return false;
    }
    
    private void reOrderBibliographicReferences() {
        for (int i = 0; i < getBibliographicReferencesListCount(); i++) {            
            getBibliographicReferencesList().get(i).setOrder(i);            
        }
    }
    
    public class BibliographicReference {
        private String year;
        private String title;
        private String authors;
        private String reference;
        private String url;
        private BibliographicReferenceType type;
        private int order;
        
        public BibliographicReference(String year, String title, String authors, String reference,
                String url, BibliographicReferenceType type, int order) {
            setInformation(year, title, authors, reference, url, type, order);
        }

        public void edit(String year, String title, String authors, String reference, String url,
                BibliographicReferenceType type) {
            setInformation(year, title, authors, reference, url, type, getOrder());
        }

        private void setInformation(String year, String title, String authors, String reference,
                String url, BibliographicReferenceType type, int order) {
            setYear(year);
            setTitle(title);
            setAuthors(authors);
            setReference(reference);
            setUrl(url);
            setType(type);
            setOrder(order);
        }
        
        public String getAuthors() {
            return authors;
        }
        public void setAuthors(String authors) {
            this.authors = authors;
        }
        public String getReference() {
            return reference;
        }
        public void setReference(String reference) {
            this.reference = reference;
        }
        public String getTitle() {
            return title;
        }
        public void setTitle(String title) {
            this.title = title;
        }
        public BibliographicReferenceType getType() {
            return type;
        }
        public void setType(BibliographicReferenceType type) {
            this.type = type;
        }
        public String getUrl() {
            return url;
        }
        public void setUrl(String url) {
            this.url = url;
        }
        public String getYear() {
            return year;
        }
        public void setYear(String year) {
            this.year = year;
        }        
        public int getOrder() {
            return order;
        }
        public void setOrder(Integer order) {
            this.order = order;
        }
    }
    
    public enum BibliographicReferenceType {        
        MAIN,        
        SECONDARY;
        
        public String getName() {
            return name();
        }
    }
}
