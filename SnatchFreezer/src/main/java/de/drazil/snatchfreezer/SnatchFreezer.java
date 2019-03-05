package de.drazil.snatchfreezer;

import de.drazil.snatchfreezer.model.ObservableActionItemBean;
import javafx.application.Application;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class SnatchFreezer extends Application {

	private VBox actionBox = null;

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

		MenuBar menubar = new MenuBar();
		menubar.getMenus().add(new Menu("Application"));
		menubar.getMenus().add(new Menu("About"));
		actionBox = new VBox();
		VBox actionButtonPane = new VBox();
		actionButtonPane.setMinWidth(100);
		actionButtonPane.setPrefWidth(100);
		actionButtonPane.setMaxWidth(100);
		Button file = new Button("\uf15b");
		file.setTooltip(new Tooltip("Open file"));
		file.getStyleClass().add("greyButton");
		file.getStyleClass().add("bigButtonIcon");
		Button open = new Button("\uf07c");
		open.setTooltip(new Tooltip("Open file"));
		open.getStyleClass().add("greyButton");
		open.getStyleClass().add("bigButtonIcon");
		Button save = new Button("\uf56e");
		save.setTooltip(new Tooltip("Open file"));
		save.getStyleClass().add("greyButton");
		save.getStyleClass().add("bigButtonIcon");
		Button camera = new Button("\uf083");
		camera.setTooltip(new Tooltip("Add one maximum two camera triggers to list"));
		camera.getStyleClass().add("orangeButton");
		camera.getStyleClass().add("bigButtonIcon");
		Button bulb = new Button("\uf0eb");
		bulb.getStyleClass().add("yellowButton");
		bulb.getStyleClass().add("bigButtonIcon");
		bulb.setTooltip(new Tooltip("Add one maximum two flash triggers to list"));
		Button tint = new Button("\uf043");
		tint.getStyleClass().add("blueButton");
		tint.getStyleClass().add("bigButtonIcon");
		tint.setTooltip(new Tooltip("Add one maximum six valve triggers to list"));
		tint.setOnAction(value -> {
			actionBox.getChildren().add(createAction());
		});
		Button go = new Button("\uf70c");
		go.getStyleClass().add("greenButton");
		go.getStyleClass().add("bigButtonIcon");
		go.setTooltip(new Tooltip("Start shooting \uf4da"));
		Button empty = new Button("\uf576");
		empty.getStyleClass().add("blueButton");
		empty.getStyleClass().add("bigButtonIcon");
		empty.setTooltip(new Tooltip("Empty siphon"));
		

		file.prefWidthProperty().bind(actionButtonPane.widthProperty());
		open.prefWidthProperty().bind(actionButtonPane.widthProperty());
		save.prefWidthProperty().bind(actionButtonPane.widthProperty());
		camera.prefWidthProperty().bind(actionButtonPane.widthProperty());
		bulb.prefWidthProperty().bind(actionButtonPane.widthProperty());
		tint.prefWidthProperty().bind(actionButtonPane.widthProperty());
		empty.prefWidthProperty().bind(actionButtonPane.widthProperty());
		go.prefWidthProperty().bind(actionButtonPane.widthProperty());

		Region spacer = new Region();
		VBox.setVgrow(spacer, Priority.ALWAYS);
		spacer.setMinWidth(Region.USE_PREF_SIZE);

		actionButtonPane.getChildren().add(file);
		actionButtonPane.getChildren().add(open);
		actionButtonPane.getChildren().add(save);
		actionButtonPane.getChildren().add(new Separator());
		actionButtonPane.getChildren().add(camera);
		actionButtonPane.getChildren().add(bulb);
		actionButtonPane.getChildren().add(tint);
		actionButtonPane.getChildren().add(new Separator());
		actionButtonPane.getChildren().add(spacer);
		actionButtonPane.getChildren().add(new Separator());
		actionButtonPane.getChildren().add(empty);
		actionButtonPane.getChildren().add(go);

		ScrollPane actionScrollPane = new ScrollPane(actionBox);

		HBox actionSplitPane = new HBox();
		actionSplitPane.getChildren().addAll(actionScrollPane, actionButtonPane);

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
		actionScrollPane.prefWidthProperty().bind(root.widthProperty());

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

		Button addButton = new Button("\uf055");
		addButton.getStyleClass().add("darkButton");

		Button removeButton = new Button("\uf056");
		removeButton.getStyleClass().add("darkButton");

		toolbar.getItems().add(addButton);
		toolbar.getItems().add(removeButton);
		toolbar.getItems().add(new TextField());
		toolbar.getItems().add(new ComboBox<String>());
		toolbar.getItems().add(spacer);
		Button closeButton = new Button("\uf410");
		closeButton.setOnAction(value -> {
			actionBox.getChildren().remove(vbox);
		});
		closeButton.getStyleClass().add("darkButton");

		toolbar.getItems().add(closeButton);
		vbox.getChildren().add(toolbar);

		TableView<ObservableActionItemBean> table = new TableView<ObservableActionItemBean>();

		TableColumn<ObservableActionItemBean, Long> delay = new TableColumn<ObservableActionItemBean, Long>("Delay");

		TableColumn<ObservableActionItemBean, Long> release = new TableColumn<ObservableActionItemBean, Long>(
				"Release");
		TableColumn<ObservableActionItemBean, Long> delayIncrement = new TableColumn<ObservableActionItemBean, Long>(
				"DelayInc");
		TableColumn<ObservableActionItemBean, Long> releaseIncrement = new TableColumn<ObservableActionItemBean, Long>(
				"ReleaseInc");
		TableColumn<ObservableActionItemBean, Boolean> ignore = new TableColumn<ObservableActionItemBean, Boolean>(
				"Active");
		table.getColumns().addAll(delay, release, delayIncrement, releaseIncrement, ignore);
		table.setPrefHeight(200);
		vbox.getChildren().add(table);
		return vbox;
	}

}
