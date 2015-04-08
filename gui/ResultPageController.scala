package kdtrees.gui

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;
import javafx.stage.Stage;

class ResultPageController {
  @FXML private var resultText : Text = null
  private var stage : Stage = null
  private var page1 : Scene = null

/**
 * Handles the pressing of the back button on the result GUI page.
 **/
@FXML protected def handleBackButtonAction(event : ActionEvent) : Unit = {
  stage.close()
  stage.setScene(page1)
  stage.show()
  }
  
  /**
 * Sets the text of resultText to the given text, and stores a reference
 * to the stage and first page of the GUI.
 *
 * @param text The text to be displayed
 * @param page1 The scene representing the main GUI page
 * @param s The stage where the result scene will be displayed
 *
 **/
def display(text : String, page1 : Scene, s : Stage) : Unit = {
  resultText.setText(text)
  this.page1 = page1
  this.stage = s
  
  }

}