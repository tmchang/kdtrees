package kdtrees.gui

import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.stage.Stage
import scala.io.Source
import kdtrees.kdtree.KDTree
import kdtrees.kdtree.Profile
import java.io.FileNotFoundException
import kdtrees.kdtree.BruteForce

class AppTemplate extends Application {
  val AppHeight = 830
  val AppWidth = 400
  
  override def start(stage: Stage) {
    val args: Array[String] = getParameters().getRaw().toArray(new Array[String](0))
    val csvFile = args(0) //Load the .csv file containing profiles
    try {
    val profiles = Parser.csvToProfiles(csvFile)
    val tree: KDTree[Profile] = new KDTree(profiles)
    val brute: BruteForce[Profile] = new BruteForce(profiles)
    // Load the FXML
    val loader: FXMLLoader = new FXMLLoader(getClass().getResource("fxml_main.fxml"))
    val root: Parent = loader.load()
    val controller: MainPageController = loader.getController()
    controller.setStage(stage)
    controller.setTree(tree)
    controller.setBrute(brute)
    
    stage.setTitle("Knight Date")
    stage.setScene(new Scene(root, AppWidth, AppHeight))
    stage.setMinWidth(AppWidth);
    stage.setMinHeight(AppHeight);
    stage.show()
    } catch {
      case e: FileNotFoundException => println("Error in csv file path: File Not Found")
    }
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

/**
 * Object Parser has one method csvToProfiles which takes in a filename
 * of a csv file containing rows of name, Doubles and creates a List
 * of Profiles
 */
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

