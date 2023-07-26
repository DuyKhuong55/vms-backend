package com.facenet.cms.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CameraFormPage<T> {
    private int pageSize;
    private int pageNumber;
    private T searchData;
}
