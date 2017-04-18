package listeners;

import gui.Login;
import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 *  This listener switches to the registration page using the openRegistrationPage method.
 */
public final class RegistrationPageButtonListener extends AbstractAction {
    @Override
    public void actionPerformed(ActionEvent e) {
        Login.openRegistrationPage();
    }
}
