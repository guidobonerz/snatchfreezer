package de.drazil.snatchfreezer;

import de.drazil.snatchfreezer.model.ObservableActionItemBean;
import de.drazil.util.EditCell;
import de.drazil.util.Long2StringConverter;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Spinner;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class SnatchFreezer extends Application {

	private FlowPane actionBox = null;
	ObservableList<ObservableActionItemBean> list = null;

	/*
	 * @Override public void start(Stage stage) throws Exception {
	 * setUserAgentStylesheet(STYLESHEET_MODENA);
	 * 
	 * FXMLLoader loader = null; try { loader = new
	 * FXMLLoader(getClass().getResource("SnatchFreezer.fxml")); } catch (Exception
	 * e) { // TODO Auto-generated catch block e.printStackTrace(); }
	 * 
	 * Parent root = loader.load();
	 * 
	 * Scene scene = new Scene(root, 1024, 715);
	 * scene.getStylesheets().add(getClass().getResource("application.css").
	 * toExternalForm()); stage.setTitle("SnatchFreezer"); stage.setScene(scene);
	 * stage.show(); stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
	 * 
	 * @Override public void handle(WindowEvent event) { System.exit(0); } }); }
	 */
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		Font font = Font.loadFont(getClass().getResource("fa-solid-900.ttf").toExternalForm(), 10);

		list = FXCollections.observableArrayList();

		MenuBar menubar = new MenuBar();
		menubar.getMenus().add(new Menu("Application"));
		menubar.getMenus().add(new Menu("About"));
		actionBox = new FlowPane(Orientation.VERTICAL);
		HBox actionButtonPane = new HBox();
		actionButtonPane.setMinHeight(60);
		actionButtonPane.setPrefHeight(60);
		actionButtonPane.setMaxHeight(60);

		Button camera = new Button("\uf083");
		camera.setTooltip(new Tooltip("Add one maximum two camera triggers to list"));
		camera.getStyleClass().add("orangeButton");
		camera.getStyleClass().add("bigButtonIcon");
		camera.setMinWidth(70);
		camera.setMaxWidth(70);
		camera.setPrefWidth(70);
		Button bulb = new Button("\uf0e7");
		bulb.getStyleClass().add("yellowButton");
		bulb.getStyleClass().add("bigButtonIcon");
		bulb.setTooltip(new Tooltip("Add one maximum two flash triggers to list"));
		bulb.setMinWidth(70);
		bulb.setMaxWidth(70);
		bulb.setPrefWidth(70);
		Button tint = new Button("\uf043");
		tint.getStyleClass().add("blueButton");
		tint.getStyleClass().add("bigButtonIcon");
		tint.setTooltip(new Tooltip("Add one maximum six valve triggers to list"));
		tint.setOnAction(value -> {
			actionBox.getChildren().add(createAction());
		});
		tint.setMinWidth(70);
		tint.setMaxWidth(70);
		tint.setPrefWidth(70);
		Button go = new Button("\uf70c");
		go.getStyleClass().add("greenButton");
		go.getStyleClass().add("bigButtonIcon");
		go.setTooltip(new Tooltip("Start shooting \uf4da"));
		go.setMinWidth(70);
		go.setMaxWidth(70);
		go.setPrefWidth(70);
		go.setOnAction(value -> {
			for (int i = 0; i < list.size(); i++) {
				System.out.println(list.get(i).getDelay());
			}
		});
		Button empty = new Button("\uf576");
		empty.getStyleClass().add("blueButton");
		empty.getStyleClass().add("bigButtonIcon");
		empty.setTooltip(new Tooltip("Empty siphon"));
		empty.setMinWidth(70);
		empty.setMaxWidth(70);
		empty.setPrefWidth(70);

		camera.prefHeightProperty().bind(actionButtonPane.heightProperty());
		bulb.prefHeightProperty().bind(actionButtonPane.heightProperty());
		tint.prefHeightProperty().bind(actionButtonPane.heightProperty());
		empty.prefHeightProperty().bind(actionButtonPane.heightProperty());
		go.prefHeightProperty().bind(actionButtonPane.heightProperty());

		Region spacer = new Region();
		HBox.setHgrow(spacer, Priority.ALWAYS);
		spacer.setMinHeight(Region.USE_PREF_SIZE);

		actionButtonPane.getChildren().add(camera);
		actionButtonPane.getChildren().add(bulb);
		actionButtonPane.getChildren().add(tint);

		actionButtonPane.getChildren().add(spacer);

		actionButtonPane.getChildren().add(empty);
		actionButtonPane.getChildren().add(new Spinner<Integer>());
		actionButtonPane.getChildren().add(go);

		ScrollPane actionScrollPane = new ScrollPane(actionBox);

		VBox actionSplitPane = new VBox();
		actionSplitPane.getChildren().addAll(actionButtonPane, actionScrollPane);

		TextArea console = new TextArea();
		TextArea description = new TextArea();

		TabPane tabPane = new TabPane();
		tabPane.getTabs().add(new Tab("Console", console));
		tabPane.getTabs().add(new Tab("Description", description));

		SplitPane tabSplitPane = new SplitPane();
		tabSplitPane.setDividerPositions(0.8f, 0.2f);
		tabSplitPane.setOrientation(Orientation.VERTICAL);
		tabSplitPane.getItems().addAll(actionSplitPane, tabPane);

		VBox root = new VBox();
		root.getChildren().addAll(menubar, tabSplitPane);

		tabSplitPane.prefHeightProperty().bind(root.heightProperty());
		actionScrollPane.prefHeightProperty().bind(root.heightProperty());

		Scene scene = new Scene(root, 1024, 768);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		primaryStage.setTitle("Snatchfreezer");
		// primaryStage.setMaximized(true);
		primaryStage.setScene(scene);
		primaryStage.show();

	}

	@SuppressWarnings("unchecked")
	private Node createAction() {
		VBox vbox = new VBox();
		ToolBar toolbar = new ToolBar();
		Region spacer = new Region();
		HBox.setHgrow(spacer, Priority.ALWAYS);
		spacer.setMinWidth(Region.USE_PREF_SIZE);

		TableView<ObservableActionItemBean> table = new TableView<ObservableActionItemBean>();

		list.add(new ObservableActionItemBean(2342, 34535, 45, 45, false));
		table.setItems(list);

		TableColumn<ObservableActionItemBean, Long> delayColumn = new TableColumn<>("Delay");
		delayColumn.setCellValueFactory(new PropertyValueFactory<ObservableActionItemBean, Long>("delay"));
		TableColumn<ObservableActionItemBean, Long> releaseColumn = new TableColumn<>("Release");
		releaseColumn.setCellValueFactory(new PropertyValueFactory<ObservableActionItemBean, Long>("release"));
		TableColumn<ObservableActionItemBean, Long> delayIncrementColumn = new TableColumn<>("DelayInc");
		delayIncrementColumn.setCellValueFactory(new PropertyValueFactory<ObservableActionItemBean, Long>("delayIncrement"));
		TableColumn<ObservableActionItemBean, Long> releaseIncrementColumn = new TableColumn<>("ReleaseInc");
		releaseIncrementColumn.setCellValueFactory(new PropertyValueFactory<ObservableActionItemBean, Long>("releaseIncrement"));
		TableColumn<ObservableActionItemBean, Boolean> ignoreColumn = new TableColumn<>("Active");
		ignoreColumn.setCellValueFactory(new PropertyValueFactory<ObservableActionItemBean, Boolean>("ignore"));
		
		table.getColumns().addAll(delayColumn, releaseColumn, delayIncrementColumn, releaseIncrementColumn,
				ignoreColumn);
		table.setPrefHeight(200);

		setDelayColumnFactory(table, delayColumn);
		setReleaseColumnFactory(table, releaseColumn);
		setDelayIncrementColumnFactory(table, delayIncrementColumn);
		setReleaseIncrementColumnFactory(table, releaseIncrementColumn);

		table.setEditable(true);
		table.getSelectionModel().cellSelectionEnabledProperty().set(true);
		table.setOnKeyPressed(event -> {
			if (event.getCode().isLetterKey() || event.getCode().isDigitKey()) {
				editFocusedCell(table);
			} else if (event.getCode() == KeyCode.RIGHT || event.getCode() == KeyCode.TAB) {
				table.getSelectionModel().selectNext();
				event.consume();
			} else if (event.getCode() == KeyCode.LEFT) {
				// work around due to
				// TableView.getSelectionModel().selectPrevious() due to a bug
				// stopping it from working on
				// the first column in the last row of the table
				selectPrevious(table);
				event.consume();
			}
		});

		Button addButton = new Button("\uf055");
		addButton.getStyleClass().add("darkButton");
		addButton.setOnAction(value -> {
			table.getItems().add(new ObservableActionItemBean(1, 1, 1, 1, false));
		});

		Button removeButton = new Button("\uf056");
		removeButton.getStyleClass().add("darkButton");

		toolbar.getItems().add(addButton);
		toolbar.getItems().add(removeButton);
		toolbar.getItems().add(new TextField());
		toolbar.getItems().add(new ComboBox<String>());
		CheckBox activeButton = new CheckBox("Active");
		activeButton.setSelected(true);
		activeButton.setOnAction(value -> {
			table.setDisable(!activeButton.isSelected());
		});
		toolbar.getItems().add(activeButton);
		toolbar.getItems().add(spacer);
		Button closeButton = new Button("\uf410");
		closeButton.setOnAction(value -> {
			actionBox.getChildren().remove(vbox);
		});
		closeButton.getStyleClass().add("darkButton");

		toolbar.getItems().add(closeButton);
		vbox.getChildren().add(toolbar);

		vbox.getChildren().add(table);
		return vbox;
	}

	@SuppressWarnings("unchecked")
	private void editFocusedCell(TableView<ObservableActionItemBean> table) {
		final TablePosition<ObservableActionItemBean, ?> focusedCell = table.focusModelProperty().get()
				.focusedCellProperty().get();
		table.edit(focusedCell.getRow(), focusedCell.getTableColumn());
	}

	@SuppressWarnings("unchecked")
	private void selectPrevious(TableView<ObservableActionItemBean> table) {
		if (table.getSelectionModel().isCellSelectionEnabled()) {
			// in cell selection mode, we have to wrap around, going from
			// right-to-left, and then wrapping to the end of the previous line
			TablePosition<ObservableActionItemBean, ?> pos = table.getFocusModel().getFocusedCell();
			if (pos.getColumn() - 1 >= 0) {
				// go to previous row
				table.getSelectionModel().select(pos.getRow(), getTableColumn(table, pos.getTableColumn(), -1));
			} else if (pos.getRow() < table.getItems().size()) {
				// wrap to end of previous row
				table.getSelectionModel().select(pos.getRow() - 1,
						table.getVisibleLeafColumn(table.getVisibleLeafColumns().size() - 1));
			}
		} else {
			int focusIndex = table.getFocusModel().getFocusedIndex();
			if (focusIndex == -1) {
				table.getSelectionModel().select(table.getItems().size() - 1);
			} else if (focusIndex > 0) {
				table.getSelectionModel().select(focusIndex - 1);
			}
		}
	}

	private TableColumn<ObservableActionItemBean, ?> getTableColumn(TableView<ObservableActionItemBean> table,
			TableColumn<ObservableActionItemBean, ?> column, int offset) {
		int columnIndex = table.getVisibleLeafIndex(column);
		int newColumnIndex = columnIndex + offset;
		return table.getVisibleLeafColumn(newColumnIndex);
	}

	private void setDelayColumnFactory(final TableView<ObservableActionItemBean> table,
			final TableColumn<ObservableActionItemBean, Long> column) {
		column.setCellFactory(EditCell.<ObservableActionItemBean, Long>forTableColumn(new Long2StringConverter()));
		column.setOnEditCommit(event -> {
			final Long value = event.getNewValue() != null ? event.getNewValue() : event.getOldValue();
			((ObservableActionItemBean) event.getTableView().getItems().get(event.getTablePosition().getRow()))
					.setDelay(value);
			table.refresh();
		});
	}

	private void setReleaseColumnFactory(final TableView<ObservableActionItemBean> table,
			final TableColumn<ObservableActionItemBean, Long> column) {
		column.setCellFactory(EditCell.<ObservableActionItemBean, Long>forTableColumn(new Long2StringConverter()));
		column.setOnEditCommit(event -> {
			final Long value = event.getNewValue() != null ? event.getNewValue() : event.getOldValue();
			((ObservableActionItemBean) event.getTableView().getItems().get(event.getTablePosition().getRow()))
					.setRelease(value);
			table.refresh();
		});
	}

	private void setDelayIncrementColumnFactory(final TableView<ObservableActionItemBean> table,
			final TableColumn<ObservableActionItemBean, Long> column) {
		column.setCellFactory(EditCell.<ObservableActionItemBean, Long>forTableColumn(new Long2StringConverter()));
		column.setOnEditCommit(event -> {
			final Long value = event.getNewValue() != null ? event.getNewValue() : event.getOldValue();
			((ObservableActionItemBean) event.getTableView().getItems().get(event.getTablePosition().getRow()))
					.setDelayIncrement(value);
			table.refresh();
		});
	}

	private void setReleaseIncrementColumnFactory(final TableView<ObservableActionItemBean> table,
			final TableColumn<ObservableActionItemBean, Long> column) {
		column.setCellFactory(EditCell.<ObservableActionItemBean, Long>forTableColumn(new Long2StringConverter()));
		column.setOnEditCommit(event -> {
			final Long value = event.getNewValue() != null ? event.getNewValue() : event.getOldValue();
			((ObservableActionItemBean) event.getTableView().getItems().get(event.getTablePosition().getRow()))
					.setReleaseIncrement(value);
			table.refresh();
		});
	}

}
