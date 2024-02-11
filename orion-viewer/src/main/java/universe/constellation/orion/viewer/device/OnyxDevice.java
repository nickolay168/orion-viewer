package universe.constellation.orion.viewer.device;

import android.view.View;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static universe.constellation.orion.viewer.LoggerKt.log;

/**
 * Created by mike on 9/9/14.
 */
public class OnyxDevice extends EInkDevice {

    private static Object fastUpdateEntry;
    private static Object fullUpdateEntry;

    private static Method invalidate;

    private static final boolean successful;

    static {
        boolean isSuccessful = false;
        try {
            Class<?> epdControllerClass = Class
                    .forName("com.onyx.android.sdk.device.EpdController");

            Class<?> epdControllerModeClass = Class
                    .forName("com.onyx.android.sdk.device.EpdController$UpdateMode");

            invalidate = epdControllerClass.getDeclaredMethod("invalidate", android.view.View.class, epdControllerModeClass);

            if (epdControllerModeClass.isEnum()) {
                Object[] updateModeEnum = epdControllerModeClass.getEnumConstants();
                for (Object entry : updateModeEnum) {
                    if ("GU".equals(entry.toString())) {
                        fastUpdateEntry = entry;
                    }

                    if ("GC".equals(entry.toString())) {
                        fullUpdateEntry = entry;
                    }
                }
                log("Fast update entry " + fastUpdateEntry);
                log("Full update entry " + fullUpdateEntry);
            }

            isSuccessful = fastUpdateEntry != null && fullUpdateEntry != null;
        } catch (Exception e) {
            log(e);
        } finally {
            successful = isSuccessful;
        }
    }

    @Override
    public void doFullUpdate(View view) {
        if (successful) {
            log("Do full update " + fullUpdateEntry);
            doUpdate(view, fullUpdateEntry);
        } else {
            super.doFullUpdate(view);
        }
    }

    @Override
    public void doPartialUpdate(View view) {
        if (successful) {
            log("Do partial update " + fastUpdateEntry);
            doUpdate(view, fastUpdateEntry);
        } else {
            super.doPartialUpdate(view);
        }
    }

    private void doUpdate(View view, Object fastUpdateEntry) {
        try {
            invalidate.invoke(null, view, fastUpdateEntry);
        } catch (IllegalAccessException e) {
            log(e);
            super.doPartialUpdate(view);
        } catch (InvocationTargetException e) {
            log(e);
            super.doPartialUpdate(view);
        }
    }


}
