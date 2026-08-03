package com.nexacare.hospital.utility;


import com.nexacare.hospital.exception.FileUploadException;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Component
public class UploadUtility {

    public void validateImage(MultipartFile imageFile) {

        if(imageFile==null || imageFile.isEmpty()){
            throw new FileUploadException("ImageFile cannot be Empty or null");
        }
        String imageName=imageFile.getOriginalFilename();

        List<String> allowedFileList= List.of("png","jpg","jpge");

        String[] arr = imageName.split("\\.");

        if(arr.length !=2){
            throw  new FileUploadException("ImageFile name Invalid");
        }
        String extension=arr[1];//arr[0] is name of file and arr[1] is a extension  which is split by . dot operator

        if(!allowedFileList.contains(extension)){
            throw  new FileUploadException(" extension  "+"Extension not Support");
        }
    }
}
