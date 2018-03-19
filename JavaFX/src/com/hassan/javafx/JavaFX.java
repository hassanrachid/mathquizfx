package com.hassan.javafx;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

public class JavaFX extends Application {

	private Label timer = new Label();
	private long seconds = 0;
	private Text question = new Text("Question:");
	private Text showQuestion = new Text("? + ? = ?");
	private Text showAnswer = new Text("Answer:");
	private TextField inputAnswer = new TextField("");
	public static ToggleGroup group = new ToggleGroup();
	public static ToggleGroup options = new ToggleGroup();
	public static String[] parts;
	public static String option;
	public static int[] answers = { 0, 0 };
	private IntegerProperty right = new SimpleIntegerProperty();
	private Label rightAnswers = new Label();
	private Text questionsAnswered = new Text("Questions Answered Correctly: ");

	@Override
	public void start(Stage primaryStage) throws Exception {

		Pane pane = new Pane();
		Scene scene = new Scene(pane, 300, 250);
		timer.setText(formatStringToTime(seconds));
		rightAnswers.textProperty().bind(right.asString());

		Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), (ActionEvent event) -> {

			seconds++;
			timer.setText(formatStringToTime(seconds));

		}));

		timeline.setCycleCount(Timeline.INDEFINITE);

		Button button = new Button("Start");
		button.setOnAction(ActionEvent -> {
			if (button.getText().equals("Stop")) {
				button.setText("Start");
				showQuestion.setText("? + ? = ?");
				inputAnswer.setText("");
				seconds = 0;
				timer.setText(formatStringToTime(seconds));
				timeline.stop();
				timeline.setCycleCount(0);
			} else {
				timeline.setCycleCount(Timeline.INDEFINITE);
				timeline.play();
				parts = options.getSelectedToggle().getUserData().toString().split(" ");
				option = group.getSelectedToggle().getUserData().toString();
				showQuestion.setText(getQuestion());
				System.out.println(answers[0] + answers[1]);
				button.setText("Stop");
			}
		});

		inputAnswer.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (!newValue.matches("\\d{0,7}([\\.]\\d{0,4})?")) {
					inputAnswer.setText(oldValue);
				}
			}
		});

		inputAnswer.setOnKeyPressed(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent key) {

				if (key.getCode().equals(KeyCode.ENTER)) {
					checkAnswer();
				}
			}

		});

		RadioButton add = new RadioButton("Add");
		add.setSelected(true);
		add.setUserData("+");
		add.setToggleGroup(group);
		RadioButton subtract = new RadioButton("Subtract");
		subtract.setUserData("-");
		subtract.setToggleGroup(group);
		RadioButton multiply = new RadioButton("Multiply");
		multiply.setUserData("*");
		multiply.setToggleGroup(group);
		RadioButton divide = new RadioButton("Divide");
		divide.setUserData("/");
		divide.setToggleGroup(group);

		RadioButton first = new RadioButton("Numbers from 0 to 5");
		first.setSelected(true);
		first.setUserData("0 5");
		first.setToggleGroup(options);
		RadioButton second = new RadioButton("Numbers from 3 to 9");
		second.setUserData("3 9");
		second.setToggleGroup(options);
		RadioButton third = new RadioButton("Numbers from 0 to 20");
		third.setUserData("0 20");
		third.setToggleGroup(options);
		RadioButton fourth = new RadioButton("Any two digits");
		fourth.setUserData("0 1000");
		fourth.setToggleGroup(options);

		VBox vbox = new VBox(5);
		vbox.setPadding(new Insets(10));
		vbox.getChildren().addAll(add, subtract, multiply, divide);
		vbox.getChildren().add(button);

		VBox vbox2 = new VBox(5);
		vbox2.setPadding(new Insets(10));
		vbox2.setLayoutX(140);
		vbox2.getChildren().addAll(first, second, third, fourth);

		// horizontal box of vertical boxes
		pane.getChildren().addAll(vbox, vbox2);

		HBox hbox = new HBox(50);
		HBox hbox2 = new HBox(50);
		hbox.getChildren().addAll(showQuestion, inputAnswer);
		hbox2.getChildren().addAll(question, showAnswer);
		hbox.setLayoutX(40);
		hbox2.setLayoutX(40);
		hbox2.setLayoutY(130);
		hbox.setLayoutY(150);

		timer.setLayoutX(247);
		timer.setLayoutY(225);

		rightAnswers.setLayoutX(175);
		rightAnswers.setLayoutY(225);

		questionsAnswered.setLayoutX(5);
		questionsAnswered.setLayoutY(238);

		button.setLayoutX(200);
		button.setLayoutY(220);

		pane.getChildren().addAll(timer, button, hbox, hbox2, rightAnswers, questionsAnswered);

		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public static String formatStringToTime(long seconds) {
		return String.format("%02d:%02d:%02d", TimeUnit.SECONDS.toHours(seconds),
				TimeUnit.SECONDS.toMinutes(seconds) - TimeUnit.HOURS.toMinutes((TimeUnit.SECONDS.toHours(seconds))),
				seconds - TimeUnit.MINUTES.toSeconds(TimeUnit.SECONDS.toMinutes(seconds)));
	}

	private String getQuestion() {
		answers[0] = ThreadLocalRandom.current().nextInt(Integer.valueOf(parts[0]), Integer.valueOf(parts[1]));
		answers[1] = ThreadLocalRandom.current().nextInt(Integer.valueOf(parts[0]), Integer.valueOf(parts[1]));
		return answers[0] + " " + option + " " + answers[1] + " = ?";
	}

	private void checkAnswer() {
		switch (option) {

		case "+":
			if (Integer.valueOf(answers[0]) + Integer.valueOf(answers[1]) == Integer.valueOf(inputAnswer.getText())) {
				right.set(right.intValue() + 1);
			}
			break;
		case "*":
			if (Integer.valueOf(answers[0]) * Integer.valueOf(answers[1]) == Integer.valueOf(inputAnswer.getText())) {
				right.set(right.intValue() + 1);
			}
			break;
		case "//":
			if (Integer.valueOf(answers[0]) / Integer.valueOf(answers[1]) == Integer.valueOf(inputAnswer.getText())) {
				right.set(right.intValue() + 1);
			}
			break;
		case "-":
			if (Integer.valueOf(answers[0]) - Integer.valueOf(answers[1]) == Integer.valueOf(inputAnswer.getText())) {
				right.set(right.intValue() + 1);
			}
			break;
		}
		inputAnswer.setText("");
		showQuestion.setText(getQuestion());
	}

	public static void main(String[] args) {
		launch(args);
	}

}
