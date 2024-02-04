package TokoHp.Objects;

import raven.toast.Notifications;

public class Toast {

    public static void toastSuccess(String message) {
        Notifications.getInstance().show(Notifications.Type.SUCCESS, Notifications.Location.TOP_RIGHT, message);
    }

    public static void toastFailed(String message) {
        Notifications.getInstance().show(Notifications.Type.ERROR, Notifications.Location.TOP_RIGHT, message);
    }

    public static void toastInfo(String message) {
        Notifications.getInstance().show(Notifications.Type.INFO, Notifications.Location.TOP_RIGHT, message);
    }

}
