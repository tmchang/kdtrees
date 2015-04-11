package kdtrees.gui

import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.stage.Stage
import scala.io.Source
import kdtrees.kdtree.KDTree
import kdtrees.kdtree.Profile

class AppTemplate extends Application {
  val AppHeight = 780
  val AppWidth = 330
  
  override def start(stage: Stage) {
    val args: Array[String] = getParameters().getRaw().toArray(new Array[String](0))
    val csvFile = args(0) //Load the .csv file containing profiles
    val profiles = Parser.csvToProfiles(csvFile)
    val tree: KDTree[Profile] = new KDTree(profiles)
    // Load the FXML
    val loader: FXMLLoader = new FXMLLoader(getClass().getResource("fxml_main.fxml"))
    val root: Parent = loader.load()
    val controller: MainPageController = loader.getController()
    controller.setStage(stage)
    controller.setTree(tree)

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

object Parser {
  import kdtrees.kdtree.Profile
  /**
   * Parses a csv file and converts the data to a list of Profiles
   * @param file the csv file containing the profile information
   * @return will return a list of Profiles created from the csv file.
   */
  def csvToProfiles(file: String): List[Profile] = {
    val src = Source.fromFile(file)
    val iter = src.getLines().map(_.split(","))
    val profiles = for (p <- iter) 
      yield new Profile(p.slice(1,p.length).map(_.toDouble), p(0))
    profiles.toList
  }
}

