package de.drazil.snatchfreezer.model;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleLongProperty;

public class ObservableActionItemBean {

	private SimpleLongProperty delay;
	private SimpleLongProperty release;
	private SimpleLongProperty delayIncrement;
	private SimpleLongProperty releaseIncrement;
	private SimpleBooleanProperty ignore;

	public ObservableActionItemBean() {
		this(new Long(0), new Long(0), new Long(0), new Long(0), false);
	}

	public ObservableActionItemBean(long delay, long release, long delayIncrement, long releaseIncrement,
			boolean ignore) {
		this.delay = new SimpleLongProperty(delay);
		this.release = new SimpleLongProperty(release);
		this.delayIncrement = new SimpleLongProperty(delayIncrement);
		this.releaseIncrement = new SimpleLongProperty(releaseIncrement);
		this.ignore = new SimpleBooleanProperty(ignore);
	}

	public Long getDelay() {
		return delay.get();
	}

	public void setDelay(Long delay) {
		this.delay.set(delay);
	}

	public Long getRelease() {
		return release.get();
	}

	public void setRelease(Long release) {
		this.release.set(release);
	}

	public Long getDelayIncrement() {
		return delayIncrement.get();
	}

	public void setDelayIncrement(Long delayIncrement) {
		this.delayIncrement.set(delayIncrement);
	}

	public Long getReleaseIncrement() {
		return releaseIncrement.get();
	}

	public void setReleaseIncrement(Long releaseIncrement) {
		this.releaseIncrement.set(releaseIncrement);
	}

	public Boolean getIgnore() {
		return ignore.get();
	}

	public void setIgnore(Boolean ignore) {
		this.ignore.set(ignore);
	}
}
