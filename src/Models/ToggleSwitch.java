package Models;



import javafx.animation.FillTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.Parent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class ToggleSwitch extends Parent {
	
	private BooleanProperty swichedOn = new SimpleBooleanProperty(false);
	
	private TranslateTransition translateAnimation = new TranslateTransition(Duration.seconds(0.25));
	private FillTransition fillAnimation = new FillTransition((Duration.seconds(0.25)));
	
	private ParallelTransition animation = new ParallelTransition(translateAnimation,fillAnimation);
	
	public BooleanProperty swichedOnProperty() {
		return swichedOn;
	}
	public ToggleSwitch() {
		Rectangle background = new Rectangle(50,25);
		background.setArcWidth(25);
		background.setArcHeight(25);
		background.setFill(Color.WHITE);
		background.setStroke(Color.LIGHTGRAY);
		Circle trigger = new Circle(12);
		trigger.setCenterX(12);
		trigger.setCenterY(12);
		trigger.setFill(Color.WHITE);
		trigger.setStroke(Color.LIGHTGRAY);
		
		translateAnimation.setNode(trigger);
		fillAnimation.setShape(background);

		getChildren().addAll(background,trigger);
		
		swichedOn.addListener((obs, oldState, newState) -> {
			boolean isOn = newState.booleanValue();
			translateAnimation.setToX(isOn ? 50 - 25 : 0);
			fillAnimation.setFromValue(isOn ? Color.WHITE : Color.GRAY);
			fillAnimation.setToValue(isOn ? Color.GRAY : Color.WHITE);

			animation.play();
		});
		
		setOnMouseClicked(event ->{
			swichedOn.set(!swichedOn.get());
		});
	}



}
