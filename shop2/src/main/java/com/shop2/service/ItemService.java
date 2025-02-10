package com.shop2.service;


//상품을 등록하는 ItemService

import com.shop2.dto.ItemFormDto;
import com.shop2.entity.Item;
import com.shop2.entity.ItemImg;
import com.shop2.repository.ItemImgRepository;
import com.shop2.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;
    private final ItemImgService itemImgService;
    private final ItemImgRepository itemImgRepository;

    // saveItem 메서드는 상품을 저장하는 역할을 한다. itemFormDto는 ItemFormDto 객체로, 사움 정보를 담고 있음. ItemImgFileList는 업로드된 상품 이미지 목록을 담고 있는 MultipartFIle 리스트
    public Long saveItem(ItemFormDto itemFormDto, List<MultipartFile> itemImgFileList) throws Exception {
        //상품 등록
        Item item = itemFormDto.createItem();itemRepository.save(item);

        //이미지 등록
        for(int i = 0; i < itemImgFileList.size(); i++) {  //상품 이미지 리스트를 반복하면서 개별 이미지 처리
            ItemImg itemImg = new ItemImg();  // 새로운 ItemImg 객체를 생성
            itemImg.setItem(item); //ItemImg 엔티티에 상품 정보를 설정 즉 현재 상품 Item과 이미지ItemImg를 연결하는 역할을 한다
            if (i == 0) {
                itemImg.setRepimgYn("Y"); // 첫번째 이미지 i == 0는 대표 이미지 썸네일로 설정하여 repImgYn = "Y"로 지정한다
            } else
                itemImg.setRepimgYn("N"); // 나머지 이미지 i > 0는 대표 이지지가 아니므로 repimgYn='N'으로 설정한다.
            itemImgService.saveItemImg(itemImg, itemImgFileList.get(i)); //saveItemImg메소드를 호출하여 상품 이미지를 저장한다. 첫번쨰 인자 : itemImg(이미지 정보를 담고 있는 ItemImg 객체, 두 번째 인자 : itemImgFileList.get(i)는 업로드된 이미지 파일)
        }
        return item.getId();  // 저장된 상품의 ID값을 반환한다.
    }
}
