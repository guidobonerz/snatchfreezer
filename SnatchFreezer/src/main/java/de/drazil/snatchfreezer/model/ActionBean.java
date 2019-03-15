package de.drazil.snatchfreezer.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
public class ActionBean {
	@Getter
	@Setter
	private int pinIndex;
	@Getter
	@Setter
	private Boolean activ;
	@Getter
	@Setter
	private String description;
	@Getter
	@Setter
	private List<ActionItemBean> actionItemList;
}
