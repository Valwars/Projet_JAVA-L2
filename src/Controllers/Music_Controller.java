package Controllers;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

public class Music_Controller implements Initializable {

	@FXML
	private Pane pane;
	@FXML
	private Label songLabel;
	@FXML
	private Button playButton, pauseButton, resetButton, previousButton, nextButton;

	@FXML
	private Slider volumeSlider;
	@FXML
	private ProgressBar songProgressBar;

	private Media media;
	private MediaPlayer mediaPlayer;

	private File directory;
	private File[] files;

	private ArrayList<File> songs;
	private double volume = 0.5;
	private int songNumber;
	private int[] speeds = { 25, 50, 75, 100, 125, 150, 175, 200 };

	private Timer timer;
	private TimerTask task;

	private boolean running;
	public boolean sp = false;
	public boolean start = false;
	public Controller_3D_Environnement controller;
	
	public Music_Controller(Controller_3D_Environnement contr) {
		this.controller = contr;

	}
	public void start() {
		start = true;
		songs = new ArrayList<File>();

		directory = new File("musiques");

		files = directory.listFiles();

		if (files != null) {

			for (File file : files) {
				if (!file.toString().contains("DS")) {
					songs.add(file);

				}

			}
		}

		media = new Media(songs.get(songNumber).toURI().toString());
		
		mediaPlayer = new MediaPlayer(media);

		songLabel.setText(songs.get(songNumber).getName());

		volumeSlider.valueProperty().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> arg0, Number arg1, Number arg2) {

				mediaPlayer.setVolume(volumeSlider.getValue() * 0.01);
				volume = volumeSlider.getValue() * 0.01;
			}
		});
		songProgressBar.setStyle("-fx-accent: #c62727;");
		
	}
	
	public void setImg() {
		
		ImageView pause ;
		
		if(sp) {
			 pause = new ImageView(new Image(getClass().getResourceAsStream("pause.png")));

		}else {
			 pause = new ImageView(new Image(getClass().getResourceAsStream("start.png")));

		}
		pause.setPreserveRatio(true);
		pause.setFitHeight(35);
		pause.setFitWidth(40);
		pauseButton.setGraphic(pause);
		songLabel.setText(songs.get(songNumber).getName());
		
		volumeSlider.setValue(volume * 100);

	}
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		volumeSlider.valueProperty().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> arg0, Number arg1, Number arg2) {

				mediaPlayer.setVolume(volumeSlider.getValue() * 0.01);
				volume = volumeSlider.getValue() * 0.01;

			}
		});
		ImageView pause = new ImageView(new Image(getClass().getResourceAsStream("start.png")));
		pause.setPreserveRatio(true);
		pause.setFitHeight(35);
		pause.setFitWidth(40);
		pauseButton.setGraphic(pause);

		ImageView next = new ImageView(new Image(getClass().getResourceAsStream("next.png")));
		next.setPreserveRatio(true);
		next.setFitHeight(35);
		next.setFitWidth(40);
		nextButton.setGraphic(next);

		ImageView previous = new ImageView(new Image(getClass().getResourceAsStream("previous.png")));
		previous.setPreserveRatio(true);
		previous.setFitHeight(35);
		previous.setFitWidth(40);
		previousButton.setGraphic(previous);
	}

	public void playMedia() {
		
		ImageView pause;
		if (!sp) {

			beginTimer();
			mediaPlayer.setVolume(volumeSlider.getValue() * 0.01);
			mediaPlayer.play();
			
			pause = new ImageView(new Image(getClass().getResourceAsStream("pause.png")));
			sp = true;

		} else {
			this.pauseMedia();
			pause = new ImageView(new Image(getClass().getResourceAsStream("start.png")));
			sp = false;
		}
		pause.setPreserveRatio(true);
		pause.setFitHeight(35);
		pause.setFitWidth(40);
		pauseButton.setGraphic(pause);
		pause.setFitHeight(15);
		pause.setFitWidth(15);
		controller.getPause_button().setGraphic(pause);
	}

	public void pauseMedia() {

		cancelTimer();
		mediaPlayer.pause();
	}

	public void resetMedia() {

		songProgressBar.setProgress(0);
		mediaPlayer.seek(Duration.seconds(0));
	}

	public void mute() {
		mediaPlayer.setMute(true);
	}

	public void demute() {
		mediaPlayer.setMute(false);

	}

	public void previousMedia() {

		if (songNumber > 0) {

			songNumber--;

			mediaPlayer.stop();

			if (running) {

				cancelTimer();
			}

			media = new Media(songs.get(songNumber).toURI().toString());
			mediaPlayer = new MediaPlayer(media);

			songLabel.setText(songs.get(songNumber).getName());
			sp = false;

			playMedia();
		} else {

			songNumber = songs.size() - 1;

			mediaPlayer.stop();

			if (running) {

				cancelTimer();
			}

			media = new Media(songs.get(songNumber).toURI().toString());
			mediaPlayer = new MediaPlayer(media);

			songLabel.setText(songs.get(songNumber).getName());
			sp = false;

			playMedia();
		}
	}
	

	public void nextMedia() {

		if (songNumber < songs.size() - 1) {

			songNumber++;

			mediaPlayer.stop();

			if (running) {

				cancelTimer();
			}

			media = new Media(songs.get(songNumber).toURI().toString());
			mediaPlayer = new MediaPlayer(media);

			songLabel.setText(songs.get(songNumber).getName());
			sp = false;
			playMedia();
			
		} else {

			songNumber = 0;

			mediaPlayer.stop();

			media = new Media(songs.get(songNumber).toURI().toString());
			mediaPlayer = new MediaPlayer(media);

			songLabel.setText(songs.get(songNumber).getName());
			sp = false;

			playMedia();
		}
	}

	public void beginTimer() {

		timer = new Timer();

		task = new TimerTask() {

			public void run() {

				running = true;
				double current = mediaPlayer.getCurrentTime().toSeconds();
				double end = media.getDuration().toSeconds();
				songProgressBar.setProgress(current / end);

				if (current / end == 1) {

					cancelTimer();
				}
			}
		};

		timer.scheduleAtFixedRate(task, 0, 1000);
	}

	public void cancelTimer() {

		running = false;
		timer.cancel();
	}
}