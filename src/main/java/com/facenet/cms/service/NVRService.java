package com.facenet.cms.service;

import com.facenet.cms.model.CameraModel;
import com.facenet.cms.model.NVRModel;
import com.facenet.cms.searchform.CameraSearchForm;
import com.facenet.cms.searchform.NVRSearchForm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

public interface NVRService {
    String updateInfoNVR(NVRModel nvrModel);

    Page<NVRModel> findAll(NVRSearchForm nvrSearchForm, Pageable pageable);

    String deleteByIdNvr(String id);

    Page<CameraModel> detailNvr(String id, Pageable pageable, CameraSearchForm cameraSearchForm);
}
