package com.shop2.service;


import com.shop2.entity.ItemImg;
import com.shop2.repository.ItemImgRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import java.io.IOException;

//상품 이미지를 업로드 하고, 상품 이미지 정보를 저장하는 ItemImgService 클래스를 service 패키지 아래에 생성한다.
@Service
@RequiredArgsConstructor
@Transactional
public class ItemImgService{

    @Value("${itemImgLocation}") // @Value 어노테이션을 통해 application.properties 파일에 등록한 itemImgLocation 값을 불러와서 itemImgLocation 변수에 넣어준다.
    private String itemImgLocation;

    private final ItemImgRepository itemImgRepository;

    private final FileService fileService;

    public void saveItemImg(ItemImg itemImg, MultipartFile itemImgFile) throws Exception {
        String oriImgName = itemImgFile.getOriginalFilename();
        String imgName = "";
        String imgUrl = "";

        //파일 업로드
        if (!StringUtils.isEmpty(oriImgName)) {
            imgName = fileService.uploadFile(itemImgLocation, oriImgName, itemImgFile.getBytes());
            imgUrl = "/images/item/" + imgName;
        }

        //상품 이미지 정보 저장
        itemImg.updateItemImg(oriImgName, imgName, imgUrl);  //imgName : 실제 로컬에 저장된 상품 이미지 파일의 이름, oriImgName : 업로드했던 상품 이미지 파일의 원래 이름, imgUrl : 업로드 결과 로컬에 저장된 상품 이미지 파일을 불러오는 경로
        itemImgRepository.save(itemImg);
    }
}
