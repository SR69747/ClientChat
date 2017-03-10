package listeners;

import gui.Login;
import networking.Sender;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Created by SR69747 on 24/02/2017.
 */
public final class SettingsSignOutListener extends AbstractAction {
    @Override
    public void actionPerformed(ActionEvent e) {
        Sender.sendMessageToServer("/exit");
        Login.enableLoginFrame();
    }
}
