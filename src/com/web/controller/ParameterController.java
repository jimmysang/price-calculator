package com.web.controller;

import com.dao.ParameterDao;
import com.model.*;
import com.service.ParameterService;
import com.sun.deploy.net.HttpResponse;
import com.util.ApplicationContextProvider;
import com.util.JsonUtil;
import com.util.ResponseUtil;
import jdk.nashorn.internal.parser.JSONParser;
import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Sang on 15/10/28.
 */
@Controller
@RequestMapping("/parameter")
public class ParameterController {
    @Resource
    private ParameterService parameterService;


    @RequestMapping("/listPrintPrice")
    private void listPrintPrice(HttpServletRequest request,HttpServletResponse response){
        JSONArray result = new JSONArray();
        result.addAll(parameterService.listPrintPrice());
        ResponseUtil.write(response,result,null);
    }
    @RequestMapping("/updatePrintPrice")
    private void updatePrintPrice(HttpServletRequest request,HttpServletResponse response){
        String editingRow = request.getParameter("data");
        PrintPrice p=(PrintPrice)JsonUtil.stringToObject(editingRow,PrintPrice.class);
        parameterService.merge(p);
    }
    @RequestMapping("/deletePrintPrice")
    private void deletePrintPrice(HttpServletRequest request,HttpServletResponse response){
        String editingRow = request.getParameter("data");
        PrintPrice p=(PrintPrice)JsonUtil.stringToObject(editingRow,PrintPrice.class);
        parameterService.delete(p);
    }
    
    @RequestMapping("/listPaperPrice")
    private void listPaperPrice(HttpServletResponse response){
        JSONArray result = new JSONArray();
        result.addAll(parameterService.listPaperPrice());
        ResponseUtil.write(response, result, null);
    }
    @RequestMapping("/updatePaperPrice")
    private void updatePaperPrice(HttpServletRequest request,HttpServletResponse response){
        String editingRow = request.getParameter("data");
        Paper p=(Paper)JsonUtil.stringToObject(editingRow,Paper.class);
        parameterService.merge(p);
    }
    @RequestMapping("/deletePaperPrice")
    private void deletePaperPrice(HttpServletRequest request,HttpServletResponse response){
        String editingRow = request.getParameter("data");
        Paper p=(Paper)JsonUtil.stringToObject(editingRow,Paper.class);
        parameterService.delete(p);
    }
    @RequestMapping("/listStickerPrice")
    private void listStickerPrice(HttpServletResponse response){
        JSONArray result = new JSONArray();
        result.addAll(parameterService.listStickerPrice());
        ResponseUtil.write(response, result, null);
    }
    @RequestMapping("/updateStickerPrice")
    private void updateStickerPrice(HttpServletRequest request,HttpServletResponse response){
        String editingRow = request.getParameter("data");
        StickerPrice s=(StickerPrice)JsonUtil.stringToObject(editingRow,StickerPrice.class);
        parameterService.merge(s);
    }
    @RequestMapping("/deleteStickerPrice")
    private void deleteStickerPrice(HttpServletRequest request,HttpServletResponse response){
        String editingRow = request.getParameter("data");
        StickerPrice s=(StickerPrice)JsonUtil.stringToObject(editingRow,StickerPrice.class);
        parameterService.delete(s);
    }
    @RequestMapping("/listCraft")
    private void listCraft(HttpServletResponse response){
        JSONArray result = new JSONArray();
        result.addAll(parameterService.listCraft());
        ResponseUtil.write(response, result, null);
    }
    @RequestMapping("/updateCraft")
    private void updateCraft(HttpServletRequest request,HttpServletResponse response){
        String editingRow = request.getParameter("data");
        Craft c=(Craft)JsonUtil.stringToObject(editingRow,Craft.class);
        parameterService.merge(c);
    }
    @RequestMapping("/deleteCraft")
    private void deleteCraft(HttpServletRequest request,HttpServletResponse response){
        String editingRow = request.getParameter("data");
        Craft c=(Craft)JsonUtil.stringToObject(editingRow,Craft.class);
        parameterService.delete(c);
    }
    @RequestMapping("/listCarbonSheet")
    private void listCarbonSheet(HttpServletResponse response){
        JSONArray result = new JSONArray();
        result.addAll(parameterService.listCarbonSheet());
        ResponseUtil.write(response, result, null);
    }
    @RequestMapping("/updateCarbonSheet")
    private void updateCarbonSheet(HttpServletRequest request,HttpServletResponse response){
        String editingRow = request.getParameter("data");
        CarbonSheetPrice c=(CarbonSheetPrice)JsonUtil.stringToObject(editingRow,CarbonSheetPrice.class);
        parameterService.merge(c);
    }
    @RequestMapping("/deleteCarbonSheet")
    private void deleteCarbonSheet(HttpServletRequest request,HttpServletResponse response){
        String editingRow = request.getParameter("data");
        CarbonSheetPrice c=(CarbonSheetPrice)JsonUtil.stringToObject(editingRow,CarbonSheetPrice.class);
        parameterService.delete(c);
    }
    @RequestMapping("/listDecoration")
    private void listDecoration(HttpServletResponse response){
        JSONArray result = new JSONArray();
        result.addAll(parameterService.listDecoration());
        ResponseUtil.write(response, result, null);
    }
    @RequestMapping("/updateDecoration")
    private void updateDecoration(HttpServletRequest request,HttpServletResponse response){
        String editingRow = request.getParameter("data");
        Decoration d=(Decoration)JsonUtil.stringToObject(editingRow,Decoration.class);
        parameterService.merge(d);
    }
}
