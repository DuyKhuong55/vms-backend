package com.facenet.cms.service;

import com.facenet.cms.model.NVRModel;
import com.facenet.cms.model.CameraModel;
import com.facenet.cms.repository.CameraRepository;
import com.facenet.cms.repository.NVRRepository;
import lombok.extern.slf4j.Slf4j;
import org.json.*;
import org.openqa.selenium.*;
import org.springframework.http.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.sql.Timestamp;
import java.util.*;


@Service
@Transactional
@Slf4j
public class NVRManualCreateImpl implements NVRManualCreate {

    @Autowired
    private NVRRepository nvrRepository;

    @Autowired
    private CameraRepository camRepository;

    @Override
    public String NVRSManualCreate(NVRModel nvrModel) {
        String s ="http://" + nvrModel.getIpAddress();
        String username = nvrModel.getUsername();
        String password = nvrModel.getPassword();
        RestTemplate restTemplate = new RestTemplate();
        NVRModel nvrModel1 = new NVRModel();
        System.setProperty("webdriver.chrome.driver",
               "lib/Chromedriver/chromedriver.exe");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--headless");
        WebDriver driver = new ChromeDriver(options);
        log.info("đăng nhập NVR");
        driver.get(s);
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        List<WebElement> elementsInput = driver.findElements(By.className("el-input__inner"));
            elementsInput.forEach((item) -> {
                if(elementsInput.indexOf(item) == 1) {
                    log.info("nhập tk");
                    item.sendKeys(username);
                } else if (elementsInput.indexOf(item) == 2) {
                    log.info("nhập mk");
                    item.sendKeys(password);
                }
            });
            WebElement submitButton = driver.findElement(By.className("el-button"));
            submitButton.click();
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // Get cookie
        log.info("lấy cookie nvr");
        String cookie = driver.manage().getCookieNamed("session").getValue();
        // Get token
        JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
        String token = (String) ((JavascriptExecutor) driver).executeScript("return sessionStorage.getItem('X-csrftoken');");
        //thêm NVR vào db

            //sinh id in db
        Random random1 = new Random();
        int randomNvr = random1.nextInt(1000000000);
        HttpHeaders headersNVRinfo = new HttpHeaders();
        headersNVRinfo.set("Accept", "application/json; charset=utf-8");
        headersNVRinfo.set("Accept-Language", "vi,vi-VN;q=0.9,en-US;q=0.8,en;q=0.7");
        headersNVRinfo.set("Authorization", "Basic Og==");
        headersNVRinfo.set("Connection", "keep-alive");
        headersNVRinfo.set("Content-Type", "application/json");
        headersNVRinfo.set("Cookie","session=" + cookie);
        headersNVRinfo.set("DNT", "1");
        headersNVRinfo.set("Origin", s);
        headersNVRinfo.set("Referer", s);
        headersNVRinfo.set("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/114.0.0.0 Safari/537.36");
        headersNVRinfo.set("X-csrftoken", token);
        HttpEntity<String> requestEntityNVRInfo = new HttpEntity<>(headersNVRinfo);
        log.info("lấy NVRinfo");
        ResponseEntity<String> responseEntityNVRinfo = restTemplate.exchange(s +"/API/SystemInfo/Base/Get",HttpMethod.POST,requestEntityNVRInfo, String.class);
        String responseBodyNVRinfo = responseEntityNVRinfo.getBody();
        JSONObject dataNvr = new JSONObject(responseBodyNVRinfo).getJSONObject("data");
        String ipnvr = dataNvr.getString("device_id");
        nvrModel1.setNvrId(dataNvr.getString("device_id"));
        nvrModel1.setNvrName(nvrModel.getNvrName());
        nvrModel1.setNvrLocation(nvrModel.getNvrLocation());
        nvrModel1.setMacAddress(nvrModel.getMacAddress());
        nvrModel1.setNvrPositionSetup(nvrModel.getNvrPositionSetup());
        nvrModel1.setGroupDevice(nvrModel.getGroupDevice());
        nvrModel1.setWeb(nvrModel.getWeb());
        nvrModel1.setIpAddress(nvrModel.getIpAddress());
        nvrModel1.setNvrTypeDevice(nvrModel.getNvrTypeDevice());
        nvrModel1.setNvrSite(nvrModel.getNvrSite());
        nvrModel1.setNvrType(dataNvr.getString("device_type"));
        nvrModel1.setHardwareVersion(dataNvr.getString("hardware_version"));
        nvrModel1.setSoftwareVersion(dataNvr.getString("software_version"));
        nvrModel1.setIeClientVersion(dataNvr.getString("ie_client_version"));
        nvrModel1.setVideoFormat(dataNvr.getString("video_format"));
        nvrModel1.setHddVolume(dataNvr.getString("hdd_volume"));
        nvrModel1.setIpv6Address(dataNvr.getString("ipv6_address"));
        nvrModel1.setP2pId(dataNvr.getString("p2p_id"));
        nvrModel1.setNetworkState(dataNvr.getString("network_state"));
        nvrModel1.setNvrIdInDB("."+ randomNvr);
        nvrModel1.setClient(" ");
        nvrModel1.setUsername(username);
        nvrModel1.setPassword(password);
              Timestamp now = new Timestamp(System.currentTimeMillis());
        nvrModel1.setCreateNvrTime(now.toString());
        nvrRepository.save(nvrModel1);
        driver.close();

        //lay ds cam tư nvr
        HttpHeaders headersChannelList = new HttpHeaders();
        headersChannelList.set("Accept", "application/json; charset=utf-8");
        headersChannelList.set("Accept-Language", "vi,vi-VN;q=0.9,en-US;q=0.8,en;q=0.7");
        headersChannelList.set("Authorization", "Basic Og==");
        headersChannelList.set("Connection", "keep-alive");
        headersChannelList.set("Content-Type", "application/json");
        headersChannelList.set("Cookie","session=" + cookie);
        headersChannelList.set("DNT", "1");
        headersChannelList.set("Origin", s );
        headersChannelList.set("Referer", s );
        headersChannelList.set("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/114.0.0.0 Safari/537.36");
        headersChannelList.set("X-csrftoken", token);
        HttpEntity<String> requestEntityChannelList = new HttpEntity<>(headersChannelList);
        log.info("lấy danh sách kênh");
        ResponseEntity<String > responseEntityChannelList = restTemplate.exchange(s + "/API/ChannelConfig/IPChannel/Get", HttpMethod.POST, requestEntityChannelList, String.class);
        String responseBodyChannelList = responseEntityChannelList.getBody();

        //lấy info từng cam
        String url;
        int i = 1;
        JSONObject data = new JSONObject(responseBodyChannelList).getJSONObject("data").getJSONObject("channel_info").getJSONObject("CH1" );
        log.info(data.toString());
        try {
            while (data.getString("state")  != "NotConfigured") {
                //Login vào cam
                data = new JSONObject(responseBodyChannelList).getJSONObject("data").getJSONObject("channel_info").getJSONObject("CH" + i);
                url = "http://" + data.getString("ip_address");
                WebDriver driverCam = new ChromeDriver(options);
                log.info("login cam " + i);
                driverCam.get(url);
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                List<WebElement> elementsInputCam = driverCam.findElements(By.className("el-input__inner"));
                elementsInputCam.forEach((item) -> {
                    if(elementsInputCam.indexOf(item) == 1) {
                        item.sendKeys(username);
                    } else if (elementsInputCam.indexOf(item) == 2) {
                        item.sendKeys(password);
                    }
                });
                WebElement submitButtonCam = driverCam.findElement(By.className("el-button"));
                submitButtonCam.click();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                // sinh id in db
                Random random2 = new Random();
                int randomCam = random2.nextInt(1000000000);
                String cookieCam = driverCam.manage().getCookieNamed("session").getValue();
                JavascriptExecutor jsExecutorCam = (JavascriptExecutor) driverCam;
                String tokenCam = (String) ((JavascriptExecutor) driverCam).executeScript("return sessionStorage.getItem('X-csrftoken');");
                HttpHeaders headersCaminfo = new HttpHeaders();
                headersCaminfo.set("Accept", "application/json; charset=utf-8");
                headersCaminfo.set("Accept-Language", "vi,vi-VN;q=0.9,en-US;q=0.8,en;q=0.7");headersChannelList.set("Authorization", "Basic Og==");
                headersCaminfo.set("Connection", "keep-alive");
                headersCaminfo.set("Content-Type", "application/json");
                headersCaminfo.set("Cookie","session=" + cookieCam);
                headersCaminfo.set("DNT", "1");
                headersCaminfo.set("Origin",url );
                headersCaminfo.set("Referer",url );
                headersCaminfo.set("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/114.0.0.0 Safari/537.36");
                headersCaminfo.set("X-csrftoken", tokenCam);
                HttpEntity<String> requestEntityCaminfo = new HttpEntity<>(headersCaminfo);
                log.info("lấy caminfo");
                ResponseEntity<String > responseEntityCamInfo = restTemplate.exchange(url +"/API/SystemInfo/Base/Get", HttpMethod.POST, requestEntityCaminfo, String.class);
                String responseBodyCamInfo = responseEntityCamInfo.getBody();
                JSONObject dataCam = new JSONObject(responseBodyCamInfo).getJSONObject("data");
                CameraModel camModel1 = new CameraModel();
                camModel1.setNvrIDInDB(ipnvr);
                camModel1.setIpAddress(data.getString("ip_address"));
                camModel1.setCamId(dataCam.getString("device_id"));
                camModel1.setCamName(dataCam.getString("device_name"));
                camModel1.setCamType(dataCam.getString("device_type"));
                camModel1.setHardVersion(dataCam.getString("hardware_version"));
                camModel1.setSoftVersion(dataCam.getString("software_version"));
                camModel1.setIeClientVersion(dataCam.getString("ie_client_version"));
                camModel1.setMacAddress(dataCam.getString("mac_address"));
                camModel1.setP2pId(dataCam.getString("p2p_id"));
                camModel1.setCamIdInDB("." + randomCam);
                camModel1.setNvrIDInDB("." + randomNvr);
                camModel1.setState(data.getString("state"));
                camModel1.setMainUrl(data.getString("main_url"));
                camModel1.setSubUrl(data.getString("sub_url"));
                camModel1.setPort(Integer.toString(data.getInt("port")));
                camModel1.setChannelNum(data.getInt("channel_num"));
                camModel1.setChannelIndex(data.getInt("channel_index"));
                camModel1.setProtocol(data.getString("protocol"));
                camModel1.setConnectMethod(data.getString("connect_method"));
                camModel1.setUsername(data.getString("username"));
                camModel1.setPassword(data.getString("password"));
                camModel1.setPasswordEmpty(data.getBoolean("password_empty"));
                camModel1.setManufacturer(data.getString("manufacturer"));
                camModel1.setDeviceType(data.getString("device_type"));
                camModel1.setCamSite(" ");
                camModel1.setCamLocation(" ");
                   now = new Timestamp(System.currentTimeMillis());
                camModel1.setCreateCamTime(now.toString());
                camRepository.save(camModel1);
                i++;
                driverCam.close();
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }

        return null;
    }
}
