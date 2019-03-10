package de.drazil.snatchfreezer.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.deser.std.NumberDeserializers.LongDeserializer;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers.LongSerializer;

import javafx.beans.property.SimpleLongProperty;

public class ObservableActionItemBean {

	@JsonDeserialize(using = LongDeserializer.class)
	@JsonSerialize(using = LongSerializer.class)
	private SimpleLongProperty delay;
	@JsonDeserialize(using = LongDeserializer.class)
	@JsonSerialize(using = LongSerializer.class)
	private SimpleLongProperty release;
	@JsonDeserialize(using = LongDeserializer.class)
	@JsonSerialize(using = LongSerializer.class)
	private SimpleLongProperty delayIncrement;
	@JsonDeserialize(using = LongDeserializer.class)
	@JsonSerialize(using = LongSerializer.class)
	private SimpleLongProperty releaseIncrement;

	public ObservableActionItemBean() {
		this(0L, 0L, 0L, 0L);
	}

	public ObservableActionItemBean(final Long delay, final Long release, final Long delayIncrement,
			final Long releaseIncrement) {
		this.delay = new SimpleLongProperty(delay);
		this.release = new SimpleLongProperty(release);
		this.delayIncrement = new SimpleLongProperty(delayIncrement);
		this.releaseIncrement = new SimpleLongProperty(releaseIncrement);

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

}
