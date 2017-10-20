package com.service;

import com.dao.CalculateCraftDao;
import com.model.Craft;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by Sang on 15/10/21.
 */
@Service
public class CalculateCraftService {
    @Resource
    private CalculateCraftDao calculateCraftDao;
    /**
     * 面积表示所有面积
     面积单位:m2
     */
    public double getFMprice(String material,double area){
        Craft fm=calculateCraftDao.findCraft("覆膜",material);
        return Math.max(fm.getManualStarting(),area*fm.getManualUnitPrice());
    }

    public double getYHprice(int amount){
        Craft md=calculateCraftDao.findCraft("压痕");
        return Math.max(md.getManualStarting(),amount*md.getManualUnitPrice());
    }

    public double getTLprice(int amount){
        Craft tl=calculateCraftDao.findCraft("台历");
        return Math.max(tl.getManualStarting(),amount*tl.getManualUnitPrice());
    }

    public double getZYprice(String type,int divideNum,int amount){
        double price=0;
        String size="";
        if(divideNum<4){
            size="对开";
        }else if(divideNum<8){
            size="四开";
        }else {
            size="8开及以下";
        }
        Craft zy=calculateCraftDao.findCraft("折页","机器折页",size);
        switch (type){
            case "对折页":price=Math.max(zy.getManualStarting(),1*amount*zy.getManualUnitPrice());break;
            case "三折页":price=Math.max(zy.getManualStarting(),2*amount*zy.getManualUnitPrice());break;
            case "风琴折":price=Math.max(zy.getManualStarting(),3*amount*zy.getManualUnitPrice());break;
            case "关门折":{
                price=Math.max(zy.getManualStarting(),2*amount*zy.getManualUnitPrice());
                Craft manaulZy=calculateCraftDao.findCraft("折页","手工折");
                price+=Math.max(manaulZy.getManualStarting(),amount*manaulZy.getManualUnitPrice());
                break;
            }
        }
        return price;
    }
    /**
     * 面积表示单张面积
     面积单位:m2 
     */
    public double getTJprice(int amount,double area,boolean needPlate){
        Craft tj=calculateCraftDao.findCraft("烫金");
        double price=Math.max(tj.getMaterialStarting(), amount * area * tj.getMaterialUnitPrice());
        price+=Math.max(tj.getManualStarting(),amount*tj.getManualUnitPrice());
        if(needPlate){
            price+=Math.max(tj.getPlateStarting(),area*tj.getPlateUnitPrice());
        }
        return price;
    }

    public double getZDprice(String type,int amount,int pNum){
        Craft zd=calculateCraftDao.findCraft("装订", type);
        return Math.max(zd.getManualStarting(),amount*pNum*zd.getManualUnitPrice());
    }

    /**
     * 面积表示单张面积
    面积单位:m2 
    */
    public double getUVprice(int amount,double area){
        Craft uv=calculateCraftDao.findCraft("局部UV");
        double price=Math.max(uv.getManualStarting(), amount * Math.max(uv.getMaterialUnitPrice(), area * uv.getManualUnitPrice()));
        return price;
    }

    public double getATprice(int amount,double area,boolean needPlate){
        Craft at=calculateCraftDao.findCraft("凹凸");
        double price=Math.max(at.getManualStarting(), amount * at.getManualUnitPrice());
        if(needPlate){
            price+=Math.max(at.getPlateStarting(),area*at.getPlateUnitPrice());
        }
        return price;
    }

    public double getHXprice(int amount){
        Craft hx=calculateCraftDao.findCraft("划线");
        return Math.max(hx.getManualStarting(),amount*hx.getManualUnitPrice());
    }

    public double getMQprice(String type,String size,int innerAmount,int amount){
        Craft mq=calculateCraftDao.findCraft("模切",type,size);
        double price=Math.max(mq.getPlateStarting(), mq.getPlateUnitPrice() * innerAmount);
        price+=Math.max(mq.getManualStarting(),mq.getManualUnitPrice()*amount);
        return price;
    }

    public double getDMprice(int mNum,int amount){
        Craft dm=calculateCraftDao.findCraft("打码");
        return Math.max(dm.getManualStarting(),mNum*amount*dm.getManualUnitPrice());
    }

    public double getSTDprice(int amount,boolean isSelf){
        Craft std=calculateCraftDao.findCraft("手提袋");
        return Math.max(std.getManualStarting(),amount*std.getManualUnitPrice())+(isSelf?std.getPlateStarting():0);
    }

    public double getMD(int amount){
        Craft md=calculateCraftDao.findCraft("打铆钉");
        return Math.max(md.getManualStarting(),amount*md.getManualUnitPrice());
    }

    public double getDJprice(int amount){
        Craft dj=calculateCraftDao.findCraft("手工打结");
        return Math.max(dj.getManualStarting(),amount*dj.getManualUnitPrice());
    }

    public double getJPSprice(int amount){
        Craft jps=calculateCraftDao.findCraft("精品绳");
        return Math.max(jps.getManualStarting(),amount*jps.getManualUnitPrice());
    }

    public double getCMSprice(int amount){
        Craft cms=calculateCraftDao.findCraft("纯棉绳");
        return Math.max(cms.getManualStarting(),amount*cms.getManualUnitPrice());
    }

    public double getFTprice(int amount){
        Craft ft=calculateCraftDao.findCraft("封套成型");
        return Math.max(ft.getManualStarting(),amount*ft.getManualUnitPrice())+ft.getPlateStarting();
    }

    public double getXFprice(int paperWeight,boolean isFM, String size, int amount){
        if(paperWeight>=200){
            Craft xf=calculateCraftDao.findCraft("信封","200克及以上纸张");
            return Math.max(xf.getManualStarting(),xf.getManualUnitPrice()*amount)+xf.getPlateStarting();
        }else {
            String key3=null;
            if(!isFM){
                switch (size){
                    case "2号":case "5号":case "6号":key3="2号、5号、6号";break;
                    default:key3="7号、9号、自定义";
                }
            }
            Craft xf=calculateCraftDao.findCraft("信封","157克及以下",isFM?"覆膜":"不覆膜",key3);
            return Math.max(xf.getManualStarting(),xf.getManualUnitPrice()*amount);
        }
    }
    public double getKTCprice(int amount){
        Craft ktc=calculateCraftDao.findCraft("开天窗");
        return Math.max(ktc.getManualStarting(),ktc.getManualUnitPrice()*amount);
    }

    public double getJSprice(int amount,String type){
        Craft js=calculateCraftDao.findCraft("胶水",type);
        return Math.max(js.getManualStarting(),js.getManualUnitPrice()*amount);
    }


    public double getMPTJprice(int amount,int side,boolean needPlate){
        String key="";
        if(amount==2){
            key="2盒";
        }else if(5<=amount&&amount<=20){
            key="5盒-20盒";
        }else {
            key="50盒及以上";
        }
        Craft mptj=calculateCraftDao.findCraft("名片烫金",key);
        double price=mptj.getManualUnitPrice()*side*amount;
        if(needPlate){
            price+=25;
        }
        return price;
    }

    public double getMPMQprice(int amount,boolean needPlate,int mSize){
        String key="";
        if(amount==2){
            key="2盒";
        }else if(5<=amount&&amount<=20){
            key="5盒-20盒";
        }else {
            key="50盒及以上";
        }
        Craft mpmq=calculateCraftDao.findCraft("名片模切",key);
        double price=mpmq.getManualUnitPrice()*amount;
        if(needPlate){
            price+=10*mSize;
        }
        return price;
    }

    public double getMPYJprice(int amount){
        String key="";
        if(amount==2){
            key="2盒";
        }else if(5<=amount&&amount<=20){
            key="5盒-20盒";
        }else {
            key="50盒及以上";
        }
        Craft mpyj=calculateCraftDao.findCraft("名片圆角",key);
        double price=mpyj.getManualUnitPrice()*amount;
        return price;
    }

    public double getMPYHprice(int amount){
        String key="";
        if(amount==2){
            key="2盒";
        }else if(5<=amount&&amount<=20){
            key="5盒-20盒";
        }else {
            key="50盒及以上";
        }
        Craft mpmd=calculateCraftDao.findCraft("名片压痕",key);
        double price=mpmd.getManualUnitPrice()*amount;
        return price;
    }

    public double getMPDMprice(int amount){
        String key="";
        if(amount<50){
            key="50盒以下";
        }else if(50<=amount&&amount<=100){
            key="50盒-100盒";
        }else {
            key="100盒及以上";
        }
        Craft mpdm=calculateCraftDao.findCraft("名片打码",key);
        double price=Math.max(mpdm.getManualStarting(),mpdm.getManualUnitPrice()*amount);
        return price;
    }

    public double getMPATprice(int amount,boolean needPlate){
        String key="";
        if(amount==2){
            key="2盒";
        }else if(5<=amount&&amount<=20){
            key="5盒-20盒";
        }else {
            key="50盒及以上";
        }
        Craft mpat=calculateCraftDao.findCraft("名片凹凸",key);
        double price=mpat.getManualUnitPrice()*amount;
        if(needPlate){
            price+=50;
        }
        return price;
    }
}
