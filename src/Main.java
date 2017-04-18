
import com.jtattoo.plaf.smart.SmartLookAndFeel;
import gui.Login;

import javax.swing.*;
import java.util.Properties;

public class Main {

    public static void main(String[] args) {
        try {
            Properties props = new Properties();

            String darkBlue = "71 166 198";
            String lightBlue = "150 231 247";
            String black = "0 0 0";
            String white = "255 255 255";
            String grey = "244 242 232";



            props.put("logoString", "LGIM");
            props.put("licenseKey", "LICENSE KEY");

            props.put("selectionBackgroundColor", lightBlue);
            props.put("menuSelectionBackgroundColor", lightBlue);

            props.put("controlColor", lightBlue);
            props.put("controlColorLight", grey);
            props.put("controlColorDark", grey);

            props.put("buttonColor", lightBlue);
            props.put("buttonColorLight", white);
            props.put("buttonColorDark", grey);

            props.put("rolloverColor", lightBlue);
            props.put("rolloverColorLight", grey);
            props.put("rolloverColorDark", lightBlue);

            props.put("windowTitleForegroundColor", white);
            props.put("windowTitleBackgroundColor", black);
            props.put("windowTitleColorLight", lightBlue);
            props.put("windowTitleColorDark", darkBlue);
            props.put("windowBorderColor", darkBlue);

            // set your theme
            SmartLookAndFeel.setCurrentTheme(props);
            // select the Look and Feel
            UIManager.setLookAndFeel("com.jtattoo.plaf.smart.SmartLookAndFeel");


        } catch (UnsupportedLookAndFeelException | IllegalAccessException | ClassNotFoundException | InstantiationException e) {
            e.printStackTrace();
        }
        Login.launchLoginGui();
    }
}
