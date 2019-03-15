package de.drazil.snatchfreezer.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
public class ActionItemBean {
	@Getter
	@Setter
	private long id;
	@Getter
	@Setter
	private long delay;
	@Getter
	@Setter
	private long release;
	@Getter
	@Setter
	private long delayIncrement;
	@Getter
	@Setter
	private long releaseIncrement;
	@Getter
	@Setter
	private boolean ignore;
}
