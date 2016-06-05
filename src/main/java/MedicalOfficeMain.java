import controller.MenuController;
import model.MainModel;
import view.MenuAndMainView;

import java.awt.*;

import javax.swing.JFrame;

/**
 * Small Medical Office Management software
 *
 * @author Pawel Raczy
 */

public class MedicalOfficeMain {

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                MenuAndMainView theView = new MenuAndMainView();
                MainModel theModel = new MainModel();
                MenuController theController = new MenuController(theView, theModel);
                theView.setVisible(true);
                theView.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            }
        });
    }
}
