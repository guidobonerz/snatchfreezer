package de.drazil.snatchfreezer;

import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

public class TableColumnFactory
{
	public final static <S, T> TableColumn<S, T> createTableColumn(String columnName, String propertyName, int columnWidth, boolean editable)
	{
		TableColumn<S, T> activeColumn = new TableColumn<>("Active");
		activeColumn.setCellValueFactory(new PropertyValueFactory<>("active"));
		//activeColumn.setCellFactory(CheckBoxTableCell.forTableColumn(activeColumn));
		activeColumn.setMinWidth(50);
		activeColumn.setEditable(true);
		return activeColumn;
	}
}
