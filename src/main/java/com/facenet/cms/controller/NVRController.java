package com.facenet.cms.controller;

import com.facenet.cms.model.CameraFormPage;
import com.facenet.cms.model.CameraModel;
import com.facenet.cms.model.NVRFormPage;
import com.facenet.cms.model.NVRModel;
import com.facenet.cms.repository.NVRRepository;
import com.facenet.cms.searchform.CameraSearchForm;
import com.facenet.cms.searchform.NVRSearchForm;
import com.facenet.cms.service.NVRManualCreate;
import com.facenet.cms.service.NVRService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/nvr")
@Slf4j
public class NVRController {
    @Autowired
    private NVRService nvrService;


    @Autowired
    private NVRManualCreate nvrManualCreateService;

    @Autowired
    private NVRRepository nvrRepository;

    @PostMapping("/getAllNvr")
    public ResponseEntity<?> getAll(@RequestBody NVRFormPage<NVRSearchForm> nvrFormPage){
        try {
            Pageable pageable = PageRequest.of(nvrFormPage.getPageSize(), nvrFormPage.getPageNumber());
            Page<NVRModel> nvrList = nvrService.findAll(nvrFormPage.getSearchData(),pageable);
            return ResponseEntity.ok().body(nvrList);
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping("/update-nvr/{id}")
    public ResponseEntity<?> update(@RequestBody NVRModel nvrModel, @PathVariable String id){
        log.info("id nvr update: ", id);
        try {
            String mes = nvrService.updateInfoNVR(nvrModel);
            return ResponseEntity.ok().body(mes);
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("/delete-nvr/{id}")
    public ResponseEntity<?> delete(@PathVariable String id){
        log.info("Delete nvr with: ", id);
        try {
            String mes = nvrService.deleteByIdNvr(id);
            return ResponseEntity.ok().body(mes);
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/detail-nvr/{id}")
    public ResponseEntity<?> detail(@RequestBody CameraFormPage<CameraSearchForm> cameraFormPage, @PathVariable String id) {
        log.info("detail nvr with: ", id);
        try {
            Pageable pageable = PageRequest.of(cameraFormPage.getPageSize(), cameraFormPage.getPageNumber());
            Page<CameraModel> rs = nvrService.detailNvr(id, pageable, cameraFormPage.getSearchData());
            return ResponseEntity.ok().body(rs);
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/manual-create-nvr")
    public ResponseEntity<?> manualCreat(@RequestBody NVRModel nvrModel) {
        try {
            String result = nvrManualCreateService.NVRSManualCreate(nvrModel);
            return ResponseEntity.ok(result);
        } catch (Exception exception) {
            log.error(exception.getMessage());
            exception.printStackTrace();
            return ResponseEntity.internalServerError().body(exception.getMessage());
        }

    }
}
