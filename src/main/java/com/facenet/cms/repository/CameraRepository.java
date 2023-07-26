package com.facenet.cms.repository;

import com.facenet.cms.model.CameraModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CameraRepository extends MongoRepository<CameraModel, String> {
    @Query(value = "{'nvrIDInDB': ?0}")
    List<CameraModel> findAllByNvrId(String nvrIDInDB);

    @Query(value = "{'nvrIDInDB': ?0}", delete = true)
    void deleteAllByNvrId(String nvrIDInDB);
}
