package net.sourceforge.fenixedu.domain.space;

import java.io.Serializable;
import java.util.Collection;
import java.util.Comparator;
import java.util.Set;
import java.util.SortedSet;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.util.FactoryExecutor;
import net.sourceforge.fenixedu.injectionCode.FenixDomainObjectActionLogAnnotation;
import net.sourceforge.fenixedu.util.MultiLanguageString;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.lang.StringUtils;

import pt.utl.ist.fenix.tools.util.CollectionUtils;
import pt.utl.ist.fenix.tools.util.StringAppender;

public class RoomClassification extends RoomClassification_Base {

    private transient String absoluteCode;

    private transient String presentationCode;

    public static final Comparator<RoomClassification> COMPARATORY_BY_CODE = new BeanComparator("code");

    public static final Comparator<RoomClassification> COMPARATORY_BY_PRESENTATION_CODE = new BeanComparator(
	    "presentationCode");

    public static final Comparator<RoomClassification> COMPARATORY_BY_PARENT_ROOM_CLASSIFICATION_AND_CODE = new BeanComparator(
	    "absoluteCode");

    public static SortedSet<RoomClassification> sortByCode(
	    final Collection<RoomClassification> roomClassifications) {
	return CollectionUtils.constructSortedSet(roomClassifications, COMPARATORY_BY_CODE);
    }

    public static SortedSet<RoomClassification> sortByPresentationCode(
	    final Collection<RoomClassification> roomClassifications) {
	return CollectionUtils.constructSortedSet(roomClassifications, COMPARATORY_BY_PRESENTATION_CODE);
    }

    public static SortedSet<RoomClassification> sortByRoomClassificationAndCode(
	    final Collection<RoomClassification> roomClassifications) {
	return CollectionUtils.constructSortedSet(roomClassifications,
		COMPARATORY_BY_PARENT_ROOM_CLASSIFICATION_AND_CODE);
    }

    public static abstract class RoomClassificationFactory implements Serializable, FactoryExecutor {
	private String code;

	private MultiLanguageString name;

	public String getCode() {
	    return code;
	}

	public void setCode(String code) {
	    this.code = code;
	}

	public MultiLanguageString getName() {
	    return name;
	}

	public void setName(MultiLanguageString name) {
	    this.name = name;
	}

	public RoomClassification getParentRoomClassification() {
	    if (getCode() != null) {
		final int index = getCode().lastIndexOf('.', getCode().length());
		if (index > 0) {
		    final String parentAbsoluteCode = getCode().substring(0, index);
		    final RoomClassification roomClassification = findRoomClassificationByPresentationCode(parentAbsoluteCode);
		    if (roomClassification == null) {
			throw new DomainException("error.unexisting.room.classification");
		    } else {
			return roomClassification;
		    }
		}
	    }
	    return null;
	}

	public Integer getChildCode() {
	    if (getCode() == null || StringUtils.isEmpty(getCode().trim())) {
		return null;
	    } else if ((!getCode().contains(".") && !StringUtils.isNumeric(getCode()))
		    || (getCode().contains(".") && (!StringUtils.isNumeric(getCode().substring(0,
			    getCode().indexOf("."))) || !StringUtils.isNumeric(getCode().substring(
			    getCode().indexOf(".") + 1, getCode().length()))))) {
		throw new DomainException("error.roomClassification.code.isn't.a.number");
	    }

	    final int index = getCode().lastIndexOf('.', getCode().length());
	    if (index > 0) {
		return Integer.valueOf(getCode().substring(index + 1, getCode().length()));
	    } else {
		return Integer.valueOf(getCode());
	    }
	}
    }

    public static class RoomClassificationFactoryCreator extends RoomClassificationFactory {
	public RoomClassification execute() {
	    return new RoomClassification(getParentRoomClassification(), getChildCode(), getName());
	}
    }

    public static class RoomClassificationFactoryEditor extends RoomClassificationFactory {
	private DomainReference<RoomClassification> roomClassificationReference;

	public RoomClassificationFactoryEditor(String code, MultiLanguageString name) {
	    super();
	}

	public RoomClassificationFactoryEditor(final RoomClassification roomClassification) {
	    setCode(roomClassification.getPresentationCode());
	    setName(roomClassification.getName());
	    setRoomClassification(roomClassification);
	}

	public RoomClassification getRoomClassification() {
	    return roomClassificationReference == null ? null : roomClassificationReference.getObject();
	}

	public void setRoomClassification(final RoomClassification roomClassification) {
	    if (roomClassification != null) {
		roomClassificationReference = new DomainReference<RoomClassification>(roomClassification);
	    }
	}

	public Object execute() {
	    getRoomClassification().edit(getParentRoomClassification(), getChildCode(), getName());
	    return null;
	}
    }

    @FenixDomainObjectActionLogAnnotation(actionName = "Created room classification", parameters = {
	    "parentRoomClassification", "code", "name" })
    public RoomClassification(final RoomClassification parentRoomClassification, final Integer code,
	    final MultiLanguageString name) {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
	edit(parentRoomClassification, code, name);
    }

    @FenixDomainObjectActionLogAnnotation(actionName = "Edited room classification", parameters = {
	    "parentRoomClassification", "code", "name" })
    public void edit(final RoomClassification parentRoomClassification, final Integer code,
	    final MultiLanguageString name) {

	final Set<RoomClassification> childRoomClassifications = parentRoomClassification == null ? getRootDomainObject()
		.getRoomClassificationSet()
		: parentRoomClassification.getChildRoomClassificationsSet();

	final RoomClassification existingRoomClassification = findRoomClassificationByCode(
		childRoomClassifications, parentRoomClassification, code);

	if (existingRoomClassification != null && existingRoomClassification != this) {
	    throw new DomainException("error.room.classification.with.same.code.exists",
		    new String[] { code.toString() });
	}
	if (code == null) {
	    throw new DomainException("error.room.classification.cannot.have.null.code");
	}
	if (name == null || name.getAllContents().isEmpty()) {
	    throw new DomainException("error.room.classification.cannot.have.null.name");
	}

	super.setParentRoomClassification(parentRoomClassification);
	super.setCode(code);
	super.setName(name);

	absoluteCode = null;
	presentationCode = null;
	for (final RoomClassification childRoomClassification : getChildRoomClassificationsSet()) {
	    childRoomClassification.absoluteCode = null;
	    childRoomClassification.presentationCode = null;
	}
    }

    @Override
    public void setCode(final Integer code) {
	throw new Error("Use edit method to change an instance of: " + getClass().getName());
    }

    @Override
    public void setName(final MultiLanguageString name) {
	throw new Error("Use edit method to change an instance of: " + getClass().getName());
    }

    @Override
    public void setParentRoomClassification(final RoomClassification parentRoomClassification) {
	throw new Error("Use edit method to change an instance of: " + getClass().getName());
    }

    public void delete() {
	if (getChildRoomClassificationsSet().isEmpty()) {
	    setRootDomainObject(null);
	    super.setParentRoomClassification(null);
	    super.deleteDomainObject();
	} else {
	    throw new DomainException(
		    "error.cannot.delete.room.classification.with.existing.child.classifications");
	}
    }

    public String getAbsoluteCode() {
	if (absoluteCode == null) {
	    final String code = getCode().toString();
	    final String normalizedCode = getCode().intValue() < 10 ? "0" + code : code;
	    if (getParentRoomClassification() == null) {
		absoluteCode = normalizedCode;
	    } else {
		final String parentCode = getParentRoomClassification().getAbsoluteCode();
		absoluteCode = StringAppender.append(parentCode, ".", normalizedCode);
	    }
	}
	return absoluteCode;
    }

    public String getPresentationCode() {
	if (presentationCode == null) {
	    final String code = getCode().toString();
	    if (getParentRoomClassification() == null) {
		presentationCode = code;
	    } else {
		final String parentCode = getParentRoomClassification().getPresentationCode();
		presentationCode = StringAppender.append(parentCode, ".", code);
	    }
	}
	return presentationCode;
    }

    public static RoomClassification findRoomClassificationByCode(
	    final Collection<RoomClassification> roomClassifications,
	    final RoomClassification parentRoomClassification, final Integer code) {

	if (code != null) {
	    for (final RoomClassification roomClassification : roomClassifications) {
		if (roomClassification.getParentRoomClassification() == parentRoomClassification
			&& code.equals(roomClassification.getCode())) {
		    return roomClassification;
		}
	    }
	}
	return null;
    }

    public static RoomClassification findRoomClassificationByPresentationCode(
	    final String presentationCode) {
	for (final RoomClassification roomClassification : RootDomainObject.getInstance()
		.getRoomClassificationSet()) {
	    if (roomClassification.getPresentationCode().equals(presentationCode)) {
		return roomClassification;
	    }
	}
	return null;
    }
}
