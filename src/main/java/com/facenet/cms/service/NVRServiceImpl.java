package com.facenet.cms.service;

import com.facenet.cms.model.CameraModel;
import com.facenet.cms.model.NVRModel;
import com.facenet.cms.repository.CameraRepository;
import com.facenet.cms.repository.NVRRepository;
import com.facenet.cms.searchform.CameraSearchForm;
import com.facenet.cms.searchform.NVRSearchForm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class NVRServiceImpl implements NVRService {

    @Autowired
    private NVRRepository nvrRepository;

    @Autowired
    private CameraRepository cameraRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public String updateInfoNVR(NVRModel nvrModelNew) {
        log.info("----------Start update info NVR---------");
        NVRModel nvrModel = nvrRepository.findById(nvrModelNew.getId()).orElse(null);
        if(nvrModel == null) {
            log.info("nvr empty!");
            return "nvr empty!";
        } else {
            nvrModel.setNvrName(nvrModelNew.getNvrName());
            nvrModel.setNvrLocation(nvrModelNew.getNvrLocation());
            nvrModel.setNvrPositionSetup(nvrModelNew.getNvrPositionSetup());
            nvrModel.setIpAddress(nvrModelNew.getIpAddress());
            nvrModel.setWeb(nvrModelNew.getWeb());
            nvrModel.setNvrSite(nvrModelNew.getNvrSite());
            nvrRepository.save(nvrModel);
            return "update infomation NVR successfully!";
        }
    }

    public Page<NVRModel> findAll(NVRSearchForm nvrSearchForm, Pageable pageable){
        log.info("------Start get all nvr with pageable");
        var query = new Query().with(pageable);
        final List<Criteria> criteria = new ArrayList<>();
        if(nvrSearchForm.getNvrName() == null &&
                nvrSearchForm.getNvrLocation() == null &&
                nvrSearchForm.getNetworkState() == null &&
                nvrSearchForm.getIpAddress() == null &&
                nvrSearchForm.getMacAddress() == null
        ) {
            Page<NVRModel> nvrModelList = nvrRepository.findAll(pageable);
            nvrModelList.forEach((item) -> {
                List<CameraModel> listCamera = cameraRepository.findAllByNvrId(item.getNvrIdInDB());
                listCamera.forEach((rs) -> {
                    System.out.println(rs);
                });
                item.setListCamera(listCamera);
            });
            return nvrModelList;
        } else{
            if(nvrSearchForm.getNvrName() != null && !nvrSearchForm.getNvrName().isBlank()) {
                criteria.add(Criteria.where("nvrName").regex(nvrSearchForm.getNvrName(), "i"));
            }
            if(nvrSearchForm.getNvrLocation() != null && !nvrSearchForm.getNvrLocation().isBlank()) {
                criteria.add(Criteria.where("nvrLocation").regex(nvrSearchForm.getNvrLocation(), "i"));
            }
            if(nvrSearchForm.getNetworkState() != null && !nvrSearchForm.getNetworkState().isBlank()) {
                criteria.add(Criteria.where("networkState").is(nvrSearchForm.getNetworkState()));
            }
            if(nvrSearchForm.getIpAddress() != null && !nvrSearchForm.getIpAddress().isBlank()) {
                criteria.add(Criteria.where("ipAddress").regex(nvrSearchForm.getIpAddress(), "i"));
            }
            if(nvrSearchForm.getMacAddress() != null && !nvrSearchForm.getMacAddress().isBlank()) {
                criteria.add(Criteria.where("macAddress").regex(nvrSearchForm.getMacAddress(), "i"));
            }
            if(nvrSearchForm.getSite() != null && !nvrSearchForm.getSite().isBlank()) {
                criteria.add(Criteria.where("nvrSite").regex(nvrSearchForm.getSite(), "i"));
            }

            query.addCriteria(new Criteria().andOperator(criteria.toArray(new Criteria[0])));
            Page<NVRModel> page = PageableExecutionUtils.getPage(
                    mongoTemplate.find(query, NVRModel.class),
                    pageable,
                    () -> mongoTemplate.count(query.skip(0).limit(0), NVRModel.class)
            );
            System.out.println("------------------------");
            page.forEach((item) -> {
                List<CameraModel> listCamera = cameraRepository.findAllByNvrId(item.getNvrIdInDB());
                listCamera.forEach((rs) -> {
                    System.out.println(rs);
                });
                item.setListCamera(listCamera);
            });
            return page;
        }
    }

    public String deleteByIdNvr(String id) {
        String mes = "";
        try {
            Optional<NVRModel> nvrInfo = nvrRepository.findById(id);
            System.out.println("-------------NVR ID--------------");
            String nvrIDInDB = nvrInfo.get().getNvrIdInDB();
            System.out.println(nvrIDInDB);
            System.out.println("-------------ID--------------");
            System.out.println(nvrInfo.get().getId());
            List<CameraModel> listCamera = cameraRepository.findAllByNvrId(nvrIDInDB);
            System.out.println("-------list camera: " + listCamera.size());
            if(listCamera.size() > 0) {
                cameraRepository.deleteAllByNvrId(nvrIDInDB);
                nvrRepository.deleteById(nvrInfo.get().getId());
                mes = "Da xoa NVR va cac thong tin lien quan!";
            } else {
                nvrRepository.deleteById(nvrInfo.get().getId());
                mes = "Da xoa NVR!";
            }
        } catch (Exception e) {
            mes = "NVR khong ton tai, thao tac xoa that bai!";
            e.printStackTrace();
            log.error(e.getMessage());
        }
        return mes;
    }

    public Page<CameraModel> detailNvr(String id, Pageable pageable, CameraSearchForm cameraSearchForm) {
        Optional<NVRModel> nvrInfo = nvrRepository.findById(id);
        NVRModel nvrModel = nvrInfo.get();
        var query = new Query().with(pageable);
        final List<Criteria> criteria = new ArrayList<>();
        criteria.add(Criteria.where("nvrIDInDB").regex(nvrModel.getNvrIdInDB(), "i"));
        if (cameraSearchForm.getCamName() == null &&
                cameraSearchForm.getCamLocation() == null &&
                cameraSearchForm.getIpAddress() == null &&
                cameraSearchForm.getState() == null &&
                cameraSearchForm.getPort() == null &&
                cameraSearchForm.getMacAddress() == null
        ) {
            query.addCriteria(new Criteria().andOperator(criteria.toArray(new Criteria[0])));
            Page<CameraModel> page = PageableExecutionUtils.getPage(
                    mongoTemplate.find(query, CameraModel.class),
                    pageable,
                    () -> mongoTemplate.count(query.skip(0).limit(0), CameraModel.class)
            );
            return page;
        } else {
            if (cameraSearchForm.getCamName() != null && !cameraSearchForm.getCamName().isBlank()) {
                criteria.add(Criteria.where("camName").regex(cameraSearchForm.getCamName(), "i"));
            }
            if (cameraSearchForm.getCamLocation() != null && !cameraSearchForm.getCamLocation().isBlank()) {
                criteria.add(Criteria.where("camLocation").regex(cameraSearchForm.getCamLocation(), "i"));
            }
            if (cameraSearchForm.getState() != null && !cameraSearchForm.getState().isBlank()) {
                criteria.add(Criteria.where("state").regex(cameraSearchForm.getState(), "i"));
            }
            if (cameraSearchForm.getIpAddress() != null && !cameraSearchForm.getIpAddress().isBlank()) {
                criteria.add(Criteria.where("ipAddress").regex(cameraSearchForm.getIpAddress(), "i"));
            }
            if (cameraSearchForm.getPort() != null) {
                criteria.add(Criteria.where("port").regex(cameraSearchForm.getPort(), "i"));
            }
            if (cameraSearchForm.getMacAddress() != null && !cameraSearchForm.getMacAddress().isBlank()) {
                criteria.add(Criteria.where("macAddress").regex(cameraSearchForm.getMacAddress(), "i"));
            }

            query.addCriteria(new Criteria().andOperator(criteria.toArray(new Criteria[0])));
            Page<CameraModel> page = PageableExecutionUtils.getPage(
                    mongoTemplate.find(query, CameraModel.class),
                    pageable,
                    () -> mongoTemplate.count(query.skip(0).limit(0), CameraModel.class)
            );
            return page;
        }
    }
}
