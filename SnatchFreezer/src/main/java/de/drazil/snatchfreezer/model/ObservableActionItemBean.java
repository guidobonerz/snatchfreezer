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

	public ObservableActionItemBean(final long delay, final long release, final long delayIncrement,
			final long releaseIncrement, final boolean ignore) {
		this.delay = new SimpleLongProperty(delay);
		this.release = new SimpleLongProperty(release);
		this.delayIncrement = new SimpleLongProperty(delayIncrement);
		this.releaseIncrement = new SimpleLongProperty(releaseIncrement);
		this.ignore = new SimpleBooleanProperty(ignore);

	}

	public Long getDelay() {
		return delay.get();
	}

	public void setDelay(final Long delay) {
		this.delay.set(delay);
	}

	public Long getRelease() {
		return release.get();
	}

	public void setRelease(final Long release) {
		this.release.set(release);
	}

	public Long getDelayIncrement() {
		return delayIncrement.get();
	}

	public void setDelayIncrement(final Long delayIncrement) {
		this.delayIncrement.set(delayIncrement);
	}

	public Long getReleaseIncrement() {
		return releaseIncrement.get();
	}

	public void setReleaseIncrement(final Long releaseIncrement) {
		this.releaseIncrement.set(releaseIncrement);
	}

	public Boolean getIgnore() {
		return ignore.get();
	}

	public void setIgnore(final Boolean ignore) {
		this.ignore.set(ignore);
	}
}
