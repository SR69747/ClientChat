package listeners;

import networking.Sender;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class TextFieldKeyListener implements KeyListener {

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        System.out.println("Pressed " + e.getKeyCode());
        if (e.getKeyCode() == 10) {
            Sender.sendMessageToServer();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
