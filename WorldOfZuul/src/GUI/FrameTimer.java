package GUI;

import javafx.animation.AnimationTimer;
import javafx.scene.control.Label;

/*
"Borrowed from...
https://stackoverflow.com/questions/28287398/what-is-the-preferred-way-of-getting-the-frame-rate-of-a-javafx-application
 */

public class FrameTimer {

        private final long[] frameTimes = new long[100];
        private int frameTimeIndex = 0 ;
        private boolean arrayFilled = false ;

        FrameTimer()    {

        }

        public Label run() {
            Label label = new Label();
            AnimationTimer frameRateMeter = new AnimationTimer() {

                @Override
                public void handle(long now) {
                    long oldFrameTime = frameTimes[frameTimeIndex];
                    frameTimes[frameTimeIndex] = now;
                    frameTimeIndex = (frameTimeIndex + 1) % frameTimes.length;
                    if (frameTimeIndex == 0) {
                        arrayFilled = true;
                    }
                    if (arrayFilled) {
                        long elapsedNanos = now - oldFrameTime;
                        long elapsedNanosPerFrame = elapsedNanos / frameTimes.length;
                        double frameRate = 1_000_000_000.0 / elapsedNanosPerFrame;
                        label.setText(String.format("Current frame rate: %.3f", frameRate));
                    }
                }
            };

            frameRateMeter.start();
            return label;
        }
}
