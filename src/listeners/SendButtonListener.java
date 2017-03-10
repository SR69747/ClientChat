package listeners;

import networking.Sender;

import javax.swing.*;
import java.awt.event.ActionEvent;

public final class SendButtonListener extends AbstractAction {

    @Override
    public void actionPerformed(ActionEvent e) {
        Sender.sendMessageToServer();
    }
}
