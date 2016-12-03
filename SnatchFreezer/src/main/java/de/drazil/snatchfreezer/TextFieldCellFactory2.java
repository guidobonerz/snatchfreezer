package de.drazil.snatchfreezer;

import de.drazil.snatchfreezer.model.ActionItemBean;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.util.Callback;

public class TextFieldCellFactory2
		implements Callback<TableColumn<ActionItemBean, Long>, TableCell<ActionItemBean, Long>>

{

	@Override
	public TableCell<ActionItemBean, Long> call(TableColumn<ActionItemBean, Long> param) {
		TextFieldCell textFieldCell = new TextFieldCell();
		return textFieldCell;
	}

	public static class TextFieldCell extends TableCell<ActionItemBean, Long> {

		private TextField textField;

		public TextFieldCell() {
			textField = new TextField();
			this.setGraphic(textField);
		}

		/*
		 * 
		 * @Override public void updateItem(Long value, boolean empty) {
		 * super.updateItem(value, empty); if (!empty && isEditing()) {
		 * setText(null); textField.setText(value == null ? "" :
		 * value.toString()); setGraphic(textField); } else {
		 * setText(value.toString()); setGraphic(null); } }
		 */

		@Override
		protected void updateItem(Long item, boolean empty) {
			super.updateItem(item, empty);
			if (!empty) {

			} else {
				this.setContentDisplay(ContentDisplay.TEXT_ONLY);
			}
		}
	}
}
