package de.drazil.snatchfreezer;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

import de.drazil.snatchfreezer.model.ObservableActionItemBean;
import de.drazil.util.EditCell;
import de.drazil.util.Long2StringConverter;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SeparatorMenuItem;
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
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class SnatchFreezer extends Application {

	private GridPane valveBox = null;
	private GridPane actionBox = null;
	private List<ObservableList<ObservableActionItemBean>> mainList;

	private File file = null;

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

		mainList = new ArrayList<ObservableList<ObservableActionItemBean>>();

		Menu applicationMenu = new Menu("Application");
		MenuItem newProject = new MenuItem("New");
		MenuItem loadProject = new MenuItem("Load");
		MenuItem saveProject = new MenuItem("Save");
		saveProject.setOnAction(value -> {
			if (file == null) {
				FileChooser fc = new FileChooser();
				file = fc.showSaveDialog(null);
			}
			saveSettings(file);
		});
		MenuItem saveAsProject = new MenuItem("Save As ...");
		MenuItem quitApplciation = new MenuItem("Quit");
		applicationMenu.getItems().addAll(newProject, loadProject, saveProject, saveAsProject, new SeparatorMenuItem(),
				quitApplciation);
		Menu helpMenu = new Menu("Help");
		MenuItem help = new MenuItem("Help");
		MenuItem checkUpdates = new MenuItem("Check for Updates");
		MenuItem about = new MenuItem("About");
		helpMenu.getItems().addAll(help, checkUpdates, about);

		MenuBar menubar = new MenuBar();
		menubar.getMenus().add(applicationMenu);
		menubar.getMenus().add(helpMenu);
		valveBox = new GridPane();
		createTableSet(valveBox, 6, 5);

		actionBox = new GridPane();
		createTableSet(actionBox, 4, 1);

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

		//ScrollPane valveScrollPane = new ScrollPane(valveBox);
		//ScrollPane actionScrollPane = new ScrollPane(actionBox);

		GridPane formPane = new GridPane();
		formPane.add(new Label("Serial Port"), 0, 0);
		formPane.add(new TextField(), 1, 0);
		formPane.add(new Label("Cycles"), 0, 1);
		formPane.add(new TextField(), 1, 1);
		formPane.add(new Label("Cycledelay"), 0, 2);
		formPane.add(new Spinner<Long>(), 1, 2);
		
		VBox rightPane = new VBox();
		rightPane.getChildren().addAll(actionBox,formPane);
		
		
		HBox controlSplitPane = new HBox();
		controlSplitPane.getChildren().addAll(valveBox, rightPane);

		

		

		TextArea console = new TextArea();
		TextArea description = new TextArea();

		TabPane tabPane = new TabPane();
		tabPane.getTabs().add(new Tab("Console", console));
		tabPane.getTabs().add(new Tab("Description", description));

		SplitPane tabSplitPane = new SplitPane();
		tabSplitPane.setDividerPositions(0.8f, 0.2f);
		tabSplitPane.setOrientation(Orientation.VERTICAL);
		tabSplitPane.getItems().addAll(controlSplitPane, tabPane);

		VBox root = new VBox();
		root.getChildren().addAll(menubar, tabSplitPane);

		tabSplitPane.prefHeightProperty().bind(root.heightProperty());
		//actionScrollPane.prefHeightProperty().bind(root.heightProperty());

		Scene scene = new Scene(root, 1024, 768);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		primaryStage.setTitle("Snatchfreezer");
		// primaryStage.setMaximized(true);
		primaryStage.setScene(scene);
		primaryStage.show();

	}

	private void createTableSet(GridPane grid, int tableCount, int rowCount) {
		for (int i = 0, x = 0, y = 0; i < tableCount; i++) {
			if ((x % 2) == 0) {
				x = 0;
				y++;
			}
			grid.add(createTable(i + 1, rowCount), x, y);
			x++;
		}
	}

	@SuppressWarnings("unchecked")
	private Node createTable(int id, int rowCount) {
		VBox vbox = new VBox();
		ToolBar toolbar = new ToolBar();
		Region spacer = new Region();
		HBox.setHgrow(spacer, Priority.ALWAYS);
		spacer.setMinWidth(Region.USE_PREF_SIZE);

		int tableWidth = 60;
		TableView<ObservableActionItemBean> table = new TableView<>(createRowSet(rowCount));

		TableColumn<ObservableActionItemBean, Long> indexColumn = new TableColumn<>("#");
		indexColumn.setCellValueFactory(new PropertyValueFactory<ObservableActionItemBean, Long>("index"));
		indexColumn.setMinWidth(30);
		indexColumn.setMaxWidth(30);
		indexColumn.setPrefWidth(30);
		indexColumn.setSortable(false);
		indexColumn.setResizable(false);
		indexColumn.setEditable(false);

		TableColumn<ObservableActionItemBean, Long> delayColumn = new TableColumn<>("Delay");
		delayColumn.setCellValueFactory(new PropertyValueFactory<ObservableActionItemBean, Long>("delay"));
		delayColumn.setMinWidth(tableWidth);
		delayColumn.setMaxWidth(tableWidth);
		delayColumn.setPrefWidth(tableWidth);
		delayColumn.setSortable(false);
		delayColumn.setResizable(false);

		TableColumn<ObservableActionItemBean, Long> releaseColumn = new TableColumn<>("Release");
		releaseColumn.setCellValueFactory(new PropertyValueFactory<ObservableActionItemBean, Long>("release"));
		releaseColumn.setMinWidth(tableWidth);
		releaseColumn.setMaxWidth(tableWidth);
		releaseColumn.setPrefWidth(tableWidth);
		releaseColumn.setSortable(false);
		releaseColumn.setResizable(false);

		TableColumn<ObservableActionItemBean, Long> delayIncrementColumn = new TableColumn<>("Add");
		delayIncrementColumn
				.setCellValueFactory(new PropertyValueFactory<ObservableActionItemBean, Long>("delayIncrement"));
		delayIncrementColumn.setMinWidth(tableWidth);
		delayIncrementColumn.setMaxWidth(tableWidth);
		delayIncrementColumn.setPrefWidth(tableWidth);
		delayIncrementColumn.setSortable(false);
		delayIncrementColumn.setResizable(false);

		TableColumn<ObservableActionItemBean, Long> releaseIncrementColumn = new TableColumn<>("Add");
		releaseIncrementColumn
				.setCellValueFactory(new PropertyValueFactory<ObservableActionItemBean, Long>("releaseIncrement"));
		releaseIncrementColumn.setMinWidth(tableWidth);
		releaseIncrementColumn.setMaxWidth(tableWidth);
		releaseIncrementColumn.setPrefWidth(tableWidth);
		releaseIncrementColumn.setSortable(false);
		releaseIncrementColumn.setResizable(false);

		table.getColumns().addAll(indexColumn, delayColumn, delayIncrementColumn, releaseColumn,
				releaseIncrementColumn);
		table.setPrefHeight(27 + rowCount * 27);
		table.setPrefWidth(285);
		table.setEditable(true);

		table.getSelectionModel().cellSelectionEnabledProperty().set(true);
		table.setOnKeyPressed(event -> {
			if (event.getCode().isLetterKey() || event.getCode().isDigitKey()) {
				editFocusedCell(table);
			} else if (event.getCode() == KeyCode.RIGHT || event.getCode() == KeyCode.TAB) {
				table.getSelectionModel().selectNext();
				event.consume();
			} else if (event.getCode() == KeyCode.LEFT) {
				selectPrevious(table);
				event.consume();
			}
		});

		setIndexColumnFactory(table, indexColumn);
		setDelayColumnFactory(table, delayColumn);
		setReleaseColumnFactory(table, releaseColumn);
		setDelayIncrementColumnFactory(table, delayIncrementColumn);
		setReleaseIncrementColumnFactory(table, releaseIncrementColumn);

		Label idLabel = new Label(Integer.toString(id));

		Button addButton = new Button("\uf055");
		addButton.getStyleClass().add("darkButton");

		Button removeButton = new Button("\uf056");
		removeButton.getStyleClass().add("darkButton");
		toolbar.getItems().add(idLabel);
		// toolbar.getItems().add(addButton);
		// toolbar.getItems().add(removeButton);
		toolbar.getItems().add(new TextField());
		// toolbar.getItems().add(new ComboBox<String>());
		CheckBox activeButton = new CheckBox("Active");
		activeButton.setSelected(true);
		activeButton.setOnAction(value -> {
			table.setDisable(!activeButton.isSelected());
		});
		toolbar.getItems().add(activeButton);
		toolbar.getItems().add(spacer);
		Button closeButton = new Button("\uf410");
		closeButton.getStyleClass().add("darkButton");

		// toolbar.getItems().add(closeButton);
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
			TablePosition<ObservableActionItemBean, ?> pos = table.getFocusModel().getFocusedCell();
			if (pos.getColumn() - 1 >= 0) {
				table.getSelectionModel().select(pos.getRow(), getTableColumn(table, pos.getTableColumn(), -1));
			} else if (pos.getRow() < table.getItems().size()) {
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

	private void setIndexColumnFactory(final TableView<ObservableActionItemBean> table,
			final TableColumn<ObservableActionItemBean, Long> column) {
		column.setCellFactory(EditCell.<ObservableActionItemBean, Long>forTableColumn(new Long2StringConverter()));
		column.setOnEditCommit(event -> {
			final Long value = event.getNewValue() != null ? event.getNewValue() : event.getOldValue();
			((ObservableActionItemBean) event.getTableView().getItems().get(event.getTablePosition().getRow()))
					.setIndex(value);
			table.refresh();
		});
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

	private ObservableList<ObservableActionItemBean> createRowSet(int count) {
		ObservableList<ObservableActionItemBean> list = FXCollections.observableArrayList();
		for (int i = 0; i < count; i++) {
			list.add(new ObservableActionItemBean(new Long(i + 1), 10000000L, 1000000L, 100L, 20L));
		}
		mainList.add(list);
		return list;
	}

	private void saveSettings(File file) {

		if (file != null) {
			ObjectMapper mapper = new ObjectMapper();
			try {
				mapper.writeValue(file, mainList);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
