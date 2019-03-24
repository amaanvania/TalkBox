package application;

import java.awt.Desktop;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import config.AudioButton;
import config.Builder;
import config.Utilities;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuBar;
import javafx.scene.control.Pagination;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.util.Callback;

public class TalkBoxApp extends Application {
	// class which builds GUI for talkbox application
	// similar to config gui
	
	// set up for logging 
	private static final Logger logr = Logger.getLogger(TalkBoxApp.class.getName());

	Image[] images;
	String[] names;
	Media[] audioFiles;
	private BorderPane appPane;
	private VBox audioCard;
	private FlowPane cardFlow;
	private Button play;
	private ImageView imgv;
	private FlowPane mainFlow;
	public Builder builder;
	public int numSetButtons;
	public int numberPages;
	volatile boolean audioPlaying = false;


	public static void main(String[] args) {
		launch(args);
	}

	public TalkBoxApp() {
	}

	public ToolBar buildBotToolbar() throws IOException { // method
		// which
		// builds
		// and
		// returns
		// the
		// bot
		// Toolbar
		Button play = new Button("Edit");
		play.setOnAction(e -> {
			try {
				builder.buildInitialGui(new Stage(),builder.pagination());
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
		ToolBar toolBar = new ToolBar(play);
		toolBar.setPrefSize(200, 20);
		toolBar.setId("bot-toolbar-config");
		return toolBar;
	}

	public TalkBoxApp(Builder builder) throws IOException {
		this.builder = builder;
	}
	
    public VBox createPage(int pageIndex) throws FileNotFoundException {
        VBox box = new VBox(20);
        int page = pageIndex;
        for (int i = page; i < page + 1; i++) {
            box.getChildren().add(buildApp(i));
        }
        return box;
    }
	public Pagination pagination(){
		Pagination pagination = new Pagination(1);
		pagination.setPageCount(builder.numPages);
		pagination.setMaxHeight(400.0);
        pagination.setPageFactory(new Callback<Integer, Node>() {	 
            @Override
            public Node call(Integer pageIndex) {
                    try {
						return createPage(pageIndex);
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
                    return null;
                }
        });
        return pagination;
	}
	
	public void buildApplication(Stage primaryStage) throws IOException {
		ToolBar botToolBar = buildBotToolbar();
		MenuBar topToolBar = builder.buildTopMenu();
		BorderPane appPane = new BorderPane();
		appPane.setCenter(pagination());
		appPane.setTop(topToolBar);
		appPane.setBottom(botToolBar);
		Scene scene = new Scene(appPane, 651, 510);
		String css = this.getClass().getResource("/resources/buttonstyle.css").toExternalForm();
		scene.getStylesheets().add(css);
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	

	
	public ImageView buildStopImage() throws FileNotFoundException {
		String s = getClass().getResource("/resources/stopbutton.png").toExternalForm();
		Image img = new Image(s);
		ImageView iv1 = new ImageView();
		iv1.setImage(img);
		iv1.setFitWidth(75);
		iv1.setPreserveRatio(true);
		iv1.setSmooth(true);
		iv1.setCache(true);
		return iv1;
	}

	
	public GridPane buildApp(int pageNumber) throws FileNotFoundException {
		GridPane gridpane = new GridPane();
		gridpane.setPrefSize(500, 500);
		gridpane.setVgap(10);
		gridpane.setHgap(10);
		int numButtons;
		if(pageNumber < builder.numPages){
			numButtons = 6;
		}
		else{
			numButtons = numSetButtons - ((numberPages - 1) * 6);
		}
		/*try { // bad method
			logr.setLevel(Level.ALL);
			String resourcePath = System.getProperty("user.home") + File.separatorChar + "TalkBoxData" + File.separatorChar;
			File f = new File(resourcePath + "UserLogs" + File.separatorChar + builder.filename + ".log");
			if(f.exists() == false) {
				f.createNewFile();
			}
			FileHandler fh = new FileHandler(resourcePath + "UserLogs" + File.separatorChar + builder.filename + ".log", 8096, 1, false);
			fh.setLevel(Level.FINEST);
			fh.setFormatter(new Formatter() {
			   public String format(LogRecord record) {
			        return 
			           record.getMessage() + "\n";
			      }
			    });
			logr.addHandler(fh);
			}
			catch(IOException e) {
				e.printStackTrace();
			}	 
		*/
		for (int i = pageNumber * 6; i < (pageNumber * 6) + numButtons; i++) {
			ImageView stop = buildStopImage();
			stop.setDisable(true);
			stop.setOpacity(0);
			int k = i;
			k = (k >= builder.buttons.size() ? builder.buttons.size() - 1 : k);
			int j = k;
			ImageView iv1 = builder.buildImageView();
			iv1.setImage(new Image(new FileInputStream(builder.buttons.get(j).getImagePath())));
			iv1.setOnMouseClicked(e -> {
				try {
					MediaPlayer p = builder.imageHandle(builder.buttons.get(j).getAudioPath());
					p.setOnPlaying(()->{
						builder.buttons.get(j).press();
						builder.saveSerializedFile(); // method 1 save constantly
						/*String outputLog = "";
						for(AudioButton b: builder.buttons) { // method 2 input into log file but forces reading of log files before every configuration
							if(b != null) {
								outputLog += b.getName() + " " + b.getPresses();
							}
						}
						logr.fine(outputLog);
						*/
						audioPlaying = true;
						iv1.setOpacity(0);
						stop.setOpacity(100);
						iv1.setDisable(true);
						stop.setDisable(false);
						stop.setOnMouseClicked(z->{
							audioPlaying = false;
							iv1.setDisable(false);
							stop.setDisable(true);
							stop.setOpacity(0);
							iv1.setOpacity(100);
							audioPlaying = false;
							p.stop();
						});
					});
					p.setOnEndOfMedia(()->{
						iv1.setDisable(false);
						stop.setDisable(true);
						stop.setOpacity(0);
						iv1.setOpacity(100);
						audioPlaying = false;
						//iv1.setImage(new Image(new FileInputStream(currentButton.getImagePath())));
					});
					if(!audioPlaying){
						p.play();
						iv1.setDisable(false);
						stop.setDisable(true);
						stop.setOpacity(0);
						iv1.setOpacity(100);
					}
				} catch (InterruptedException e1) {
					audioPlaying = false;
				}
			});
			GridPane.setConstraints(iv1, j % 6, 4 + 2);
			GridPane.setConstraints(stop, j % 6, 3 + 2);
			gridpane.getChildren().addAll(stop, iv1);
		}
		return gridpane;
	}
	public int getNumButtons() {
		return 0;

	}

	public Image[] getImages() {
		return images;

	}

	public String[] getNames() {
		return names;
	}

	public Media[] getAudioFiles() {
		return audioFiles;
	}



	public BorderPane getPane() {
		return appPane;
	}

	public void setPane(BorderPane mainPane) {
		this.appPane = mainPane;
	}

	@Override
	public void start(Stage primaryStage) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/resources/WelcomeScreen.fxml"));
			Scene scene = new Scene(root, 900, 650);
			scene.getStylesheets().add(getClass().getResource("/resources/WelcomeScreen.css").toExternalForm());
			primaryStage.setTitle("TalkBox App");
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	public void createNewTalkFileButton() {
		try {
			Builder config = new Builder();
			Stage stage = new Stage();
			FlowPane fp = config.buildWelcomeScreen(stage);
			Scene scene = new Scene(fp, 310, 440);
			stage.setScene(scene);
			stage.show();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	@FXML
	public void openExistingTalkFileButton() {
		try {
			Builder b = Builder.openSerializedFile(Utilities.configFileChoose(new Stage()));
			TalkBoxApp a = new TalkBoxApp(b);
			/*BorderPane c = a.getPane();
			Stage primaryStage = new Stage();
			primaryStage.setTitle("TalkBox Application");
			Scene scene = new Scene(c, 900, 650);
			String css = this.getClass().getResource("/resources/buttonstyle.css").toExternalForm();
			scene.getStylesheets().add(css);
			primaryStage.setScene(scene);
			primaryStage.show();
			*/
			a.buildApplication(new Stage());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	public void helpButton() {
		try {
			// Desktop.getDesktop().browse(new URI(
			// "https://github.com/amaanvania/TalkBox/blob/master/Documentation/Talk%20Box%20User%20Manual.pdf"));
			Desktop.getDesktop().browse(new URI("https://github.com/amaanvania/TalkBox/blob/master/Documentation"));
		} catch (IOException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}

	@FXML
	public void githubLink() {
		try {
			// Desktop.getDesktop().browse(new URI(
			// "https://github.com/amaanvania/TalkBox/blob/master/Documentation/Talk%20Box%20User%20Manual.pdf"));
			Desktop.getDesktop().browse(new URI("https://github.com/amaanvania/TalkBox"));
		} catch (IOException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}

}
