package de.drazil.snatchfreezer;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;

import de.drazil.snatchfreezer.model.ActionBean;
import de.drazil.snatchfreezer.model.ActionItemBean;
import de.drazil.snatchfreezer.model.ActionPinBean;
import de.drazil.snatchfreezer.model.ConfigurationBean;
import de.drazil.util.ArrayUtil;
import de.drazil.util.NumericConverter;
import de.drazil.util.TextfieldUtil;
import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;

public class SnatchFreezerController implements Initializable {

	private final static int TABLE_MAX_ROWS = 5;

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
	// 10|001010 = log_info
	// 10|001011 = log_debug
	// 10|001100 = log_error

	// llllllll = length

	// new protocol frame
	// SYNC1|SYNC2|COMMAND|PARAMETER_COUNT|PARAMETER_TYPE1|PARAMETER_LENGTH1|VALUE1|..|CHECKSUM

	private final static int PARAMETER_NO = 0b00000000;
	private final static int PARAMETER_STRING = 0b10000000;
	private final static int PARAMETER_NUMBER = 0b11000000;
	private final static int SYNCBYTE1 = 0xaa;
	private final static int SYNCBYTE2 = 0x55;
	private final static int COMMAND_SET_ACTION_PIN = 0b11000001;
	private final static int COMMAND_SET_ACTION_DELAY_TIME = 0b11000010;
	private final static int COMMAND_SET_ACTION_RELEASE_TIME = 0b11000011;
	private final static int COMMAND_SET_ACTION_DELAY_INCREMENT_TIME = 0b11000100;
	private final static int COMMAND_SET_ACTION_RELEASE_INCREMENT_TIME = 0b11000101;
	private final static int COMMAND_SET_CYCLE_COUNT = 0b11000110;
	private final static int COMMAND_SET_CYCLE_DELAY = 0b11000111;
	private final static int COMMAND_FLUSH = 0b11001001;
	private final static int COMMAND_TEST = 0b11001010;
	private final static int COMMAND_ADD_ACTION = 0b00000001;
	private final static int COMMAND_ADD_ACTION_TIMINGS = 0b00000010;
	private final static int COMMAND_NEXT = 0b00000011;
	private final static int COMMAND_REPEAT = 0b00000100;
	private final static int COMMAND_SET_LOG_LEVEL = 0b11001011;
	private final static int COMMAND_RUN = 0b00000110;
	private final static int COMMAND_CANCEL = 0b00000111;
	private final static int COMMAND_RESET = 0b00001000;
	private final static int COMMAND_FINISHED = 0b00001001;
	private final static int COMMAND_DUMMY = 0b00111111;
	private final static int COMMAND_ECHO = 0b10001001;
	private final static int COMMAND_LOG_INFO = 0b11001010;
	private final static int COMMAND_LOG_DEBUG = 0b11001011;
	private final static int COMMAND_LOG_ERROR = 0b11001100;
	private final static int COMMAND_LOG_OFF = 0b10001110;

	private final static int MESSAGE_BYTE = 1;
	private final static int MESSAGE_WORD = 2;
	private final static int MESSAGE_DWORD = 3;
	private final static int MESSAGE_STRING_PARAMETER = 4;
	private final static int MESSAGE_NUMBER_PARAMETER = 5;
	private final static int MESSAGE_NO_PARAMETER = 6;
	private final static int MESSAGE_RUN = 7;
	private final static int MESSAGE_RESET = 8;
	private final static int MESSAGE_CANCEL = 9;
	private final static int MESSAGE_ECHO = 10;
	private final static int MESSAGE_SET_LOG_LEVEL = 11;
	private final static int MESSAGE_ADD_ACTION = 12;
	private final static int MESSAGE_SET_ACTION_PIN = 13;
	private final static int MESSAGE_ADD_ACTION_TIMIMGS = 14;
	private final static int MESSAGE_SET_ACTION_DELAY = 15;
	private final static int MESSAGE_SET_ACTION_DELAY_INCREMENT = 16;
	private final static int MESSAGE_SET_ACTION_RELEASE = 17;
	private final static int MESSAGE_SET_ACTION_RELEASE_INCREMENT = 18;
	private final static int MESSAGE_SET_CYCLE_COUNT = 19;
	private final static int MESSAGE_SET_CYCLE_DELAY = 20;
	private final static int MESSAGE_SYNC1 = 21;
	private final static int MESSAGE_SYNC2 = 22;
	private final static int MESSAGE_READ_COMMAND = 23;
	private final static int MESSAGE_READ_LENGTH = 24;
	private final static int MESSAGE_READ_DATA = 25;
	private final static int MESSAGE_READ_CHECKSUM = 26;
	private final static int MESSAGE_CHECKSUM_ERROR = 27;
	private final static int MESSAGE_CHECKSUM_OK = 28;
	private final static int MESSAGE_MAX_ACTION_COUNT = 29;

	private final static int READ_DATA_PREFIX = 10;
	private final static int READ_COMMAND = 20;
	private final static int READ_LENGTH = 30;
	private final static int READ_DATA = 40;
	private final static int EXECUTE_COMMAND = 50;

	private final static int DEBUG = 1;
	private final static int INFO = 2;
	private final static int ERROR = 3;
	private final static int OFF = 99;

	// private final static int READ_DATA_NEXT = 50;
	// private final static int COMMAND_RESET = 0;
	// private final static int COMMAND_GET_NEXT_DATA_RECEIVED = 200;
	// private final static int COMMAND_CONFIGURE_RECEIVED = 300;
	// private final static int COMMAND_FINISHED_RECEIVED = 400;
	// private final static int COMMAND_DISPLAY_VALUE_RECEIVED = 500;
	// private final static int COMMAND_COUNTCYCLE_RECEIVED = 600;
	private int syncCount = 0;
	private boolean prefixSynced = false;
	private boolean suffixSynced = false;
	private int phase;
	private int serialCommand;
	private int value;
	private List<byte[]> commandList = null;
	private Iterator<byte[]> dataIterator;
	private int count;
	private int dataIndex;
	private byte dataBuffer[] = new byte[] { 0, 0, 0, 0 };
	private byte readDataBuffer[] = null;
	private File file = null;
	private int progressCount;
	private String actionButtonText = "Action";
	private int cycleCount = 0;
	private int dataLength = 0;
	private int currentCommand = 0;
	private boolean commandReady = false;
	private boolean isParsing = false;
	private int lastAvailableBytes = 0;
	private int availableBytes = 0;
	private byte byteBuffer[] = new byte[] {};
	// private byte sendBuffer[] = new byte[] {};
	private int readIndex = 0;
	private boolean canceled = false;
	private byte currentCommandBuffer[];
	private SpinnerValueFactory<Integer> maxCycles = null;

	@FXML
	private MenuBar menubar;
	@FXML
	private ChoiceBox<String> serialSelectChoiceBox;
	@FXML
	private Button actionButton;
	@FXML
	private Button loadButton;
	@FXML
	private Button saveButton;
	@FXML
	private Button saveAsButton;
	@FXML
	private TableView<ActionItemBean> valve1Grid;
	@FXML
	private TableView<ActionItemBean> valve2Grid;
	@FXML
	private TableView<ActionItemBean> valve3Grid;
	@FXML
	private TableView<ActionItemBean> valve4Grid;
	@FXML
	private Button valve1ClearButton;
	@FXML
	private Button valve2ClearButton;
	@FXML
	private Button valve3ClearButton;
	@FXML
	private Button valve4ClearButton;
	@FXML
	private Button valve1AddButton;
	@FXML
	private Button valve2AddButton;
	@FXML
	private Button valve3AddButton;
	@FXML
	private Button valve4AddButton;
	@FXML
	private Button valve1RemoveButton;
	@FXML
	private Button valve2RemoveButton;
	@FXML
	private Button valve3RemoveButton;
	@FXML
	private Button valve4RemoveButton;
	@FXML
	private ComboBox<ActionPinBean> action1Pin;
	@FXML
	private ComboBox<ActionPinBean> action2Pin;
	@FXML
	private ComboBox<ActionPinBean> action3Pin;
	@FXML
	private ComboBox<ActionPinBean> action4Pin;
	@FXML
	private TextField cameraTriggerDelayTextField;
	@FXML
	private TextField cameraTriggerReleaseTextField;
	@FXML
	private Spinner cyclesSpinner;
	@FXML
	private Button cameraTriggerTestButton;
	@FXML
	private TextField flash1TriggerDelayTextField;
	@FXML
	private TextField flash2TriggerDelayTextField;
	@FXML
	private Button flash1TriggerTestButton;
	@FXML
	private Button flash2TriggerTestButton;
	@FXML
	private TextArea note;
	@FXML
	private Label filename;
	@FXML
	private TextField cycleDelayTextField;
	@FXML
	private ProgressIndicator cycleIndicator;
	@FXML
	private ProgressIndicator transmissionIndicator;

	private SerialPort serialPort;
	private SerialPort serialPorts[];
	private String[] portNames;
	private ObservableList<ActionItemBean> valve1List;
	private ObservableList<ActionItemBean> valve2List;
	private ObservableList<ActionItemBean> valve3List;
	private ObservableList<ActionItemBean> valve4List;

	private String ttyDevice = null;

	public SnatchFreezerController() {
		super();
		// TODO Auto-generated constructor stub
	}

	/*
	 * private FilteredList<ActionPinBean> action1FiltertedList; private
	 * FilteredList<ActionPinBean> action2FiltertedList; private
	 * FilteredList<ActionPinBean> action3FiltertedList; private
	 * FilteredList<ActionPinBean> action4FiltertedList;
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		initialzeSerialPortSelector();
		initializeSerialConnection();
		initializeUi();
	}

	private void buildData() {
		commandList = new ArrayList<byte[]>();

		addSetLogLevel(INFO);
		addEcho("Hello Snatchfreezer");
		addReset();
		addAction(2);
		addActionTimings(2000, 2000, 0, 0);

		/*
		 * addAction(3); addActionTimings(500, 500, 0, 0); addActionTimings(500, 500, 0,
		 * 0);
		 * 
		 * addAction(4); addActionTimings(400, 600, 0, 0);
		 * 
		 * addAction(5); addActionTimings(600, 400, 0, 0);
		 * 
		 * addAction(6); addActionTimings(800, 200, 0, 0); addActionTimings(100, 50, 0,
		 * 0);
		 * 
		 * addAction(7); addActionTimings(1000, 1000, 0, 0);
		 */
		addCycleCount(maxCycles.getValue());
		addCycleDelay(new Long(cycleDelayTextField.getText()));
		addRun();

	}

	private void addEcho(String text) {
		byte[] ba = NumericConverter.toByteArray(COMMAND_ECHO, 1);
		ba = ArrayUtil.add(ba, NumericConverter.toByteArray((byte) text.length(), 1));
		ba = ArrayUtil.add(ba, ArrayUtil.getByteArrayFromString(text));
		commandList.add(getCommand(ba));
	}

	private void addRun() {
		commandList.add(getCommand(NumericConverter.toByteArray(COMMAND_RUN, 1)));
	}

	private void sendCancel() {
		commandList.add(getCommand(NumericConverter.toByteArray(COMMAND_CANCEL, 1)));
	}

	private void addReset() {
		commandList.add(getCommand(NumericConverter.toByteArray(COMMAND_RESET, 1)));
	}

	private void addSetLogLevel(int logLevel) {
		byte[] ba = NumericConverter.toByteArray(COMMAND_SET_LOG_LEVEL, 1);
		ba = ArrayUtil.add(ba, new byte[] { 1 });
		ba = ArrayUtil.add(ba, NumericConverter.toByteArray(logLevel, 1));
		commandList.add(getCommand(ba));
	}

	public void addAction(int pin) {
		commandList.add(getCommand(NumericConverter.toByteArray(COMMAND_ADD_ACTION, 1)));
		byte[] ba = NumericConverter.toByteArray(COMMAND_SET_ACTION_PIN, 1);
		ba = ArrayUtil.add(ba, NumericConverter.toByteArray((byte) 1));
		ba = ArrayUtil.add(ba, NumericConverter.toByteArray(pin, 1));
		commandList.add(getCommand(ba));
	}

	public void addActionTimings(long startDelay, long releaseDelay, long delayIncrement, long releaseIncrement) {
		commandList.add(getCommand(NumericConverter.toByteArray(COMMAND_ADD_ACTION_TIMINGS, 1)));
		byte ba[] = NumericConverter.toByteArray(COMMAND_SET_ACTION_DELAY_TIME, 1);
		ba = ArrayUtil.add(ba, NumericConverter.toByteArray((byte) 4));
		ba = ArrayUtil.add(ba, NumericConverter.toByteArray(startDelay, 4));
		commandList.add(getCommand(ba));
		ba = NumericConverter.toByteArray(COMMAND_SET_ACTION_RELEASE_TIME, 1);
		ba = ArrayUtil.add(ba, NumericConverter.toByteArray((byte) 4));
		ba = ArrayUtil.add(ba, NumericConverter.toByteArray(releaseDelay, 4));
		commandList.add(getCommand(ba));
		ba = NumericConverter.toByteArray(COMMAND_SET_ACTION_DELAY_INCREMENT_TIME, 1);
		ba = ArrayUtil.add(ba, NumericConverter.toByteArray((byte) 4));
		ba = ArrayUtil.add(ba, NumericConverter.toByteArray(delayIncrement, 4));
		commandList.add(getCommand(ba));
		ba = NumericConverter.toByteArray(COMMAND_SET_ACTION_RELEASE_INCREMENT_TIME, 1);
		ba = ArrayUtil.add(ba, NumericConverter.toByteArray((byte) 4));
		ba = ArrayUtil.add(ba, NumericConverter.toByteArray(releaseIncrement, 4));
		commandList.add(getCommand(ba));

	}

	public void addCycleCount(int cycleCount) {
		byte[] ba = NumericConverter.toByteArray(COMMAND_SET_CYCLE_COUNT, 1);
		ba = ArrayUtil.add(ba, NumericConverter.toByteArray((byte) 1));
		ba = ArrayUtil.add(ba, NumericConverter.toByteArray(cycleCount, 1));
		commandList.add(getCommand(ba));
	}

	public void addCycleDelay(long cycleDelay) {
		byte[] ba = NumericConverter.toByteArray(COMMAND_SET_CYCLE_DELAY, 1);
		ba = ArrayUtil.add(ba, NumericConverter.toByteArray((byte) 4));
		ba = ArrayUtil.add(ba, NumericConverter.toByteArray(cycleDelay, 4));
		commandList.add(getCommand(ba));
	}

	/*
	 * private void buildData() { int size = 0; dataList = new ArrayList<>();
	 * dataList
	 * .add(getDataBlock(NumericConverter.toByteArray(TextfieldUtil.getLongValue
	 * (cyclesTextField), 4)));
	 * dataList.add(getDataBlock(NumericConverter.toByteArray
	 * (TextfieldUtil.getLongValue(cycleDelayTextField), 4)));
	 * dataList.add(getDataBlock
	 * (NumericConverter.toByteArray(TextfieldUtil.getLongValue
	 * (cameraTriggerDelayTextField), 4)));
	 * dataList.add(getDataBlock(NumericConverter
	 * .toByteArray(TextfieldUtil.getLongValue(cameraTriggerReleaseTextField), 4)));
	 * dataList.add(getDataBlock(NumericConverter.toByteArray(TextfieldUtil
	 * .getLongValue(flash1TriggerDelayTextField), 4))); dataList.add(getDataBlock
	 * (NumericConverter.toByteArray(TextfieldUtil.getLongValue
	 * (flash2TriggerDelayTextField), 4)));
	 * 
	 * // valve1 dataList.add(getDataBlock(NumericConverter.toByteArray(action1Pin
	 * .getValue().getPin(), 1))); size = valve1List.size();
	 * dataList.add(getDataBlock(NumericConverter.toByteArray(size, 1))); for (int i
	 * = 0; i < size; i++) { ActionItemBean bean = valve1List.get(i);
	 * 
	 * dataList.add(getDataBlock(NumericConverter.toByteArray(bean.getDelay(), 4)));
	 * dataList.add(getDataBlock(NumericConverter.toByteArray(bean.getRelease(),
	 * 4)));
	 * dataList.add(getDataBlock(NumericConverter.toByteArray(bean.getRepeats(),
	 * 1))); }
	 * 
	 * // filler for (int i = 0; i < TABLE_MAX_ROWS - size; i++) {
	 * dataList.add(getDataBlock(NumericConverter.toByteArray(0, 4)));
	 * dataList.add(getDataBlock(NumericConverter.toByteArray(0, 4)));
	 * dataList.add(getDataBlock(NumericConverter.toByteArray(0, 1))); }
	 * 
	 * // valve2 dataList.add(getDataBlock(NumericConverter.toByteArray(action2Pin
	 * .getValue().getPin(), 1))); size = valve2List.size();
	 * dataList.add(getDataBlock(NumericConverter.toByteArray(size, 1))); for (int i
	 * = 0; i < size; i++) { ActionItemBean bean = valve2List.get(i);
	 * 
	 * dataList.add(getDataBlock(NumericConverter.toByteArray(bean.getDelay(), 4)));
	 * dataList.add(getDataBlock(NumericConverter.toByteArray(bean.getRelease(),
	 * 4)));
	 * dataList.add(getDataBlock(NumericConverter.toByteArray(bean.getRepeats(),
	 * 1))); } // filler for (int i = 0; i < TABLE_MAX_ROWS - size; i++) {
	 * dataList.add(getDataBlock(NumericConverter.toByteArray(0, 4)));
	 * dataList.add(getDataBlock(NumericConverter.toByteArray(0, 4)));
	 * dataList.add(getDataBlock(NumericConverter.toByteArray(0, 1))); } // valve3
	 * dataList.add(getDataBlock(NumericConverter.toByteArray(action3Pin. getValue
	 * ().getPin(), 1))); size = valve3List.size();
	 * dataList.add(getDataBlock(NumericConverter.toByteArray(size, 1))); for (int i
	 * = 0; i < size; i++) { ActionItemBean bean = valve3List.get(i);
	 * 
	 * dataList.add(getDataBlock(NumericConverter.toByteArray(bean.getDelay(), 4)));
	 * dataList.add(getDataBlock(NumericConverter.toByteArray(bean.getRelease(),
	 * 4)));
	 * dataList.add(getDataBlock(NumericConverter.toByteArray(bean.getRepeats(),
	 * 1))); } // filler for (int i = 0; i < TABLE_MAX_ROWS - size; i++) {
	 * dataList.add(getDataBlock(NumericConverter.toByteArray(0, 4)));
	 * dataList.add(getDataBlock(NumericConverter.toByteArray(0, 4)));
	 * dataList.add(getDataBlock(NumericConverter.toByteArray(0, 1))); } // valve4
	 * dataList.add(getDataBlock(NumericConverter.toByteArray(action4Pin. getValue
	 * ().getPin(), 1))); size = valve4List.size();
	 * dataList.add(getDataBlock(NumericConverter.toByteArray(size, 1))); for (int i
	 * = 0; i < size; i++) { ActionItemBean bean = valve4List.get(i);
	 * 
	 * dataList.add(getDataBlock(NumericConverter.toByteArray(bean.getDelay(), 4)));
	 * dataList.add(getDataBlock(NumericConverter.toByteArray(bean.getRelease(),
	 * 4)));
	 * dataList.add(getDataBlock(NumericConverter.toByteArray(bean.getRepeats(),
	 * 1))); } // filler for (int i = 0; i < TABLE_MAX_ROWS - size; i++) {
	 * dataList.add(getDataBlock(NumericConverter.toByteArray(0, 4)));
	 * dataList.add(getDataBlock(NumericConverter.toByteArray(0, 4)));
	 * dataList.add(getDataBlock(NumericConverter.toByteArray(0, 1))); }
	 * 
	 * dataList.add(getFinalBlock()); }
	 */
	private void setConfiguration(ConfigurationBean configurationBean) {
		valve1List = valve1Grid.getItems();
		valve2List = valve2Grid.getItems();
		valve3List = valve3Grid.getItems();
		valve4List = valve4Grid.getItems();

		valve1List.clear();
		valve2List.clear();
		valve3List.clear();
		valve4List.clear();

		valve1List.addAll(configurationBean.getValve1ActionBean().getActionItemList());
		valve2List.addAll(configurationBean.getValve2ActionBean().getActionItemList());
		valve3List.addAll(configurationBean.getValve3ActionBean().getActionItemList());
		valve4List.addAll(configurationBean.getValve4ActionBean().getActionItemList());

		action1Pin.setValue(action1Pin.getItems().get(configurationBean.getValve1ActionBean().getPinIndex()));
		action2Pin.setValue(action2Pin.getItems().get(configurationBean.getValve2ActionBean().getPinIndex()));
		action3Pin.setValue(action3Pin.getItems().get(configurationBean.getValve3ActionBean().getPinIndex()));
		action4Pin.setValue(action4Pin.getItems().get(configurationBean.getValve4ActionBean().getPinIndex()));

		cameraTriggerDelayTextField
				.setText(String.valueOf(configurationBean.getCameraActionBean().getActionItemList().get(0).getDelay()));
		cameraTriggerReleaseTextField.setText(
				String.valueOf(configurationBean.getCameraActionBean().getActionItemList().get(0).getRelease()));

		flash1TriggerDelayTextField
				.setText(String.valueOf(configurationBean.getFlash1ActionBean().getActionItemList().get(0).getDelay()));
		flash2TriggerDelayTextField
				.setText(String.valueOf(configurationBean.getFlash2ActionBean().getActionItemList().get(0).getDelay()));

		cyclesSpinner.getValueFactory().setValue(configurationBean.getCycles());
		// cyclesTextField.setText(String.valueOf(configurationBean.getCycles()));
		cycleDelayTextField.setText(String.valueOf(configurationBean.getCycleDelay()));

		note.setText(configurationBean.getNote());

	}

	private ConfigurationBean getConfiguration() {
		ConfigurationBean configurationBean = new ConfigurationBean();
		configurationBean.setNote(note.getText());

		configurationBean.setCycles(maxCycles.getValue());
		configurationBean.setCycleDelay(TextfieldUtil.getLongValue(cycleDelayTextField));

		ActionBean cameraActionBean = new ActionBean();
		cameraActionBean.setPinIndex(0);
		List<ActionItemBean> cameraActionItemBeanList = new ArrayList<>();
		cameraActionItemBeanList.add(new ActionItemBean(TextfieldUtil.getLongValue(cameraTriggerDelayTextField),
				TextfieldUtil.getLongValue(cameraTriggerReleaseTextField), 0, 0));
		cameraActionBean.setActionItemList(cameraActionItemBeanList);

		ActionBean flash1TriggerActionBean = new ActionBean();
		flash1TriggerActionBean.setPinIndex(0);
		List<ActionItemBean> flash1TriggerItemBeanList = new ArrayList<>();
		flash1TriggerItemBeanList
				.add(new ActionItemBean(TextfieldUtil.getLongValue(flash1TriggerDelayTextField), 10, 0, 0));
		flash1TriggerActionBean.setActionItemList(flash1TriggerItemBeanList);

		ActionBean flash2TriggerActionBean = new ActionBean();
		flash2TriggerActionBean.setPinIndex(0);
		List<ActionItemBean> flash2TriggerItemBeanList = new ArrayList<>();
		flash2TriggerItemBeanList
				.add(new ActionItemBean(TextfieldUtil.getLongValue(flash2TriggerDelayTextField), 10, 0, 0));
		flash2TriggerActionBean.setActionItemList(flash2TriggerItemBeanList);

		ActionBean valve1ActionBean = new ActionBean();
		valve1ActionBean.setPinIndex(action1Pin.getSelectionModel().getSelectedIndex());
		valve1ActionBean.setDescription("Test");
		valve1ActionBean.setActionItemList(valve1List);

		ActionBean valve2ActionBean = new ActionBean();
		valve2ActionBean.setPinIndex(action2Pin.getSelectionModel().getSelectedIndex());
		valve2ActionBean.setDescription("Test");
		valve2ActionBean.setActionItemList(valve2List);

		ActionBean valve3ActionBean = new ActionBean();
		valve3ActionBean.setPinIndex(action3Pin.getSelectionModel().getSelectedIndex());
		valve3ActionBean.setDescription("Test");
		valve3ActionBean.setActionItemList(valve3List);

		ActionBean valve4ActionBean = new ActionBean();
		valve4ActionBean.setPinIndex(action4Pin.getSelectionModel().getSelectedIndex());
		valve4ActionBean.setDescription("Test");
		valve4ActionBean.setActionItemList(valve4List);

		configurationBean.setCameraActionBean(cameraActionBean);
		configurationBean.setFlash1ActionBean(flash1TriggerActionBean);
		configurationBean.setFlash2ActionBean(flash2TriggerActionBean);
		configurationBean.setValve1ActionBean(valve1ActionBean);
		configurationBean.setValve2ActionBean(valve2ActionBean);
		configurationBean.setValve3ActionBean(valve3ActionBean);
		configurationBean.setValve4ActionBean(valve4ActionBean);
		return configurationBean;
	}

	private void initializeUi() {

		maxCycles = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, 1);
		cyclesSpinner.setValueFactory(maxCycles);
		menubar.setUseSystemMenuBar(true);

		cycleDelayTextField.setPromptText("cycleDelayTime");
		cycleDelayTextField.setText("1000");

		ActionPinBean action1Bean = new ActionPinBean(100, false, "N/A");
		ActionPinBean action2Bean = new ActionPinBean(12, false, "OUTPUT 1");
		ActionPinBean action3Bean = new ActionPinBean(5, false, "OUTPUT 2");
		ActionPinBean action4Bean = new ActionPinBean(5, false, "OUTPUT 3");
		ActionPinBean action5Bean = new ActionPinBean(5, false, "OUTPUT 4");
		ActionPinBean action6Bean = new ActionPinBean(5, false, "OUTPUT 5");
		ActionPinBean action7Bean = new ActionPinBean(5, false, "OUTPUT 6");

		action1Pin.getItems().addAll(action1Bean, action2Bean, action3Bean, action4Bean, action5Bean, action6Bean,
				action7Bean);
		action2Pin.getItems().addAll(action1Bean, action2Bean, action3Bean, action4Bean, action5Bean, action6Bean,
				action7Bean);
		action3Pin.getItems().addAll(action1Bean, action2Bean, action3Bean, action4Bean, action5Bean, action6Bean,
				action7Bean);
		action4Pin.getItems().addAll(action1Bean, action2Bean, action3Bean, action4Bean, action5Bean, action6Bean,
				action7Bean);

		/*
		 * action1FiltertedList = new FilteredList<>(action1Pin.getItems());
		 * action2FiltertedList = new FilteredList<>(action2Pin.getItems());
		 * action3FiltertedList = new FilteredList<>(action3Pin.getItems());
		 * action4FiltertedList = new FilteredList<>(action4Pin.getItems());
		 * 
		 * action1Pin.setItems(action1FiltertedList);
		 * action2Pin.setItems(action2FiltertedList);
		 * action3Pin.setItems(action3FiltertedList);
		 * action4Pin.setItems(action4FiltertedList);
		 */
		action1Pin.setValue(action1Bean);
		action2Pin.setValue(action1Bean);
		action3Pin.setValue(action1Bean);
		action4Pin.setValue(action1Bean);

		valve1List = valve1Grid.getItems();
		valve2List = valve2Grid.getItems();
		valve3List = valve3Grid.getItems();
		valve4List = valve4Grid.getItems();

		loadButton.setOnAction(new LoadHandler());
		saveButton.setOnAction(new SaveHandler());
		saveAsButton.setOnAction(new SaveAsHandler());

		valve1List.addListener(new ListChangeListener<ActionItemBean>() {
			@Override
			public void onChanged(javafx.collections.ListChangeListener.Change<? extends ActionItemBean> ev) {
				valve1RemoveButton.setDisable(valve1List.size() == 0);
			}
		});

		valve2List.addListener(new ListChangeListener<ActionItemBean>() {
			@Override
			public void onChanged(javafx.collections.ListChangeListener.Change<? extends ActionItemBean> ev) {
				valve2RemoveButton.setDisable(valve2List.size() == 0);
			}
		});

		valve3List.addListener(new ListChangeListener<ActionItemBean>() {
			@Override
			public void onChanged(javafx.collections.ListChangeListener.Change<? extends ActionItemBean> ev) {
				valve3RemoveButton.setDisable(valve3List.size() == 0);
			}
		});

		valve4List.addListener(new ListChangeListener<ActionItemBean>() {
			@Override
			public void onChanged(javafx.collections.ListChangeListener.Change<? extends ActionItemBean> ev) {
				valve4RemoveButton.setDisable(valve4List.size() == 0);
			}
		});

		valve1AddButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent ev) {
				if (valve1List.size() < TABLE_MAX_ROWS) {
					valve1List.add(new ActionItemBean(0, 0, 0, 0));
				}
			}
		});

		valve1RemoveButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent ev) {
				if (valve1Grid.getSelectionModel().getSelectedIndex() != -1) {
					valve1List.remove(valve1Grid.getSelectionModel().getSelectedIndex());
				}
			}
		});

		valve2AddButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent ev) {
				if (valve2List.size() < TABLE_MAX_ROWS) {
					valve2List.add(new ActionItemBean(0, 0, 0, 0));
				}
			}
		});

		valve2RemoveButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent ev) {
				if (valve2Grid.getSelectionModel().getSelectedIndex() != -1) {
					valve2List.remove(valve2Grid.getSelectionModel().getSelectedIndex());
				}
			}
		});

		valve3AddButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent ev) {
				if (valve3List.size() < TABLE_MAX_ROWS) {
					valve3List.add(new ActionItemBean(0, 0, 0, 0));
				}
			}
		});

		valve3RemoveButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent ev) {
				if (valve3Grid.getSelectionModel().getSelectedIndex() != -1) {
					valve3List.remove(valve3Grid.getSelectionModel().getSelectedIndex());
				}
			}
		});

		valve4AddButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent ev) {
				if (valve4List.size() < TABLE_MAX_ROWS) {
					valve4List.add(new ActionItemBean(0, 0, 0, 0));
				}
			}
		});

		valve4RemoveButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent ev) {
				if (valve4Grid.getSelectionModel().getSelectedIndex() != -1) {
					valve4List.remove(valve4Grid.getSelectionModel().getSelectedIndex());
				}
			}
		});

		actionButton.setOnAction(new RunHandler());

		valve1Grid.setEditable(true);
		valve2Grid.setEditable(true);
		valve3Grid.setEditable(true);
		valve4Grid.setEditable(true);

		valve1Grid.getColumns().addAll(createGridColumns());
		valve2Grid.getColumns().addAll(createGridColumns());
		valve3Grid.getColumns().addAll(createGridColumns());
		valve4Grid.getColumns().addAll(createGridColumns());

	}

	private void initialzeSerialPortSelector() {
		serialPorts = SerialPort.getCommPorts();
		// serialPort = SerialPort.getCommPorts()[2];

		for (int i = 0; i < serialPorts.length; i++) {
			serialSelectChoiceBox.getItems().add(serialPorts[i].getSystemPortName());
		}

		if (serialPorts.length == 1) {
			ttyDevice = serialPorts[0].getSystemPortName();
			serialSelectChoiceBox.setValue(ttyDevice);
			serialPort = serialPorts[0];
		}

	}

	private void initializeSerialConnection() {
		serialPort.setBaudRate(57600);// 57600
		serialPort.openPort();
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
							cycleIndicator.setProgress((double) value / maxCycles.getValue());
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
							echo(s);
							break;
						}
						case COMMAND_RESET: {
							// debug(s);
							break;
						}
						case COMMAND_DUMMY: {
							// System.out.println("dummy");
							break;
						}
						case COMMAND_FINISHED: {

							final ScheduledExecutorService executorService = Executors
									.newSingleThreadScheduledExecutor();
							executorService.schedule(new Runnable() {
								@Override
								public void run() {
									setActionButtonDisabled(false);
									transmissionIndicator.setProgress(0f);
									cycleIndicator.setProgress(0f);
									actionButton.setStyle("-fx-base: #00ff00;");
									System.out.println("Finished");
								}
							}, 1, TimeUnit.SECONDS);
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
									// TODO Auto-generated catch block
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
									// TODO Auto-generated catch block
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

	private void clearSerialBuffer(SerialPort serialPort) {

		int bc = serialPort.bytesAvailable();
		byte b[] = new byte[bc];
		serialPort.readBytes(b, bc);
		byteBuffer = new byte[] {};

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

	private List<TableColumn<ActionItemBean, ?>> createGridColumns() {

		TableColumn<ActionItemBean, Long> delayColumn = new TableColumn<>("Delay");
		delayColumn.setCellValueFactory(new PropertyValueFactory<>("delay"));
		// delayColumn.setCellFactory(TextFieldTableCell.<ActionItemBean, Long>
		// forTableColumn(new LongStringConverter()));
		delayColumn.setCellFactory(new TextFieldCellFactory2());
		delayColumn.setMinWidth(90);
		delayColumn.setSortable(false);
		delayColumn.setEditable(true);
		delayColumn.onEditCommitProperty();
		delayColumn.setOnEditCommit(new EventHandler<CellEditEvent<ActionItemBean, Long>>() {
			@Override
			public void handle(CellEditEvent<ActionItemBean, Long> t) {
				((ActionItemBean) t.getTableView().getItems().get(t.getTablePosition().getRow()))
						.setDelay(t.getNewValue());
			}
		});

		TableColumn<ActionItemBean, Long> releaseColumn = new TableColumn<>("Release");
		releaseColumn.setCellValueFactory(new PropertyValueFactory<>("release"));
		// releaseColumn.setCellFactory(TextFieldTableCell.<ActionItemBean,
		// Long> forTableColumn(new LongStringConverter()));
		releaseColumn.setCellFactory(new TextFieldCellFactory2());
		releaseColumn.setMinWidth(90);
		releaseColumn.setSortable(false);
		releaseColumn.setEditable(true);
		releaseColumn.onEditCommitProperty();
		releaseColumn.setOnEditCommit(new EventHandler<CellEditEvent<ActionItemBean, Long>>() {
			@Override
			public void handle(CellEditEvent<ActionItemBean, Long> t) {
				((ActionItemBean) t.getTableView().getItems().get(t.getTablePosition().getRow()))
						.setRelease(t.getNewValue());
			}
		});

		TableColumn<ActionItemBean, Long> delayIncrementColumn = new TableColumn<>("DelayIncrement");
		delayIncrementColumn.setCellValueFactory(new PropertyValueFactory<>("delayIncrement"));
		// delayIncrementColumn.setCellFactory(TextFieldTableCell.<ActionItemBean,
		// Long> forTableColumn(new LongStringConverter()));
		delayIncrementColumn.setCellFactory(new TextFieldCellFactory2());
		delayIncrementColumn.setMinWidth(90);
		delayIncrementColumn.setSortable(false);
		delayIncrementColumn.setEditable(true);
		delayIncrementColumn.onEditCommitProperty();
		delayIncrementColumn.setOnEditCommit(new EventHandler<CellEditEvent<ActionItemBean, Long>>() {
			@Override
			public void handle(CellEditEvent<ActionItemBean, Long> t) {
				((ActionItemBean) t.getTableView().getItems().get(t.getTablePosition().getRow()))
						.setRelease(t.getNewValue());
			}
		});

		TableColumn<ActionItemBean, Long> releaseIncrementColumn = new TableColumn<>("ReleaseIncrement");
		releaseIncrementColumn.setCellValueFactory(new PropertyValueFactory<>("releaseIncrement"));
		// releaseIncrementColumn.setCellFactory(TextFieldTableCell.<ActionItemBean,
		// Long> forTableColumn(new LongStringConverter()));
		releaseIncrementColumn.setCellFactory(new TextFieldCellFactory2());
		releaseIncrementColumn.setMinWidth(90);
		releaseIncrementColumn.setSortable(false);
		releaseIncrementColumn.setEditable(true);
		releaseIncrementColumn.onEditCommitProperty();
		releaseIncrementColumn.setOnEditCommit(new EventHandler<CellEditEvent<ActionItemBean, Long>>() {
			@Override
			public void handle(CellEditEvent<ActionItemBean, Long> t) {
				((ActionItemBean) t.getTableView().getItems().get(t.getTablePosition().getRow()))
						.setRelease(t.getNewValue());
			}
		});

		List<TableColumn<ActionItemBean, ?>> list = new ArrayList<>();
		list.add(delayColumn);
		list.add(delayIncrementColumn);
		list.add(releaseColumn);
		list.add(releaseIncrementColumn);
		return list;
	}

	public void run() throws Exception {
		// initializeSerialConnection();
		setActionButtonDisabled(true);
		actionButton.setStyle("-fx-base: #ff0000;");
		cycleIndicator.setProgress(0f);
		phase = READ_DATA_PREFIX;
		buildData();
		byteBuffer = new byte[] {};
		readIndex = 0;
		canceled = false;
		sendNextCommand();
	}

	private boolean sendNextCommand() throws Exception {
		if (dataIterator == null) {
			dataIterator = commandList.iterator();
		}
		boolean hasNext = dataIterator.hasNext();
		if (hasNext) {
			System.out.println("Send Command");
			currentCommandBuffer = dataIterator.next();
			int index = commandList.indexOf(currentCommandBuffer);

			double d = (double) index / commandList.size();

			transmissionIndicator.setProgress(d);

			serialPort.writeBytes(currentCommandBuffer, currentCommandBuffer.length);
			serialPort.getOutputStream().flush();
		} else {
			System.out.println("No More Commands");
			dataIterator = null;
			transmissionIndicator.setProgress(1f);
		}
		return hasNext;
	}

	private void sendRepeatCommand() throws Exception {
		System.out.println("resend Command");
		serialPort.writeBytes(currentCommandBuffer, currentCommandBuffer.length);
		serialPort.getOutputStream().flush();

	}

	public byte[] getCommand(byte byteArray[]) {
		byte[] ba = ArrayUtil.add(new byte[] { (byte) 0xaa, (byte) 0x55 }, byteArray);
		ba = ArrayUtil.add(ba, new byte[] { ArrayUtil.getChecksum(ba) });
		return ba;
	}

	/*
	 * public byte[] getFinalBlock() { return new byte[] { (byte) 0xaa, (byte) 0x55,
	 * (byte) 0b01110000 }; }
	 */
	private long getValue(byte buffer[]) {
		long value = NumericConverter.toLong(buffer);
		dataBuffer = new byte[] { 0, 0, 0, 0 };
		return value;
	}

	private void saveConfiguration(File file) {

		if (file != null) {
			ObjectMapper mapper = new ObjectMapper();
			try {
				mapper.writeValue(file, getConfiguration());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void loadConfiguration(File file) {
		if (file != null) {
			ObjectMapper mapper = new ObjectMapper();
			try {
				setConfiguration(mapper.readValue(file, ConfigurationBean.class));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public class SaveHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent event) {
			if (file == null) {
				FileChooser fc = new FileChooser();
				file = fc.showSaveDialog(null);
			}
			saveConfiguration(file);
		}
	}

	public class SaveAsHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent event) {
			FileChooser fc = new FileChooser();
			file = fc.showSaveDialog(null);
			if (file != null) {
				filename.setText(file.getAbsolutePath());
			}
			saveConfiguration(file);

		}
	}

	public class LoadHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent event) {
			FileChooser fc = new FileChooser();
			file = fc.showOpenDialog(null);
			if (file != null) {
				filename.setText(file.getAbsolutePath());
			}
			loadConfiguration(file);
		}
	}

	public class RunHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent arg0) {
			try {
				run();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	public void setActionButtonDisabled(boolean disable) {

		// Platform.runLater(() -> {
		actionButton.setDisable(disable);

		// });
	}

	public void setButtonText(Button button, String text) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				button.setText(text);
			}
		});
	}

}
