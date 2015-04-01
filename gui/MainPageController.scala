package kdtrees.gui

import javafx.fxml.FXML
import javafx.scene.text.Text
import javafx.stage.Stage
import javafx.scene.paint.Color

class MainPageController {
  @FXML private var welcomeText: Text = _
  
  private var stage: Stage = _

  @FXML def changeColor = {
    val r = (System.currentTimeMillis() % 71 / 71.0)
    val g = (System.currentTimeMillis() % 97 / 97.0)
    val b = (System.currentTimeMillis() % 83 / 83.0)
    welcomeText.setFill(Color.color(r, g, b))
  }
      
  /**
   * 	Sets the stage field of the controller to the given stage.
   *
   *  @param stage The stage
   */
  def setStage(stage: Stage) {
    this.stage = stage
  }
}
