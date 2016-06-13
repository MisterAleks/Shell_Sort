package Main;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.concurrent.Task;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;

public class Controller {

    ////////////////////////
    //Tab "Работа с Excel"//
    ////////////////////////

    //Fields

    //This data will be loaded to ListView object
    private ObservableList<Pupil> data = FXCollections.observableArrayList();

    //This array for sort
    private Pupil[] arrayPupil;

    @FXML
    //It is our ListView on form
    private ListView<String> list;

    //Methods

    @FXML
    void openExcel(ActionEvent event) throws IOException {

        //Checking for correctness
        try {
            new FileInputStream("Main.xlsx");
        } catch (Exception err) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);

            alert.setTitle("Упс!");
            alert.setHeaderText(null);
            alert.setContentText("Не получается открыть файл. Возможны следующие проблемы:\nФайл отсутствует" +
            "\nФайл поврежден\nФайл имеет неверный формат\n\nУбедитесь, что файл лежит в одной директории " +
                    "с программой и имеет название и формат Main.xlsx");

            alert.showAndWait();
        }

        //Open file and assign with WorkBook
        FileInputStream fin = new FileInputStream("Main.xlsx");
        Workbook wb = new XSSFWorkbook(fin);

        //Counter
        int n = 1;

        while (true) {
            try {
                //Our tabulated values
                int id = (int) wb.getSheetAt(0).getRow(n).getCell(0).getNumericCellValue();
                String name = wb.getSheetAt(0).getRow(n).getCell(1).getStringCellValue();
                double misses = wb.getSheetAt(0).getRow(n).getCell(2).getNumericCellValue();

                Pupil pupil = new Pupil(id, name, misses);

                data.add(pupil);

                n++;
            } catch (Exception er) {
                n--;
                break;
            }
        }

        arrayPupil = new Pupil[n];

        for (int i = 0; i < n; i++) {
            String s = data.get(i).getId() + " - " + data.get(i).getName() + " Процент пропусков: " +
                    data.get(i).getMisses();

            list.getItems().add(s);

            arrayPupil[i] = data.get(i);
        }

        fin.close();
    }

    @FXML
    void sortByNumber() {

        list.getItems().clear();

        if (data.isEmpty()){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);

            alert.setTitle("Упс!");
            alert.setHeaderText(null);
            alert.setContentText("Кажется вы еще не загрузили файл! Попробуйте еще раз после загрузки файла");

            alert.showAndWait();
        } else {
            //Shell sort
            int increment = arrayPupil.length / 2;

            while (increment > 0) {

                for (int i = increment; i < arrayPupil.length; i++) {

                    int j = i;
                    Pupil temp = arrayPupil[i];

                    while (j >= increment && arrayPupil[j - increment].getId() > temp.getId()) {
                        arrayPupil[j] = arrayPupil[j - increment];
                        j = j - increment;
                    }

                    arrayPupil[j] = temp;
                }
                increment *= 0.5;
            }

            //Output to the list
            for (int i = 0; i < arrayPupil.length; i++) {
                String s = arrayPupil[i].getId() + " - " + arrayPupil[i].getName() + " Процент пропусков: " +
                        arrayPupil[i].getMisses();

                list.getItems().add(s);
            }
        }
    }

    @FXML
    void sortByMisses() {

        list.getItems().clear();

        if (data.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);

            alert.setTitle("Упс!");
            alert.setHeaderText(null);
            alert.setContentText("Кажется вы еще не загрузили файл! Попробуйте еще раз после загрузки файла");

            alert.showAndWait();
        } else {
            //Shell sort
            int increment = arrayPupil.length / 2;

            while (increment > 0) {

                for (int i = increment; i < arrayPupil.length; i++) {

                    int j = i;
                    Pupil temp = arrayPupil[i];

                    while (j >= increment && arrayPupil[j - increment].getMisses() > temp.getMisses()) {
                        arrayPupil[j] = arrayPupil[j - increment];
                        j = j - increment;
                    }

                    arrayPupil[j] = temp;
                }
                increment *= 0.5;
            }

            //Output to the list
            for (int i = 0; i < arrayPupil.length; i++) {
                String s = arrayPupil[i].getId() + " - " + arrayPupil[i].getName() + " Процент пропусков: " +
                        arrayPupil[i].getMisses();

                list.getItems().add(s);
            }
        }

    }

    /////////////////////////////////
    //Tab "Визуализация сортировки"//
    /////////////////////////////////

    //Fields

    @FXML
    //Values of array
    private Text t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12;

    @FXML
    //Rectangles
    private Rectangle rt1, rt2, rt3, rt4, rt5, rt6, rt7, rt8, rt9, rt10, rt11, rt12;

    @FXML
    //Step of sorting as a Label for clarity
    private Label dLabel;

    //Methods

    @FXML
        //Shell sort
    void startVisualisation(ActionEvent event) {

        //We have 12 rectangles with 12 Text objects
        Text[] arrayText = {t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12};
        Rectangle[] arrayRectangle = {rt1, rt2, rt3, rt4, rt5, rt6, rt7, rt8, rt9, rt10, rt11, rt12};

        //Clean our data
        for (int i = 0; i < 12; i++) {
            arrayText[i].setText("");
            arrayRectangle[i].setFill(Color.DODGERBLUE);
        }

        //And clean D step label
        dLabel.setText("");

        /*As long as our code is executed, redrawing is not satisfied, because the flow is only one
         *and at the moment (when it's on a cycle), it performs just your code.
         *He may not redraw elements, as long as our code will not work.
         *Solution - to run our code in a separate thread*/

        //This is special task for parallel thread
        Task<Void> task = new Task<Void>() {

            @Override
            public Void call() {
                try {
                    //Main code

                    //Initialize array
                    for (int i = 0; i < arrayText.length; i++, Thread.sleep(250)) {

                        //Math.random() - [0,1); right border is not strict
                        //On typecast there will be a maximum value equal 12 (Fraction of a number will be removed)
                        int a = (int) (Math.random() * 13);
                        String a_str = Integer.toString(a);

                        arrayText[i].setText(a_str);
                    }

                    //The beggining of sort

                    //Looking for a initial value
                    int increment = arrayText.length / 2;
                    String dLabelMessage = Integer.toString(increment);

                    //Transfer it to main thread
                    updateMessage(dLabelMessage);

                    while (increment > 0) {

                        //Reset color of label
                        dLabel.setTextFill(Color.BLACK);

                        for (int i = increment; i < arrayText.length; i++) {
                            int j = i;

                            //Checking sorted
                            boolean less = true;

                            //Objects on right half of massive
                            int buffer = Integer.parseInt(arrayText[i].getText());

                            //Increment less && wrong sorting
                            while (j >= increment && Integer.parseInt(arrayText[j - increment].getText()) > buffer) {

                                //Selected object will be a YELLOW
                                //Pair object will be a RED because sorting is wrong
                                arrayRectangle[j].setFill(Color.YELLOW);
                                arrayRectangle[j - increment].setFill(Color.RED);

                                //Always will be wait for a second
                                Thread.sleep(1000);

                                //Wrong sorted object moved to right position
                                //Right position - RED
                                //Previous position - standart DODGERBLUE
                                arrayRectangle[j].setFill(Color.RED);
                                arrayRectangle[j - increment].setFill(Color.DODGERBLUE);

                                //Swap of values
                                arrayText[j].setText(arrayText[j - increment].getText());
                                arrayText[j - increment].setText(Integer.toString(buffer));

                                //Wait
                                Thread.sleep(1000);

                                //Return to position[j] standart color
                                arrayRectangle[j].setFill(Color.DODGERBLUE);

                                //Next step. Always with 'increment' variable
                                j = j - increment;

                                //Flag wrong sorting
                                less = false;
                            }

                            //If sort right
                            if (less) {

                                //Only show colors YELLOW (selected) and GREEN (previous)
                                //It means that all correct
                                arrayRectangle[j].setFill(Color.YELLOW);
                                arrayRectangle[j - increment].setFill(Color.GREEN);
                                Thread.sleep(1000);

                                //Return standart colors
                                arrayRectangle[j].setFill(Color.DODGERBLUE);
                                arrayRectangle[j - increment].setFill(Color.DODGERBLUE);
                            }
                        }

                        //Decrease variable in half
                        increment *= 0.5;

                        //Clean colors
                        for (int i = 0; i < arrayRectangle.length; i++) {
                            arrayRectangle[i].setFill(Color.DODGERBLUE);
                        }

                        //Highlighted a dLabel with RED color
                        dLabel.setTextFill(Color.RED);

                        //Transfer a value
                        dLabelMessage = Integer.toString(increment);
                        updateMessage(dLabelMessage);

                        //Wait
                        Thread.sleep(1000);
                    }
                } catch (InterruptedException ex) {
                }
                return null;
            }
        };

        //Action on get a message to dLabel from second thread
        task.messageProperty().addListener((observable, oldMessage, newMessage) -> dLabel.setText(newMessage));

        //Start a new thread with task settings
        Thread th = new Thread(task);
        th.start();

        //Visualization done
    }
}