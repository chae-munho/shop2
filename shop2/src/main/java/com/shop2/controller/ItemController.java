package com.shop2.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

//상품 등록 페이지에 접근
@Controller
public class ItemController {
    @GetMapping(value = "/admin/item/new")
    public String itemForm() {
        return "/item/itemForm";
    }
}
