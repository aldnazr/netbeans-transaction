package TokoHp.Objects;

import raven.alerts.MessageAlerts;

public class PopUp {

//    PopUp Error
    public static void errorMessage(String title, String message) {
        MessageAlerts.getInstance().showMessage(title, message, MessageAlerts.MessageType.ERROR);
    }

//    PopUp Sukses
    public static void successMessage(String title, String message) {
        MessageAlerts.getInstance().showMessage(title, message, MessageAlerts.MessageType.SUCCESS);
    }
}
