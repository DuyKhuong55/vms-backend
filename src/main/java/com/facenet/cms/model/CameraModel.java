package com.facenet.cms.model;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "CAM")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CameraModel {
    @Field("camId")
    private String camId;

    @Field("camIdInDB")
    private String camIdInDB;

    @Field("camName")
    private String camName;

    @Field("hardVersion")
    private String hardVersion;

    @Field("softVersion")
    private String softVersion;

    @Field("ieClientVersion")
    private String ieClientVersion;

    @Field("macAddress")
    private String macAddress;

    @Field("ipAddress")
    private String ipAddress;

    @Field("p2pId")
    private String p2pId;

    @Field("camSite")
    private String camSite;

    @Field("nvrIDInDB")
    private String nvrIDInDB;

    @Field("camType")
    private String camType;

    @Field("state")
    private String state;

    @Field("mainUrl")
    private String mainUrl;

    @Field("subUrl")
    private String subUrl;

    @Field("port")
    private String port;

    @Field("channelNum")
    private Integer channelNum;

    @Field("channelIndex")
    private Integer channelIndex;

    @Field("protocol")
    private String protocol;

    @Field("connectMethod")
    private String connectMethod;

    @Field("username")
    private String username;

    @Field("password")
    private String password;

    @Field("passwordEmpty")
    private Boolean passwordEmpty;

    @Field("manufacturer")
    private String manufacturer;

    @Field("deviceType")
    private String deviceType;

    @Field("camLocation")
    private String camLocation;

    @Field("createCamTime")
    private String createCamTime;
}
