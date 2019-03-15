package de.drazil.snatchfreezer.model;

import org.nield.dirtyfx.beans.DirtyBooleanProperty;
import org.nield.dirtyfx.beans.DirtyLongProperty;

import javafx.beans.property.SimpleLongProperty;

public class ActionItemPropertyBean {

	private SimpleLongProperty index;
	private DirtyLongProperty delay;
	private DirtyLongProperty release;
	private DirtyLongProperty delayIncrement;
	private DirtyLongProperty releaseIncrement;
	private DirtyBooleanProperty ignore;
	private ActionItemBean bean;

	public ActionItemPropertyBean() {
		this(new ActionItemBean());
	}

	public ActionItemPropertyBean(ActionItemBean bean) {

		this(bean.getId(), bean.getDelay(), bean.getRelease(), bean.getDelayIncrement(), bean.getReleaseIncrement(),
				bean.isIgnore());
		this.bean = bean;
	}

	public ActionItemPropertyBean(long index) {
		this(index, 0L, 0L, 0L, 0L, false);
	}

	public ActionItemPropertyBean(final Long index, final Long delay, final Long release, final Long delayIncrement,
			final Long releaseIncrement, Boolean ignore) {
		this.index = new SimpleLongProperty(index);
		this.delay = new DirtyLongProperty(delay);
		this.release = new DirtyLongProperty(release);
		this.delayIncrement = new DirtyLongProperty(delayIncrement);
		this.releaseIncrement = new DirtyLongProperty(releaseIncrement);
		this.ignore = new DirtyBooleanProperty(ignore);
	}

	public Long getIndex() {
		return index.get();
	}

	public void setIndex(final Long index) {
		this.index.set(index);
		bean.setId(index);
	}

	public Long getDelay() {
		return delay.get();
	}

	public void setDelay(final Long delay) {
		this.delay.set(delay);
		bean.setDelay(delay);
	}

	public Long getRelease() {
		return release.get();
	}

	public void setRelease(final Long release) {
		this.release.set(release);
		bean.setRelease(release);
	}

	public Long getDelayIncrement() {
		return delayIncrement.get();
	}

	public void setDelayIncrement(final Long delayIncrement) {
		this.delayIncrement.set(delayIncrement);
		bean.setDelayIncrement(delayIncrement);
	}

	public Long getReleaseIncrement() {
		return releaseIncrement.get();
	}

	public void setReleaseIncrement(final Long releaseIncrement) {
		this.releaseIncrement.set(releaseIncrement);
		bean.setReleaseIncrement(releaseIncrement);
	}

	public Boolean getIgnore() {
		return ignore.get();
	}

	public void setIgnore(final Boolean ignore) {
		this.ignore.set(ignore);
		bean.setIgnore(ignore);
	}

	public boolean isDelayDirty() {
		return delay.isDirty();
	}

	public boolean isReleaseDirty() {
		return release.isDirty();
	}

	public boolean isDelayIncrementDirty() {
		return delayIncrement.isDirty();
	}

	public boolean isReleaseIncrementDirty() {
		return releaseIncrement.isDirty();
	}

	public void reset() {
		bean.setDelay(delay.getOriginalValue());
		bean.setRelease(release.getOriginalValue());
		bean.setDelayIncrement(delayIncrement.getOriginalValue());
		bean.setReleaseIncrement(releaseIncrement.getOriginalValue());
		bean.setIgnore(ignore.getOriginalValue());
		delay.reset();
		release.reset();
		delayIncrement.reset();
		releaseIncrement.reset();
	}

	public ActionItemBean getBean() {
		return bean;
	}
}
