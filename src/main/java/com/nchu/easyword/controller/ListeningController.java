package com.nchu.easyword.controller;

import com.nchu.easyword.dao.model.Listening;
import com.nchu.easyword.dto.PageViewDTO;
import com.nchu.easyword.dto.ResponseDTO;
import com.nchu.easyword.service.inface.ListenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 2018-5-10 10:42:29
 * 听力相关
 */
@RestController
@CrossOrigin(allowCredentials = "true")
@RequestMapping("/listen")
public class ListeningController {
    @Autowired
    ListenService listenService;

    @RequestMapping(value = "/getListenListPage", method = RequestMethod.GET)
    ResponseDTO<PageViewDTO> getListenListPage(@RequestParam("page") int page, @RequestParam("pageSize") int pageSize,
                                               @RequestAttribute("responseDTO") ResponseDTO responseDTO) {
        PageViewDTO<Listening> listeningPageViewDTO = new PageViewDTO(page, pageSize, listenService.getCount(),
                listenService.getByPage(page, pageSize), "听力列表");
        return responseDTO.setData(listeningPageViewDTO).setMessage("听力列表");
    }

    @RequestMapping(value = "/getListenById/{id}", method = RequestMethod.GET)
    ResponseDTO<Listening> getListenById(@PathVariable("id") long id, @RequestAttribute("responseDTO") ResponseDTO responseDTO) {
        return responseDTO.setData(listenService.selectByPrimaryKey(id)).setMessage("听力详情");
    }
}
