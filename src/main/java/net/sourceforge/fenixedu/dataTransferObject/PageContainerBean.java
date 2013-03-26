package net.sourceforge.fenixedu.dataTransferObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import pt.ist.fenixframework.DomainObject;

public class PageContainerBean implements Serializable {

    private transient List<DomainObject> objects;
    private List<DomainObject> pageObjects;
    private DomainObject selected;
    private Integer numberOfPages;

    private Integer page = 1;

    public List<DomainObject> getObjects() {
        return objects;
    }

    public void setObjects(List<DomainObject> objects) {
        this.objects = objects;
        setPageObjects(null);
    }

    protected List<DomainObject> getPageObjects() {
        return this.pageObjects;
    }

    protected void setPageObjects(List<DomainObject> pageObjects) {
        this.pageObjects = pageObjects;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public DomainObject getSelected() {
        return this.selected;
    }

    public void setSelected(DomainObject selected) {
        this.selected = selected;
    }

    public List<DomainObject> getPageByPageSize(int pageSize) {
        List<DomainObject> pageObjects = getPageObjects();
        if (pageObjects != null) {
            return pageObjects;
        } else {
            List<DomainObject> objects = getObjects();
            if (objects != null && !objects.isEmpty()) {
                validatePageNumber(pageSize);
                int from = (getPage() - 1) * pageSize;
                int to = getObjects().size() > getPage() * pageSize ? getPage() * pageSize : objects.size();
                List<DomainObject> subList = new ArrayList<DomainObject>(objects.subList(from, to));
                setPageObjects(subList);
                return subList;
            } else {
                return Collections.emptyList();
            }
        }
    }

    private void validatePageNumber(int pageSize) {
        if (getPage() < 1) {
            setPage(1);
        } else {
            Integer numberOfPages = getNumberOfPages(pageSize);
            if (getPage() > numberOfPages) {
                setPage(numberOfPages);
            }
        }

    }

    public int getNumberOfPages(int pageSize) {
        if (getObjects() != null) {
            this.numberOfPages = (int) Math.ceil((double) getObjects().size() / pageSize);
        }
        return this.numberOfPages;
    }

    public boolean hasNextPage(int pageSize) {
        return getPage() < getNumberOfPages(pageSize);
    }

    public boolean hasPreviousPage(int pageSize) {
        return getPage() > 1;
    }

    public List<DomainObject> getAllObjects() {
        if (getPageObjects() != null) {
            return getPageObjects();
        } else {
            if (getObjects() != null) {
                setPageObjects(getObjects());
                return getObjects();
            } else {
                return Collections.emptyList();
            }
        }
    }

    public void setPageJump(Integer pageJump) {
        setPage(pageJump);
    }

    public Integer getPageJump() {
        return null;
    }

}
