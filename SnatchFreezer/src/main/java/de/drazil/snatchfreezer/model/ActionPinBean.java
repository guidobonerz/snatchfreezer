package de.drazil.snatchfreezer.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
public class ActionPinBean
{
	@Getter
	@Setter
	private int pin;
	@Getter
	@Setter
	private boolean selected;
	@Getter
	@Setter
	private String description;

	@Override
	public String toString()
	{
		return description;
	}
}
