
import com.jtattoo.plaf.smart.SmartLookAndFeel;
import gui.Login;

import javax.swing.*;
import java.util.Properties;

public class Main {

    public static void main(String[] args) {
        try {
            Properties props = new Properties();

            props.put("logoString", "LGIM");
            props.put("licenseKey", "LICENSE KEY");

            props.put("selectionBackgroundColor", "180 240 197");
            props.put("menuSelectionBackgroundColor", "180 240 197");

            props.put("controlColor", "218 254 230");
            props.put("controlColorLight", "218 254 230");
            props.put("controlColorDark", "180 240 197");

            props.put("buttonColor", "218 230 254");
            props.put("buttonColorLight", "255 255 255");
            props.put("buttonColorDark", "244 242 232");

            props.put("rolloverColor", "218 254 230");
            props.put("rolloverColorLight", "218 254 230");
            props.put("rolloverColorDark", "180 240 197");

            props.put("windowTitleForegroundColor", "0 0 0");
            props.put("windowTitleBackgroundColor", "180 240 197");
            props.put("windowTitleColorLight", "218 254 230");
            props.put("windowTitleColorDark", "180 240 197");
            props.put("windowBorderColor", "218 254 230");

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
