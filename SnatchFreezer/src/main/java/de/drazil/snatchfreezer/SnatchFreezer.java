package de.drazil.snatchfreezer;

import static de.drazil.snatchfreezer.Constants.OFF;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import de.drazil.snatchfreezer.model.ActionBean;
import de.drazil.snatchfreezer.model.ActionItemBean;
import de.drazil.snatchfreezer.model.ActionItemPropertyBean;
import de.drazil.snatchfreezer.model.Settings;
import de.drazil.util.EditCell;
import de.drazil.util.Long2StringConverter;
import javafx.application.Application;
import javafx.beans.property.Property;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.Spinner;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToolBar;
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

	private File file = null;
	private ResourceBundle messages = null;
	private Settings settings = null;
	private List<ObservableList<ActionItemPropertyBean>> actionList = null;
	private List<Property<?>> activePropertyList = null;
	private List<Property<?>> actionDescriptionPropertyList = null;
	private ConfigurationBuilder cb = null;
	private ComboBox<Object> serialPortComboBox = null;
	private Spinner<Integer> cyclesSpinner = null;
	private Spinner<Integer> cycleDelaySpinner = null;
	private TextArea console = null;
	private TextArea description = null;
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
		cb = new ConfigurationBuilder();
		actionList = new ArrayList<ObservableList<ActionItemPropertyBean>>();
		activePropertyList = new ArrayList<Property<?>>();
		messages = ResourceBundle.getBundle("de.drazil.snatchfreezer.i18n.message", Locale.ENGLISH);

		Font.loadFont(getClass().getResource("fa-solid-900.ttf").toExternalForm(), 10);

		Menu applicationMenu = new Menu(messages.getString("menu.application"));
		MenuItem newProject = new MenuItem(messages.getString("menu.application.new"));
		MenuItem loadProject = new MenuItem(messages.getString("menu.application.load"));
		loadProject.setOnAction(value -> {
			if (file == null) {
				FileChooser fc = new FileChooser();
				file = fc.showOpenDialog(null);
			}
			loadSettings(file);
		});
		MenuItem saveProject = new MenuItem(messages.getString("menu.application.save"));
		saveProject.setOnAction(value -> {
			if (file == null) {
				FileChooser fc = new FileChooser();
				file = fc.showSaveDialog(null);
			}
			saveSettings(file);
		});
		MenuItem saveAsProject = new MenuItem(messages.getString("menu.application.saveAs"));
		saveAsProject.setOnAction(value -> {
			FileChooser fc = new FileChooser();
			file = fc.showSaveDialog(null);
			saveSettings(file);
		});
		MenuItem quitApplciation = new MenuItem(messages.getString("menu.application.quit"));
		quitApplciation.setOnAction(value -> {
			System.exit(0);
		});

		applicationMenu.getItems().addAll(newProject, loadProject, saveProject, saveAsProject, new SeparatorMenuItem(),
				quitApplciation);
		Menu helpMenu = new Menu(messages.getString("menu.help"));
		MenuItem help = new MenuItem(messages.getString("menu.help.help"));
		MenuItem checkUpdates = new MenuItem(messages.getString("menu.help.updates"));
		MenuItem about = new MenuItem(messages.getString("menu.help.about"));
		helpMenu.getItems().addAll(help, checkUpdates, about);

		MenuBar menubar = new MenuBar();
		menubar.getMenus().add(applicationMenu);
		menubar.getMenus().add(helpMenu);
		valveBox = new GridPane();
		TitledPane valvePane = new TitledPane(messages.getString("pane.devices"), valveBox);
		valvePane.setCollapsible(false);
		valvePane.getStyleClass().add("titledValvePane");

		createTableSet(valveBox, 6, 5);

		actionBox = new GridPane();
		TitledPane cameraFlashPane = new TitledPane(messages.getString("pane.camera_flash"), actionBox);
		cameraFlashPane.getStyleClass().add("titledActionPane");
		cameraFlashPane.setCollapsible(false);
		createTableSet(actionBox, 4, 1);

		HBox actionButtonPane = new HBox();
		actionButtonPane.setMinHeight(60);
		actionButtonPane.setPrefHeight(60);
		actionButtonPane.setMaxHeight(60);

		Button camera = new Button("\uf083");
		camera.getStyleClass().add("orangeButton");
		camera.getStyleClass().add("bigButtonIcon");
		camera.setMinWidth(70);
		camera.setMaxWidth(70);
		camera.setPrefWidth(70);
		camera.setOnAction(value -> {
			buildShotConfiguration();
		});

		ProgressIndicator progressIndicator = new ProgressIndicator();
		progressIndicator.setProgress(-1f);

		console = new TextArea();
		description = new TextArea();

		VBox control2 = new VBox();
		GridPane formPane = new GridPane();
		TitledPane controlPane = new TitledPane(messages.getString("pane.control"), control2);

		controlPane.getStyleClass().add("titledControlPane");
		controlPane.setCollapsible(false);

		TabPane tabPane = new TabPane();
		tabPane.setManaged(true);
		tabPane.getTabs().add(new Tab(messages.getString("tab.console"), console));
		tabPane.getTabs().add(new Tab(messages.getString("tab.description"), description));

		serialPortComboBox = new ComboBox<Object>();
		formPane.add(new Label(messages.getString("label.serialPort")), 0, 0);
		formPane.add(serialPortComboBox, 1, 0);

		cyclesSpinner = new Spinner<Integer>(1, 50, 1);
		cyclesSpinner.setEditable(true);

		formPane.add(new Label(messages.getString("label.cycles")), 0, 1);
		formPane.add(cyclesSpinner, 1, 1);

		cycleDelaySpinner = new Spinner<Integer>(0, 10000, 0, 500);
		cycleDelaySpinner.setEditable(true);
		formPane.add(new Label(messages.getString("label.cycleDelay")), 0, 2);
		formPane.add(cycleDelaySpinner, 1, 2);

		HBox actionBar = new HBox();
		actionBar.getChildren().addAll(camera, progressIndicator);
		formPane.add(actionBar, 2, 0, 1, 3);

		control2.getChildren().addAll(formPane, tabPane);
		tabPane.setMinHeight(250);

		VBox rightPane = new VBox();

		rightPane.getChildren().addAll(cameraFlashPane, controlPane);

		rightPane.prefHeightProperty().bind(valvePane.heightProperty());

		HBox controlSplitPane = new HBox();
		controlSplitPane.getChildren().addAll(valvePane, rightPane);

		VBox root = new VBox();
		root.getChildren().addAll(menubar, controlSplitPane);

		Scene scene = new Scene(root, 1280, 768);
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
			ScrollPane sp = new ScrollPane(createTable(i + 1, rowCount, rowCount > 1));
			sp.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
			sp.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
			grid.add(sp, x, y);
			x++;
		}
	}

	@SuppressWarnings("unchecked")
	private Node createTable(int id, int rowCount, boolean canFlush) {

		Region spacer = new Region();
		HBox.setHgrow(spacer, Priority.ALWAYS);
		spacer.setMinWidth(Region.USE_COMPUTED_SIZE);

		int tableWidth = 60;
		ObservableList<ActionItemPropertyBean> list = createRowSet(rowCount);
		actionList.add(list);

		TableView<ActionItemPropertyBean> table = new TableView<>(list);

		TableColumn<ActionItemPropertyBean, Long> indexColumn = new TableColumn<>("#");
		indexColumn.setCellValueFactory(new PropertyValueFactory<ActionItemPropertyBean, Long>("index"));
		indexColumn.setMinWidth(30);
		indexColumn.setMaxWidth(30);
		indexColumn.setPrefWidth(30);
		indexColumn.setSortable(false);
		indexColumn.setResizable(false);
		indexColumn.setEditable(false);

		TableColumn<ActionItemPropertyBean, Long> delayColumn = new TableColumn<>(
				messages.getString("action.table.delay"));
		delayColumn.setCellValueFactory(new PropertyValueFactory<ActionItemPropertyBean, Long>("delay"));
		delayColumn.setMinWidth(tableWidth);
		delayColumn.setMaxWidth(tableWidth);
		delayColumn.setPrefWidth(tableWidth);
		delayColumn.setSortable(false);
		delayColumn.setResizable(false);

		TableColumn<ActionItemPropertyBean, Long> releaseColumn = new TableColumn<>(
				messages.getString("action.table.release"));
		releaseColumn.setCellValueFactory(new PropertyValueFactory<ActionItemPropertyBean, Long>("release"));
		releaseColumn.setMinWidth(tableWidth);
		releaseColumn.setMaxWidth(tableWidth);
		releaseColumn.setPrefWidth(tableWidth);
		releaseColumn.setSortable(false);
		releaseColumn.setResizable(false);

		TableColumn<ActionItemPropertyBean, Long> delayIncrementColumn = new TableColumn<>(
				messages.getString("action.table.cycleAdd"));
		delayIncrementColumn
				.setCellValueFactory(new PropertyValueFactory<ActionItemPropertyBean, Long>("delayIncrement"));
		delayIncrementColumn.setMinWidth(tableWidth);
		delayIncrementColumn.setMaxWidth(tableWidth);
		delayIncrementColumn.setPrefWidth(tableWidth);
		delayIncrementColumn.setSortable(false);
		delayIncrementColumn.setResizable(false);

		TableColumn<ActionItemPropertyBean, Long> releaseIncrementColumn = new TableColumn<>(
				messages.getString("action.table.cycleAdd"));
		releaseIncrementColumn
				.setCellValueFactory(new PropertyValueFactory<ActionItemPropertyBean, Long>("releaseIncrement"));
		releaseIncrementColumn.setMinWidth(tableWidth);
		releaseIncrementColumn.setMaxWidth(tableWidth);
		releaseIncrementColumn.setPrefWidth(tableWidth);
		releaseIncrementColumn.setSortable(false);
		releaseIncrementColumn.setResizable(false);

		table.getColumns().addAll(indexColumn, delayColumn, delayIncrementColumn, releaseColumn,
				releaseIncrementColumn);
		table.setPrefHeight(24 + rowCount * 25);
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
		idLabel.getStyleClass().add("fatLabel");

		TextField description = new TextField();
		Button flush = new Button("\uf576");
		flush.getStyleClass().add("blueButton");
		flush.setOnAction(value -> {
			buildFlushConfiguration();
		});
		Button heart = new Button("\uf004");
		heart.getStyleClass().add("redButton");
		heart.setOnAction(value -> {
			buildTestConfiguration();
		});

		ToggleButton activeButton = new ToggleButton("\uf205");
		activeButton.getStyleClass().add("toggleOn");
		activeButton.setSelected(true);
		activeButton.setOnAction(value -> {
			table.setDisable(!activeButton.isSelected());
			idLabel.setDisable(!activeButton.isSelected());
			description.setDisable(!activeButton.isSelected());
			heart.setDisable(!activeButton.isSelected());
			flush.setDisable(!activeButton.isSelected());
			activeButton.getStyleClass().remove("toggleOn");
			activeButton.getStyleClass().remove("toggleOff");
			activeButton.getStyleClass().add(activeButton.isSelected() ? "toggleOn" : "toggleOff");
			activeButton.setText(activeButton.isSelected() ? "\uf205" : "\uf204");
		});

		activePropertyList.add(activeButton.selectedProperty());

		ToolBar toolbar = new ToolBar();
		toolbar.getItems().add(idLabel);
		toolbar.getItems().add(activeButton);
		toolbar.getItems().add(description);
		if (canFlush) {
			toolbar.getItems().add(flush);
		}

		toolbar.getItems().add(spacer);
		toolbar.getItems().add(heart);

		VBox vbox = new VBox();
		vbox.getChildren().addAll(toolbar, table);

		return vbox;
	}

	@SuppressWarnings("unchecked")
	private void editFocusedCell(TableView<ActionItemPropertyBean> table) {
		final TablePosition<ActionItemPropertyBean, ?> focusedCell = table.focusModelProperty().get()
				.focusedCellProperty().get();
		table.edit(focusedCell.getRow(), focusedCell.getTableColumn());
	}

	@SuppressWarnings("unchecked")
	private void selectPrevious(TableView<ActionItemPropertyBean> table) {
		if (table.getSelectionModel().isCellSelectionEnabled()) {
			TablePosition<ActionItemPropertyBean, ?> pos = table.getFocusModel().getFocusedCell();
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

	private TableColumn<ActionItemPropertyBean, ?> getTableColumn(TableView<ActionItemPropertyBean> table,
			TableColumn<ActionItemPropertyBean, ?> column, int offset) {
		int columnIndex = table.getVisibleLeafIndex(column);
		int newColumnIndex = columnIndex + offset;
		return table.getVisibleLeafColumn(newColumnIndex);
	}

	private void setIndexColumnFactory(final TableView<ActionItemPropertyBean> table,
			final TableColumn<ActionItemPropertyBean, Long> column) {
		column.setCellFactory(EditCell.<ActionItemPropertyBean, Long>forTableColumn(new Long2StringConverter()));

		column.setOnEditCommit(event -> {
			final Long value = event.getNewValue() != null ? event.getNewValue() : event.getOldValue();
			((ActionItemPropertyBean) event.getTableView().getItems().get(event.getTablePosition().getRow()))
					.setIndex(value);
			table.refresh();
		});
	}

	private void setDelayColumnFactory(final TableView<ActionItemPropertyBean> table,
			final TableColumn<ActionItemPropertyBean, Long> column) {
		column.setCellFactory(EditCell.<ActionItemPropertyBean, Long>forTableColumn(new Long2StringConverter()));
		column.setOnEditCommit(event -> {
			final Long value = event.getNewValue() != null ? event.getNewValue() : event.getOldValue();
			((ActionItemPropertyBean) event.getTableView().getItems().get(event.getTablePosition().getRow()))
					.setDelay(value);
			table.refresh();
		});
	}

	private void setReleaseColumnFactory(final TableView<ActionItemPropertyBean> table,
			final TableColumn<ActionItemPropertyBean, Long> column) {
		column.setCellFactory(EditCell.<ActionItemPropertyBean, Long>forTableColumn(new Long2StringConverter()));
		column.setOnEditCommit(event -> {
			final Long value = event.getNewValue() != null ? event.getNewValue() : event.getOldValue();
			((ActionItemPropertyBean) event.getTableView().getItems().get(event.getTablePosition().getRow()))
					.setRelease(value);
			table.refresh();
		});
	}

	private void setDelayIncrementColumnFactory(final TableView<ActionItemPropertyBean> table,
			final TableColumn<ActionItemPropertyBean, Long> column) {
		column.setCellFactory(EditCell.<ActionItemPropertyBean, Long>forTableColumn(new Long2StringConverter()));
		column.setOnEditCommit(event -> {
			final Long value = event.getNewValue() != null ? event.getNewValue() : event.getOldValue();
			((ActionItemPropertyBean) event.getTableView().getItems().get(event.getTablePosition().getRow()))
					.setDelayIncrement(value);
			table.refresh();
		});
	}

	private void setReleaseIncrementColumnFactory(final TableView<ActionItemPropertyBean> table,
			final TableColumn<ActionItemPropertyBean, Long> column) {
		column.setCellFactory(EditCell.<ActionItemPropertyBean, Long>forTableColumn(new Long2StringConverter()));
		column.setOnEditCommit(event -> {
			final Long value = event.getNewValue() != null ? event.getNewValue() : event.getOldValue();
			((ActionItemPropertyBean) event.getTableView().getItems().get(event.getTablePosition().getRow()))
					.setReleaseIncrement(value);
			table.refresh();
		});
	}

	private ObservableList<ActionItemPropertyBean> createRowSet(int count) {
		ObservableList<ActionItemPropertyBean> list = FXCollections.observableArrayList();
		for (int i = 0; i < count; i++) {
			ActionItemBean actionBean = new ActionItemBean(i + 1, 0L, 0L, 0L, 0L, false);
			list.add(new ActionItemPropertyBean(actionBean));
		}

		return list;
	}

	private Settings getSettings() {
		if (settings == null) {
			settings = new Settings();
		}
		List<ActionBean> actionBeanList = new ArrayList<ActionBean>();
		for (int i = 0; i < actionList.size(); i++) {
			List<ActionItemPropertyBean> list = actionList.get(i);

			List<ActionItemBean> beanList = new ArrayList<ActionItemBean>();
			for (int j = 0; j < list.size(); j++) {
				beanList.add(list.get(j).getBean());
			}
			ActionBean actionBean = new ActionBean(i + 1, true, "", beanList);
			actionBeanList.add(actionBean);
		}
		settings.setActionBeanList(actionBeanList);
		settings.setCycleDelay(cycleDelaySpinner.getValue());
		settings.setCycles(cyclesSpinner.getValue());
		settings.setDescription(description.getText());

		return settings;
	}

	private void setSettings(Settings settings) {
		this.settings = settings;

		List<ActionBean> actionBeanList = settings.getActionBeanList();
		description.setText(settings.getDescription());
		cycleDelaySpinner.getValueFactory().setValue(settings.getCycleDelay());
		cyclesSpinner.getValueFactory().setValue(settings.getCycles());

		for (int i = 0; i < actionBeanList.size(); i++) {
			ObservableList<ActionItemPropertyBean> list = actionList.get(i);
			list.clear();
			List<ActionItemBean> actionItemBeanList = actionBeanList.get(i).getActionItemList();
			for (int j = 0; j < actionItemBeanList.size(); j++) {

				ActionItemBean actionItembean = actionItemBeanList.get(j);
				list.add(new ActionItemPropertyBean(actionItembean.getId(), actionItembean.getDelay(),
						actionItembean.getRelease(), actionItembean.getDelayIncrement(),
						actionItembean.getReleaseIncrement(), actionItembean.isIgnore()));
			}
		}
	}

	private void saveSettings(File file) {

		if (file != null) {
			ObjectMapper mapper = new ObjectMapper();
			try {
				mapper.enable(SerializationFeature.INDENT_OUTPUT);
				mapper.writeValue(file, getSettings());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void loadSettings(File file) {
		if (file != null) {
			ObjectMapper mapper = new ObjectMapper();
			try {
				setSettings(mapper.readValue(file, Settings.class));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void buildShotConfiguration() {
		cb.reset();
		cb.addSetLogLevel(OFF);
		cb.addReset();
		for (int i = 0; i < actionList.size(); i++) {
			cb.addAction(i + 2);
			ObservableList<ActionItemPropertyBean> list = actionList.get(i);
			for (int j = 0; j < list.size(); j++) {
				ActionItemPropertyBean bean = list.get(j);
				if (!isEmptyAction(bean)) {
					cb.addActionTimings(bean.getDelay(), bean.getRelease(), bean.getDelayIncrement(),
							bean.getReleaseIncrement());
				}
			}
		}
		cb.addCycleCount(cyclesSpinner.getValue());
		cb.addCycleDelay(cycleDelaySpinner.getValue());
		cb.addRun();
	}

	private void buildTestConfiguration() {

	}

	private void buildFlushConfiguration() {

	}

	private boolean isEmptyAction(ActionItemPropertyBean bean) {
		return bean.getDelay() == 0 && bean.getRelease() == 0 && bean.getDelayIncrement() == 0
				&& bean.getReleaseIncrement() == 0;
	}
}
