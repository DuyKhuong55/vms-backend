package com.facenet.cms.searchform;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NVRSearchForm {
    private String nvrName;
    private String nvrLocation;
    private String networkState;
    private String ipAddress;
    private String macAddress;
    private String site;
}
