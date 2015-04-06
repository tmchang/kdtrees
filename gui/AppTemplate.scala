package kdtrees.gui

import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.stage.Stage

class AppTemplate extends Application {
  val AppHeight = 780
  val AppWidth = 330
  
  override def start(stage: Stage) {
    val args: Array[String] = getParameters().getRaw().toArray(new Array[String](0))
    val csvFile = args(0) //Load the .csv file containing profiles
    // Load the FXML
    val loader: FXMLLoader = new FXMLLoader(getClass().getResource("fxml_main.fxml"))
    val root: Parent = loader.load()
    val controller: MainPageController = loader.getController()
    controller.setStage(stage)

    stage.setTitle("Knight Date")
    stage.setScene(new Scene(root, AppWidth, AppHeight))
    stage.setMinWidth(AppWidth);
    stage.setMinHeight(AppHeight);
    stage.show()  
  }
}

/**
 * Main class for the JavaFX application.
 * The start() method is the entry point for the application.
 * The main method here is essentially just calling start.
 */
object AppTemplate {
  def main(args: Array[String]) {
    Application.launch(classOf[AppTemplate], args: _*)
  }
}

