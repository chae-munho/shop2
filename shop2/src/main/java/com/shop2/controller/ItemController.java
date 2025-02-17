package com.shop2.controller;

import com.shop2.dto.ItemFormDto;
import com.shop2.dto.ItemSearchDto;
import com.shop2.entity.Item;
import com.shop2.service.ItemService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

//상품 등록 페이지에 접근

@Controller
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @GetMapping(value = "/admin/item/new")
    public String itemForm(Model model){
        model.addAttribute("itemFormDto", new ItemFormDto());
        return "item/itemForm";
    }

    @PostMapping(value = "/admin/item/new")
    public String itemNew(@Valid ItemFormDto itemFormDto, BindingResult bindingResult,
                          Model model, @RequestParam("itemImgFile") List<MultipartFile> itemImgFileList){

        if(bindingResult.hasErrors()){
            return "item/itemForm";
        }

        if(itemImgFileList.get(0).isEmpty() && itemFormDto.getId() == null){
            model.addAttribute("errorMessage", "첫번째 상품 이미지는 필수 입력 값 입니다.");
            return "item/itemForm";
        }

        try {
            itemService.saveItem(itemFormDto, itemImgFileList);
        } catch (Exception e){
            model.addAttribute("errorMessage", "상품 등록 중 에러가 발생하였습니다.");
            return "item/itemForm";
        }

        return "redirect:/";
    }
    //상품 수정 페이지로 진입하기 위해서 ItemController 클래스에 코드 추가 상품 수정 페이지도 기존에 사용하던 상품 등록 페이지 ItemForm.html을 그대로 사용
    @GetMapping(value = "/admin/item/{itemId}")
    public String itemDtl(@PathVariable("itemId") Long itemId, Model model) {
        try {
            //조회한 상품 데이터를 모델에 담아서 뷰로 전달
            ItemFormDto itemFormDto = itemService.getItemDtl(itemId);
            model.addAttribute("itemFormDto", itemFormDto);

        }catch (EntityNotFoundException e) {
            // 상품 엔티티가 존재하지 않을 경우 에러메시지를 담아서 상품 등록 페이지로 이동한다.
            model.addAttribute("errorMessage", "존재하지 않는 상품 입니다.");
            model.addAttribute("itemFormDto", new ItemFormDto());
            return "item/itemForm";
        }
        return "item/itemForm";
    }
    //상품 수정 url 클래스 추가
    @PostMapping(value = "/admin/item/{itemId}")
    public String itemUpdate(@Valid ItemFormDto itemFormDto, BindingResult bindingResult, @RequestParam("itemImgFile") List<MultipartFile> itemImgFileList, Model model) {
        if (bindingResult.hasErrors()) {
            return "item/itemForm";
        }
        if (itemImgFileList.get(0).isEmpty() && itemFormDto.getId() == null) {
            model.addAttribute("errorMessage", "첫 번째 이미지는 필수 입력 값 입니다.");
            return "item/itemForm";
        }
        try {
            itemService.updateItem(itemFormDto, itemImgFileList);
        } catch (Exception e) {
            model.addAttribute("errorMessage", "상품 수정 중 에러가 발생했습니다.");
            return "item/itemForm";
        }
        return "redirect:/";
    }

    //상품 관리 화면 및 조회한 상품 데이터를 화면에 전달하는 로직 구현 현재는 상품 데이터가 많이 없는 관계로 한 페이지당 총 3개의 상품만 보여주도록 페이지 번호는 0부터
    @GetMapping(value = {"/admin/items", "/admin/items/{page}"})  //url에 페이지 번호가 없는 경우와 페이지 번호가 있는 경우 2가지를 매핑
    public String itemManage(ItemSearchDto itemSearchDto, @PathVariable("page")Optional<Integer> page, Model model) {
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 3);  // Url경로에 페이지 번호가 있으면 해당 페이지를 조회하도록 하고 페이지 번호가 없으면 0페이지를 조회하도록, 첫 번째 파라미터로는 조회할 페이지 번호, 두번쨰 파라미터로는 한 번에 가지고 올 데이터 수를 넣어움
        Page<Item> items =
                itemService.getAdminItemPage(itemSearchDto ,pageable);  //조회조건과 페이징 정보를 파라미터로 넘겨서 Page<Item> 객체를 반환 받습니다.
        model.addAttribute("items", items);  // 조회한 상품 데이터 및 페이징 정보를 뷰에 전달한다
        model.addAttribute("itemSearchDto", itemSearchDto);  // 페이지 전환 시 기존 검색 조건을 유지한 채 이동할 수 있도록 뷰에 다시 전달한다
        model.addAttribute("maxPage", 5);  //상품 관리 메뉴 하단에 보여줄 페이지 번호의 최대 개수. 5로 설정했으므로 최대 5개의 이동할 페이지 번호만 보여준다.
        return "item/itemMng";
    }


}