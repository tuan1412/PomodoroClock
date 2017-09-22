package ui;


import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


public class ResumeToggle extends ImageView {
    private final Image PAUSE_IMG = new Image("/images/pause.png");
    private final Image RESUME_IMG = new Image("/images/resume.png");
    private boolean pause;

    public ResumeToggle() {
        setImage(RESUME_IMG);
        pause = true;
    }

    public boolean isPause(){
        return pause;
    }

    public void change(){
        if (pause){
            pause = false;
            setImage(PAUSE_IMG);
        }else{
            pause = true;
            setImage(RESUME_IMG);
        }
    }

}
