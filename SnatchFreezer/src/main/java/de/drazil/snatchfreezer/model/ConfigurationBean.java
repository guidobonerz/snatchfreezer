package de.drazil.snatchfreezer.model;

import lombok.Getter;
import lombok.Setter;

public class ConfigurationBean {
	@Getter
	@Setter
	private String note;
	@Getter
	@Setter
	private ActionBean valve1ActionBean;
	@Getter
	@Setter
	private ActionBean valve2ActionBean;
	@Getter
	@Setter
	private ActionBean valve3ActionBean;
	@Getter
	@Setter
	private ActionBean valve4ActionBean;
	@Getter
	@Setter
	private ActionBean cameraActionBean;
	@Getter
	@Setter
	private ActionBean flash1ActionBean;
	@Getter
	@Setter
	private ActionBean flash2ActionBean;
	@Getter
	@Setter
	private long cycles;
	@Getter
	@Setter
	private long cycleDelay;

}
