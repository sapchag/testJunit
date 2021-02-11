package spec;

import org.slf4j.Logger;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

import org.apache.commons.validator.routines.EmailValidator;
import org.slf4j.LoggerFactory;

/**
 * Класс для проверки доступности ссылок URL и адресов email
 */
public class UrlChecks {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private String message;
    private String url;
    private boolean checkOk;
    private String proxyLogs;

    public UrlChecks(String url) {
        this.url = url;
        message = url;
        checkOk = false;
        if (isValidUrl()) {
            checkResponse();
        }
    }

    /**
     * Проверка синтаксиса сслыки URL
     * @return boolean
     */
    boolean isValidUrl() {
        try {
            new URL(url).toURI();
            return true;
        } catch (MalformedURLException | URISyntaxException e) {
            message = e.getMessage();
            checkOk = false;
        }
        return false;
    }

    /**
     * Отправлеят заголовок HTTP запроса и анализирует ответ
     * результат анализа пишет в checkOk
     * ошибки или неподходящие ответы пишет в message
     */
    public void checkResponse() {
        HttpURLConnection huc = null;
        int respCode = 200;

        try {
            huc = (HttpURLConnection) (new URL(url).openConnection());
            huc.setRequestMethod("HEAD");
            huc.connect();
            respCode = huc.getResponseCode();
            if (respCode == 404 || respCode == 500) {
                message = String.valueOf(respCode);
            } else {
                checkOk = true;
            }

        } catch (IOException | ClassCastException | IllegalArgumentException e) {
            if (e.getMessage().contains("MailToURLConnection")) {
                checkOk = isValidEmail(url.replace("mailto:", ""));
                message = "email";
            } else {
                message = e.getMessage();
                logger.error(message);
            }
        }

        proxyLogs = url + " " + respCode;
    }

    /**
     * Проверка доступности email адреса
     */
    public boolean isValidEmail(String email) {

        EmailValidator validator = EmailValidator.getInstance();
        return validator.isValid(email);
    }

    public boolean isCheckOk() {
        return checkOk;
    }

    public String getMessage() {
        return message;
    }

    public String getProxyLogs() {
        return proxyLogs;
    }
}
