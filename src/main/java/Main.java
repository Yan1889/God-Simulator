import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;


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