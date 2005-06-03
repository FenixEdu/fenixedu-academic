/*
 * InfoShift.java
 *
 * Created on December 23rd, 2002, 17:02
 */

package net.sourceforge.fenixedu.dataTransferObject;

/**
 * 
 * @author tfc130
 */
import net.sourceforge.fenixedu.domain.ShiftType;

public class TypeLessonAndInfoShift extends InfoObject {
    protected ShiftType _typeLesson;

    protected InfoShift _infoShift;

    public TypeLessonAndInfoShift() {
    }

    public TypeLessonAndInfoShift(ShiftType typeLesson, InfoShift infoShift) {
        setTypeLesson(typeLesson);
        setInfoShift(infoShift);
    }

    public ShiftType getTypeLesson() {
        return _typeLesson;
    }

    public void setTypeLesson(ShiftType typeLesson) {
        _typeLesson = typeLesson;
    }

    public InfoShift getInfoShift() {
        return _infoShift;
    }

    public void setInfoShift(InfoShift infoShift) {
        _infoShift = infoShift;
    }

    public boolean equals(Object obj) {
        boolean resultado = false;
        if (obj instanceof TypeLessonAndInfoShift) {
            TypeLessonAndInfoShift tLAIS = (TypeLessonAndInfoShift) obj;
            resultado = getTypeLesson().equals(tLAIS.getTypeLesson())
                    && getInfoShift().equals(tLAIS.getInfoShift());
        }
        return resultado;
    }

    public String toString() {
        String result = "[TYPELESSONANDINFOSHIFT";
        result += ", typeLesson=" + _typeLesson;
        result += ", infoShift=" + _infoShift;
        result += "]";
        return result;
    }

}