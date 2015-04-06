package kdtrees.gui

import javafx.fxml.FXML
import javafx.scene.text.Text
import javafx.stage.Stage
import javafx.scene.paint.Color
import javafx.scene.control.TextField
import javafx.event.ActionEvent
import kdtrees.gui.ResultPageController
import javafx.scene.Scene
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import scala.util.Random

class MainPageController {
//  @FXML private var welcomeText: Text = _
  @FXML private var nameText : TextField = null;
  @FXML private var attr1Text : TextField = null;
  @FXML private var attr2Text : TextField = null;
  @FXML private var attr3Text : TextField = null;
  @FXML private var attr4Text : TextField = null;
  @FXML private var attr5Text : TextField = null;
  @FXML private var attr6Text : TextField = null;
  @FXML private var attr7Text : TextField = null;
  @FXML private var attr8Text : TextField = null;
  @FXML private var attr9Text : TextField = null;
  @FXML private var attr10Text : TextField = null;
  
  private var stage: Stage = _
  private var page2 : Scene = null;
  private var page2Controller : ResultPageController = null;

//  @FXML def changeColor = {
//    val r = (System.currentTimeMillis() % 71 / 71.0)
//    val g = (System.currentTimeMillis() % 97 / 97.0)
//    val b = (System.currentTimeMillis() % 83 / 83.0)
//    welcomeText.setFill(Color.color(r, g, b))
//  }
  
 /**
 * Handles the pressing of the clear button on the main GUI page.
 **/
 @FXML protected def handleClearButtonAction(event : ActionEvent) : Unit = {
   this.nameText.clear
   this.attr1Text.clear
   this.attr2Text.clear
   this.attr3Text.clear
   this.attr4Text.clear
   this.attr5Text.clear
   this.attr6Text.clear
   this.attr7Text.clear
   this.attr8Text.clear
   this.attr9Text.clear
   this.attr10Text.clear
   
 }
 
 /**
 * Handles the pressing of the submit button on the main GUI page.
 **/
  @FXML protected def handleSubmitButtonAction(event : ActionEvent) : Unit = {
       if (page2 == null) {
       val loader : FXMLLoader = new FXMLLoader(getClass().getResource("fxml_result.fxml"))
       var root: Parent = loader.load()
       var tempc : ResultPageController = loader.getController()
       this.page2Controller = tempc
       this.page2 = new Scene(root, 780, 330)
     }
     page2Controller.display("test text", stage.getScene(), stage)
     stage.close()
     stage.setScene(page2)
     stage.show()
  }
  
   /**
 * Handles the pressing of the randomize button on the main GUI page.
 **/
  @FXML protected def handleRandomizeButtonAction(event : ActionEvent) : Unit = {
   val randomGen = new Random()
   nameText.setText(randomGen.nextString(8))
   attr1Text.setText(randomGen.nextDouble.toString)
   attr2Text.setText(randomGen.nextDouble.toString)
   attr3Text.setText(randomGen.nextDouble.toString)
   attr4Text.setText(randomGen.nextDouble.toString)
   attr5Text.setText(randomGen.nextDouble.toString)
   attr6Text.setText(randomGen.nextDouble.toString)
   attr7Text.setText(randomGen.nextDouble.toString)
   attr8Text.setText(randomGen.nextDouble.toString)
   attr9Text.setText(randomGen.nextDouble.toString)
   attr10Text.setText(randomGen.nextDouble.toString)
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
