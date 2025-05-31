package adamyan;

import adamyan.neuralengine.Network;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

/**
 * represents a single ant
 */
public class Ant {
    private Network network;
    private Vector2D position;
    private double rotationAngle;

    public static final Image antImage = new Image("ant.png");


    public Ant(Vector2D position, Network network) {
        this.position = position;
        this.network = network;
        this.rotationAngle = 0;
    }

    public void act_logic() {
        // random movement without network
        position = new Vector2D(
                position.x() + 2 * Math.random() - 1,
                position.y() + 2 * Math.random() - 1
        );
    }

    public void drawOnCanvas(GraphicsContext gc) {
        gc.save();
        gc.translate(position.x(), position.y());
        gc.rotate(Math.toDegrees(rotationAngle));
        gc.drawImage(antImage, -antImage.getWidth() / 2, -antImage.getHeight() / 2);
        gc.restore();
    }

    public Vector2D getPosition() {
        return position;
    }
    public double getRotationAngle() {
        return rotationAngle;
    }
}
