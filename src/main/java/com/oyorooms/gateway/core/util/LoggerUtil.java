package com.oyorooms.gateway.core.util;

import com.oyo.platform.logger.Level;
import com.oyo.platform.logger.Logger;
import com.oyorooms.service.ordering.enums.LogAction;
import com.oyorooms.gateway.helper.ServiceConstants;
import org.json.JSONObject;
import java.util.Objects;

public class LoggerUtil {

    private LoggerUtil() {}

    private static final Logger logger = new Logger(ServiceConstants.SERVICE_NAME);

    /**
     * 2 Kinds of syntax supported -
     * a. Plain normal string - "This is a sample log"
     * b. Json Object format string - "{bookingId : 32, userProfileId : 1}"
     *
     * @param data
     */
    public static void info(String data) {
        JSONObject jsonData = null;
        if (Objects.nonNull(data) && !data.startsWith("{")) {
            jsonData = new JSONObject();
            jsonData.put(ServiceConstants.INFO_LOGGING_LABEL, data);
        } else {
            jsonData = new JSONObject(data);
        }
        commonLogHelper(Level.INFO, jsonData, LogAction.INFO, null);
    }

    public static void error(String data) {
        JSONObject jsonData = null;
        if (Objects.nonNull(data) && !data.startsWith("{")) {
            jsonData = new JSONObject();
            jsonData.put(ServiceConstants.ERROR_LOGGING_LABEL, data);
        } else {
            jsonData = new JSONObject(data);
        }
        commonLogHelper(Level.ERROR, jsonData, LogAction.ERROR, null);
    }

    public static void error(String data, Exception e) {
        JSONObject jsonData = null;
        if (Objects.nonNull(data) && !data.startsWith("{")) {
            jsonData = new JSONObject();
            jsonData.put(ServiceConstants.ERROR_LOGGING_LABEL, data);
        } else {
            jsonData = new JSONObject(data);
        }
        jsonData.put(ServiceConstants.ERROR_MESSAGE_KEY, e.getMessage());
        commonLogHelper(Level.ERROR, jsonData, LogAction.ERROR, null);
    }

    public static void error(LogAction logAction, String data, Exception e) {
        JSONObject jsonData = null;
        if (Objects.nonNull(data) && !data.startsWith("{")) {
            jsonData = new JSONObject();
            jsonData.put(ServiceConstants.ERROR_LOGGING_LABEL, data);
        } else {
            jsonData = new JSONObject(data);
        }
        jsonData.put(ServiceConstants.ERROR_MESSAGE_KEY, e.getMessage());
        jsonData.put(ServiceConstants.STACKTRACE_KEY, e.getStackTrace());
        commonLogHelper(Level.ERROR, jsonData, logAction, null);
    }


    public static void error(LogAction logAction, String data) {
        JSONObject jsonData = null;
        if (Objects.nonNull(data) && !data.startsWith("{")) {
            jsonData = new JSONObject();
            jsonData.put(ServiceConstants.ERROR_LOGGING_LABEL, data);
        } else {
            jsonData = new JSONObject(data);
        }
        commonLogHelper(Level.ERROR, jsonData, logAction, null);
    }

    private static void commonLogHelper(Level level, JSONObject jsonObject, LogAction logAction, String customMsg) {
        String threadName = Thread.currentThread().getId() + " " + Thread.currentThread().getName();
        jsonObject.put(ServiceConstants.THREAD_NAME_KEY, threadName);
        jsonObject.put(ServiceConstants.LOGGING_ACTION, logAction);
        jsonObject.put(ServiceConstants.CUSTOM_MESSAGE_KEY, customMsg);
        logger.log(level, null, jsonObject);
    }

}
