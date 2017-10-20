package com.service;

import com.dao.ParameterDao;
import com.model.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by Sang on 15/10/28.
 */
@Service
public class ParameterService {
    @Resource
    private ParameterDao parameterDao;

    public List<PrintPrice> listPrintPrice(){
        return parameterDao.listPrintPrice();
    }

    public List<Paper> listPaperPrice(){
        return parameterDao.listPaperPrice();
    }

    public List<StickerPrice> listStickerPrice(){return parameterDao.listStickerPrice();}

    public List<Craft> listCraft(){return parameterDao.listCraft();}

    public List<Decoration> listDecoration(){return parameterDao.listDecoration();}

    public List<CarbonSheetPrice> listCarbonSheet(){return parameterDao.listCarbonSheet();}

    public void merge(Object parameter){
        parameterDao.mergeParameter(parameter);
    }
    public void delete(Object parameter){
        parameterDao.deleteParamter(parameter);
    }


}
