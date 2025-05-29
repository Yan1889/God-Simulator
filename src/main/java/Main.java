import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import adamyan.neuralengine.activations.*;
import adamyan.neuralengine.*;

import java.util.Arrays;

public class Main extends Application {
    @Override
    public void start(Stage stage) {
        constructWindows();
    }

    private void constructWindows() {
        Text helloWorldText = new Text("Hello world!");
        helloWorldText.setFont(new Font(50));
        helloWorldText.setLayoutX(400);
        helloWorldText.setLayoutY(400);

        Layer[] layers = {
                new Layer(4, ActivationFunctions::linear),
                new Layer(10, ActivationFunctions::sigmoid),
                new Layer(10, ActivationFunctions::sigmoid),
                new Layer(2, ActivationFunctions::sigmoid),
        };

        Network network = new Network(layers);

        double[] output = network.feedForward(new double[]{0.2, 0.4, 0.5, 0.1});

        System.out.println(Arrays.toString(output));

        Group rootNode = new Group(helloWorldText);

        Scene scene = new Scene(rootNode, 1000, 900);

        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Main window");
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}