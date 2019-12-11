/**
* <h1>Calculator FX RMI</h1>
* <h2>A simple/basic calculator implementing application</h2>
* <p>
* The calculator performs basic functionalities like addition, subtraction and multiplication.
* The results generated from the arithmetic equations are then stored in a mysql database through a connector.
* <p>
* @author Rashid Salim - 114161
* @author William Gayo - 091921
* @version 1.0
*/


import java.util.Stack;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.sql.*;
import java.util.*;

public class Main extends Application {
    /**
     * @param conn is the connection string
     * @param DB_URL is the Database URL Endpointr
     * @param USER is the username
     * @param PASSWORD is the user's authentication credentials
     */

    Connection conn = null;
    static String DB_URL = "jdbc:mysql://localhost:3306/Calculator";
    static String USER = "root";
    static String PASSWORD = "password";

    /**
     * The keyboard key values
     */

    @Override
    public static void Main(final String[] args) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            final Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
          System.out.println("Connection Sucessfull");
        } catch(final Exception e) {
            System.out.println("Error: Check your Connection String");
            System.exit(1);
        }
    }
    

    private static final String[][] key_values =
            {{"7", "8", "9", "/"}, {"4", "5", "6", "*"},
             {"1", "2", "3", "-"}, {"0", "c", "=", "+"}};

    private final Button btn[][] = new Button[4][4];
    /**
     * Buttons to all keys
     */
    TextField calculator_screen;
    /**
     * Calculator screen
     */

    boolean isEqualCalled = false;
    int flag = 0, repeat = 0;
    String exp;
    String temp;
    String sample = "0";
    String sample2 = "0";
    Double num1 = 0.0, num2 = 0.0, sum = 0.0;
    Double checkNum = 0.0;
    Double temp_sum = 0.0;
    Stack<String> stack = new Stack<>();
    Stack<String> stack_new = new Stack<>();


    public static void main(final String[] args) {
        launch(args);

    }

    @Override
    public void start(final Stage stage) {

        /** The outside layout
         * */
        final VBox layout = new VBox(30);
        /**
         * The size vertically
         */

        /** The inside layout for keys or buttons
         * */
        final TilePane keypad = new TilePane(); // even it is called keypad, it is a layout
        keypad.setVgap(7);
        keypad.setHgap(7);
        /**
         * Setting gaps between keys
         */

        /**Create Calculator Screen
         * */
        calculator_screen = new TextField();
        calculator_screen.setStyle("-fx-background-color: #FFFFFF;");
        /**
         * Setting the style of the screen
         */
        calculator_screen.setAlignment(Pos.CENTER_RIGHT);
        /**
         * Placing the screen in the center of the calculator
         */
        calculator_screen.setEditable(false);
        calculator_screen.setPrefWidth(700);
        /**
         * Setting the width of the screen
         */

        /** Create Calculator keyboard
         * */
        keypad.setPrefColumns(key_values[0].length);
        /**
         * Setting preferred number of columns.
         */

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                btn[i][j] = new Button(key_values[i][j]);
                final int a = i;
                final int b = j;

                /** Add button event
                 * */
                btn[i][j].setOnAction(new EventHandler<ActionEvent>() {

                                          @Override
                                          public void handle(final ActionEvent event) {
                                              if (isEqualCalled) {
                                                  calculator_screen.clear();
                                                  isEqualCalled = false;
                                              }
                                              calculator_screen.appendText(key_values[a][b]);
                                              exp = calculator_screen.getText().toString();

                                          }

                                      }

                );

                keypad.getChildren().add(btn[i][j]);
            }
        }

        btn[3][1].setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(final ActionEvent arg0) {

                // TODO Auto-generated method stub
                
                calculator_screen.setText("");
            }

        });

        // -------------When "=" button is pressed--------

        btn[3][2].setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(final ActionEvent arg0) {
                // TODO Auto-generated method stub
                // System.out.println("=============");
                // System.out.println("Expression = "+ exp);
                isEqualCalled = true;

                // --------------Pushing the elements to the stack-------------------
                exp = exp + "\n";
                final char[] ch = exp.toCharArray();
                final int len = ch.length;
                int i = 0;

                for (int j = 0; j < len; j++) {
                    if (ch[j] >= '0' && ch[j] <= '9') {

                        i = j;
                        sample = "0";
                        while (ch[i] >= '0' && ch[i] <= '9' && i < len) // To check if there is a more than 1 digit number

                        {
                            if (ch[i] >= '0' && ch[i] <= '9') {
                                System.out.println("Digit = " + ch[i]);
                                System.out.println("sample before = " + sample);
                                sample = sample + exp.charAt(i);
                                System.out.println("sample after = " + sample);
                                i++;
                            }
                        }
                        stack.push(sample);
                        j = i - 1;
                    } else {
                        System.out.println("Sign = " + ch[i]);

                        stack.push(Character.toString(ch[i]));
                    }
                }
                temp = stack.pop();
                final int size = stack.size();
                System.out.println("Size of stack = " + size);

                // -----------Reversing the order of the stack-------------

                while (!stack.isEmpty()) {
                    sample2 = stack.pop();
                    stack_new.push(sample2);
                }

                // -----------Evaluating the expression--------------------

                while (!stack_new.isEmpty()) {
                    System.out.println("--------");

                    temp = stack_new.peek();
                    System.out.println("Stack item = " + temp);
                    int type = checkString(temp);

                    if (type == 0) {
                        num1 = Double.parseDouble(temp);
                        stack_new.pop();


                    } else if (type == 5) {
                        System.out.println("Stack Empty");

                        flag = 2;
                        break;
                    } else {
                        final int op = checkString(temp);
                        stack_new.pop();

                        temp = stack_new.peek();
                        type = checkString(temp);
                        if (type != 0) {
                            System.out.println("Invalid");
                            flag = 2;
                        } else {

                            num2 = Double.parseDouble(temp);
                            if (op == 1) {
                                temp_sum = num1 + num2;
                                System.out.println("Sum = " + temp_sum);
                            } else if (op == 2) {
                                temp_sum = num1 - num2;
                                System.out.println("Diff = " + temp_sum);
                            } else if (op == 3) {
                                temp_sum = num1 * num2;
                                System.out.println("Product = " + temp_sum);
                            } else {
                                if (num2 != 0) {
                                    temp_sum = num1 / num2;
                                    System.out.println("Division = " + temp_sum);
                                } else {
                                    System.out.println("Cannot divide by 0");
                                    flag = 1;
                                }
                            }
                            num1 = temp_sum;
                        }
                        stack_new.pop();
                    }

                }
                System.out.println("result = " + temp_sum);
                if (flag == 0)
                    calculator_screen.setText(temp_sum.toString());
                else if (flag == 1) {
                    calculator_screen.setText("Error");
                    calculator_screen.setStyle("-fx-text-fill: red;");

                } else if (flag == 2) {
                    calculator_screen.setText("Invalid Expression");
                    calculator_screen.setStyle("-fx-text-fill: red;");

                }

            }

            public int checkString(final String temp) {
                // TODO Auto-generated method stub

                if (temp.length() == 1) {

                    final char ch = temp.charAt(0);
                    if (ch == '+')
                        return 1;
                    else if (ch == '-')
                        return 2;
                    else if (ch == '*')
                        return 3;
                    else if (ch == '/')
                        return 4;
                    else
                        return 5;

                } else
                    return 0;
            }

        });

        /** Put the calculator screen and keypad into a VBox layout */
        layout.setAlignment(Pos.CENTER);
        layout.getChildren().addAll(calculator_screen, keypad);
        calculator_screen.prefWidthProperty().bind(keypad.widthProperty());

        
        stage.setTitle("Calculator");
        stage.initStyle(StageStyle.UTILITY);
        stage.setResizable(false);
        final Scene scene = new Scene(layout);
        stage.setScene(scene);
        stage.show();
    }
    /** Static main method */
    public void handle() {
        launch();
    }
}