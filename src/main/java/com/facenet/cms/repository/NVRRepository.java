package com.facenet.cms.repository;

import com.facenet.cms.model.NVRModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NVRRepository extends MongoRepository<NVRModel, String>{
    Page<NVRModel> findAll(Pageable pageable);

    @Query(value = "{'_id': ?0}", delete = true)
    void deleteById(String id);

//    @Query(value = "{'_id' : ?0}")
//    Optional<NVRModel> findById(String id);
}