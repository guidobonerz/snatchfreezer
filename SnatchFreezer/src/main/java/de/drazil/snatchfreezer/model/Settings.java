package de.drazil.snatchfreezer.model;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

public class Settings {
	@Getter
	@Setter
	private String description;
	@Getter
	@Setter
	private long cycles;
	@Getter
	@Setter
	private long cycleDelay;
	@Getter
	@Setter
	private List<ActionBean> actionBeanList;
}
