package podomoro;


import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import ui.ResumeToggle;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    private final int START_WORK_TIME_MIN = 0;
    private final int START_WORK_TIME_SEC = 20;
    private final int START_RECESS_TIME_MIN = 0;
    private final int START_RECESS_TIME_SEC = 5;
    private final int START_LONG_RECESS_TIME_MIN = 0;
    private final int START_LONG_RECESS_TIME_SEC = 10;

    @FXML
    private Label clockLabel;

    @FXML
    private Pane resumeBtn;

    private ResumeToggle resumeToggle = new ResumeToggle();
    private Timeline workTimeLine;
    private Timeline recessTimeLine;

    private int min;
    private int sec;

    private boolean isFirstTime;
    private int numsPoroTime;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        resumeBtn.getChildren().add(resumeToggle);
        min = START_WORK_TIME_MIN;
        sec = START_WORK_TIME_SEC;
        clockLabel.setText(showTime());


    }

    private void coutDown(){
        if (sec > 0){
            sec--;
        }else{
            min--;
            sec = 59;
        }
    }

    private boolean isFullTime(){
        return min == 0 && sec == 0;
    }

    private String showTime(){
        if (sec == 60){
            return String.format("%02d : %02d", min + 1, 0);
        }else
            return String.format("%02d : %02d", min, sec);
    }

    private void setTextLabel(){
        clockLabel.setText(showTime());
    }

    private void startRecessTime(){
        min = START_RECESS_TIME_MIN;
        sec = START_RECESS_TIME_SEC;

        recessTimeLine = new Timeline(new KeyFrame(Duration.seconds(0), event -> setTextLabel()), new KeyFrame(Duration.seconds(1),
                                        event -> {
                                            coutDown();
                                            setTextLabel();
                                            if (isFullTime()){
                                                setTextLabel();
                                                recessTimeLine.stop();
                                                startWorkTime();
                                            }
                                        }));
        recessTimeLine.setCycleCount(Animation.INDEFINITE);
        recessTimeLine.setDelay(Duration.seconds(0.6));
        recessTimeLine.playFromStart();
    }

    private void startLongRecessTime(){
        min = START_LONG_RECESS_TIME_MIN;
        sec = START_LONG_RECESS_TIME_SEC;

        recessTimeLine = new Timeline(new KeyFrame(Duration.seconds(0), event -> setTextLabel()), new KeyFrame(Duration.seconds(1),
                event -> {
                    coutDown();
                    setTextLabel();
                    if (isFullTime()){
                        setTextLabel();
                        recessTimeLine.stop();
                        startWorkTime();
                    }
                }));
        recessTimeLine.setCycleCount(Animation.INDEFINITE);
        recessTimeLine.setDelay(Duration.seconds(0.6));
        recessTimeLine.playFromStart();
    }

    private void startWorkTime() {
        min = START_WORK_TIME_MIN;
        sec = START_WORK_TIME_SEC;

        workTimeLine = new Timeline(new KeyFrame(Duration.seconds(0),
                                        event -> setTextLabel()),
                                    new KeyFrame(Duration.seconds(1),
                                        event -> {
                                            coutDown();
                                            setTextLabel();
                                            if (isFullTime()){
                                                workTimeLine.stop();
                                                isFirstTime = false;
                                                numsPoroTime++;
                                                if (numsPoroTime % 4 == 0){
                                                    startLongRecessTime();
                                                }else {
                                                    startRecessTime();
                                                }
                                            }
                                        }));
        workTimeLine.setCycleCount(Animation.INDEFINITE);
        if(!isFirstTime){
            workTimeLine.setDelay(Duration.seconds(0.6));
        }
        workTimeLine.playFromStart();
    }

    private void makeDefaultTime(){
        min = START_WORK_TIME_MIN;
        sec = START_WORK_TIME_SEC;
        setTextLabel();
        try{
            isFirstTime = false;
            workTimeLine.stop();
            recessTimeLine.stop();
        }catch (NullPointerException e){
            e.printStackTrace();
        }


    }

    @FXML
    public void onClicked(MouseEvent event) {
        if (resumeToggle.isPause()) {
            resumeToggle.change();
            isFirstTime = true;
            startWorkTime();
        } else {
            makeDefaultTime();
            resumeToggle.change();
        }


    }
}
