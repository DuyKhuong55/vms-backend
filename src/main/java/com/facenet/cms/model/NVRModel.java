package com.facenet.cms.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Document(collection = "NVR")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class NVRModel {
    @Id
    private String id;

    @Field("nvrId")
    private String nvrId;

    @Field("nvrIdInDB")
    private String nvrIdInDB;

    @Field("nvrName")
    private String nvrName;

    @Field("nvrType")
    private String nvrType;

    @Field("nvrGroupDevice")
    private String groupDevice;

    @Field("hardwareVersion")
    private String hardwareVersion;

    @Field("softwareVersion")
    private String softwareVersion;

    @Field("ieClientVersion")
    private String ieClientVersion;

    @Field("videoFormat")
    private String videoFormat;

    @Field("hddVolume")
    private String hddVolume;

    @Field("nvrPositionSetup")
    private String nvrPositionSetup;

    @Field("nvrLocation")
    private String nvrLocation;

    @Field("ipAddress")
    private String ipAddress;

    @Field("ipv6Address")
    private String ipv6Address;

    @Field("web")
    private String web;

    @Field("client")
    private String client;

    @Field("macAddress")
    private  String macAddress;

    @Field("p2pId")
    private String p2pId;

    @Field("networkState")
    private String networkState;

    @Field("username")
    private String username;

    @Field("password")
    private String password;

    @Field("nvrTypeDevice")
    private String nvrTypeDevice;

    @Field("nvrSite")
    private String nvrSite;

    @Field("createNvrTime")
    private String createNvrTime;

    private List<CameraModel> listCamera;


}
