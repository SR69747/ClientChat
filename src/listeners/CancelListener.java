package listeners;

import gui.Login;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 *  This listener switches back to the login page from registration page
 */
public class CancelListener extends AbstractAction {

    @Override
    public void actionPerformed(ActionEvent e) {
        Login.openLoginPage();
    }
}
