package de.drazil.snatchfreezer;

import static de.drazil.util.Constants.COMMAND_CANCEL;
import static de.drazil.util.Constants.COMMAND_DUMMY;
import static de.drazil.util.Constants.COMMAND_ECHO;
import static de.drazil.util.Constants.COMMAND_FINISHED;
import static de.drazil.util.Constants.COMMAND_LOG_DEBUG;
import static de.drazil.util.Constants.COMMAND_LOG_ERROR;
import static de.drazil.util.Constants.COMMAND_LOG_INFO;
import static de.drazil.util.Constants.COMMAND_LOG_OFF;
import static de.drazil.util.Constants.COMMAND_NEXT;
import static de.drazil.util.Constants.COMMAND_REPEAT;
import static de.drazil.util.Constants.COMMAND_RESET;
import static de.drazil.util.Constants.COMMAND_RUN;
import static de.drazil.util.Constants.COMMAND_SET_CYCLE_COUNT;
import static de.drazil.util.Constants.EXECUTE_COMMAND;
import static de.drazil.util.Constants.FLUSH_OFF;
import static de.drazil.util.Constants.FLUSH_ON;
import static de.drazil.util.Constants.HELO;
import static de.drazil.util.Constants.INFO;
import static de.drazil.util.Constants.MESSAGE_ADD_ACTION;
import static de.drazil.util.Constants.MESSAGE_ADD_ACTION_TIMIMGS;
import static de.drazil.util.Constants.MESSAGE_BYTE;
import static de.drazil.util.Constants.MESSAGE_CANCEL;
import static de.drazil.util.Constants.MESSAGE_CHECKSUM_ERROR;
import static de.drazil.util.Constants.MESSAGE_CHECKSUM_OK;
import static de.drazil.util.Constants.MESSAGE_DWORD;
import static de.drazil.util.Constants.MESSAGE_ECHO;
import static de.drazil.util.Constants.MESSAGE_MAX_ACTION_COUNT;
import static de.drazil.util.Constants.MESSAGE_NO_PARAMETER;
import static de.drazil.util.Constants.MESSAGE_NUMBER_PARAMETER;
import static de.drazil.util.Constants.MESSAGE_READ_CHECKSUM;
import static de.drazil.util.Constants.MESSAGE_READ_COMMAND;
import static de.drazil.util.Constants.MESSAGE_READ_DATA;
import static de.drazil.util.Constants.MESSAGE_READ_LENGTH;
import static de.drazil.util.Constants.MESSAGE_RESET;
import static de.drazil.util.Constants.MESSAGE_RUN;
import static de.drazil.util.Constants.MESSAGE_SET_ACTION_DELAY;
import static de.drazil.util.Constants.MESSAGE_SET_ACTION_DELAY_INCREMENT;
import static de.drazil.util.Constants.MESSAGE_SET_ACTION_PIN;
import static de.drazil.util.Constants.MESSAGE_SET_ACTION_RELEASE;
import static de.drazil.util.Constants.MESSAGE_SET_ACTION_RELEASE_INCREMENT;
import static de.drazil.util.Constants.MESSAGE_SET_CYCLE_COUNT;
import static de.drazil.util.Constants.MESSAGE_SET_CYCLE_DELAY;
import static de.drazil.util.Constants.MESSAGE_SET_LOG_LEVEL;
import static de.drazil.util.Constants.MESSAGE_STRING_PARAMETER;
import static de.drazil.util.Constants.MESSAGE_SYNC1;
import static de.drazil.util.Constants.MESSAGE_SYNC2;
import static de.drazil.util.Constants.MESSAGE_WORD;
import static de.drazil.util.Constants.PARAMETER_NUMBER;
import static de.drazil.util.Constants.PARAMETER_STRING;
import static de.drazil.util.Constants.READ_COMMAND;
import static de.drazil.util.Constants.READ_DATA;
import static de.drazil.util.Constants.READ_DATA_PREFIX;
import static de.drazil.util.Constants.READ_LENGTH;
import static de.drazil.util.Constants.SHOT;
import static de.drazil.util.Constants.SYNCBYTE1;
import static de.drazil.util.Constants.SYNCBYTE2;
import static de.drazil.util.Constants.TEST;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;

import de.drazil.snatchfreezer.model.ActionBean;
import de.drazil.snatchfreezer.model.ActionItemBean;
import de.drazil.snatchfreezer.model.ActionItemPropertyBean;
import de.drazil.snatchfreezer.model.Settings;
import de.drazil.util.ArrayUtil;
import de.drazil.util.ConfigurationBuilder;
import de.drazil.util.EditCell;
import de.drazil.util.Long2StringConverter;
import de.drazil.util.NumericConverter;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.Property;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckMenuItem;
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
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class SnatchFreezer extends Application {

	// cccccctt-llllllll-dddddddd...
	// type = ttt
	// 00 000000 = no parameter
	// 10 000000 = string
	// 11 000000 = number
	// command = cccccc

	// 11|000001 = set action pin
	// 11|000010 = set action delay time
	// 11|000011 = set action release time
	// 11|000100 = set action delay increment time
	// 11|000101 = set action release increment time
	// 11|000110 = set cycle count
	// 11|000111 = set cycle delay
	// 11|001001 = flush
	// 11|001010 = test
	// 11|001011 = set log level
	// 00|000001 = add action
	// 00|000010 = add action timings
	// 00|000011 = next command
	// 00|000100 = repeat command
	// 00|000110 = run
	// 00|000111 = cancel
	// 00|001000 = reset
	// 00|001001 = finished
	// 00|111111 = dummy
	// 10|001001 = echo
	// 11|001010 = log_info
	// 11|001011 = log_debug
	// 11|001100 = log_error

	// llllllll = length

	// new protocol frame
	// SYNC1|SYNC2|COMMAND|PARAMETER_COUNT|PARAMETER_TYPE1|PARAMETER_LENGTH1|VALUE1|..|CHECKSUM

	private GridPane valveBox = null;
	private GridPane actionBox = null;
	private Button camera = null;

	private File file = null;
	private ResourceBundle messages = null;
	private Settings settings = null;
	private List<ObservableList<ActionItemPropertyBean>> actionList = null;
	private List<Property<?>> activePropertyList = null;
	private List<Property<?>> actionDescriptionPropertyList = null;
	private ConfigurationBuilder cb = null;
	private ComboBox<String> serialPortComboBox = null;
	private Spinner<Integer> cyclesSpinner = null;
	private Spinner<Integer> cycleDelaySpinner = null;
	private TextArea console = null;
	private TextArea description = null;
	private Label connectedButton = null;
	private ProgressIndicator transferIndicator = null;
	private ProgressIndicator progressIndicator = null;

	private Iterator<byte[]> dataIterator;
	private byte currentCommandBuffer[];
	private byte byteBuffer[] = new byte[] {};
	private byte readDataBuffer[] = null;
	private int lastAvailableBytes = 0;
	private int availableBytes = 0;
	private int dataLength = 0;
	private int dataIndex;
	private int currentCommand = 0;
	private SerialPort serialPort;
	private SerialPort serialPorts[];
	private String ttyDevice = null;
	private String[] portNames;
	private int phase;
	private int value;
	private int syncCount = 0;
	private int readIndex = 0;
	private boolean canceled = false;
	private boolean heloMode = false;

	private ExtensionFilter fileFilter = null;

	private List<Integer> pinOutMappingValve = null;
	private List<Integer> pinOutMappingCameraFlash = null;
	private List<Integer> pinOutMappingAll = null;

	private File projectFolder = null;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {

		String sep = System.getProperty("file.separator");
		File projectFolder = new File(System.getProperty("user.home") + sep + ".snatchfreezer");
		boolean exists = projectFolder.exists();
		if (!exists) {
			projectFolder.mkdir();
		}

		fileFilter = new ExtensionFilter("SnatchFreezer Settings", "sf");
		

		pinOutMappingValve = new ArrayList<Integer>();
		pinOutMappingValve.add(new Integer(2));
		pinOutMappingValve.add(new Integer(3));
		pinOutMappingValve.add(new Integer(4));
		pinOutMappingValve.add(new Integer(5));
		pinOutMappingValve.add(new Integer(6));
		pinOutMappingValve.add(new Integer(7));

		pinOutMappingCameraFlash = new ArrayList<Integer>();
		pinOutMappingCameraFlash.add(new Integer(8));
		pinOutMappingCameraFlash.add(new Integer(9));
		pinOutMappingCameraFlash.add(new Integer(10));
		pinOutMappingCameraFlash.add(new Integer(11));

		pinOutMappingAll = new ArrayList<Integer>();
		pinOutMappingAll.addAll(pinOutMappingValve);
		pinOutMappingAll.addAll(pinOutMappingCameraFlash);

		cb = new ConfigurationBuilder();
		actionList = new ArrayList<ObservableList<ActionItemPropertyBean>>();
		activePropertyList = new ArrayList<Property<?>>();
		messages = ResourceBundle.getBundle("de.drazil.snatchfreezer.i18n.message", Locale.ENGLISH);

		Font.loadFont(getClass().getResource("fa-solid-900.ttf").toExternalForm(), 10);

		Region spacer = new Region();
		HBox.setHgrow(spacer, Priority.ALWAYS);
		spacer.setMinWidth(Region.USE_COMPUTED_SIZE);

		Menu applicationMenu = new Menu(messages.getString("menu.application"));
		MenuItem newProject = new MenuItem(messages.getString("menu.application.new"));
		MenuItem recent = new MenuItem(messages.getString("menu.application.recent"));
		MenuItem loadProject = new MenuItem(messages.getString("menu.application.load"));
		loadProject.setOnAction(value -> {
			FileChooser fc = new FileChooser();
			fc.setInitialDirectory(projectFolder);
			fc.setSelectedExtensionFilter(fileFilter);
			file = fc.showOpenDialog(null);
			loadSettings(file);
		});
		MenuItem saveProject = new MenuItem(messages.getString("menu.application.save"));
		saveProject.setOnAction(value -> {
			if (file == null) {
				FileChooser fc = new FileChooser();
				fc.setInitialDirectory(projectFolder);
				fc.setSelectedExtensionFilter(fileFilter);
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

		applicationMenu.getItems().addAll(newProject, recent, loadProject, saveProject, saveAsProject,
				new SeparatorMenuItem(), quitApplciation);

		Menu controlMenu = new Menu(messages.getString("menu.control"));
		CheckMenuItem flushAll = new CheckMenuItem(messages.getString("menu.control.flushAll"));
		MenuItem testAll = new MenuItem(messages.getString("menu.control.testAll"));
		controlMenu.getItems().addAll(flushAll, testAll);

		Menu presetMenu = new Menu(messages.getString("menu.preset"));
		MenuItem presetCROWN = new MenuItem(messages.getString("menu.preset.crown"));
		MenuItem presetTAT = new MenuItem(messages.getString("menu.preset.tat"));
		presetMenu.getItems().addAll(presetCROWN, presetTAT);

		Menu helpMenu = new Menu(messages.getString("menu.help"));
		MenuItem help = new MenuItem(messages.getString("menu.help.help"));
		MenuItem checkUpdates = new MenuItem(messages.getString("menu.help.updates"));
		MenuItem about = new MenuItem(messages.getString("menu.help.about"));
		helpMenu.getItems().addAll(help, checkUpdates, about);

		MenuBar menubar = new MenuBar();
		menubar.getMenus().add(applicationMenu);
		menubar.getMenus().add(controlMenu);
		menubar.getMenus().add(presetMenu);
		menubar.getMenus().add(helpMenu);
		valveBox = new GridPane();
		TitledPane valvePane = new TitledPane(messages.getString("pane.devices"), valveBox);
		valvePane.setCollapsible(false);
		valvePane.getStyleClass().add("titledValvePane");

		createTableSet(valveBox, 6, 5, pinOutMappingValve);

		actionBox = new GridPane();
		TitledPane cameraFlashPane = new TitledPane(messages.getString("pane.camera_flash"), actionBox);
		cameraFlashPane.getStyleClass().add("titledActionPane");
		cameraFlashPane.setCollapsible(false);
		createTableSet(actionBox, 4, 1, pinOutMappingCameraFlash);

		camera = new Button("\uf083");
		camera.getStyleClass().add("orangeButton");
		camera.getStyleClass().add("bigButtonIcon");
		camera.setMinWidth(70);
		camera.setMaxWidth(70);
		camera.setPrefWidth(70);
		camera.setOnAction(value -> {
			try {
				run();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});

		transferIndicator = new ProgressIndicator(0);
		transferIndicator.setProgress(20);
		transferIndicator.setPrefHeight(60);
		transferIndicator.setPrefWidth(60);
		transferIndicator.setPadding(new Insets(10, 0, 0, 0));

		progressIndicator = new ProgressIndicator(0);
		progressIndicator.setProgress(20);
		progressIndicator.setPrefHeight(60);
		progressIndicator.setPrefWidth(60);
		progressIndicator.setPadding(new Insets(10, 0, 0, 0));

		console = new TextArea();
		description = new TextArea();

		VBox control2 = new VBox();
		GridPane formPane = new GridPane();
		formPane.setMinWidth(300);
		formPane.setPrefWidth(300);
		formPane.setMaxWidth(300);
		TitledPane controlPane = new TitledPane(messages.getString("pane.control"), control2);

		controlPane.getStyleClass().add("titledControlPane");
		controlPane.setCollapsible(false);

		TabPane tabPane = new TabPane();
		tabPane.setManaged(true);
		tabPane.getTabs().add(new Tab(messages.getString("tab.console"), console));
		tabPane.getTabs().add(new Tab(messages.getString("tab.description"), description));

		serialPortComboBox = new ComboBox<String>();
		formPane.add(new Label(messages.getString("label.serialPort")), 0, 0);
		formPane.add(serialPortComboBox, 1, 0);

		connectedButton = new Label("\uf119");
		connectedButton.getStyleClass().add("imageLabel");
		connectedButton.getStyleClass().add("darkRedButton");
		formPane.add(connectedButton, 2, 0);

		cyclesSpinner = new Spinner<Integer>(1, 50, 1);
		cyclesSpinner.setEditable(true);

		formPane.add(new Label(messages.getString("label.cycles")), 0, 1);
		formPane.add(cyclesSpinner, 1, 1, 2, 1);

		cycleDelaySpinner = new Spinner<Integer>(0, 10000, 0, 500);
		cycleDelaySpinner.setEditable(true);
		formPane.add(new Label(messages.getString("label.cycleDelay")), 0, 2);
		formPane.add(cycleDelaySpinner, 1, 2, 2, 1);

		ToolBar actionBar = new ToolBar();
		actionBar.getItems().addAll(spacer, camera, transferIndicator, progressIndicator);

		HBox controlBar = new HBox();
		controlBar.getChildren().addAll(formPane, actionBar);

		control2.getChildren().addAll(controlBar, tabPane);
		tabPane.setMinHeight(250);

		VBox rightPane = new VBox();

		rightPane.getChildren().addAll(cameraFlashPane, controlPane);
		rightPane.prefHeightProperty().bind(valvePane.heightProperty());

		HBox controlSplitPane = new HBox();
		controlSplitPane.getChildren().addAll(valvePane, rightPane);

		VBox root = new VBox();
		root.getChildren().addAll(menubar, controlSplitPane);

		Scene scene = new Scene(root, 1220, 768);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		primaryStage.setTitle("Snatchfreezer");
		// primaryStage.setMaximized(true);
		primaryStage.setScene(scene);
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent event) {
				System.exit(0);
			}
		});

		primaryStage.show();

		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				initialzeSerialPortSelector();
				initializeSerialConnection();
			}
		});
	}

	private void createTableSet(GridPane grid, int tableCount, int rowCount, List<Integer> pinOutMapping) {
		for (int i = 0, x = 0, y = 0; i < tableCount; i++) {
			if ((x % 2) == 0) {
				x = 0;
				y++;
			}
			ScrollPane sp = new ScrollPane(createTable(i, rowCount, rowCount > 1, pinOutMapping));
			sp.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
			sp.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
			grid.add(sp, x, y);
			x++;
		}
	}

	@SuppressWarnings("unchecked")
	private Node createTable(int id, int rowCount, boolean canFlush, List<Integer> pinOutMapping) {

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

		Label idLabel = new Label(Integer.toString(id + 1));
		idLabel.getStyleClass().add("fatLabel");

		TextField description = new TextField();
		ToggleButton flush = new ToggleButton("\uf576");
		flush.getStyleClass().add("flushButton");
		flush.setUserData(pinOutMapping.get(id));
		flush.setOnAction(value -> {
			ToggleButton b = (ToggleButton) value.getSource();
			int buttonId = (Integer) b.getUserData();
			buildFlushConfiguration(buttonId, b.isSelected());
		});
		Button bolt = new Button("\uf0e7");
		bolt.getStyleClass().add("testButton");
		bolt.setUserData(pinOutMapping.get(id));
		bolt.setOnAction(value -> {
			Button b = (Button) value.getSource();
			int buttonId = (Integer) b.getUserData();
			buildTestConfiguration(buttonId);
		});

		ToggleButton activeButton = new ToggleButton("\uf205");
		activeButton.getStyleClass().add("toggleOn");
		activeButton.setSelected(true);
		activeButton.setOnAction(value -> {
			table.setDisable(!activeButton.isSelected());
			idLabel.setDisable(!activeButton.isSelected());
			description.setDisable(!activeButton.isSelected());
			bolt.setDisable(!activeButton.isSelected());
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
		toolbar.getItems().add(bolt);

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
				ActionItemPropertyBean propertyBean = new ActionItemPropertyBean(actionItembean.getId(),
						actionItembean.getDelay(), actionItembean.getRelease(), actionItembean.getDelayIncrement(),
						actionItembean.getReleaseIncrement(), actionItembean.isIgnore());
				propertyBean.setBean(actionItembean);
				list.add(propertyBean);

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

	private boolean buildShotConfiguration() {
		cb.reset();
		cb.addSetLogLevel(INFO);
		cb.addReset();
		int hasActions = 0;
		for (int i = 0; i < actionList.size(); i++) {
			ObservableList<ActionItemPropertyBean> list = actionList.get(i);
			if (!isTableEmpty(list)) {
				hasActions++;
				// cb.addAction(i + 2);
				cb.addAction(pinOutMappingAll.get(i));
				for (int j = 0; j < list.size(); j++) {
					ActionItemPropertyBean bean = list.get(j);
					if (!isEmptyOrIgnoredAction(bean)) {
						cb.addActionTimings(bean.getDelay(), bean.getRelease(), bean.getDelayIncrement(),
								bean.getReleaseIncrement());
					}
				}
			}
		}
		cb.addCycleCount(cyclesSpinner.getValue());
		cb.addCycleDelay(cycleDelaySpinner.getValue());
		cb.addRun(SHOT);
		return hasActions > 0;
	}

	private void buildTestConfiguration(int pin) {
		cb.reset();
		cb.addRun(TEST | (pin << 8));
		phase = READ_DATA_PREFIX;
		byteBuffer = new byte[] {};
		readIndex = 0;
		canceled = false;
		try {
			sendNextCommand();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void buildFlushConfiguration(int pin, boolean active) {
		cb.reset();
		cb.addRun((active ? FLUSH_ON : FLUSH_OFF) | (pin << 8));
		phase = READ_DATA_PREFIX;
		byteBuffer = new byte[] {};
		readIndex = 0;
		canceled = false;
		try {
			sendNextCommand();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void buildHeloConfiguration() {
		cb.reset();
		cb.addRun(HELO);
		phase = READ_DATA_PREFIX;
		byteBuffer = new byte[] {};
		readIndex = 0;
		canceled = false;
		try {
			sendNextCommand();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private boolean isTableEmpty(ObservableList<ActionItemPropertyBean> list) {
		int x = 0;
		for (int i = 0; i < list.size(); i++) {
			x += (isEmptyOrIgnoredAction(list.get(i)) ? 0 : 1);
		}
		return x == 0;
	}

	private boolean isEmptyOrIgnoredAction(ActionItemPropertyBean bean) {
		return (bean.getDelay() == 0 && bean.getRelease() == 0 && bean.getDelayIncrement() == 0
				&& bean.getReleaseIncrement() == 0) || bean.getIgnore();
	}

	private void initialzeSerialPortSelector() {
		serialPorts = SerialPort.getCommPorts();

		for (int i = 0; i < serialPorts.length; i++) {
			serialPortComboBox.getItems().add(serialPorts[i].getSystemPortName());
		}

		if (serialPorts.length == 1) {
			ttyDevice = serialPorts[0].getSystemPortName();
			serialPortComboBox.setValue(ttyDevice);
			serialPort = serialPorts[0];
		}
	}

	private void initializeSerialConnection() {
		serialPort.setComPortParameters(19200, 8, 1, 0);
		if (serialPort.openPort()) {
			connectedButton.setText("\uf118");
			connectedButton.getStyleClass().add("darkGreenButton");
		}

		serialPort.addDataListener(new SerialPortDataListener() {
			@Override
			public int getListeningEvents() {
				return SerialPort.LISTENING_EVENT_DATA_AVAILABLE;
			}

			@Override
			public void serialEvent(SerialPortEvent ev) {

				if (ev.getEventType() != SerialPort.LISTENING_EVENT_DATA_AVAILABLE)
					return;

				byteBuffer = buildBuffer(ev.getSerialPort());

				while (readIndex < byteBuffer.length && !canceled) {

					switch (phase) {
					case READ_DATA_PREFIX: {
						value = ((int) byteBuffer[readIndex++] & 0xff);
						switch (value) {
						case SYNCBYTE1: {
							++syncCount;
							break;
						}
						case SYNCBYTE2: {
							++syncCount;
							if (syncCount == 2) {
								syncCount = 0;
								phase = READ_COMMAND;
							}
							break;
						}
						}
						break;
					}
					case READ_COMMAND: {
						value = ((int) byteBuffer[readIndex++] & 0xff);
						currentCommand = 0;
						switch (value) {
						case COMMAND_SET_CYCLE_COUNT:
						case COMMAND_ECHO:
						case COMMAND_LOG_DEBUG:
						case COMMAND_LOG_INFO:
						case COMMAND_LOG_ERROR: {
							phase = READ_LENGTH;
							break;
						}
						case COMMAND_RUN:
						case COMMAND_RESET:
						case COMMAND_NEXT:
						case COMMAND_REPEAT:
						case COMMAND_CANCEL:
						case COMMAND_FINISHED:
						case COMMAND_DUMMY: {
							phase = EXECUTE_COMMAND;
							break;
						}
						}
						currentCommand = value;
						break;
					}
					case READ_LENGTH: {
						value = ((int) byteBuffer[readIndex++] & 0xff);
						dataIndex = 0;
						dataLength = value;
						readDataBuffer = new byte[dataLength];
						phase = READ_DATA;
						break;
					}
					case READ_DATA: {
						if (readIndex + dataLength > byteBuffer.length)
							return;

						while (dataIndex < dataLength && readIndex < byteBuffer.length) {
							value = ((int) byteBuffer[readIndex++] & 0xff);
							readDataBuffer[dataIndex++] = (byte) value;
						}
						phase = EXECUTE_COMMAND;
					}
					case EXECUTE_COMMAND: {
						String s = "";
						long value = 0;
						if ((currentCommand & PARAMETER_NUMBER) == PARAMETER_NUMBER) {
							switch (dataLength) {
							case 1: {
								value = NumericConverter.toByte(readDataBuffer);
								break;
							}
							case 2: {
								value = NumericConverter.toInt(readDataBuffer);
								break;
							}
							case 4: {
								value = NumericConverter.toLong(readDataBuffer);
								break;
							}
							}

						} else if ((currentCommand & PARAMETER_STRING) == PARAMETER_STRING) {
							s = new String(readDataBuffer, 0, dataLength);
						} else {
						}
						phase = READ_DATA_PREFIX;
						switch (currentCommand) {
						case COMMAND_SET_CYCLE_COUNT: {
							System.out.println("count:" + value);
							buildBuffer(ev.getSerialPort());
							progressIndicator.setProgress((double) value / cyclesSpinner.getValue());
							break;
						}
						case COMMAND_LOG_DEBUG: {
							debug((int) value);
							break;
						}
						case COMMAND_LOG_ERROR: {
							error((int) value);
							break;
						}
						case COMMAND_LOG_INFO: {
							info((int) value);
							break;
						}
						case COMMAND_LOG_OFF: {
							break;
						}
						case COMMAND_ECHO: {
							if (s.equals("HELLO_SNATCHFREEZER")) {
								System.out.println("CONNECTED");
							}

							echo(s);
							break;
						}
						case COMMAND_RESET: {
							// debug(s);
							break;
						}
						case COMMAND_DUMMY: {
							//System.out.println("dummy");
							break;
						}
						case COMMAND_FINISHED: {

							final ScheduledExecutorService executorService = Executors
									.newSingleThreadScheduledExecutor();
							executorService.schedule(new Runnable() {
								@Override
								public void run() {
									camera.setDisable(false);

									transferIndicator.setProgress(0f);
									progressIndicator.setProgress(0f);
									// actionButton.setStyle("-fx-base: #00ff00;");
									System.out.println("Finished");
								}
							}, 500, TimeUnit.MILLISECONDS);
							break;
						}
						case COMMAND_CANCEL: {
							System.out.println("Cancel");
							canceled = true;
							break;
						}
						case COMMAND_NEXT: {
							if (!canceled) {
								System.out.println("Next Serial Command");
								try {
									System.out.println("------------------------------------------------");
									sendNextCommand();
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
							break;
						}
						case COMMAND_REPEAT: {
							if (!canceled) {
								System.out.println("Repeat Serial Command");
								try {
									sendRepeatCommand();
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
							break;
						}
						}
						break;
					}
					}
				}
			}
		});
	}

	private void run() throws Exception {

		// actionButton.setStyle("-fx-base: #ff0000;");
		progressIndicator.setProgress(0f);

		if (buildShotConfiguration()) {
			camera.setDisable(true);
			phase = READ_DATA_PREFIX;
			byteBuffer = new byte[] {};
			readIndex = 0;
			canceled = false;
			sendNextCommand();
		} else {
			// Alert alert = new Alert(AlertType.WARNING);
			// alert.setTitle(messages.getString("message.warning"));
			// alert.setHeaderText(null);
			// alert.setContentText(messages.getString("message.no_actions"));
			// alert.showAndWait();
		}
	}

	private boolean sendNextCommand() throws Exception {
		if (dataIterator == null) {
			dataIterator = cb.getIterator();
		}
		boolean hasNext = dataIterator.hasNext();
		if (hasNext) {
			System.out.println("Send Command");
			currentCommandBuffer = dataIterator.next();
			int index = cb.indexOfCommand(currentCommandBuffer);
			double d = (double) index / cb.getCommandListSize();
			transferIndicator.setProgress(d);

			serialPort.writeBytes(currentCommandBuffer, currentCommandBuffer.length);
			serialPort.getOutputStream().flush();
		} else {
			System.out.println("No More Commands");
			dataIterator = null;
			transferIndicator.setProgress(1f);
		}
		return hasNext;
	}

	private void sendRepeatCommand() throws Exception {
		System.out.println("resend Command");
		serialPort.writeBytes(currentCommandBuffer, currentCommandBuffer.length);
		serialPort.getOutputStream().flush();

	}

	private void echo(String text) {
		System.out.println("ECHO: " + text);
	}

	private void debug(int id) {
		System.out.println("DEBUG: " + getMessage(id));
	}

	private void info(int id) {
		System.out.println("INFO: " + getMessage(id));
	}

	private void error(int id) {
		System.out.println("INFO: " + getMessage(id));
	}

	private String getMessage(int id) {
		String s = "";
		switch (id) {
		case MESSAGE_BYTE: {
			s = "Byte";
			break;
		}
		case MESSAGE_WORD: {
			s = "Word";
			break;
		}
		case MESSAGE_DWORD: {
			s = "DWord";
			break;
		}
		case MESSAGE_STRING_PARAMETER: {
			s = "String Parameter";
			break;
		}
		case MESSAGE_NUMBER_PARAMETER: {
			s = "Numeric Parameter";
			break;
		}
		case MESSAGE_NO_PARAMETER: {
			s = "No Parameter";
			break;
		}
		case MESSAGE_RUN: {
			s = "Run";
			break;
		}
		case MESSAGE_RESET: {
			s = "Reset";
			break;
		}
		case MESSAGE_CANCEL: {
			s = "Cancel";
			break;
		}
		case MESSAGE_ECHO: {
			s = "Echo";
			break;
		}
		case MESSAGE_SET_LOG_LEVEL: {
			s = "Set Log Level";
			break;
		}
		case MESSAGE_ADD_ACTION: {
			s = "Add Action";
			break;
		}
		case MESSAGE_SET_ACTION_PIN: {
			s = "Set Action Pin";
			break;
		}
		case MESSAGE_ADD_ACTION_TIMIMGS: {
			s = "Add Action Timings";
			break;
		}
		case MESSAGE_SET_ACTION_DELAY: {
			s = "Set Action Delay";
			break;
		}
		case MESSAGE_SET_ACTION_DELAY_INCREMENT: {
			s = "Set Action Delay Increment";
			break;
		}
		case MESSAGE_SET_ACTION_RELEASE: {
			s = "Set Action Release";
			break;
		}
		case MESSAGE_SET_ACTION_RELEASE_INCREMENT: {
			s = "Set Action Release Increment";
			break;
		}
		case MESSAGE_SET_CYCLE_COUNT: {
			s = "Set Cycle Count";
			break;
		}
		case MESSAGE_SET_CYCLE_DELAY: {
			s = "Set Cycle Delay";
			break;
		}
		case MESSAGE_SYNC1: {
			s = "Sync1";
			break;
		}
		case MESSAGE_SYNC2: {
			s = "Sync2";
			break;
		}
		case MESSAGE_READ_COMMAND: {
			s = "Read Command";
			break;
		}
		case MESSAGE_READ_LENGTH: {
			s = "Read Length";
			break;
		}
		case MESSAGE_READ_DATA: {
			s = "Read Data";
			break;
		}
		case MESSAGE_READ_CHECKSUM: {
			s = "Read Checksum";
			break;
		}
		case MESSAGE_CHECKSUM_ERROR: {
			s = "Checksum Error";
			break;
		}
		case MESSAGE_CHECKSUM_OK: {
			s = "Checksum OK";
			break;
		}
		case MESSAGE_MAX_ACTION_COUNT: {
			s = "Max Action Count";
			break;
		}
		}
		return s;
	}

	private byte[] buildBuffer(SerialPort serialPort) {
		lastAvailableBytes = availableBytes;
		availableBytes = serialPort.bytesAvailable();
		if (lastAvailableBytes != availableBytes) {
			byte newBytes[] = new byte[availableBytes];
			serialPort.readBytes(newBytes, availableBytes);
			byteBuffer = ArrayUtil.add(byteBuffer, newBytes);
		}
		return byteBuffer;
	}

}
