package ServidorApresentacao.TagLib.sop.v3;

import DataBeans.InfoLesson;

/**
 * @author jpvl
 *
 */
public class InfoLessonWrapper implements Comparable{
	private InfoLesson infoLesson;
	private boolean locked;
	private int slotIndex;
	private LessonSlot lessonSlot;

	public InfoLessonWrapper (InfoLesson infoLesson){
		this.infoLesson = infoLesson;
		this.locked = false;
	}
	/**
	 * Returns the locked.
	 * @return boolean
	 */
	public boolean isLocked() {
		return locked;
	}
	
	public int getSlotIndex(){
		return slotIndex;
	}

	/**
	 * Sets the locked.
	 * @param locked The locked to set
	 */
	public void setLocked(boolean locked, int slotIndex) {
		this.locked = locked;
		this.slotIndex = slotIndex;
	}

	/**
	 * Returns the infoLesson.
	 * @return InfoLesson
	 */
	public InfoLesson getInfoLesson() {
		return infoLesson;
	}
	
	protected void setLessonSlot(LessonSlot lessonSlot){
		this.lessonSlot = lessonSlot;
	}

	/**
	 * Returns the lessonSlot.
	 * @return LessonSlot
	 */
	public LessonSlot getLessonSlot() {
		return lessonSlot;
	}
	
	public int getSpan(){
		int startIndex = lessonSlot.getStartIndex();
		int endIndex = lessonSlot.getEndIndex();
		return endIndex - startIndex;
	}
	
	/**
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(Object toCompareWith) {
		int compareResult = 0;
		if (toCompareWith instanceof InfoLessonWrapper){
			InfoLessonWrapper infoLessonWrapper = (InfoLessonWrapper) toCompareWith;
			compareResult = infoLessonWrapper.getSpan() - getSpan();
		}
		return compareResult;
	}

}
