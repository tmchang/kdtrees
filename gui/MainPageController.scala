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
import kdtrees.kdtree.Profile
import kdtrees.kdtree.KDTree
import kdtrees.kdtree.KDTreeNode
import scala.Double
import kdtrees.kdtree.BruteForce

class MainPageController {

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
  private var tree : KDTree[Profile] = null
  private var brute : BruteForce[Profile] = null
  
  def setTree(t: KDTree[Profile]) {
    this.tree = t
  }

  def setBrute(t: BruteForce[Profile]) {
    this.brute = t
  }
  
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

    var yourMatch = findMatch()
       if (nameText.getText() == "") {
         yourMatch = "Error: Please input a name \n" + 
     " and attributes between -1 and 1"
       }
    
       if (page2 == null) {
       val loader : FXMLLoader = new FXMLLoader(getClass().getResource("fxml_result.fxml"))
       var root: Parent = loader.load()
       var tempc : ResultPageController = loader.getController()
       this.page2Controller = tempc
       this.page2 = new Scene(root, 400, 820)
     }
     page2Controller.display(yourMatch, stage.getScene(), stage)
     stage.close()
     stage.setScene(page2)
     stage.setMinWidth(400)
     stage.setMinHeight(830)
     stage.show()

  }
  
   /**
   * Handles the pressing of the randomize button on the main GUI page.
   **/
  @FXML protected def handleRandomizeButtonAction(event : ActionEvent) : Unit = {
   val randomGen = new Random()
   val strBuild = new StringBuilder()
   for (i <- 1 to 8) {
     strBuild.append(randomGen.nextPrintableChar())
   }
   nameText.setText(strBuild.toString())
   attr1Text.setText((((randomGen.nextDouble)*2)-1).toString)
   attr2Text.setText((((randomGen.nextDouble)*2)-1).toString)
   attr3Text.setText((((randomGen.nextDouble)*2)-1).toString)
   attr4Text.setText((((randomGen.nextDouble)*2)-1).toString)
   attr5Text.setText((((randomGen.nextDouble)*2)-1).toString)
   attr6Text.setText((((randomGen.nextDouble)*2)-1).toString)
   attr7Text.setText((((randomGen.nextDouble)*2)-1).toString)
   attr8Text.setText((((randomGen.nextDouble)*2)-1).toString)
   attr9Text.setText((((randomGen.nextDouble)*2)-1).toString)
   attr10Text.setText((((randomGen.nextDouble)*2)-1).toString)
  }
 
  /**
   * 	Sets the stage field of the controller to the given stage.
   *
   *  @param stage The stage
   */
  def setStage(stage: Stage) {
    this.stage = stage
  }
  
  /**
   * Constructs the profile based on user input and finds the nearest neighbor to the 
   * constructed profile
   */
  def findMatch() : String = {
   try { val myProfile = new Profile(Array(stringToDouble(attr1Text.getText),
                                      stringToDouble(attr2Text.getText),
                                      stringToDouble(attr3Text.getText),
                                      stringToDouble(attr4Text.getText),
                                      stringToDouble(attr5Text.getText),
                                      stringToDouble(attr6Text.getText),
                                      stringToDouble(attr7Text.getText),
                                      stringToDouble(attr8Text.getText),
                                      stringToDouble(attr9Text.getText),
                                      stringToDouble(attr10Text.getText)), 
                                      nameText.getText) 
     this.tree.findNN(myProfile).get.toString 
     //+ "\n" + this.brute.findNN(myProfile).get.toString
   }
   catch { 
     case e : NumberFormatException => "Error: Please input a name \n" + 
     " and attributes between -1 and 1"
     
   }


  }
  
  /**
   * converts a string to a double
   */
  private def stringToDouble(s: String): Double = {
    val d = s.toDouble
    if (d > 1.0 || d < -1.0) throw new NumberFormatException()
    else d
  }
}
