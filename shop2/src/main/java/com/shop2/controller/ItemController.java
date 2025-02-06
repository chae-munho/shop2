package com.shop2.controller;

import com.shop2.dto.ItemFormDto;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

//상품 등록 페이지에 접근
@Controller
public class ItemController {
    @GetMapping(value = "/admin/item/new")
    public String itemForm(Model model) {
        model.addAttribute("itemFormDto", new ItemFormDto());
        return "/item/itemForm";
    }
}
