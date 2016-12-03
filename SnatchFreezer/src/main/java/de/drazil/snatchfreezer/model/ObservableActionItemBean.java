package de.drazil.snatchfreezer.model;

import javafx.beans.property.SimpleLongProperty;

public class ObservableActionItemBean {

	private SimpleLongProperty delay;
	private SimpleLongProperty release;
	private SimpleLongProperty delayIncrement;
	private SimpleLongProperty releaseIncrement;

	public ObservableActionItemBean() {
		this(new Long(0), new Long(0), new Long(0), new Long(0));
	}

	public ObservableActionItemBean(long delay, long release, long delayIncrement, long releaseIncrement) {
		this.delay = new SimpleLongProperty(delay);
		this.release = new SimpleLongProperty(release);
		this.delayIncrement = new SimpleLongProperty(delayIncrement);
		this.releaseIncrement = new SimpleLongProperty(releaseIncrement);
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
}
