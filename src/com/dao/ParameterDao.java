package com.dao;

import com.model.*;
import com.util.HibernateUtil;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sang on 15/10/21.
 */
@Repository
public class ParameterDao {
    @Autowired
    private SessionFactory sessionFactory;

    private Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    public Size findSize(String sizeName,int side){
        Query query = getCurrentSession().createQuery("from Size s where s.name like ? and s.side=?");
        query.setString(0, "%"+sizeName+"%");
        query.setInteger(1, side);
        List<Size> sizeList=query.list();
        return sizeList.get(0);
    }

    public Paper findPaper(String paperName){
        Query query = getCurrentSession().createQuery("from Paper p where p.name = ?");
        query.setString(0, paperName);
        List<Paper> paperList=query.list();
        return paperList.get(0);
    }

    public PrintPrice findPrintPrice(String machine,String colorNum){
        Query query = getCurrentSession().createQuery("from PrintPrice p where p.machine = ? and p.colorNum=?");
        query.setString(0, machine);
        query.setString(1, colorNum);
        List<PrintPrice> printPriceList=query.list();
        return printPriceList.get(0);
    }

    public double findCarbonSheetPrice(String size,int lianAmount,int taoAmount,int amount){
        Query query = getCurrentSession().createQuery("from CarbonSheetPrice c where c.size=? and c.lianAmount=? and c.taoAmount=? and c.amount=?");
        query.setString(0, size);
        query.setInteger(1, lianAmount);
        query.setInteger(2, taoAmount);
        query.setInteger(3, amount);
        List<CarbonSheetPrice> carbonSheetPrices=query.list();
        if(carbonSheetPrices.size()==0){
            return 1000000;
        }
        return carbonSheetPrices.get(0).getPrice();
    }

    public double findStickerPrice(String type,int amount,String fmType){
        Query query = getCurrentSession().createQuery("from StickerPrice s where s.type=? and s.fmType=? and s.amount=?");
        query.setString(0, type);
        query.setString(1, fmType);
        query.setInteger(2, amount);
        List<StickerPrice> stickerPrices=query.list();
        if(stickerPrices.size()==0){
            return 1000000;
        }
        return stickerPrices.get(0).getPrice();
    }

    public List<PrintPrice> listPrintPrice(){
        Query query = getCurrentSession().createQuery("from PrintPrice");
        return query.list();
    }
    public List<Paper> listPaperPrice(){
        Query query = getCurrentSession().createQuery("from Paper");
        return query.list();
    }
    public List<StickerPrice> listStickerPrice(){
        Query query = getCurrentSession().createQuery("from StickerPrice");
        return query.list();
    }
    public List<Craft> listCraft(){
        Query query = getCurrentSession().createQuery("from Craft");
        return query.list();
    }
    public List<CarbonSheetPrice> listCarbonSheet(){
        Query query = getCurrentSession().createQuery("from CarbonSheetPrice");
        return query.list();
    }
    public List<Decoration> listDecoration(){
        Query query = getCurrentSession().createQuery("from Decoration");
        return query.list();
    }
    public void mergeParameter(Object o){
        getCurrentSession().merge(o);
    }
    public void deleteParamter(Object o){
        getCurrentSession().delete(o);
    }
}
