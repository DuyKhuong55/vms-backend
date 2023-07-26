package com.facenet.cms.searchform;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CameraSearchForm {
    private String camName;
    private String camLocation;
    private String state;
    private String ipAddress;
    private  String port;
    private  String macAddress;
}
