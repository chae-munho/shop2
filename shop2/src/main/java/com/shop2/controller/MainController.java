package com.shop2.controller;

import com.shop2.dto.ItemSearchDto;
import com.shop2.dto.MainItemDto;
import com.shop2.service.ItemService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.eclipse.jdt.internal.compiler.batch.Main;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.swing.text.html.Option;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final ItemService itemService;

    @GetMapping(value = "/")
    public String main(ItemSearchDto itemSearchDto,
                       @RequestParam(value = "page", required = false) Integer page,
                       Model model) {

        Pageable pageable = PageRequest.of(page != null ? page : 0, 6);
        Page<MainItemDto> items = itemService.getMainItemPage(itemSearchDto, pageable);

        for (MainItemDto item : items.getContent()) {
            System.out.println("Item Image URL : " + item.getImgUrl());
        }

        model.addAttribute("items", items);
        model.addAttribute("itemSearchDto", itemSearchDto);
        model.addAttribute("maxPage", 5);

        return "main";
    }


}