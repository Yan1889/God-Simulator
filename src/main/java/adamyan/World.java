package adamyan;

import adamyan.Obstacles.Obstacle;
import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;

import java.util.List;

/**
 * A world object represents the state of a game
 */
public class World {
    private List<AntPopulation> antPopulations;
    private List<Obstacle> obstacles;


    public World(List<AntPopulation> antPopulations, List<Obstacle> obstacles) {
        this.antPopulations = antPopulations;
        this.obstacles = obstacles;
    }

    public void calculateFrame() {
        for (AntPopulation population : antPopulations) {
            population.calculateFrame();
        }
    }

    public void draw(Canvas canvas) {
        var gc = canvas.getGraphicsContext2D();

        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        // canvas outline
        gc.strokeRect(0, 0, canvas.getWidth(), canvas.getHeight());

        for (Obstacle o : obstacles) {
            o.drawOnCanvas(canvas);
        }
        for (AntPopulation p : antPopulations) {
            p.drawOnCanvas(canvas);
        }
    }

    public void startSimulation(Canvas canvas) {
        // handle() gets called 60 times per second
        new AnimationTimer() {
            @Override
            public void handle(long now) {
                calculateFrame();
                draw(canvas);
            }
        }.start();
    }
}
